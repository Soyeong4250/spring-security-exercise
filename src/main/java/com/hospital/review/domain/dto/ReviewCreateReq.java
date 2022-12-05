package com.hospital.review.domain.dto;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateReq {

    private Long hospitalId;
    private String title;
    private String content;
    private String userName;

}
