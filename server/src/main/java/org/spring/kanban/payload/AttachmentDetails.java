package org.spring.kanban.payload;

import java.time.Instant;

import org.bson.types.Binary;
import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AttachmentDetails extends BaseResponse {

	private Binary file;
	private String contentType;
	private ObjectId idCard;
	private ObjectId idBoard;

	public AttachmentDetails(ObjectId id, String name, String createdBy, Instant createdDate, String updatedBy,
			Instant updatedDate, Binary file, String contentType, ObjectId idCard, ObjectId idBoard) {
		super(id, name, createdBy, createdDate, updatedBy, updatedDate);
		this.contentType = contentType;
		this.file = file;
		this.idCard = idCard;
		this.idBoard = idBoard;
	}
}
