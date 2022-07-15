package com.quiztok.kakaobot.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {

    private String code;
    private String body;
    private int bannedCount;
    private boolean modify;

}
