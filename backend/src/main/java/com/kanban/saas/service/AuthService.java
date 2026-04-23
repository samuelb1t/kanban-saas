package com.kanban.saas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kanban.saas.config.JwtUtil;
import com.kanban.saas.mappers.UserMapper;
import com.kanban.saas.model.dtos.LoginRequest;
import com.kanban.saas.model.dtos.LoginResponse;
import com.kanban.saas.model.dtos.UserRequest;
import com.kanban.saas.model.dtos.UserResponse;
import com.kanban.saas.model.entities.User;
import com.kanban.saas.repository.UserRepository;

@Service
public class AuthService {

  private final UserRepository repository;
  private final UserMapper mapper;
  private final PasswordEncoder encoder;
  private final JwtUtil jwtUtil;

  public AuthService(UserRepository repository, UserMapper mapper, PasswordEncoder encoder, JwtUtil jwtUtil) {
    this.repository = repository;
    this.mapper = mapper;
    this.encoder = encoder;
    this.jwtUtil = jwtUtil;
  }

  public void save(UserRequest userDto){
    User user = mapper.toDomain(userDto);
    user.setPassword(encoder.encode(userDto.password()));
    try{
      repository.save(user);
    }
    catch(DataIntegrityViolationException e){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado",null);
    }
  }

  public LoginResponse login(LoginRequest request){

    Optional<User> opUser = repository.findByEmail(request.email());

    if(opUser.isEmpty()){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Senha inválida");
    }

    User user = opUser.get();

    if(!encoder.matches(request.password(), user.getPassword()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida",null);

    String token = jwtUtil.generateToken(user.getEmail());

    return new LoginResponse(token, mapper.toDto(user));
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
      user.setName(userDto.name());
      user.setEmail(userDto.email());
      user.setPassword(userDto.password());

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
