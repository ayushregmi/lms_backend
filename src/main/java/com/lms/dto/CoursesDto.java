package com.lms.dto;

import java.util.UUID;

import com.lms.entities.Courses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoursesDto {
	private UUID courseId;
	private String imageUrl;
	private String title;
	private String description;
	private UUID userId;
	
	public CoursesDto(Courses course) {
		this.courseId = course.getCourseId();
		this.imageUrl = course.getImageUrl();
		this.title = course.getTitle();
		this.description = course.getDescription();
		this.userId = course.getAuthor().getUserId();
	}
	
}
