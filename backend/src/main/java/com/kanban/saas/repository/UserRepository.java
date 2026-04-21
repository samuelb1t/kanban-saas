package com.kanban.saas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kanban.saas.model.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
  List<User> findAll();
  Optional<User> findById(Long id);
  void delete(User user);
  Optional<User> findByEmail(String email);
}
