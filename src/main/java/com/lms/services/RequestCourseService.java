package com.lms.services;

import java.util.List;
import java.util.UUID;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.entities.RequestCourse;
import com.lms.repositories.RequestCourseRepository;
import java.util.Optional;
@Service
public class RequestCourseService {

	@Autowired
	private RequestCourseRepository requestCourseRepository;
	
	public RequestCourse getRequestById(UUID requestId) {
		Optional<RequestCourse>req = requestCourseRepository.findById(requestId);
		
		if(req.isPresent()) {
			return req.get();
		}
		return null;
		
	}
	
	public List<RequestCourse> getAllRequests(){
		return requestCourseRepository.findAll();
	}
	
	public List<RequestCourse> getRequestByStudent(UUID userId){
		return requestCourseRepository.findAllByStudentId(userId);
	}
	
	public List<RequestCourse> getRequestByCourse(UUID courseId){
		return requestCourseRepository.findAllByCourseId(courseId);
	}
	
	public boolean createRequest(RequestCourse requestCourse){
		try {
			
			requestCourse.setRequestDate(new Date());
			requestCourse.setRequestId(UUID.randomUUID());
			requestCourse.setStatus("pending");
			
			requestCourseRepository.save(requestCourse);
			return true;
		}
		catch(Exception e) {
			
		}
		return false;
	}
	
	public void updateRequest(RequestCourse requestCourse) {
		RequestCourse oldRequest = getRequestById(requestCourse.getRequestId());
		
		if(requestCourse.getCourse() != null) {
			oldRequest.setCourse(requestCourse.getCourse());
		}
		
		if(requestCourse.getStudent() != null) {
			oldRequest.setStudent(requestCourse.getStudent());
		}
		
		if(requestCourse.getStatus() != null) {
			oldRequest.setStatus(requestCourse.getStatus());
		}
		
		requestCourseRepository.save(oldRequest);
		
	}
	
	
}
