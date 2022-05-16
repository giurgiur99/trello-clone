package org.spring.kanban.payload;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
public class JwtResponse {

	private ObjectId id;
	private String username;
	private String email;
	private List<String> roles = new ArrayList<String>();
	private String accessToken;
	private String tokenType;
	
	public JwtResponse(ObjectId id, String username, String email, List<String> roles, String jwt) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.accessToken = jwt;
		this.tokenType = "Bearer";
	}
}
