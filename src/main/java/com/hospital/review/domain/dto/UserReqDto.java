package com.hospital.review.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserReqDto {
    private String userName;
    private String password;
    private String email;
}
