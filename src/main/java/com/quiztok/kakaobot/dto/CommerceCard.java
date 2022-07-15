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
public class CommerceCard {
    private String description;
    private int price;
    private String currency;
    private int discount;
    private int discountRate;
    private int discountedPrice;
    private List<Thumbnail> thumbnail;
    private Profile profile;
    private List<Button> buttons;
}
