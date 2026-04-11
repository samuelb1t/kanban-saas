package com.kanban.saas.mappers;

import org.springframework.stereotype.Component;

import com.kanban.saas.model.dtos.BoardColumnRequest;
import com.kanban.saas.model.dtos.BoardColumnResponse;
import com.kanban.saas.model.entities.BoardColumn;

@Component
public class BoardColumnMapper {

  public BoardColumnResponse toDto(BoardColumn column){
    return new BoardColumnResponse(column.getId(), column.getName(), column.getOrder(), column.getBoard() != null ? column.getBoard().getId() : null);
  }

  public BoardColumn toDomain(BoardColumnRequest req){
    BoardColumn column = new BoardColumn();
    column.setName(req.getName());
    if(req.getOrder() != null) column.setOrder(req.getOrder());
    return column;
  }
}
