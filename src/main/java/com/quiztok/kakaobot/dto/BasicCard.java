package com.quiztok.kakaobot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quiztok.kakaobot.dto.common.Button;
import com.quiztok.kakaobot.dto.common.Thumbnail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicCard {
    private String title;
    private String description;
    private Thumbnail thumbnail;
    private BasicCardProfile profile;
    private Social social;
    private List<Button> buttons;
}
