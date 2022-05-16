package org.spring.kanban.service;

import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.domain.Attachment;
import org.spring.kanban.domain.KanbanCard;
import org.spring.kanban.domain.KanbanColumn;
import org.spring.kanban.exception.EntityNotFoundException;
import org.spring.kanban.exception.PermissionErrorException;
import org.spring.kanban.payload.CardWithAttachmentsResponse;
import org.spring.kanban.payload.CoverDetail;
import org.spring.kanban.payload.DescriptionRequest;
import org.spring.kanban.payload.NameRequest;
import org.spring.kanban.repository.AttachmentRepository;
import org.spring.kanban.repository.KanbanCardRepository;
import org.spring.kanban.repository.KanbanColumnRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class KanbanCardService {

	private final KanbanCardRepository kanbanCardRepository;
	private final KanbanColumnRepository kanbanColumnRepository;
	private final MongoOperations mongoOperations;
	private final AttachmentRepository attachmentRepository;
	private final PositionService positionService;

	public KanbanCardService(KanbanCardRepository kanbanCardRepository, MongoOperations mongoOperations,
			AttachmentRepository attachmentRepository, KanbanColumnRepository kanbanColumnRepository,
			PositionService positionService) {
		this.kanbanCardRepository = kanbanCardRepository;
		this.mongoOperations = mongoOperations;
		this.attachmentRepository = attachmentRepository;
		this.kanbanColumnRepository = kanbanColumnRepository;
		this.positionService = positionService;
	}

	public KanbanCard findById(ObjectId id, UserPrincipal currentUser) {
		return this.kanbanCardRepository.findById(id).map(board -> this.isOwner(board, currentUser)).orElseThrow(() -> {
			throw new EntityNotFoundException("Card", "id", id);
		});
	}

	public KanbanCard saveName(ObjectId idColumn, NameRequest name, UserPrincipal currentUser) {
		KanbanColumn kanbanColumn = this.kanbanColumnRepository.findById(idColumn).map(columnMap -> {
			if (columnMap.getCreatedBy().equalsIgnoreCase(currentUser.getUsername()) || currentUser.getUsername().contains("admin")) {
				return columnMap;
			} else {
				throw new PermissionErrorException("Cards");
			}
		}).orElseThrow(() -> new EntityNotFoundException("Column", "id", idColumn));

		Long countPositionByIdColumn = this.kanbanCardRepository.countByIdColumn(idColumn);
		KanbanCard kanbanCard = new KanbanCard();
		kanbanCard.setName(name.getName());
		kanbanCard.setIdColumn(kanbanColumn.getId());
		kanbanCard.setIdBoard(kanbanColumn.getIdBoard());
		kanbanCard.setPosition(countPositionByIdColumn);
		log.info("Card Saved Successfully");
		return this.kanbanCardRepository.save(kanbanCard);
	}

	public void deleteById(ObjectId id, UserPrincipal currentUser) {
		this.kanbanCardRepository.findById(id).map(kanbanCard -> this.isOwner(kanbanCard, currentUser))
				.ifPresentOrElse(kanbanCard -> {
					Query query = new Query(Criteria.where("idBoard").is(kanbanCard.getIdBoard()));
					this.mongoOperations.findAllAndRemove(query, "attachments");
					log.info("Attachments Deleted Successfully");
					this.positionService.deleteADocumentByIdAndDecreaseAllPositionsGreaterThanCurrentPosition(
							kanbanCard.getIdColumn(), kanbanCard.getPosition(), kanbanCard);
					log.info("Card Deleted Successfully");
				}, () -> {
					log.error("Card cannot be deleted");
					throw new RuntimeException("Card cannot be deleted");
				});
	}

	public CardWithAttachmentsResponse findCardWithAttachmentsById(ObjectId id, UserPrincipal currentUser) {
		this.findById(id, currentUser);
		AggregationOperation match = Aggregation.match(new Criteria("id").is(id));
		LookupOperation attachmentsLookup = LookupOperation.newLookup().from("attachments").localField("_id")
				.foreignField("idCard").as("attachments");

		Aggregation aggregation = Aggregation.newAggregation(match, attachmentsLookup);
		AggregationResults<CardWithAttachmentsResponse> result = mongoOperations.aggregate(aggregation,
				KanbanCard.class, CardWithAttachmentsResponse.class);
		return result.getUniqueMappedResult();
	}

	public KanbanCard updateName(ObjectId id, NameRequest name, UserPrincipal currentUser) {
		KanbanCard card = this.findById(id, currentUser);
		card.setName(name.getName());
		log.info("Name Updated Successfully");
		return this.kanbanCardRepository.save(card);
	}

	public KanbanCard updateDescription(ObjectId id, DescriptionRequest description, UserPrincipal currentUser) {
		KanbanCard card = this.findById(id, currentUser);
		card.setDescription(description.getDescription());
		log.info("Description Updated Successfully");
		return this.kanbanCardRepository.save(card);
	}

	public KanbanCard UploadingMultipleFiles(ObjectId idCard, MultipartFile[] files, UserPrincipal currentUser) {
		KanbanCard kanbanCard = this.findById(idCard, currentUser);
		try {
			if (files != null) {
				CoverDetail coverDetail = new CoverDetail();
				for (MultipartFile file : files) {
					Attachment attachment = new Attachment();
					attachment.setName(file.getOriginalFilename());
					attachment.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
					attachment.setContentType(file.getContentType());
					attachment.setIdCard(kanbanCard.getId());
					attachment.setIdBoard(kanbanCard.getIdBoard());
					Attachment attachmentSaved = this.attachmentRepository.save(attachment);
					if (file.getContentType().equalsIgnoreCase("image/jpeg")
							|| file.getContentType().equalsIgnoreCase("image/png")
							|| file.getContentType().equalsIgnoreCase("image/jpg")) {
						coverDetail.setAttachment(attachmentSaved);
					}
				}
				if (coverDetail.getIdAttachment() != null) {
					kanbanCard.setCover(coverDetail);
					log.info("Attachments Updated Successfully");
					this.kanbanCardRepository.save(kanbanCard);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return kanbanCard;
	}

	public void deleteAttachmentByIdCard(ObjectId idCard, ObjectId idAttachment, UserPrincipal currentUser) {
		this.kanbanCardRepository.findById(idCard).map(card -> this.isOwner(card, currentUser))
				.ifPresentOrElse(card -> {
					Attachment attachment = this.attachmentRepository.findById(idAttachment)
							.orElseThrow(() -> new EntityNotFoundException("Attachment", "id", idAttachment));
					if (card.getCover().getIdAttachment().equals(attachment.getId())) {
						card.setCover(null);
						this.kanbanCardRepository.save(card);
						this.attachmentRepository.deleteById(attachment.getId());
					}
				}, () -> {
					new RuntimeException("Cannot deleted this file");
				});
	}

	public KanbanCard setCover(ObjectId cardId, ObjectId coverId, UserPrincipal currentUser) {
		KanbanCard card = this.findById(cardId, currentUser);
		Attachment attachment = this.attachmentRepository.findById(coverId)
				.orElseThrow(() -> new EntityNotFoundException("Attachment", "id", coverId));
		if (attachment.getContentType().equalsIgnoreCase("image/jpeg")
				|| attachment.getContentType().equalsIgnoreCase("image/png")
				|| attachment.getContentType().equalsIgnoreCase("image/jpg")) {
			CoverDetail coverDetail = new CoverDetail();
			coverDetail.setAttachment(attachment);
			card.setCover(coverDetail);
			this.kanbanCardRepository.save(card);
		} else {
			log.error("Cover is not a JPEG,PNG or JPG");
			throw new RuntimeException("Cover is not a JPEG,PNG or JPG");
		}
		return card;
	}

	public KanbanCard removeCover(ObjectId cardId, ObjectId coverId, UserPrincipal currentUser) {
		KanbanCard card = this.findById(cardId, currentUser);
		Attachment attachment = this.attachmentRepository.findById(coverId)
				.orElseThrow(() -> new EntityNotFoundException("Attachment", "id", coverId));
		if (card.getCover().getIdAttachment().equals(attachment.getId())) {
			card.setCover(null);
			return this.kanbanCardRepository.save(card);
		} else {
			log.error("This is not current Cover");
			throw new RuntimeException("This is not current Cover");
		}
	}

	public void moveCardPosition(Long currentPosition, Long desiredPosition, ObjectId IdColumn) {
		KanbanCard currentkanbanCard = this.kanbanCardRepository.findByPositionAndIdColumn(currentPosition, IdColumn)
				.orElseThrow(() -> new EntityNotFoundException("Card", "position", currentPosition));
		KanbanCard desiredCard = this.kanbanCardRepository.findByPositionAndIdColumn(desiredPosition, IdColumn)
				.orElseThrow(() -> new EntityNotFoundException("Card", "position", desiredPosition));
		this.positionService.MovePositions(currentkanbanCard.getPosition(), desiredCard.getPosition(),
				currentkanbanCard.getIdColumn(), currentkanbanCard);
	}

	public void moveCardToAnotherColumn(ObjectId idCurrentCard, Long desiredPosition, ObjectId idColumn) {
		KanbanColumn kanbanColumn = this.kanbanColumnRepository.findById(idColumn)
				.orElseThrow(() -> new EntityNotFoundException("Column", "id", idColumn));
		KanbanCard currentKanbanCard = this.kanbanCardRepository.findById(idCurrentCard)
				.orElseThrow(() -> new EntityNotFoundException("Card", "id", idCurrentCard));
		Long countPositionByIdColumn = this.kanbanCardRepository.countByIdColumn(idColumn) + 1;
		if (desiredPosition <= countPositionByIdColumn) {
			this.positionService.decreaseAllPositionsGreaterThanCurrentPosition(currentKanbanCard.getPosition(),
					currentKanbanCard.getIdColumn(), currentKanbanCard);
			this.positionService.incrementAllPositionsByOneGreatherThanDesiredPosition(desiredPosition, idColumn,
					currentKanbanCard);
			currentKanbanCard.setIdColumn(idColumn);
			currentKanbanCard.setPosition(desiredPosition);
			this.kanbanCardRepository.save(currentKanbanCard);
		} else {
			throw new RuntimeException("Cannot move for this position: " + desiredPosition);
		}
	}

	public KanbanCard isOwner(KanbanCard card, UserPrincipal currentUser) {
		if (card.getCreatedBy().equalsIgnoreCase(currentUser.getUsername()) ||  currentUser.getUsername().contains("admin")) {
			return card;
		} else {
			throw new PermissionErrorException("Cards");
		}
	}

}
