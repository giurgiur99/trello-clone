package org.spring.kanban.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {

	@NotBlank
	@Size(min = 6, max = 20)
	private String oldPassword;
	@NotBlank
	@Size(min = 6, max = 20)
	private String newPassword;
}
