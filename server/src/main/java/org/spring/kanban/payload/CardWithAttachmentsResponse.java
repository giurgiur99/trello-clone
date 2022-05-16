package org.spring.kanban.payload;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CardWithAttachmentsResponse extends BaseResponse {

	private String description;
	private ObjectId idColumn;
	private CoverDetail cover;
	private List<AttachmentDetails> attachments = new ArrayList<AttachmentDetails>();

	public CardWithAttachmentsResponse(ObjectId id, String name, String description, String createdBy,
			Instant createdDate, String updatedBy, Instant updatedDate, ObjectId idColumn, CoverDetail cover,
			List<AttachmentDetails> attachments) {
		super(id, name, createdBy, createdDate, updatedBy, updatedDate);
		this.description = description;
		this.idColumn = idColumn;
		this.cover = cover;
		this.attachments = attachments;
	}

}
