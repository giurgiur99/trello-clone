package org.spring.kanban.domain;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Document(value = "attachments")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Attachment extends BaseEntity<ObjectId> {

	private static final long serialVersionUID = 1L;
	private Binary file;
	private String contentType;
	private ObjectId idCard;
	private ObjectId idBoard;

}
