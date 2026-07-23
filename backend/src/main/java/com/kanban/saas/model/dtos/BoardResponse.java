package com.kanban.saas.model.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardResponse {
  private Long id;
  private String name;
  private Long workspaceId;
  private List<BoardColumnResponse> columns;
}
