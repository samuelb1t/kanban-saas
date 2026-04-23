package com.kanban.saas.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kanban.saas.model.entities.User;
import com.kanban.saas.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Optional<User> opUser = userRepository.findByEmail(email);

    if(opUser.isEmpty()){
      throw new UsernameNotFoundException("User not found");
    }

    User user = opUser.get();

    return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
      .password(user.getPassword())
      .authorities("ROLE_USER")
      .build();
  }
  
}
