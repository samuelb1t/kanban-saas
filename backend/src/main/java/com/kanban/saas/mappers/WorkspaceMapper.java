package com.kanban.saas.mappers;

import org.springframework.stereotype.Component;

import com.kanban.saas.model.dtos.WorkspaceRequest;
import com.kanban.saas.model.dtos.WorkspaceResponse;
import com.kanban.saas.model.entities.Workspace;

@Component
public class WorkspaceMapper {

  private final BoardMapper boardMapper;

  public WorkspaceMapper(BoardMapper boardMapper) {
    this.boardMapper = boardMapper;
  }

  public WorkspaceResponse toDto(Workspace workspace) {
    return new WorkspaceResponse(workspace.getId(), workspace.getName(),workspace.getBoards().stream().map(b -> boardMapper.toDto(b)).toList());
  }

  public Workspace toDomain(WorkspaceRequest workspaceDto) {
    Workspace workspace = new Workspace();
    workspace.setName(workspaceDto.getName());
    return workspace;
  }
}
