package com.quiztok.kakaobot.dto.common;

import com.quiztok.kakaobot.dto.ContextValue;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContextControl {
    private List<ContextValue> values;
}
