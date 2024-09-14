package com.lms.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.entities.Courses;
import com.lms.repositories.CoursesRepository;

@Service
public class CoursesService {

	@Autowired
	private CoursesRepository coursesRepository;
	
	public List<Courses> getAllCourse(){
		return coursesRepository.findAll();
	}
	
	public Courses getCourseById(UUID id) {
		Optional<Courses> course = coursesRepository.findById(id);
		
		if(course.isPresent()) {
			return course.get();
		}
		return null;	
	}

	public boolean addCourse(Courses course) {

		try {
			course.setCreatedDate(new Date());
			course.setCourseId(UUID.randomUUID());
			coursesRepository.save(course);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}
