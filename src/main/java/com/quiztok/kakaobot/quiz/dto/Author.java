package com.quiztok.kakaobot.quiz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {
    private String id;
    private String email;
    private String name;
//    private Date time;
    private List<Tag> tags;
    private String imgKey;



}
