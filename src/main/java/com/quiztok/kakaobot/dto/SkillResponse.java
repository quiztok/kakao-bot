package com.quiztok.kakaobot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quiztok.kakaobot.dto.common.ContextControl;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillResponse {
    private String version;
    private SkillTemplate template;
    private ContextControl context;
    private Map<String, Object> data;
}
