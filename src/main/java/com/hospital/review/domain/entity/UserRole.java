package com.hospital.review.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private String name;
}
