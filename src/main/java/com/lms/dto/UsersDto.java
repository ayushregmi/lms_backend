package com.lms.dto;

import java.util.Date;
import java.util.UUID;

import com.lms.entities.Users;

import lombok.Data;

@Data
public class UsersDto {
	private UUID userId;
	private String fullName;
	private String email;
	private String contact;
	private Date joinDate;
	private Date expirtyDate;
	private boolean activeStatus;
	private String role;
	
	public UsersDto(Users user) {
		this.userId = user.getUserId();
		this.fullName = user.getFullName();
		this.email = user.getEmail();
		this.contact = user.getContact();
		this.joinDate = user.getJoinDate();
		this.expirtyDate = user.getExpiryDate();
		this.activeStatus = user.isActiveStatus();
		this.role = user.getRole();
	}
	
}
