package org.spring.kanban.service;

import org.bson.types.ObjectId;
import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.domain.KanbanBoard;
import org.spring.kanban.domain.KanbanColumn;
import org.spring.kanban.exception.EntityNotFoundException;
import org.spring.kanban.exception.PermissionErrorException;
import org.spring.kanban.payload.NameRequest;
import org.spring.kanban.repository.AttachmentRepository;
import org.spring.kanban.repository.KanbanBoardRepository;
import org.spring.kanban.repository.KanbanColumnRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KanbanColumnService {

	private final KanbanColumnRepository kanbanColumnRepository;
	private final KanbanBoardRepository kanbanBoardRepository;
	private final MongoOperations mongoOperations;
	private final PositionService positionService;

	public KanbanColumnService(KanbanColumnRepository kanbanColumnRepository,
			KanbanBoardRepository kanbanBoardRepository, MongoOperations mongoOperations,
			AttachmentRepository attachmentRepository, PositionService positionService) {
		this.kanbanColumnRepository = kanbanColumnRepository;
		this.kanbanBoardRepository = kanbanBoardRepository;
		this.mongoOperations = mongoOperations;
		this.positionService = positionService;
	}

	public KanbanColumn saveName(ObjectId idBoard, NameRequest name, UserPrincipal currentUser) {
		KanbanBoard kanbanBoard = this.kanbanBoardRepository.findById(idBoard).map(board -> {
			if (board.getCreatedBy().equalsIgnoreCase(currentUser.getUsername())) {
				return board;
			} else {
				throw new PermissionErrorException("Boards");
			}
		}).orElseThrow(() -> {
			throw new EntityNotFoundException("Board", "id", idBoard);
		});
		Long positionCountByIdColumn = this.kanbanColumnRepository.countByIdBoard(idBoard);
		KanbanColumn kanbanColumn = new KanbanColumn();
		kanbanColumn.setName(name.getName());
		kanbanColumn.setIdBoard(kanbanBoard.getId());
		kanbanColumn.setPosition(positionCountByIdColumn);
		log.info("Column Saved Successfully");
		return this.kanbanColumnRepository.save(kanbanColumn);
	}

	public void deleteById(ObjectId id, UserPrincipal currentUser) {
		this.kanbanColumnRepository.findById(id).map(kanbanColumn -> this.isOwner(kanbanColumn, currentUser))
				.ifPresentOrElse(kanbanColumn -> {
					Query query = new Query(Criteria.where("idBoard").is(kanbanColumn.getIdBoard()));
					this.mongoOperations.findAllAndRemove(query, "attachments");
					log.info("Attachments Deleted Successfully");
					this.mongoOperations.findAllAndRemove(query, "cards");
					log.info("Cards Deleted Successfully");
					this.positionService.deleteADocumentByIdAndDecreaseAllPositionsGreaterThanCurrentPosition(
							kanbanColumn.getIdBoard(), kanbanColumn.getPosition(), kanbanColumn);
					log.info("Column Deleted Successfully");
				}, () -> {
					log.error("Column cannot be deleted");
					throw new RuntimeException("Card cannot be deleted");
				});
	}

	public KanbanColumn findById(ObjectId id, UserPrincipal currentUser) {
		return this.kanbanColumnRepository.findById(id).map(card -> this.isOwner(card, currentUser)).orElseThrow(() -> {
			throw new EntityNotFoundException("Board", "id", id);
		});
	}

	public KanbanColumn updateName(ObjectId id, NameRequest name, UserPrincipal currentUser) {
		KanbanColumn kanbanColumn = this.findById(id, currentUser);
		kanbanColumn.setName(name.getName());
		log.info("Card Updated Successfully");
		return this.kanbanColumnRepository.save(kanbanColumn);
	}

	public void moveColumnPosition(Long currentPosition, Long desiredPosition, ObjectId idBoard) {
		KanbanBoard kanbanBoardExist = this.kanbanBoardRepository.findById(idBoard)
				.orElseThrow(() -> new EntityNotFoundException("Board", "Id", idBoard));
		KanbanColumn currentPositionExist = this.kanbanColumnRepository
				.findByPositionAndIdBoard(currentPosition, idBoard)
				.orElseThrow(() -> new EntityNotFoundException("Column", "position", currentPosition));
		KanbanColumn desiredPositionExist = this.kanbanColumnRepository
				.findByPositionAndIdBoard(desiredPosition, idBoard)
				.orElseThrow(() -> new EntityNotFoundException("Column", "position", desiredPosition));
		this.positionService.MovePositions(currentPositionExist.getPosition(), desiredPositionExist.getPosition(),
				kanbanBoardExist.getId(), currentPositionExist);
	}

	public KanbanColumn isOwner(KanbanColumn column, UserPrincipal currentUser) {
		if (column.getCreatedBy().equalsIgnoreCase(currentUser.getUsername())) {
			return column;
		} else {
			log.warn("You are not allowed to access the Columns");
			throw new PermissionErrorException("Columns");
		}
	}
}
