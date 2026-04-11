package com.kanban.saas.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskRequest {
  @NotBlank(message = "Nome não pode ser vazio")
  private String name;

  private String description;

  @NotNull(message = "Ordem é obrigatória")
  private Integer order;

  @NotNull(message = "Column é obrigatório")
  private Long columnId;
}
