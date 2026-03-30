package com.kanban.saas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.saas.model.dtos.UserResponse;
import com.kanban.saas.model.entities.User;
import com.kanban.saas.service.UserService;

@RestController("/auth")
public class AuthController {

  @Autowired
  private UserService service;

  @PostMapping("/auth")
  public boolean save(@RequestBody User user){
    return service.save(user);
  }

  @GetMapping("/get")
  public ResponseEntity<List<UserResponse>> findAll(){
    return ResponseEntity.ok(service.getUsers());
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable Long id){
    return ResponseEntity.ok(service.findById(id));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
