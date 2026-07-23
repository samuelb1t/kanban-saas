package com.kanban.saas.mappers;

import org.springframework.stereotype.Component;

import com.kanban.saas.model.dtos.BoardColumnRequest;
import com.kanban.saas.model.dtos.BoardColumnResponse;
import com.kanban.saas.model.entities.BoardColumn;

@Component
public class BoardColumnMapper {

  private final TaskMapper taskMapper;

  public BoardColumnMapper(TaskMapper taskMapper) {
    this.taskMapper = taskMapper;
  }

  public BoardColumnResponse toDto(BoardColumn column){
    return new BoardColumnResponse(column.getId(), column.getName(), column.getOrder(), column.getBoard().getId(), column.getTasks().stream().map(t -> taskMapper.toDto(t)).toList());
  }

  public BoardColumn toDomain(BoardColumnRequest req){
    BoardColumn column = new BoardColumn();
    column.setName(req.getName());
    if(req.getOrder() != null) column.setOrder(req.getOrder());
    return column;
  }
}
