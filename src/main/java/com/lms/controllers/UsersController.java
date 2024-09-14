package com.lms.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dto.AuthRequest;
import com.lms.dto.AuthResponse;
import com.lms.dto.CoursesDto;
import com.lms.dto.UserInfoDetails;
import com.lms.dto.UsersDto;
import com.lms.entities.Courses;
import com.lms.entities.Users;
import com.lms.services.JwtService;
import com.lms.services.UsersService;

@RestController
@RequestMapping(value = "/api")
public class UsersController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsersService usersService;

	@GetMapping(value = "/users/home")
	public String home() {
		return "Home";
	}

	@GetMapping(value = "/users/user")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String user() {
		return "User";
	}

	@GetMapping(value = "/users/admin")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public Object admin() {

//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		
//		UserInfoDetails userInfo = (UserInfoDetails) auth.getPrincipal();
//		return usersService.getByEmail(userInfo.getUsername());
//		

		return "Admin";
	}

	@GetMapping(value = "/users/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> getAllUsers() {

		List<UsersDto> users = usersService.getAllUsers().stream().map(UsersDto::new).collect(Collectors.toList());
		return new ResponseEntity<List<UsersDto>>(users, HttpStatus.OK);
	}

	@GetMapping(value = "/users/{user_id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> getUserById(@PathVariable("user_id") UUID id) {
		Users u = usersService.getById(id);

		if (u == null) {
			return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UsersDto>(new UsersDto(u), HttpStatus.OK);
	}

	@GetMapping(value = "/users/{user_id}/courses")
	public ResponseEntity<?> getCoursesByAuthor(@PathVariable("user_id") UUID id) {

		Users u = usersService.getById(id);

		if (u == null) {
			return new ResponseEntity<String>("Invalid user id", HttpStatus.NOT_FOUND);
		}

		List<CoursesDto> courses = u.getCoursesCreated().stream().map(CoursesDto::new).collect(Collectors.toList());

		return new ResponseEntity<List<CoursesDto>>(courses, HttpStatus.OK);

	}

	@GetMapping(value = "/users/enrolled/{user_id}")
	public ResponseEntity<?> getEnrolledCourses(@PathVariable("user_id") UUID userId) {

		Users u = this.usersService.getById(userId);

		if (u == null) {
			return new ResponseEntity<String>("Invalid user id", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Courses>>(u.getEnrolledCourses(), HttpStatus.OK);
	}

	@PostMapping(value = "/users/signup")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> signUp(@RequestBody Users user) {

		Users u = usersService.getByEmail(user.getEmail());

		if (u != null) {
			return new ResponseEntity<String>("Email already exists", HttpStatus.CONFLICT);
		}

		u = usersService.getByContact(user.getContact());

		if (u != null) {
			return new ResponseEntity<String>("Contact Number already exists", HttpStatus.CONFLICT);
		}

		u = usersService.addUser(user);

		return new ResponseEntity<String>("User created successfully", HttpStatus.OK);
	}

	@PostMapping(value = "/users/signin")
	public ResponseEntity<?> signIn(@RequestBody AuthRequest authRequest) {

		Authentication authentication;

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		} catch (AuthenticationException exception) {
			Map<String, Object> map = new HashMap<>();
			map.put("message", "Bad credentials");
			map.put("status", false);
			return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
		}

		SecurityContextHolder.getContext();
		UserInfoDetails userInfoDetail = (UserInfoDetails) authentication.getPrincipal();
		String email = userInfoDetail.getUsername();

		String jwtToken = jwtService.generateToken(email);
		Users user = this.usersService.getByEmail(email);

		AuthResponse authResponse = new AuthResponse(user);
		authResponse.setToken(jwtToken);

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
	}

}
