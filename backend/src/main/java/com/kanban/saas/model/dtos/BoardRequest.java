package com.kanban.saas.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardRequest {
  @NotBlank(message = "Nome não pode ser vazio")
  private String name;

  @NotNull(message = "Workspace é obrigatório")
  private Long workspaceId;
}
