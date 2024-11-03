package com.doacoes.api.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;

public class ChatRequest {
  @NotBlank
  private Long user1;

  @NotBlank
  private Long user2;

  public Long getUser1() {
    return user1;
  }

  public void setUser1(Long user1) {
    this.user1 = user1;
  }

  public Long getUser2() {
    return user2;
  }

  public void setUser2(Long user2) {
    this.user2 = user2;
  }
}
