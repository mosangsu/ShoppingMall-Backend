package com.uniwear.backend.enums;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_NOT_PERMITTED("0"), ROLE_USER("1"), ROLE_MANAGER("2"), ROLE_ADMIN("3");
    
    private final String value;

    Role(String value) { this.value = value; }
}