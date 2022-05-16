package org.spring.kanban.service;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.spring.kanban.domain.BaseEntity;
import org.spring.kanban.domain.KanbanColumn;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;


@Service
public class PositionService {

	private final MongoOperations mongoOperations;

	public PositionService(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public void MovePositions(Long currentPosition, Long desiredPosition, ObjectId idReference,
			BaseEntity<ObjectId> entity) {
		if (currentPosition > desiredPosition) {
			this.incrementAllPositionsByOneGreaterThanDesiredPositionAndCurrentPosition(currentPosition,
					desiredPosition, idReference, entity);
		} else {
			this.decreaseAllPositionsGreaterThanCurrentPositionAndDesiredPosition(currentPosition, desiredPosition,
					idReference, entity);
		}
	}

	public void deleteADocumentByIdAndDecreaseAllPositionsGreaterThanCurrentPosition(ObjectId idReference, Long currentPosition,
			BaseEntity<ObjectId> entity) {
		MongoCollection<Document> collection = null;
		Bson filter = null;
		if (entity.getClass() == KanbanColumn.class) {
			collection = mongoOperations.getCollection("columns");
			filter = Filters.and(eq("idBoard", idReference), Filters.and(gte("position", currentPosition)));
		} else {
			collection = mongoOperations.getCollection("cards");
			filter = Filters.and(eq("idColumn", idReference), Filters.and(gte("position", currentPosition)));
		}
		BulkOperations bulkOperations = mongoOperations.bulkOps(BulkMode.ORDERED, entity.getClass().toString());
		List<WriteModel<Document>> writes = new ArrayList<WriteModel<Document>>();
		writes.add(new UpdateManyModel<Document>(filter, new Document("$inc", new Document("position", -1l))));
		writes.add(new DeleteOneModel<Document>(eq("_id", entity.getId())));
		BulkWriteResult bulkWriteResult = collection.bulkWrite(writes);
	}

	public void incrementAllPositionsByOneGreaterThanDesiredPositionAndCurrentPosition(Long currentPosition,
			Long desiredPosition, ObjectId idReference, BaseEntity<ObjectId> entity) {
		MongoCollection<Document> collection = null;
		Bson filter = null;
		if (entity.getClass() == KanbanColumn.class) {
			collection = mongoOperations.getCollection("columns");
			filter = Filters.and(eq("idBoard", idReference),
					Filters.and(gte("position", desiredPosition), lt("position", currentPosition)));
		} else {
			collection = mongoOperations.getCollection("cards");
			filter = Filters.and(eq("idColumn", idReference),
					Filters.and(gte("position", desiredPosition), lt("position", currentPosition)));
		}
		BulkOperations bulkOperations = mongoOperations.bulkOps(BulkMode.ORDERED, entity.getClass().toString());
		List<WriteModel<Document>> writes = new ArrayList<WriteModel<Document>>();
		writes.add(new UpdateManyModel<Document>(filter, new Document("$inc", new Document("position", 1l))));
		writes.add(new UpdateOneModel<Document>(eq("_id", entity.getId()),
				new Document("$set", new Document("position", desiredPosition))));
		BulkWriteResult bulkWriteResult = collection.bulkWrite(writes);
	}

	public void incrementAllPositionsByOneGreatherThanDesiredPosition(Long desiredPosition, ObjectId idReference,
			BaseEntity<ObjectId> entity) {
		MongoCollection<Document> collection = null;
		Bson filter = null;
		if (entity.getClass() == KanbanColumn.class) {
			collection = mongoOperations.getCollection("columns");
			filter = Filters.and(eq("idBoard", idReference), Filters.and(gte("position", desiredPosition)));
		} else {
			collection = mongoOperations.getCollection("cards");
			filter = Filters.and(eq("idColumn", idReference), Filters.and(gte("position", desiredPosition)));
		}
		BulkOperations bulkOperations = mongoOperations.bulkOps(BulkMode.ORDERED, entity.getClass().toString());
		List<WriteModel<Document>> writes = new ArrayList<WriteModel<Document>>();
		writes.add(new UpdateManyModel<Document>(filter, new Document("$inc", new Document("position", 1l))));
		BulkWriteResult bulkWriteResult = collection.bulkWrite(writes);
	}

	public void decreaseAllPositionsGreaterThanCurrentPosition(Long currentPosition, ObjectId idReference,
			BaseEntity<ObjectId> entity) {
		MongoCollection<Document> collection = null;
		Bson filter = null;
		if (entity.getClass() == KanbanColumn.class) {
			collection = mongoOperations.getCollection("columns");
			filter = Filters.and(Filters.eq("idBoard", idReference), Filters.and(gt("position", currentPosition)));
		} else {
			collection = mongoOperations.getCollection("cards");
			filter = Filters.and(Filters.eq("idColumn", idReference), Filters.and(gt("position", currentPosition)));
		}
		BulkOperations bulkOperations = mongoOperations.bulkOps(BulkMode.ORDERED, entity.getClass().toString());
		List<WriteModel<Document>> writes = new ArrayList<WriteModel<Document>>();
		writes.add(new UpdateManyModel<Document>(filter, new Document("$inc", new Document("position", -1l))));
		BulkWriteResult bulkWriteResult = collection.bulkWrite(writes);
	}

	public void decreaseAllPositionsGreaterThanCurrentPositionAndDesiredPosition(Long currentPosition,
			Long desiredPosition, ObjectId idReference, BaseEntity<ObjectId> entity) {
		MongoCollection<Document> collection = null;
		Bson filter = null;
		if (entity.getClass() == KanbanColumn.class) {
			collection = mongoOperations.getCollection("columns");
			filter = Filters.and(Filters.eq("idBoard", idReference),
					Filters.and(gt("position", currentPosition), lte("position", desiredPosition)));
		} else {
			collection = mongoOperations.getCollection("cards");
			filter = Filters.and(Filters.eq("idColumn", idReference),
					Filters.and(gt("position", currentPosition), lte("position", desiredPosition)));
		}
		BulkOperations bulkOperations = mongoOperations.bulkOps(BulkMode.ORDERED, entity.getClass().toString());
		List<WriteModel<Document>> writes = new ArrayList<WriteModel<Document>>();
		writes.add(new UpdateManyModel<Document>(filter, new Document("$inc", new Document("position", -1l))));
		writes.add(new UpdateOneModel<Document>(eq("_id", entity.getId()),
				new Document("$set", new Document("position", desiredPosition))));
		BulkWriteResult bulkWriteResult = collection.bulkWrite(writes);
	}

}
