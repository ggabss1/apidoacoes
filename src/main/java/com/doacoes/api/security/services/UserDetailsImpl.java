package com.doacoes.api.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.doacoes.api.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String first_name;

  private String last_name;

  private String birth_date;

  private String email;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(Long id, String first_name, String last_name, String birth_date, String email, String password) {
    this.id = id;
    this.first_name = first_name;
    this.last_name = last_name;
    this.birth_date = birth_date;
    this.email = email;
    this.password = password;
  }

  public static UserDetailsImpl build(User user) {
    return new UserDetailsImpl(
        user.getId(),
        user.getFirst_name(),
        user.getLast_name(),
        user.getBirth_date().toString(),
        user.getEmail(),
        user.getPassword()
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  public Long getId() {
    return id;
  }

  public String getFirst_name() {
    return first_name;
  }

  public String getLast_name() {
    return last_name;
  }

  public String getBirth_date() {
    return birth_date;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public String getUsername() {
    return email;
  }
}
