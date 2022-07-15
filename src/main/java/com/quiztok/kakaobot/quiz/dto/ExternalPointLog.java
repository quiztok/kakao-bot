package com.quiztok.kakaobot.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalPointLog {
    private String userId;
    private String externalCode;
    private double point;
    private String createTime;

}
