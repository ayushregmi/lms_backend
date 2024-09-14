package com.lms.dto;

import java.util.Date;
import java.util.UUID;

import com.lms.entities.RequestCourse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestsDto {

	private UUID requestId;
	private UsersDto user;
	private CoursesDto course;
	private String status;
	private Date requestDate;
	private UUID userId;
	private UUID courseId;
	
	public RequestsDto(RequestCourse requestCourse) {
		this.requestId = requestCourse.getRequestId();
		this.user = new UsersDto(requestCourse.getStudent());
		this.course = new CoursesDto(requestCourse.getCourse());
		this.status = requestCourse.getStatus();
		this.requestDate = requestCourse.getRequestDate();
	}
	
}
