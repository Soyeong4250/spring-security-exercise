package com.hospital.review.domain.dto;

import com.hospital.review.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserJoinReqDto {
    private String userName;
    private String password;
    private String email;

    public User toEntity(String password) {
        return User.builder()
                .userName(this.userName)
                .password(password)
                .email(this.email)
                .build();
    }
}
