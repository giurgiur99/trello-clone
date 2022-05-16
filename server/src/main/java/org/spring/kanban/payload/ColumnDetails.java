package org.spring.kanban.payload;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ColumnDetails extends BaseResponse {

	private ObjectId idBoard;

	private Long position;

	List<CardDetails> cards = new ArrayList<CardDetails>();

	public ColumnDetails(ObjectId id, String name, String createdBy, Instant createdDate, String updatedBy,
			Instant updatedDate, ObjectId idBoard, Long position, List<CardDetails> cards) {
		super(id, name, createdBy, createdDate, updatedBy, updatedDate);
		this.position = position;
		this.idBoard = idBoard;
		this.cards = cards;
	}

}
