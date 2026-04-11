package com.kanban.saas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kanban.saas.mappers.TaskMapper;
import com.kanban.saas.model.dtos.TaskRequest;
import com.kanban.saas.model.dtos.TaskResponse;
import com.kanban.saas.model.entities.BoardColumn;
import com.kanban.saas.model.entities.Task;
import com.kanban.saas.repository.BoardColumnRepository;
import com.kanban.saas.repository.TaskRepository;

@Service
public class TaskService {

  @Autowired
  private TaskRepository repository;

  @Autowired
  private BoardColumnRepository columnRepository;

  @Autowired
  private TaskMapper mapper;

  public void save(TaskRequest req){
    Optional<BoardColumn> opCol = columnRepository.findById(req.getColumnId());
    if(opCol.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Column não encontrado", null);

    Task task = mapper.toDomain(req);
    task.setColumn(opCol.get());

    try{
      repository.save(task);
    }
    catch(DataIntegrityViolationException e){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflito ao salvar task", null);
    }
  }

  public List<TaskResponse> getTasks(){
    return repository.findAll().stream().map(t -> mapper.toDto(t)).toList();
  }

  public TaskResponse findById(Long id){
    Optional<Task> op = repository.findById(id);
    if(op.isPresent()) return mapper.toDto(op.get());
    return null;
  }

  public boolean update(Long id, TaskRequest req){
    Optional<Task> op = repository.findById(id);
    if(op.isPresent()){
      Task task = op.get();
      task.setName(req.getName());
      task.setDescription(req.getDescription());
      if(req.getOrder() != null) task.setOrder(req.getOrder());

      Optional<BoardColumn> opCol = columnRepository.findById(req.getColumnId());
      if(opCol.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Column não encontrado", null);

      task.setColumn(opCol.get());

      try{
        repository.save(task);
        return true;
      }
      catch(DataIntegrityViolationException e){
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflito ao atualizar task", null);
      }
    }

    return false;
  }

  public boolean delete(Long id){
    Optional<Task> op = repository.findById(id);
    if(op.isPresent()){
      repository.delete(op.get());
      return true;
    }
    return false;
  }
}
