package org.spring.kanban.domain;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class BaseEntity<U> extends DateAudit<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private U id;

	@NotBlank
	private String name;

	public BaseEntity(String name) {
		super();
		this.name = name;
	}



}
