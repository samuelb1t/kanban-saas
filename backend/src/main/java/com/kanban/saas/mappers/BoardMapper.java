package com.kanban.saas.mappers;

import org.springframework.stereotype.Component;

import com.kanban.saas.model.dtos.BoardRequest;
import com.kanban.saas.model.dtos.BoardResponse;
import com.kanban.saas.model.entities.Board;

@Component
public class BoardMapper {

  private final BoardColumnMapper boardColumnMapper;

  public BoardMapper(BoardColumnMapper boardColumnMapper) {
    this.boardColumnMapper = boardColumnMapper;
  }

  public BoardResponse toDto(Board board) {
    return new BoardResponse(board.getId(), board.getName(), board.getWorkspace().getId(), board.getColumns().stream().map(c -> boardColumnMapper.toDto(c)).toList());
  }

  public Board toDomain(BoardRequest boardDto) {
    Board board = new Board();
    board.setName(boardDto.getName());

    return board;
  }
}
