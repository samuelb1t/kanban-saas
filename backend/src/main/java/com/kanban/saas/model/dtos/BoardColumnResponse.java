package com.kanban.saas.model.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardColumnResponse {
  private Long id;
  private String name;
  private Integer order;
  private Long boardId;
  private List<TaskResponse> tasks;
}
