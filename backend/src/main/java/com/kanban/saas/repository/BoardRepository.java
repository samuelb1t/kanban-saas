package com.kanban.saas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kanban.saas.model.entities.Board;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {
  List<Board> findAll();
  List<Board> findByWorkspace_Id(Long workspaceId);
  Optional<Board> findById(Long id);
  void delete(Board board);
}
