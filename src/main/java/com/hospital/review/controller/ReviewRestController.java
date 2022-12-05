package com.hospital.review.controller;

import com.hospital.review.domain.dto.ReviewCreateReq;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewRestController {

    @PostMapping
    public String write(@RequestBody ReviewCreateReq dto) {
        return  "리뷰 등록 성공";
    }
}
