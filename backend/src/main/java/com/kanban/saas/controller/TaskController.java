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

import com.kanban.saas.model.dtos.TaskRequest;
import com.kanban.saas.model.dtos.TaskResponse;
import com.kanban.saas.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private TaskService service;

  @PostMapping
  public ResponseEntity<Void> save(@Valid @RequestBody TaskRequest dto){
    service.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @GetMapping
  public ResponseEntity<List<TaskResponse>> findAll(){
    return ResponseEntity.ok(service.getTasks());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponse> findById(@PathVariable Long id){
    TaskResponse res = service.findById(id);
    if(res != null) return ResponseEntity.ok(res);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @GetMapping("/column/{columnId}/tasks")
  public ResponseEntity<List<TaskResponse>> findByColumnId(@PathVariable Long columnId){
    List<TaskResponse> res = service.findByColumnId(columnId);
    if(res != null) return ResponseEntity.ok(res);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody TaskRequest dto){
    if(service.update(id, dto)) return ResponseEntity.ok().build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    if(service.delete(id)) return ResponseEntity.noContent().build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }
}
