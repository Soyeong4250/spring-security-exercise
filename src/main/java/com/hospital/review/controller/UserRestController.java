package com.hospital.review.controller;

import com.hospital.review.domain.Response;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserReqDto;
import com.hospital.review.domain.dto.UserResDto;
import com.hospital.review.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserResDto> join(@RequestBody UserReqDto userReqDto) {
        log.info("userName : {}", userReqDto.getUserName());
        UserDto userDto = userService.join(userReqDto);
        return Response.success(new UserResDto(userDto.getUserName(), userDto.getEmail()));
    }
}
