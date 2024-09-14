package com.lms.entities;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCourse {

	@Id()
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="request_id")
	private UUID requestId;
	
	@Column(name="request_date")
	private Date requestDate;
	
	@Column(name="status")
	private String status;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="student_id", referencedColumnName="user_id", nullable=false)
	@JsonIgnore
	private Users student;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="course_id", referencedColumnName="course_id", nullable=false)
	@JsonIgnore
	private Courses course;
	
}
