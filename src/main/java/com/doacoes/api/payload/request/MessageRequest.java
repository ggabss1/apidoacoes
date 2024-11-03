package com.doacoes.api.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;

public class MessageRequest {
  @NotBlank
  private String text;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}