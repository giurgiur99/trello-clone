package org.spring.kanban;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.spring.kanban.domain.Role;
import org.spring.kanban.domain.RoleName;
import org.spring.kanban.domain.User;
import org.spring.kanban.repository.AttachmentRepository;
import org.spring.kanban.repository.KanbanBoardRepository;
import org.spring.kanban.repository.KanbanCardRepository;
import org.spring.kanban.repository.KanbanColumnRepository;
import org.spring.kanban.repository.UserRepository;
import org.spring.kanban.service.KanbanBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@SpringBootApplication
@EnableMongoAuditing
@ComponentScan(basePackages = "org.spring.kanban")
public class KanbanSpringAppApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KanbanSpringAppApplication.class, args);
	}

	@Configuration
	public static class WebAppConfig implements WebMvcConfigurer {

		@Override
		public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
			Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
			builder.serializerByType(ObjectId.class, new ToStringSerializer());
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(builder.build());
			converters.add(converter);
		}
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	KanbanBoardRepository boardRepository;

	@Autowired
	KanbanColumnRepository columnRepository;

	@Autowired
	KanbanCardRepository cardRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	KanbanBoardService kanbanBoardService;

	@Override
	public void run(String... args) throws Exception {

//		 userRepository.deleteAll();
//		boardRepository.deleteAll();
//		columnRepository.deleteAll();
//		cardRepository.deleteAll();
//		attachmentRepository.deleteAll();

		Role RoleADmin = new Role(RoleName.ROLE_ADMIN);
		Role RoleUser = new Role(RoleName.ROLE_USER);

		Set<Role> ROLE_USER_ADMIN = new HashSet<Role>();
		ROLE_USER_ADMIN.add(RoleADmin);
		ROLE_USER_ADMIN.add(RoleUser);
//		 this.userRepository.deleteAll();
		if (this.userRepository.findByUsername("admin").isEmpty()) {
			User user = new User();
			user.setName("admin");
			user.setUsername("admin");
			user.setPassword(passwordEncoder.encode("@admin"));
			user.setRoles(ROLE_USER_ADMIN);
			userRepository.save(user);
		}

	}
	

}
