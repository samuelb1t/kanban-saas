package com.kanban.saas.mappers;

import org.springframework.stereotype.Component;

import com.kanban.saas.model.dtos.TaskRequest;
import com.kanban.saas.model.dtos.TaskResponse;
import com.kanban.saas.model.entities.Task;

@Component
public class TaskMapper {

  public TaskResponse toDto(Task task){
    return new TaskResponse(task.getId(), task.getName(), task.getDescription(), task.getOrder(), task.getColumn() != null ? task.getColumn().getId() : null);
  }

  public Task toDomain(TaskRequest req){
    Task task = new Task();
    task.setName(req.getName());
    task.setDescription(req.getDescription());
    if(req.getOrder() != null) task.setOrder(req.getOrder());
    return task;
  }
}
