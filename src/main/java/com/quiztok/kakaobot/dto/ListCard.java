package com.quiztok.kakaobot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quiztok.kakaobot.dto.common.Button;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListCard {
    private ListItem header;
    private List<ListItem> items;
    private List<Button> buttons;
}
