package org.spring.kanban.controller;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.spring.kanban.configuration.CurrentUser;
import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.payload.ApiResponse;
import org.spring.kanban.payload.BoardDetailsResponse;
import org.spring.kanban.payload.NameRequest;
import org.spring.kanban.payload.SuccessResponse;
import org.spring.kanban.service.KanbanBoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/board")
public class KanbanBoardController {

	private final KanbanBoardService kanbanBoardService;

	public KanbanBoardController(KanbanBoardService kanbanBoardService) {
		this.kanbanBoardService = kanbanBoardService;
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<BoardDetailsResponse> findBoardDetails(@PathVariable("id") ObjectId id,
			@CurrentUser UserPrincipal currentUser) {
		return new ResponseEntity<>(this.kanbanBoardService.findBoardById(id, currentUser),
				HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteById(@PathVariable("id") ObjectId id,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanBoardService.deleteById(id, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Board Deleted Successfully"), HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> saveBoard(@RequestBody NameRequest name) {
		this.kanbanBoardService.saveBoard(name);
		return new ResponseEntity<>(new SuccessResponse("Board Saved Successfully"), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> updateName(@PathVariable("id") ObjectId id, @Valid @RequestBody NameRequest name,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanBoardService.updateName(id, name, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Board Updated Successfully"), HttpStatus.OK);
	}

}
