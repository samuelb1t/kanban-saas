package com.kanban.saas.mappers;

import org.springframework.stereotype.Component;

import com.kanban.saas.model.dtos.WorkspaceRequest;
import com.kanban.saas.model.dtos.WorkspaceResponse;
import com.kanban.saas.model.entities.Workspace;

@Component
public class WorkspaceMapper {

  public WorkspaceResponse toDto(Workspace workspace) {
    return new WorkspaceResponse(workspace.getId(), workspace.getName());
  }

  public Workspace toDomain(WorkspaceRequest workspaceDto) {
    Workspace workspace = new Workspace();
    workspace.setName(workspaceDto.getName());
    return workspace;
  }
}
