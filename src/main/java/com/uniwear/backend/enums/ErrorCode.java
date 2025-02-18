package com.uniwear.backend.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode {

  // COMMON
  EXPIRED_CODE(400, "C0001", "Expired Code");
  

  private int status;
  private String code;
  private String message;
  private String detail;

  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  public String getKey() {
    return this.code;
  }

  public String getValue() {
    return this.message;
  }
}