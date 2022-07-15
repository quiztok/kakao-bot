package com.quiztok.kakaobot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Component {

    private SimpleText simpleText;
    private SimpleImage simpleImage;
    private BasicCard basicCard;
    private CommerceCard commerceCard;
    private ListCard listCard;
}
