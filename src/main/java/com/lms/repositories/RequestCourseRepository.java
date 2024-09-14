package com.lms.repositories;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lms.entities.RequestCourse;

@Repository
public interface RequestCourseRepository extends JpaRepository<RequestCourse, UUID>{
	
	@Query("SELECT req FROM RequestCourse req WHERE req.student.userId = %:userId%")
	List <RequestCourse> findAllByStudentId(UUID userId);
	
	@Query("SELECT req FROM RequestCourse req WHERE req.course.courseId = %:courseId%")
	List <RequestCourse> findAllByCourseId(UUID courseId);
	
}
