package ru.achernyavskiy0n.orderservice.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

  private Boolean success;
  private int status;
  private String message;
}