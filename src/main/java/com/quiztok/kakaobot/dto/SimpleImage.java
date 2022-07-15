package com.quiztok.kakaobot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleImage {
    private String imageUrl;
    private String altText;
}
