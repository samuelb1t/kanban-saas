package com.kanban.saas.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  public String generateToken(String email){
    return Jwts.builder()
      .subject(email)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
      .signWith(getKey())
      .compact();
  }

  public String extractEmail(String token){
    return Jwts.parser()
      .verifyWith(getKey())
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getSubject();
  }

  public boolean isValid(String token){
    try{
      extractEmail(token);
      return true;
    }
    catch(Exception e){
      return false;
    }
  }

  private SecretKey getKey(){
    return Keys.hmacShaKeyFor(secret.getBytes());
  }
}
