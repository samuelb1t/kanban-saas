package com.kanban.saas.mappers;

import org.springframework.stereotype.Component;

import com.kanban.saas.model.dtos.BoardRequest;
import com.kanban.saas.model.dtos.BoardResponse;
import com.kanban.saas.model.entities.Board;
import com.kanban.saas.model.entities.Workspace;

@Component
public class BoardMapper {

  public BoardResponse toDto(Board board) {
    Long workspaceId = board.getWorkspace() != null ? board.getWorkspace().getId() : null;
    return new BoardResponse(board.getId(), board.getName(), workspaceId);
  }

  public Board toDomain(BoardRequest boardDto) {
    Board board = new Board();
    board.setName(boardDto.getName());

    Workspace workspace = new Workspace();
    workspace.setId(boardDto.getWorkspaceId());
    board.setWorkspace(workspace);

    return board;
  }
}
