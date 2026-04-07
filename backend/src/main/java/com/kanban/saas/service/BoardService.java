package com.kanban.saas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kanban.saas.mappers.BoardMapper;
import com.kanban.saas.model.dtos.BoardRequest;
import com.kanban.saas.model.dtos.BoardResponse;
import com.kanban.saas.model.entities.Board;
import com.kanban.saas.model.entities.Workspace;
import com.kanban.saas.repository.BoardRepository;
import com.kanban.saas.repository.WorkspaceRepository;

@Service
public class BoardService {

  @Autowired
  private BoardRepository repository;

  @Autowired
  private WorkspaceRepository workspaceRepository;

  @Autowired
  private BoardMapper mapper;

  public void save(BoardRequest boardDto) {
    if (!workspaceRepository.existsById(boardDto.getWorkspaceId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace não encontrado", null);
    }

    Board board = mapper.toDomain(boardDto);
    repository.save(board);
  }

  public List<BoardResponse> getBoards() {
    return repository.findAll().stream().map(b -> mapper.toDto(b)).toList();
  }

  public List<BoardResponse> getBoardsByWorkspace(Long workspaceId) {
    return repository.findByWorkspace_Id(workspaceId).stream().map(b -> mapper.toDto(b)).toList();
  }

  public BoardResponse findById(Long id) {
    Optional<Board> opBoard = repository.findById(id);

    if (opBoard.isPresent()) {
      return mapper.toDto(opBoard.get());
    }

    return null;
  }

  public boolean update(Long id, BoardRequest boardDto) {
    Optional<Board> opBoard = repository.findById(id);

    if (opBoard.isPresent() && workspaceRepository.existsById(boardDto.getWorkspaceId())) {
      Board board = opBoard.get();
      board.setName(boardDto.getName());

      Workspace workspace = new Workspace();
      workspace.setId(boardDto.getWorkspaceId());
      board.setWorkspace(workspace);

      repository.save(board);
      return true;
    }

    return false;
  }

  public boolean delete(Long id) {
    Optional<Board> opBoard = repository.findById(id);

    if (opBoard.isPresent()) {
      Board board = opBoard.get();
      repository.delete(board);
      return true;
    }

    return false;
  }
}
