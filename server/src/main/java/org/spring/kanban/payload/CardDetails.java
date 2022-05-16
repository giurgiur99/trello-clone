package org.spring.kanban.payload;

import java.time.Instant;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDetails extends BaseResponse {

	private ObjectId idColumn;
	private ObjectId idBoard;
	private Long position;
	private AttachmentDetails cover;
	private Integer fileSize;

	public CardDetails(ObjectId id, String name, String createdBy, Instant createdDate, String updatedBy,
			Instant updatedDate, ObjectId idColumn, ObjectId idBoard, Long position, AttachmentDetails cover,
			Integer fileSize) {
		super(id, name, createdBy, createdDate, updatedBy, updatedDate);
		this.idColumn = idColumn;
		this.idBoard = idBoard;
		this.position = position;
		this.cover = cover;
		this.fileSize = fileSize;
	}
}
