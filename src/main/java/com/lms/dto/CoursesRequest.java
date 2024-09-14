package com.lms.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CoursesRequest {
	private String imageUrl;
	private String title;
	private String description;
	private UUID userId;
}
