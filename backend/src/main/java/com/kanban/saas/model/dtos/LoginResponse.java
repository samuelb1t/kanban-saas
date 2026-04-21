package com.kanban.saas.model.dtos;

public record LoginResponse(
  String token,
  UserResponse user
) {} 