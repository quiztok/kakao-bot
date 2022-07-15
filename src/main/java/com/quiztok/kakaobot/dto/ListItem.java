package com.quiztok.kakaobot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quiztok.kakaobot.dto.common.Link;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListItem {
    private String title;
    private String description;
    private String imageUrl;
    private Link link;
}
