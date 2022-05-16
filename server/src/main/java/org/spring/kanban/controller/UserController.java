package org.spring.kanban.controller;

import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.spring.kanban.configuration.CurrentUser;
import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.domain.KanbanBoard;
import org.spring.kanban.domain.User;
import org.spring.kanban.payload.ApiResponse;
import org.spring.kanban.payload.EmailRequest;
import org.spring.kanban.payload.NameRequest;
import org.spring.kanban.payload.PasswordRequest;
import org.spring.kanban.payload.SignUpRequest;
import org.spring.kanban.payload.SuccessResponse;
import org.spring.kanban.payload.UserProfileResponse;
import org.spring.kanban.payload.UserSummaryResponse;
import org.spring.kanban.service.KanbanBoardService;
import org.spring.kanban.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/user")

public class UserController {

	private final UserService userService;
	private final KanbanBoardService kanbanBoardService;

	public UserController(UserService userService, KanbanBoardService kanbanBoardService) {
		this.userService = userService;
		this.kanbanBoardService = kanbanBoardService;
	}

	@GetMapping(value = "/me")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<UserSummaryResponse> currentUser(@CurrentUser UserPrincipal currentUser) {
		return new ResponseEntity<UserSummaryResponse>(this.userService.currentUser(currentUser), HttpStatus.OK);
	}

	@GetMapping(value = "/{username}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> findUserProfile(@PathVariable("username") String username) {
		return new ResponseEntity<>(new UserProfileResponse(this.userService.findUserProfile(username)), HttpStatus.OK);
	}

	@PutMapping(value = "/{id}/changePassword")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> changePasswordById(@PathVariable("id") ObjectId id,
			@RequestBody PasswordRequest passwordRequest, @CurrentUser UserPrincipal currentUser) {
		this.userService.changePasswordById(id, passwordRequest, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Password Updated Successfully"), HttpStatus.OK);
	}

	@PutMapping(value = "/{id}/changeEmail")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> changeEmailById(@PathVariable("id") ObjectId id,
			@RequestBody @Valid EmailRequest emailRequest, @CurrentUser UserPrincipal currentUser) {
		this.userService.changeEmailById(id, emailRequest, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Email Updated Successfully"), HttpStatus.OK);
	}


	@GetMapping(value = "/{username}/boards")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<List<KanbanBoard>> findAllBoards(@PathVariable("username") String username,
			@CurrentUser UserPrincipal currentUser) {
		return new ResponseEntity<>(this.kanbanBoardService.findAllByCreatedBy(username, currentUser), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteById(@PathVariable("id") ObjectId id,
			@CurrentUser UserPrincipal currentUser) {
		this.userService.deleteById(id, currentUser);
		return new ResponseEntity<>(new SuccessResponse("User Deleted Successfully"), HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> saveUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		this.userService.saveUser(signUpRequest);
		return new ResponseEntity<>(new SuccessResponse("User Created Successfully"), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> updateName(@PathVariable("id") ObjectId id,
			@Valid @RequestBody NameRequest nameRequest, @CurrentUser UserPrincipal currentUser) {
		this.userService.updateName(id, nameRequest, currentUser);
		return new ResponseEntity<>(new SuccessResponse("User Updated Successfully"), HttpStatus.OK);
	}

	@PutMapping(value = "/{id}/setProfilePicture")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> setProfilePicture(@PathVariable("id") ObjectId id,
			@RequestParam("profilePicture") MultipartFile profilePicture, @CurrentUser UserPrincipal currentUser) {
		this.userService.setProfilePicture(id, profilePicture, currentUser);
		return new ResponseEntity<>(new SuccessResponse("User Updated Successfully"), HttpStatus.OK);
	}
}
