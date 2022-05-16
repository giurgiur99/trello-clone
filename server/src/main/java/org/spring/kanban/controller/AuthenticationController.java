package org.spring.kanban.controller;

import javax.validation.Valid;

import org.spring.kanban.payload.LoginRequest;
import org.spring.kanban.payload.SignUpRequest;
import org.spring.kanban.payload.SuccessResponse;
import org.spring.kanban.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * Log in service
	 * @param loginRequest
	 * @return
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		return new ResponseEntity<>(this.authenticationService.signin(loginRequest), HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
		this.authenticationService.signup(signUpRequest);
		return new ResponseEntity<>(new SuccessResponse("Registred Successfully"), HttpStatus.OK);
	}
}
