package com.lms.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID>{
	Optional<Users> findByEmail(String email);
	Optional<Users> findByContact(String contact);
}
