package org.spring.kanban.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.spring.kanban.domain.KanbanBoard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanbanBoardRepository extends MongoRepository<KanbanBoard, ObjectId> {	
	Optional<KanbanBoard> findByCreatedBy(String cratedBy);
	Optional<KanbanBoard> findByName(String name);
	List<KanbanBoard> findAllByCreatedBy(String createdBy);
}
