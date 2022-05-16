package org.spring.kanban.domain;

import org.bson.types.ObjectId;
import org.spring.kanban.payload.CoverDetail;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KanbanCard extends BaseEntity<ObjectId> {

	private static final long serialVersionUID = 1L;

	private ObjectId idColumn;

	private String description;

	private CoverDetail cover;

	private ObjectId idBoard;

	private Long position;

	public KanbanCard(String name, ObjectId idColumn, String description, CoverDetail cover, ObjectId idBoard,
			Long position) {
		super(idBoard, name);
		this.idColumn = idColumn;
		this.description = description;
		this.cover = cover;
		this.idBoard = idBoard;
		this.position = position;
	}


	public KanbanCard(String name, Long position) {
		super(name);
		this.position = position;
	}

}
