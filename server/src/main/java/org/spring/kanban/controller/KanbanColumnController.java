package org.spring.kanban.controller;

import org.bson.types.ObjectId;
import org.spring.kanban.configuration.CurrentUser;
import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.domain.KanbanColumn;
import org.spring.kanban.payload.ApiResponse;
import org.spring.kanban.payload.NameRequest;
import org.spring.kanban.payload.SuccessResponse;
import org.spring.kanban.service.KanbanColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/column")
public class KanbanColumnController {

	private final KanbanColumnService kanbanColumnService;

	public KanbanColumnController(KanbanColumnService kanbanColumnService) {
		this.kanbanColumnService = kanbanColumnService;
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<KanbanColumn> findById(@PathVariable("id") ObjectId id,
			@CurrentUser UserPrincipal currentUser) {
		return new ResponseEntity<KanbanColumn>(this.kanbanColumnService.findById(id, currentUser), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteById(@PathVariable("id") ObjectId id,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanColumnService.deleteById(id, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Column Deleted Successfully"), HttpStatus.OK);
	}

	@PostMapping(value = "/{idBoard}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> saveColumn(@PathVariable("idBoard") ObjectId idBoard, @RequestBody NameRequest name,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanColumnService.saveName(idBoard, name, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Column Saved Successfully"), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> updateName(@PathVariable("id") ObjectId id, @RequestBody NameRequest name,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanColumnService.updateName(id, name, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Column Updated Successfully"), HttpStatus.OK);
	}
	
	@PutMapping(value = "/{idBoard}/{currentPosition}/{desiredPosition}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> MovePositions(@PathVariable("idBoard") ObjectId idBoard,@PathVariable("currentPosition") Long currentPosition
			,@PathVariable("desiredPosition") Long desiredPosition,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanColumnService.moveColumnPosition(currentPosition, desiredPosition, idBoard);
		return new ResponseEntity<>(new SuccessResponse("Column Updated Successfully"), HttpStatus.OK);
	}

}
