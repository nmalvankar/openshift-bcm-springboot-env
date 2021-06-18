package com.company.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  public String login(String username, String password) {
    List<Role> roles = null;
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      roles = new ArrayList<Role>();
      roles.add(Role.KIE_SERVER);
      
    } catch (AuthenticationException e) {
        e.printStackTrace();
    }
    return jwtTokenProvider.createToken(username, roles);
  }
}
