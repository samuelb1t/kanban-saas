package com.kanban.saas.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kanban.saas.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
  List<User> findAll();
}
