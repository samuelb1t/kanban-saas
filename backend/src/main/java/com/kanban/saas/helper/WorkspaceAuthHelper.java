package com.kanban.saas.helper;

import java.util.Optional;


import org.springframework.stereotype.Component;

import com.kanban.saas.model.entities.UserWorkspace;
import com.kanban.saas.model.enums.Role;
import com.kanban.saas.repository.UserWorkspaceRepository;

@Component
public class WorkspaceAuthHelper {
  
  private final UserWorkspaceRepository userWorkspaceRepository;

  public WorkspaceAuthHelper(UserWorkspaceRepository userWorkspaceRepository){
    this.userWorkspaceRepository = userWorkspaceRepository;
  }

  public boolean hasAccess(Long workspaceId, String email) {
    Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByWorkspaceIdAndUserEmail(workspaceId, email);
    return userWorkspace.isPresent();
  }

  public boolean havePermission(Long workspaceId, String email, Role role){
    Optional<UserWorkspace> userWorkspace = userWorkspaceRepository.findByWorkspaceIdAndUserEmail(workspaceId, email);

    if(userWorkspace.isEmpty()) return false;

    return hasMinRole(userWorkspace.get().getRole(), role);
  }

  private boolean hasMinRole(Role role, Role minRole){
    return role.ordinal() <= minRole.ordinal();
  }
}
