package com.kanban.saas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kanban.saas.mappers.WorkspaceMapper;
import com.kanban.saas.model.dtos.WorkspaceRequest;
import com.kanban.saas.model.dtos.WorkspaceResponse;
import com.kanban.saas.model.entities.User;
import com.kanban.saas.model.entities.UserWorkspace;
import com.kanban.saas.model.entities.Workspace;
import com.kanban.saas.model.enums.Role;
import com.kanban.saas.repository.UserRepository;
import com.kanban.saas.repository.WorkspaceRepository;

import jakarta.transaction.Transactional;

import com.kanban.saas.repository.UserWorkspaceRepository;

@Service
public class WorkspaceService {

  @Autowired
  private WorkspaceRepository repository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserWorkspaceRepository userWorkspaceRepository;

  @Autowired
  private WorkspaceMapper mapper;

  public void save(WorkspaceRequest workspaceDto, Long userId) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if(optionalUser.isEmpty()){throw new Error();}
    User user = optionalUser.get();

    Workspace workspace = mapper.toDomain(workspaceDto);
    workspace = repository.save(workspace);

    UserWorkspace userWorkspace = new UserWorkspace(user, workspace, Role.OWNER);
    userWorkspaceRepository.save(userWorkspace);
  }

  public List<WorkspaceResponse> getWorkspaces() {
    return repository.findAll().stream().map(w -> mapper.toDto(w)).toList();
  }

  public WorkspaceResponse findById(Long id) {
    Optional<Workspace> opWorkspace = repository.findById(id);

    if (opWorkspace.isPresent()) {
      return mapper.toDto(opWorkspace.get());
    }

    return null;
  }

  @Transactional
  public boolean update(Long id, WorkspaceRequest workspaceDto) {
    Optional<Workspace> opWorkspace = repository.findById(id);

    if (opWorkspace.isPresent()) {
      Workspace workspace = opWorkspace.get();
      workspace.setName(workspaceDto.getName());
      repository.save(workspace);
      return true;
    }

    return false;
  }

  @Transactional
  public boolean delete(Long id) {
    Optional<Workspace> opWorkspace = repository.findById(id);

    if (opWorkspace.isPresent()) {
      Workspace workspace = opWorkspace.get();

      userWorkspaceRepository.deleteAllByWorkspace(workspace);

      repository.delete(workspace);
      return true;
    }

    return false;
  }
}
