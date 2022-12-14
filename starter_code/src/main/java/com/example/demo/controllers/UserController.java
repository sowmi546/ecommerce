package com.example.demo.controllers;

import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		logger.info("Trying to get the user with user id {}", id);
		return ResponseEntity.of(userRepository.findById(id));

	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);

			if(user == null) {

				logger.error("Exception : Error with retrieving the user with username {}", username);
				return ResponseEntity.notFound().build();
			}

		logger.info("Success : User with username {} retrieved", username);
		 return ResponseEntity.ok(user);
	}
//	public ResponseEntity<User> findByUserName(@PathVariable String username) {
//		User user = userRepository.findByUsername(username);
//		logger.info("GET user by name {}", user.getUsername());
//		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
//	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
        logger.info("Info Message : Trying to create user with username {}", user.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword().length()<7 ||
		!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			logger.error("Exception : Error with creating the user as password length is less than 7 or password doesn't match, for username {}", createUserRequest.getUsername());
			logger.info("Failed : User creation failed");
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		logger.info("User created successfully with username {}", user.getUsername());
		logger.info("Success : User creation success");
		return ResponseEntity.ok(user);
	}
	
}
