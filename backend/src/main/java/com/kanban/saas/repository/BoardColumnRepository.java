package com.kanban.saas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kanban.saas.model.entities.BoardColumn;

@Repository
public interface BoardColumnRepository extends CrudRepository<BoardColumn, Long> {
  List<BoardColumn> findAll();
  List<BoardColumn> findByBoardId(Long boardId);
  Optional<BoardColumn> findById(Long id);
  void delete(BoardColumn column);
}
