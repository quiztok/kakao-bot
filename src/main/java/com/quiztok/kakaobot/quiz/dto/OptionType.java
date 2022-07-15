package com.quiztok.kakaobot.quiz.dto;

public enum OptionType {
    TEXT(1, "TextOption")
    , IMAGE(2, "ImageOption")
    , TEXT_FIVE(3, "TextOption_five")
    ;

    private final int code;
    private final String desc;

    private OptionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Return the integer value of this status code.
     */
    public int value() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }
}
