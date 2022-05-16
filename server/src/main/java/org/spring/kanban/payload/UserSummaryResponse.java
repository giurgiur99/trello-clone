package org.spring.kanban.payload;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class UserSummaryResponse {

	private ObjectId id;
	private String username;
	private String name;
}
