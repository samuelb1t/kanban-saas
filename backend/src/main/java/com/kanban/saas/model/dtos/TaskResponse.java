package com.kanban.saas.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskResponse {
  private Long id;
  private String name;
  private String description;
  private Integer order;
  private Long columnId;
}
