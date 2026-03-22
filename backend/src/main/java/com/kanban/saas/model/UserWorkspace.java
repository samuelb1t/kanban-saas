package com.kanban.saas.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "User_Workspace")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserWorkspace {

  @EmbeddedId
  private UserWorkspaceId id;

  @ManyToOne
  private User user;

  @ManyToOne
  private Workspace workspace;

  private Role role;
}
