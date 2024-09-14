package com.lms.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.entities.Courses;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, UUID>{

}
