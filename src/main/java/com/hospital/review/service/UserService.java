package com.hospital.review.service;

import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserReqDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserDto join(UserReqDto request) {
        return new UserDto(request.getUserName(), request.getPassword(), request.getEmail());
    }
}
