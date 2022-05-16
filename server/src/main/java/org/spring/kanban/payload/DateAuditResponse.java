package org.spring.kanban.payload;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class DateAuditResponse {
	private String createdBy;
	private Instant createdDate;
	private String updatedBy;
	private Instant updatedDate;
}
