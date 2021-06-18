package com.company.service;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  KIE_SERVER, REST_ALL;

  public String getAuthority() {
    return name();
  }

}
