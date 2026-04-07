package com.kanban.saas.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.kanban.saas.model.enums.Role;

@Entity
@Table(name = "User_Workspace")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserWorkspace {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @MapsId("workspaceId")
  @JoinColumn(name = "workspace_id")
  private Workspace workspace;

  private Role role;

  public UserWorkspace(User user, Workspace workspace, Role role){
    this.user = user;
    this.workspace = workspace;
    this.role = role;
  }
}
