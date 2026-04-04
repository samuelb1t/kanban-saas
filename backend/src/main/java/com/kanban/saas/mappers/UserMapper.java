package com.kanban.saas.mappers;


import org.springframework.stereotype.Component;

import com.kanban.saas.model.dtos.UserRequest;
import com.kanban.saas.model.dtos.UserResponse;
import com.kanban.saas.model.entities.User;

@Component
public class UserMapper {

  public UserResponse toDto(User user){
    return new UserResponse(user.getName(),user.getEmail(),user.getPassword());
  }

  public User toDomain(UserRequest user){
    return new User(user.getName(),user.getEmail(),user.getPassword());
  }
}
