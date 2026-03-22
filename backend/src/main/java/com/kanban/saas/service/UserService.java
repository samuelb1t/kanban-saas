package com.kanban.saas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kanban.saas.model.User;
import com.kanban.saas.repository.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  public List<User> getUsers(){
    return repository.findAll();
  }
}
