package com.quiztok.kakaobot.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class Button {
    private String label;
    private String action;
    private String webLinkUrl;
    private String messageText;
    private String phoneNumber;
    private String blockId;
    private Map<String, Object> extra;
}
