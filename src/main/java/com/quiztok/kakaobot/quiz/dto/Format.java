package com.quiztok.kakaobot.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Format {
    private QuizType type;
    private OptionType optionType;
    private AnswerType answerType;
    private Integer botType;
    private String body;
}
