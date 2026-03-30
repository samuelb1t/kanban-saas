package com.kanban.saas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kanban.saas.mappers.UserMapper;
import com.kanban.saas.model.dtos.UserResponse;
import com.kanban.saas.model.entities.User;
import com.kanban.saas.repository.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private UserMapper mapper;

  public boolean save(User user){
    repository.save(user);
    return true;
  }

  public List<UserResponse> getUsers(){
    return repository.findAll().stream().map(u -> mapper.toDto(u)).toList();
  }

  public UserResponse findById(Long id){
    Optional<User> opUser = repository.findById(id);

    if(opUser.isPresent()){return mapper.toDto(opUser.get());}

    return null;
  }

  public boolean delete(Long id){
    Optional<User> opUser = repository.findById(id);

    if(opUser.isPresent()){
      User user = opUser.get();
      repository.delete(user);
      return true;
    }

    return false;
  }
}
