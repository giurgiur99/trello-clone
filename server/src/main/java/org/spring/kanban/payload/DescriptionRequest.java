package org.spring.kanban.payload;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionRequest {	
	
	@Size(max = 2000)
	private String Description;

}