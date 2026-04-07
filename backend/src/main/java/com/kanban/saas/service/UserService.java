package com.kanban.saas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kanban.saas.mappers.UserMapper;
import com.kanban.saas.model.dtos.UserRequest;
import com.kanban.saas.model.dtos.UserResponse;
import com.kanban.saas.model.entities.User;
import com.kanban.saas.repository.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private UserMapper mapper;

  public void save(UserRequest userDto){
    User user = mapper.toDomain(userDto);
    try{
      repository.save(user);
    }
    catch(DataIntegrityViolationException e){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado",null);
    }
  }

  public List<UserResponse> getUsers(){
    return repository.findAll().stream().map(u -> mapper.toDto(u)).toList();
  }

  public UserResponse findById(Long id){
    Optional<User> opUser = repository.findById(id);

    if(opUser.isPresent()){return mapper.toDto(opUser.get());}

    return null;
  }

  public boolean update(Long id, UserRequest userDto){
    Optional<User> opUser = repository.findById(id);

    if(opUser.isPresent()){
      User user = opUser.get();
      user.setName(userDto.getName());
      user.setEmail(userDto.getEmail());
      user.setPassword(userDto.getPassword());

      try{
        repository.save(user);
        return true;
      }
      catch(DataIntegrityViolationException e){
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado",null);
      }
    }

    return false;
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
