package com.lms.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="courses")
public class Courses {
	
	@Id
	@Column(name="course_id", nullable=false)
	private UUID courseId;
	@Column(name="image_url", nullable=false)
	private String imageUrl;
	@Column(name="course_title", nullable=false, unique=true)
	private String title;
	@Column(name="description", nullable=false)
	private String description;
	@Column(name="created_date", nullable=false)
	private Date createdDate;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="author_id", referencedColumnName="user_id", nullable=false)
	@JsonIgnore
	private Users author;
	
	@OneToMany(mappedBy="course", fetch=FetchType.EAGER)
	@JsonIgnore
	private List<RequestCourse> requests;
	
	@ManyToMany(mappedBy="enrolledCourses", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Users> students;
	
}
