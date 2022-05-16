package org.spring.kanban.repository;

import org.bson.types.ObjectId;
import org.spring.kanban.domain.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment, ObjectId> {
	Long countByIdCard(ObjectId idCard);
}
