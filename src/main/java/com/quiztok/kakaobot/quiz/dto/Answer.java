package com.quiztok.kakaobot.quiz.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

//import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Answer {
    @NotBlank
    private String text;
    private String exampleId;
}
