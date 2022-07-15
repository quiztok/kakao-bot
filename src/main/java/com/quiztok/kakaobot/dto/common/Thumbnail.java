package com.quiztok.kakaobot.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Thumbnail {
    private String imageUrl;
    private Link link;
    private boolean fixedRatio;
    private int width;
    private int height;
}
