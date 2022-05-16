package org.spring.kanban.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.domain.Role;
import org.spring.kanban.domain.RoleName;
import org.spring.kanban.domain.User;
import org.spring.kanban.exception.EmailAlreadyExistException;
import org.spring.kanban.exception.EmailInvalidException;
import org.spring.kanban.exception.EntityAlreadyExistException;
import org.spring.kanban.exception.EntityNotFoundException;
import org.spring.kanban.exception.PermissionErrorException;
import org.spring.kanban.payload.EmailRequest;
import org.spring.kanban.payload.NameRequest;
import org.spring.kanban.payload.PasswordRequest;
import org.spring.kanban.payload.SignUpRequest;
import org.spring.kanban.payload.UserSummaryResponse;
import org.spring.kanban.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserSummaryResponse currentUser(UserPrincipal currentUser) {
		return new UserSummaryResponse(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
	}

	public User findUserProfile(String username) {
		return this.userRepository.findByUsername(username)
				.orElseThrow(() -> new EntityNotFoundException("User", "id", username));
	}

	public User changePasswordById(ObjectId id, PasswordRequest password, UserPrincipal currentUser) {
		User user = this.findById(id);
		boolean oldPasswordIsEqualCurrentPassword = this.passwordEncoder.matches(password.getOldPassword(),
				user.getPassword());
		if (user.getId().equals(currentUser.getId()) && oldPasswordIsEqualCurrentPassword) {

			boolean newPasswordIsEqualCurrentPassword = this.passwordEncoder.matches(password.getNewPassword(),
					user.getPassword());
			if (newPasswordIsEqualCurrentPassword) {
				throw new BadCredentialsException("Password must be different");
			} else {
				user.setPassword(this.passwordEncoder.encode(password.getNewPassword()));
				return this.userRepository.save(user);
			}
		} else {
			throw new RuntimeException("You cannot change this password");
		}
	}

	public User changeEmailById(ObjectId id, EmailRequest emailReqeust, UserPrincipal currentUser) {
		User user = this.findById(id);
		boolean currentPasswordIsEqualPasswordUserLogged = this.passwordEncoder
				.matches(emailReqeust.getCurrentPassword(), user.getPassword());
		if (user.getId().equals(currentUser.getId()) && currentPasswordIsEqualPasswordUserLogged) {
			if (emailReqeust.getEmail().equalsIgnoreCase(user.getEmail())) {
				throw new EmailAlreadyExistException("You entered the current email address.");
			}
			user.setEmail(emailReqeust.getEmail());
			return this.userRepository.save(user);
		} else {
			throw new EmailInvalidException("You entered an incorrect password. Please try again");
		}
	}

	public User saveUser(SignUpRequest signUpRequest) {
		this.userRepository.findByUsername(signUpRequest.getUsername()).ifPresent(user -> {
			throw new EntityAlreadyExistException("Username alread exist");
		});
		Set<Role> roles = signUpRequest.getRoles();
		if (roles.isEmpty()) {
			Role userRole = new Role(RoleName.ROLE_USER);
			roles.add(userRole);
		} else {
			roles.forEach(role -> {
				switch (role.getName()) {
				case ROLE_ADMIN:
					Role adminRole = new Role(RoleName.ROLE_ADMIN);
					roles.add(adminRole);
					break;
				case ROLE_USER:
					Role userRole = new Role(RoleName.ROLE_USER);
					roles.add(userRole);
					break;
				}
			});
		}
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
				passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail());
		user.setRoles(roles);
		log.info("User Saved Successfully");
		return this.userRepository.save(user);
	}

	public void deleteById(ObjectId id, UserPrincipal currentUser) {
		this.findById(id);
		for (GrantedAuthority role : currentUser.getAuthorities()) {
			if (role.getAuthority().equalsIgnoreCase("ROLE_ADMIN")
					|| role.getAuthority().equalsIgnoreCase("ROLE_USER") && currentUser.getId().equals(id)) {
				log.info("User Deleted Successfully");
				this.userRepository.deleteById(id);
				break;
			} else {
				log.error("This ID cannot be deleted");
				throw new RuntimeException("This ID cannot be deleted");
			}
		}
	}

	public void deleteByUsername(String username, UserPrincipal currentUser) {
		this.findByUsername(username);
		for (GrantedAuthority role : currentUser.getAuthorities()) {
			if (role.getAuthority().equalsIgnoreCase("ROLE_ADMIN")
					|| role.getAuthority().equalsIgnoreCase("ROLE_USER") && currentUser.getUsername().equals(username)){
				log.info("User Deleted Successfully");
				this.userRepository.deleteByUsername(username);
				break;
			} else {
				log.error("This UsER cannot be deleted");
				throw new RuntimeException("This User cannot be deleted");
			}
		}
	}

	public User setProfilePicture(ObjectId id, MultipartFile profilePicture, UserPrincipal currentUser) {
		return this.userRepository.findById(id).map(user -> {
			User userisOwner = this.isOwner(user, currentUser);
			try {
				if ((profilePicture.getContentType().equalsIgnoreCase("image/jpeg")
						|| profilePicture.getContentType().equalsIgnoreCase("image/png")
						|| profilePicture.getContentType().equalsIgnoreCase("image/jpg"))) {
					userisOwner.setProfilePicture(new Binary(BsonBinarySubType.BINARY, profilePicture.getBytes()));
					this.userRepository.save(userisOwner);
				} else {
					throw new RuntimeException("Cover is not a JPEG,PNG or JPG");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return userisOwner;
		}).orElseThrow(() -> new EntityNotFoundException("User", "id", id));

	}

	public User findById(ObjectId id) {
		return this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", "id", id));
	}

	public User findByUsername(String username) {
		return this.userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User", "id", username));
	}

	public List<User> findAll() {
		return this.userRepository.findAll();
	}

	public User updateName(ObjectId id, NameRequest nameRequest, UserPrincipal currentUser) {
		return this.userRepository.findById(id).map(user -> {
			User userIsOwner = this.isOwner(user, currentUser);
			userIsOwner.setName(nameRequest.getName());
			User usenameUpdated = this.userRepository.save(userIsOwner);
			return usenameUpdated;
		}).orElseThrow(() -> new EntityNotFoundException("User", "id", id));
	}

	public User isOwner(User user, UserPrincipal currentUser) {
		if (user.getUsername().equalsIgnoreCase(currentUser.getUsername())) {
			return user;
		} else {
			throw new PermissionErrorException("User");
		}
	}

}
