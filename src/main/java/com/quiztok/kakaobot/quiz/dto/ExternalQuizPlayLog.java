package com.quiztok.kakaobot.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalQuizPlayLog {
    private String id;
    private String userId;
    private String externalCode;
    private QuizDto playQuiz;
    private Choice choice;
    private int dailyPoint;
    private String createTime;
}
