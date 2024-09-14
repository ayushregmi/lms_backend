package com.lms.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.lms.entities.Users;

import lombok.Data;


@Data
public class AuthResponse {	
	private UUID userId;
	private String email;
	private String token;
	private Date joinDate;
	private Date expiryDate;
	
	public AuthResponse(Users user) {
		this.userId = user.getUserId();
		this.email = user.getEmail();
		this.joinDate = user.getJoinDate();
		this.expiryDate = user.getExpiryDate();
//		this.roles = Arrays.asList(user.getRole()).stream()
//				.map(SimpleGrantedAuthority::new)
//				.collect(Collectors.toList());
	}
	
}
