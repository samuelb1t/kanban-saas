package com.kanban.saas.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kanban.saas.model.entities.UserWorkspace;
import com.kanban.saas.model.entities.Workspace;

@Repository
public interface UserWorkspaceRepository extends CrudRepository<UserWorkspace, Long> {
  void deleteAllByWorkspace(Workspace workspace);
  Optional<UserWorkspace> findByWorkspaceIdAndUserEmail(Long workspaceId, String email);
}
