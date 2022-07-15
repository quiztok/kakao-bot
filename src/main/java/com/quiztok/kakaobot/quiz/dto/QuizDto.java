package com.quiztok.kakaobot.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizDto {

    private String id;
    private Link link;
    private Format format;
    @NotBlank
    private String title;
    private List<@Valid Example> examples;

    private @Valid Answer answer;
    private Hint hint;
    private List<@Valid Tag> tags;
    private Author creator;
    private Author editor;
    private Status status;
//    private Limit limit;
    @NotBlank
    private String explanation;
    private String explanationUrl;

    private String text;

    private int count;

    private int point;

    private PPL ppl;

}
