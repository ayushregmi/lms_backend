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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dto.CoursesDto;
import com.lms.dto.UsersDto;
import com.lms.entities.Courses;
import com.lms.entities.Users;
import com.lms.services.CoursesService;
import com.lms.services.UsersService;

@RestController
@RequestMapping("/api/courses")
public class CoursesController {

	@Autowired
	private CoursesService coursesService;
	
	@Autowired
	private UsersService usersService;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllCourses(){
		
		List<CoursesDto> courses = coursesService.getAllCourse().stream().map(CoursesDto::new).collect(Collectors.toList());
		
		
		return new ResponseEntity<List<CoursesDto>>(courses, HttpStatus.OK);
	}
	
	@GetMapping("/{course_id}")
	public ResponseEntity<?> getCourseById(@PathVariable("course_id") UUID id){
		
		Courses course = coursesService.getCourseById(id);
		
		if(course != null) {
			return new ResponseEntity<CoursesDto> (new CoursesDto(course), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Course not found", HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER')")
	public ResponseEntity<String> createCourse(@RequestBody CoursesDto courseRequest){
		
		Courses course = new Courses();
		course.setImageUrl(courseRequest.getImageUrl());
		course.setDescription(courseRequest.getDescription());
		course.setTitle(courseRequest.getTitle());
		
		
		Users u = usersService.getById(courseRequest.getUserId());
			
		if(u == null) {
			return new ResponseEntity<String>("User Not Found", HttpStatus.NOT_FOUND);
		}
		
		course.setAuthor(u);
		
		boolean courseAdded = coursesService.addCourse(course);
		
		if(courseAdded) {
			
			
			return new ResponseEntity<String>("Course Created", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);		
	}
	
	@GetMapping("/enrolled/{course_id}")
	public ResponseEntity<?> getEnrolledStudents(@PathVariable("course_id") UUID courseId){
		
		Courses course = this.coursesService.getCourseById(courseId);
		
		if(course != null) {
			
			List<UsersDto> u = course.getStudents().stream().map(UsersDto::new).collect(Collectors.toList());
			
			return new ResponseEntity<List<UsersDto>>(u, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("Invalid course id", HttpStatus.NOT_FOUND);
		
		
	}
	
}
