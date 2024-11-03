package com.doacoes.api.models;

import java.util.HashSet;
import java.util.Set;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table( name = "users", 
        uniqueConstraints = { 
          @UniqueConstraint(columnNames = "email") 
        })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 50)
  private String first_name;

  @NotBlank
  @Size(max = 50)
  private String last_name;

  private LocalDate birth_date;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  public User() {
  }

  public User(String first_name, String last_name, String birth_date, String email, String password) {
    this.first_name = first_name;
    this.last_name = last_name;
    this.birth_date = LocalDate.parse(birth_date);
    // this.birth_date = birth_date;
    this.email = email;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirst_name() {
    return first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public String getLast_name() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  public String get_full_name() {
    return first_name + " " + last_name;
  }

  public String getBirth_date() {
    return birth_date.toString();
  }

  public void setBirth_date(String birth_date) {
    this.birth_date = LocalDate.parse(birth_date);
    // this.birth_date = birth_date;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
