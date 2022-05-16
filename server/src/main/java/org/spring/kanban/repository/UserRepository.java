package org.spring.kanban.repository;

import java.util.Optional;

import org.spring.kanban.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Object> {
	Optional<User> findByUsername(String username);
	Optional<User> deleteByUsername(String username);
}
