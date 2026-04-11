package com.kanban.saas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kanban.saas.model.entities.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
  List<Task> findAll();
  List<Task> findByColumn_Id(Long columnId);
  Optional<Task> findById(Long id);
  void delete(Task task);
}
