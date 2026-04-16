package com.kanban.saas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kanban.saas.mappers.BoardColumnMapper;
import com.kanban.saas.model.dtos.BoardColumnRequest;
import com.kanban.saas.model.dtos.BoardColumnResponse;
import com.kanban.saas.model.entities.Board;
import com.kanban.saas.model.entities.BoardColumn;
import com.kanban.saas.repository.BoardColumnRepository;
import com.kanban.saas.repository.BoardRepository;

@Service
public class BoardColumnService {

  @Autowired
  private BoardColumnRepository repository;

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private BoardColumnMapper mapper;

  public void save(BoardColumnRequest req, Long boardId){
    Optional<Board> opBoard = boardRepository.findById(boardId);
    if(opBoard.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board não encontrado", null);

    BoardColumn column = mapper.toDomain(req);
    column.setBoard(opBoard.get());

    try{
      repository.save(column);
    }
    catch(DataIntegrityViolationException e){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflito ao salvar coluna", null);
    }
  }

  public List<BoardColumnResponse> getColumns(){
    return repository.findAll().stream().map(c -> mapper.toDto(c)).toList();
  }

  public List<BoardColumnResponse> getColumnsByBoardId(Long boardId){
    return repository.findByBoardId(boardId).stream().map(c -> mapper.toDto(c)).toList();
  }

  public BoardColumnResponse findById(Long id){
    Optional<BoardColumn> op = repository.findById(id);
    if(op.isPresent()) return mapper.toDto(op.get());
    return null;
  }

  public boolean update(Long id, BoardColumnRequest req){
    Optional<BoardColumn> op = repository.findById(id);
    if(op.isPresent()){
      BoardColumn column = op.get();
      column.setName(req.getName());
      if(req.getOrder() != null) column.setOrder(req.getOrder());

      try{
        repository.save(column);
        return true;
      }
      catch(DataIntegrityViolationException e){
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflito ao atualizar coluna", null);
      }
    }

    return false;
  }

  public boolean delete(Long id){
    Optional<BoardColumn> op = repository.findById(id);
    if(op.isPresent()){
      repository.delete(op.get());
      return true;
    }
    return false;
  }
}
