package com.kanban.saas.model.dtos;

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
}
