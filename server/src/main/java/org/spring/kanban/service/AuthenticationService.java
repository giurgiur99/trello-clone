package org.spring.kanban.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.domain.Role;
import org.spring.kanban.domain.RoleName;
import org.spring.kanban.domain.User;
import org.spring.kanban.exception.EntityAlreadyExistException;
import org.spring.kanban.payload.JwtResponse;
import org.spring.kanban.payload.LoginRequest;
import org.spring.kanban.payload.SignUpRequest;
import org.spring.kanban.repository.UserRepository;
import org.spring.kanban.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;

	public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository,
			PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtils = jwtUtils;
	}

	public JwtResponse signin(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return new JwtResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles, jwt);
	}
	
	public User signup(SignUpRequest signUpRequest) {
		if (this.userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
			throw new EntityAlreadyExistException("Error: entity with this name exist");
		}
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
				passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail());
		Set<Role> ROLE_USER = new HashSet<Role>();
		ROLE_USER.add(new Role(RoleName.ROLE_USER));
		user.setRoles(ROLE_USER);
		return userRepository.save(user);
	}

}
