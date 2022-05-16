package org.spring.kanban.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "columns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KanbanColumn extends BaseEntity<ObjectId> {

	private static final long serialVersionUID = 1L;

	private ObjectId idBoard;
	private Long position;

	public KanbanColumn(String nome, Long position) {
		super(nome);
		this.position = position;
	}

	public KanbanColumn(String name, ObjectId idBoard, Long position) {
		super(name);
		this.idBoard = idBoard;
		this.position = position;
	}
}
