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

import com.kanban.saas.model.dtos.BoardRequest;
import com.kanban.saas.model.dtos.BoardResponse;
import com.kanban.saas.service.BoardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping
public class BoardController {

  @Autowired
  private BoardService service;

  @PostMapping("/workspaces/{workspaceId}/boards")
  public ResponseEntity<Void> save(@Valid @RequestBody BoardRequest boardDto, @PathVariable Long workspaceId){
    service.save(boardDto, workspaceId);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @GetMapping("/boards")
  public ResponseEntity<List<BoardResponse>> findAll(){
    return ResponseEntity.ok(service.getBoards());
  }

  // @GetMapping("/{id}")
  // public ResponseEntity<BoardResponse> findById(@PathVariable Long id){
  //   BoardResponse board = service.findById(id);
  //   if(board != null)
  //     return ResponseEntity.ok(board);
  //   return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  // }

  @GetMapping("/workspaces/{workspaceId}/boards")
  public ResponseEntity<List<BoardResponse>> boardsByWorkspace(@PathVariable Long workspaceId){
    return ResponseEntity.ok(service.getBoardsByWorkspace(workspaceId));
  }

  @PutMapping("/boards/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody BoardRequest boardDto){
    if (service.update(id, boardDto))
      return ResponseEntity.ok().build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  } 

  @DeleteMapping("boards/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    if (service.delete(id))
      return ResponseEntity.noContent().build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }
}
