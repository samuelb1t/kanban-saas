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

import com.kanban.saas.model.dtos.BoardColumnRequest;
import com.kanban.saas.model.dtos.BoardColumnResponse;
import com.kanban.saas.service.BoardColumnService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/columns")
public class BoardColumnController {

  @Autowired
  private BoardColumnService service;

  @PostMapping
  public ResponseEntity<Void> save(@Valid @RequestBody BoardColumnRequest dto){
    service.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @GetMapping
  public ResponseEntity<List<BoardColumnResponse>> findAll(){
    return ResponseEntity.ok(service.getColumns());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardColumnResponse> findById(@PathVariable Long id){
    BoardColumnResponse res = service.findById(id);
    if(res != null) return ResponseEntity.ok(res);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody BoardColumnRequest dto){
    if(service.update(id, dto)) return ResponseEntity.ok().build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    if(service.delete(id)) return ResponseEntity.noContent().build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }
}
