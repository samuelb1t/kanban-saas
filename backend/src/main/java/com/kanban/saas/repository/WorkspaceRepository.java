package com.kanban.saas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kanban.saas.model.entities.Workspace;

@Repository
public interface WorkspaceRepository extends CrudRepository<Workspace, Long> {
  List<Workspace> findAll();
  Optional<Workspace> findById(Long id);
  void delete(Workspace workspace);
}
