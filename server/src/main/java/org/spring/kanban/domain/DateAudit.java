package org.spring.kanban.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(
		value = { "createdBy", "createdDate",
				"updatedBy", "updatedDate" },
		allowGetters = true)
@Getter @Setter
public abstract class DateAudit<U> {

	@CreatedBy
	private U createdBy;

	@CreatedDate
	private Instant createdDate;

	@LastModifiedBy
	private U updatedBy;

	@LastModifiedDate
	private Instant updatedDate;
}
