package org.spring.kanban.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.spring.kanban.domain.KanbanColumn;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanbanColumnRepository extends MongoRepository<KanbanColumn, ObjectId> {
	List<KanbanColumn> findAllByIdBoard(ObjectId id);
	Optional<KanbanColumn> findByPosition(Long position);
	Optional<KanbanColumn> findByPositionAndIdBoard(Long position, ObjectId idBoard);
	Long countByIdBoard(ObjectId idBoard);
}
