package org.spring.kanban.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "boards")
@Getter @Setter
@NoArgsConstructor
@ToString
public class KanbanBoard extends BaseEntity<ObjectId> {

	private static final long serialVersionUID = 1L;

	public KanbanBoard(String nome) {
		super(nome);
	}
}
