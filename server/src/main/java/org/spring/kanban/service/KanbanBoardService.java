package org.spring.kanban.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.spring.kanban.configuration.CustomProjectAggregationOperation;
import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.domain.KanbanBoard;
import org.spring.kanban.exception.EntityNotFoundException;
import org.spring.kanban.exception.PermissionErrorException;
import org.spring.kanban.payload.BoardDetailsResponse;
import org.spring.kanban.payload.NameRequest;
import org.spring.kanban.repository.AttachmentRepository;
import org.spring.kanban.repository.KanbanBoardRepository;
import org.spring.kanban.repository.UserRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KanbanBoardService {
	
	private final KanbanBoardRepository kanbanBoardRepository;	
	private final UserRepository userRepository;	
	private final MongoOperations mongoOperations;	

	public KanbanBoardService(
			KanbanBoardRepository kanbanBoardRepository,
			UserRepository userRepository,
			MongoOperations mongoOperations,
			AttachmentRepository attachmentRepository) {
		this.kanbanBoardRepository = kanbanBoardRepository;
		this.userRepository = userRepository;
		this.mongoOperations = mongoOperations;
	}

	public KanbanBoard saveBoard(NameRequest name) {
		KanbanBoard board = new KanbanBoard();
		board.setName(name.getName());
		log.info("Board Saved Successfully");
		return this.kanbanBoardRepository.save(board);
	}

	public void deleteById(ObjectId id, UserPrincipal currentUser) {
		this.kanbanBoardRepository.findById(id)
				.map(board -> this.isOwner(board, currentUser)).ifPresentOrElse(board -> {				
					Query query = new Query(Criteria.where("idBoard").is(board.getId()));
					this.mongoOperations.findAllAndRemove(query, "attachments");
					log.info("Attachments Deleted Successfully");
					this.mongoOperations.findAllAndRemove(query, "cards");
					log.info("Cards Deleted Successfully");
					this.mongoOperations.findAllAndRemove(query, "columns");
					log.info("Columns Deleted Successfully");
			this.kanbanBoardRepository.deleteById(id);
			log.info("Board Deleted Successfully",board.getId());
		}, () -> {
			log.error("Board cannot be deleted");
			throw new RuntimeException("Board cannot be deleted");
		});
	}
	
	public BoardDetailsResponse findBoardById(ObjectId id, UserPrincipal currentUser) {
		this.findById(id, currentUser);
		String columnsWithCards = 
				"{$lookup: {\r\n" + 
				"        from:'columns',\r\n" + 
				"        let:{'boardId':'$_id'},\r\n" + 
				"        pipeline: [\r\n" + 
				"            {$match:{$expr: {$eq: ['$idBoard','$$boardId']}}},\r\n" + 
				"            {$lookup:{\r\n" + 
				"                from:'cards',\r\n" + 
				"                let:{'columnId':'$_id'},\r\n" + 
				"                pipeline:[\r\n" + 
				"                   {$match:{$expr: {$eq: ['$idColumn','$$columnId']}}},\r\n" + 
				"                   {$lookup:{\r\n" + 
				"                       from:'attachments',\r\n" + 
				"                       let:{'cardId':'$_id'},\r\n" + 
				"                       pipeline:[\r\n" + 
				"                           {$match: {$expr: {$eq: ['$idCard','$$cardId']}}},\r\n" + 
				"                       ],\r\n" + 
				"                       as:'attachments'\r\n" + 
				"                   }},\r\n" + 
				"                   {$project:{\r\n" + 
				"                    name:1,\r\n" + 
				"                    createdBy: 1,\r\n" + 
				"                    createdDate: 1,\r\n" + 
				"                    updatedBy: 1,\r\n" + 
				"                    updatedDate: 1,\r\n" + 
				"                    idColumn: 1,\r\n" +
				"                    idBoard: 1,\r\n" +
				"                    position: 1,\r\n" + 
				"                    cover: 1,\r\n" + 
				"                    fileSize:{$size:'$attachments'}\r\n" + 
				"                   }}\r\n" + 
				"				{$sort:{'position':1}}"+
				"                ],\r\n" + 
				"                as:'cards'\r\n" + 
				"            }}\r\n" + 
				"		{$sort:{'position':1}}"+
				"        ],\r\n" + 
				"        as:'columns'\r\n" + 
				"    }}";
		TypedAggregation<BoardDetailsResponse> aggregation = Aggregation.newAggregation(
				BoardDetailsResponse.class, Aggregation.match(new Criteria("id").is(id)),
				new CustomProjectAggregationOperation(columnsWithCards));		
		AggregationResults<BoardDetailsResponse> boardDetailsResponse = mongoOperations
				.aggregate(aggregation, "boards", BoardDetailsResponse.class);
		return boardDetailsResponse.getUniqueMappedResult();
	}

	public List<KanbanBoard> findAllByCreatedBy(String createdBy, UserPrincipal currentUser) {
		this.userRepository.findByUsername(createdBy)		
		.map(user -> {
			if (user.getUsername().equalsIgnoreCase(currentUser.getUsername()) || currentUser.isEnabled()) {
				return user;
			} else {
				throw new PermissionErrorException("You are not allowed to access the Boards");
			}
		}).orElseThrow(() -> {
			throw new EntityNotFoundException("Board", "id", createdBy);
		});
		return this.kanbanBoardRepository.findAllByCreatedBy(createdBy);
	}	

	public KanbanBoard updateName(ObjectId id, NameRequest name, UserPrincipal currentUser) {
		KanbanBoard board = this.findById(id, currentUser);
		board.setName(name.getName());
		return this.kanbanBoardRepository.save(board);		
	}
	
	public KanbanBoard findById(ObjectId id, UserPrincipal currentUser) {
		return this.kanbanBoardRepository.findById(id)
				.map(board -> this.isOwner(board, currentUser))
				.orElseThrow(() -> {
					throw new EntityNotFoundException("Board", "id", id);
				});
	}

	public KanbanBoard isOwner(KanbanBoard board, UserPrincipal currentUser) {
		if (board.getCreatedBy().equalsIgnoreCase(currentUser.getUsername()) || currentUser.getUsername().contains("admin")) {
			return board;
		} else {
			throw new PermissionErrorException("Boards");
		}
	}
}
