package com.lms.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dto.RequestsDto;
import com.lms.entities.Courses;
import com.lms.entities.RequestCourse;
import com.lms.entities.Users;
import com.lms.services.CoursesService;
import com.lms.services.RequestCourseService;
import com.lms.services.UsersService;

@RestController
@RequestMapping("/api/requests")
public class RequestCourseController {

	@Autowired
	private RequestCourseService requestCourseService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private CoursesService coursesService;

	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<RequestsDto>> getAllRequests() {

		List<RequestsDto> requests = requestCourseService.getAllRequests().stream().map(RequestsDto::new)
				.collect(Collectors.toList());

		return new ResponseEntity<List<RequestsDto>>(requests, HttpStatus.OK);
	}

	@GetMapping("/user/{user_id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<RequestsDto>> getAllByUser(@PathVariable("user_id") UUID userId) {

		List<RequestsDto> requests = requestCourseService.getRequestByStudent(userId).stream().map(RequestsDto::new)
				.collect(Collectors.toList());

		return new ResponseEntity<List<RequestsDto>>(requests, HttpStatus.OK);
	}

	@GetMapping("/course/{course_id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<RequestsDto>> getAllByCourse(@PathVariable("course_id") UUID courseId) {

		List<RequestsDto> requests = requestCourseService.getRequestByCourse(courseId).stream().map(RequestsDto::new)
				.collect(Collectors.toList());

		return new ResponseEntity<List<RequestsDto>>(requests, HttpStatus.OK);
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ROLE_STUDENT')")
	public ResponseEntity<String> requestCourse(@RequestBody RequestsDto req) {

		UUID userId = req.getUserId();
		UUID courseId = req.getCourseId();

		Users u = usersService.getById(userId);

		if (u == null) {
			return new ResponseEntity<String>("User doesn't exist", HttpStatus.NOT_FOUND);
		}

		Courses course = coursesService.getCourseById(courseId);

		if (course == null) {
			return new ResponseEntity<String>("Course doesn't exist", HttpStatus.NOT_FOUND);
		}

		List<RequestCourse> requests = requestCourseService.getRequestByStudent(userId);
		requests = requests.stream().filter(
				request -> request.getCourse().getCourseId().equals(courseId) && !request.getStatus().equals("denied"))
				.collect(Collectors.toList());

		if (requests.isEmpty()) {

			RequestCourse reqCourse = new RequestCourse();

			reqCourse.setCourse(course);
			reqCourse.setStudent(u);

			requestCourseService.createRequest(reqCourse);

			return new ResponseEntity<String>("Course added", HttpStatus.OK);
		}

		return new ResponseEntity<String>("Request already pending", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping("/grant/{request_id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> grantCourseRequest(@PathVariable("request_id") UUID requestId) {

		RequestCourse requestCourse = requestCourseService.getRequestById(requestId);

		if (requestCourse == null) {
			return new ResponseEntity<String>("Invalid request id", HttpStatus.NOT_FOUND);

		}

		if (requestCourse.getStatus().equals("pending")) {
			requestCourse.setStatus("granted");
			requestCourseService.updateRequest(requestCourse);

			Courses course = requestCourse.getCourse();
			UUID userId = requestCourse.getStudent().getUserId();

			boolean success = usersService.addEnrolledCourse(userId, course);
			
			if (success)
				return new ResponseEntity<String>("Course Request Granted", HttpStatus.OK);
			else
				return new ResponseEntity<String>("Error granting course", HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<String>("Course Request is already granted or denied", HttpStatus.CONFLICT);
		}

	}

	@PutMapping("/deny/{request_id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> denyCourseRequest(@PathVariable("request_id") UUID requestId) {

		RequestCourse requestCourse = requestCourseService.getRequestById(requestId);

		if (requestCourse == null) {
			return new ResponseEntity<String>("Invalid request id", HttpStatus.NOT_FOUND);

		}

		if (requestCourse.getStatus().equals("pending")) {
			requestCourse.setStatus("denied");
			requestCourseService.updateRequest(requestCourse);
			return new ResponseEntity<String>("Course Request Denied", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Course Request is already granted or denied", HttpStatus.CONFLICT);
		}

	}

}
