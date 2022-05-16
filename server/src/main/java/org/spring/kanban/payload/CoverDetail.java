package org.spring.kanban.payload;



import java.time.Instant;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.spring.kanban.domain.Attachment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class CoverDetail extends DateAuditResponse {

	private ObjectId idAttachment;
	private String name;
	private Binary file;
	private String contentType;
	private ObjectId idCard;
	private ObjectId idBoard;

	public CoverDetail(ObjectId idAttachment, String name, String createdBy, Instant createdDate, String updatedBy,
			Instant updatedDate, Binary file, String contentType, ObjectId idCard, ObjectId idBoard) {
		super(createdBy, createdDate, updatedBy, updatedDate);
		this.idAttachment = idAttachment;
		this.name = name;
		this.file = file;
		this.contentType = contentType;
		this.idCard = idCard;
		this.idBoard = idBoard;
	}

	public void setAttachment(Attachment attachment) {
		this.idAttachment = attachment.getId();
		this.name = attachment.getName();
		this.file = attachment.getFile();
		this.contentType = attachment.getContentType();
		this.idCard = attachment.getIdCard();
		this.idBoard = attachment.getIdBoard();
		setCreatedBy(attachment.getCreatedBy());
		setCreatedDate(attachment.getCreatedDate());
		setUpdatedBy(attachment.getUpdatedBy());
		setUpdatedDate(attachment.getUpdatedDate());
	}

}
