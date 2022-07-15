package com.quiztok.kakaobot.quiz.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

//import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Example {
    private String id;
    @NotBlank
    private String body;

}
