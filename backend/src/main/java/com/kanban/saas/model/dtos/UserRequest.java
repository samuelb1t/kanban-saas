package com.kanban.saas.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequest {
  @NotBlank(message = "Nome não pode ser vazio")
  private String name;

  @NotBlank(message = "Email não pode ser vazio")
  @Email(message = "Email inválido")
  private String email;

  @NotBlank(message = "Senha não pode ser vazia")
  private String password;
}
