package com.quiztok.kakaobot.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Limit {
    private Date StartDate;
    private Date endDate;
}
