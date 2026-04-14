package com.kanban.saas.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.kanban.saas.model.entities.UserWorkspace;
import com.kanban.saas.model.entities.Workspace;

@Repository
public interface UserWorkspaceRepository extends CrudRepository<UserWorkspace, Long> {
  void deleteAllByWorkspace(Workspace workspace);
}
