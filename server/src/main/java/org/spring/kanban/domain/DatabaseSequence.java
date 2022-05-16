package org.spring.kanban.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "database_sequence")
@Getter @Setter
public class DatabaseSequence {

	@Id
	private String id;

	private long sequence;

}
