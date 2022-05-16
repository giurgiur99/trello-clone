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
public abstract class BaseResponse extends DateAuditResponse {
	private ObjectId id;
	private String name;

	public BaseResponse(ObjectId id, String name, String createdBy, Instant createdDate, String updatedBy,
			Instant updatedDate) {
		super(createdBy, createdDate, updatedBy, updatedDate);
		this.id = id;
		this.name = name;
	}
}
