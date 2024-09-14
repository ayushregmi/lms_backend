package com.lms.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable=false, name="user_id")
	private UUID userId;
	
	@Column(nullable=false, name="full_name")
	private String fullName;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private String contact;
	
	@Column(nullable=false)
	private String role;
	
	@Column(name="join_date", nullable=false)
	private Date joinDate;
	
	@Column(name="expiry_date", nullable=false)
	private Date expiryDate;
	
	@Column(name="active_status", nullable=false)
	private boolean activeStatus;
	
	
	@OneToMany(mappedBy="author", fetch=FetchType.EAGER)
	@JsonIgnore
	private List<Courses> coursesCreated;
	
	@OneToMany(mappedBy="student", fetch=FetchType.EAGER)
	@JsonIgnore
	private List<RequestCourse> requests;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		name="enrolled_courses",
		joinColumns=@JoinColumn(name="user_id"),
		inverseJoinColumns=@JoinColumn(name="course_id")
	)
	@JsonIgnore
	private List<Courses> enrolledCourses;
	
}
