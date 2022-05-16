package org.spring.kanban.payload;

import java.time.Instant;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.spring.kanban.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class UserProfileResponse {

	private ObjectId id;
	private String name;
	private String username;
	private String email;
	private Binary profilePicture;	
	private Instant joinedAt;

	public UserProfileResponse(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.profilePicture = user.getProfilePicture();
		this.joinedAt = user.getCreatedDate();
	}

}
