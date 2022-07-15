package com.quiztok.kakaobot.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tag {
    @NotBlank
    private String name;
    private Integer count;

    private List<String> quizHistory;
    private Integer cursor;
    private Long quizCount;


}
