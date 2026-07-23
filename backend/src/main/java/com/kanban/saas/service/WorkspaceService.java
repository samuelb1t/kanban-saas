package com.kanban.saas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kanban.saas.helper.WorkspaceAuthHelper;
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

  private WorkspaceRepository repository;
  private UserRepository userRepository;
  private UserWorkspaceRepository userWorkspaceRepository;
  private WorkspaceMapper mapper;
  private WorkspaceAuthHelper workspaceAuthHelper;

  public WorkspaceService(WorkspaceRepository repository, UserRepository userRepository, UserWorkspaceRepository userWorkspaceRepository, WorkspaceMapper mapper, WorkspaceAuthHelper workspaceAuthHelper){
    this.repository = repository;
    this.userRepository = userRepository;
    this.userWorkspaceRepository = userWorkspaceRepository;
    this.mapper = mapper;
    this.workspaceAuthHelper = workspaceAuthHelper;
  }

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

  public WorkspaceResponse findById(Long id, String email) {
    if (!workspaceAuthHelper.hasAccess(id, email)) {
      throw new Error("Acesso negado ao workspace");
    }

    Optional<Workspace> opWorkspace = repository.findById(id);

    if (opWorkspace.isPresent()) {
      return mapper.toDto(opWorkspace.get());
    }

    return null;
  }

  @Transactional
  public boolean update(Long id, WorkspaceRequest workspaceDto, String email) {
    Optional<Workspace> opWorkspace = repository.findById(id);

    if(opWorkspace.isEmpty()) return false;
    
    if(!workspaceAuthHelper.havePermission(id, email, Role.EDITOR)){
      throw new Error("Você não tem permissão para editar o workspace");
    }

    Workspace workspace = opWorkspace.get();
    workspace.setName(workspaceDto.getName());
    repository.save(workspace);
    return true;
  }

  @Transactional
  public boolean delete(Long id, String email) {
    Optional<Workspace> opWorkspace = repository.findById(id);

    if(opWorkspace.isEmpty()) return false;

    if(!workspaceAuthHelper.havePermission(id, email, Role.OWNER)){
      throw new Error("Você não tem permissão para deletar o workspace");
    }

    Workspace workspace = opWorkspace.get();
    userWorkspaceRepository.deleteAllByWorkspace(workspace);
    repository.delete(workspace);
    return true;
    
  }
}
