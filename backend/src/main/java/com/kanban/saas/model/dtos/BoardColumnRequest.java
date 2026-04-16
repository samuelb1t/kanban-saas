package com.kanban.saas.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardColumnRequest {
  @NotBlank(message = "Nome não pode ser vazio")
  private String name;

  @NotNull(message = "Ordem é obrigatória")
  private Integer order;
}
