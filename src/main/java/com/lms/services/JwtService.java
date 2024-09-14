package com.lms.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtService {

	private String jwtSecret = "dfsakjfsdlkjflkd1234234123423jflkasjdflkasjdflkasjdfl;";
	private int jwtExpirationMs = 600000;

	private UsersService usersService;

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public String getJwtFromHeader(HttpServletRequest request) {

		String token = request.getHeader("Authorization");

		if (token != null && token.startsWith("Bearer ")) {
			return token.substring(7); // Remove Bearer prefix
		}
		return null;
	}
	
	public String generateToken(String email) {	
//		UserInfoDetails userInfoDetails = (UserInfoDetails) this.userInfoService.loadUserByUsername(username);

		Map<String, Object> claims = new HashMap<>();
//		claims.put("user", userInfoDetails);
		return createToken(claims, email);
	}

	private String createToken(Map<String, Object> claims, String email) {
		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(key()).claims().add(claims)
				.and().compact();
	}
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload();
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public boolean validateJwtToken(String authToken, UserDetails userDetails) {
		try {
			System.out.println("Validate");
//            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);

			String username = extractEmail(authToken);

			if (username.equals(userDetails.getUsername()) && !isTokenExpired(authToken)) {

				return true;
			}

		} catch (MalformedJwtException e) {
			System.out.println(e);
		}
		return false;
	}


}
