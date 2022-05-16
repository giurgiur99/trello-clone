package org.spring.kanban.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.spring.kanban.domain.KanbanCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanbanCardRepository extends MongoRepository<KanbanCard, ObjectId> {
	List<KanbanCard> findAllByIdColumn(ObjectId id);
	Optional<KanbanCard> findByPositionAndIdColumn(Long position,ObjectId idColumn);
	Long countByIdColumn(ObjectId idColumn);
}
