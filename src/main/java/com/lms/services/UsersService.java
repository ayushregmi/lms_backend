package com.lms.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.dto.UserInfoDetails;
import com.lms.entities.Courses;
import com.lms.entities.Users;
import com.lms.repositories.UsersRepository;

@Service
public class UsersService implements UserDetailsService {
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public List<Users> getAllUsers() {
		return usersRepository.findAll();
	}

	public Users getById(UUID id) {
		Optional<Users> user = usersRepository.findById(id);

		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public Users getByContact(String contact) {
		Optional<Users> user = usersRepository.findByContact(contact);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public Users getByEmail(String email) {
		Optional<Users> user = usersRepository.findByEmail(email);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public Users addUser(Users user) {

		user.setUserId(UUID.randomUUID());
		user.setJoinDate(new Date());
		user.setActiveStatus(true);
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));

		if (user.getExpiryDate() == null) {
			ZonedDateTime joinDateZoned = ZonedDateTime.ofInstant(user.getJoinDate().toInstant(),
					ZoneId.systemDefault());
			LocalDate joinDate = joinDateZoned.toLocalDate();

			LocalDate expiryDate = joinDate.plusMonths(3);

			ZonedDateTime expiryZonedDateTime = expiryDate.atStartOfDay(ZoneId.systemDefault());
			Date expiryDateDate = Date.from(expiryZonedDateTime.toInstant());

			user.setExpiryDate(expiryDateDate);
		}

		Users savedUser = usersRepository.save(user);

		return savedUser;
	}

	public boolean addEnrolledCourse(UUID userId, Courses course) {
		Users u = this.getById(userId);

		if (u != null) {
			List<Courses> enrolledCourses;
			if (u.getEnrolledCourses() == null) {
				enrolledCourses = new ArrayList<Courses>();
			} else {
				enrolledCourses = u.getEnrolledCourses();
			}
			enrolledCourses.add(course);
			u.setEnrolledCourses(enrolledCourses);

			usersRepository.save(u);
			return true;
		}
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<Users> user = usersRepository.findByEmail(email);

		return user.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + email));

	}

}
