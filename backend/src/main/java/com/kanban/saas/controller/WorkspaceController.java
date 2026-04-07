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

import com.kanban.saas.model.dtos.WorkspaceRequest;
import com.kanban.saas.model.dtos.WorkspaceResponse;
import com.kanban.saas.service.WorkspaceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

  @Autowired
  private WorkspaceService service;

  @PostMapping("/{userId}")
  public ResponseEntity<Void> save(@Valid @RequestBody WorkspaceRequest workspaceDto, @PathVariable Long userId){
    service.save(workspaceDto, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @GetMapping
  public ResponseEntity<List<WorkspaceResponse>> findAll(){
    return ResponseEntity.ok(service.getWorkspaces());
  }

  @GetMapping("/{id}")
  public ResponseEntity<WorkspaceResponse> findById(@PathVariable Long id){
    WorkspaceResponse workspace = service.findById(id);
    if(workspace != null)
      return ResponseEntity.ok(workspace);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody WorkspaceRequest workspaceDto){
    if (service.update(id, workspaceDto))
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
