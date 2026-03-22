package com.kanban.saas.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserWorkspaceId implements Serializable{
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "workspace_id")
  private Long workspaceId;
}
