package org.spring.kanban.payload;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.spring.kanban.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

	@NotBlank
	@Size(max = 40)
	private String name;

	@Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Letters, numbers, dashes, and underscores only.")
	@NotBlank
	@Size(min = 4, max = 20)
	private String username;

	@NotBlank
	@Size(max = 40)
	@Email
	private String email;

	@NotBlank
	@Size(min = 6, max = 20)
	private String password;

	private Set<Role> roles = new HashSet<Role>();

}
