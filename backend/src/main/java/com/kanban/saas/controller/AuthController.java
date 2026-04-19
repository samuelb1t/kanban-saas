package com.kanban.saas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.saas.model.dtos.UserRequest;
import com.kanban.saas.model.dtos.UserResponse;
import com.kanban.saas.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserService service;

  @PostMapping
  public ResponseEntity<Void> save(@Valid @RequestBody UserRequest userDto){
    System.out.println("=============================");
    System.out.println("Saving user: " + userDto.getName());
    service.save(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> findAll(){
    return ResponseEntity.ok(service.getUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable Long id){
    UserResponse user = service.findById(id);
    if(user != null)
      return ResponseEntity.ok(user);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UserRequest userDto){
    if (service.update(id, userDto))
      return ResponseEntity.ok().build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    if (service.delete(id))
      return ResponseEntity.noContent().build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }
}
