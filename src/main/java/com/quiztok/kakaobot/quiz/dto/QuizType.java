package com.quiztok.kakaobot.quiz.dto;

public enum QuizType {
    TEXT(1, "TextQuestion")
    , IMAGE(2, "ImageQuestion")
    , VIDEO(3, "VideoQuestion")
    , RESULT(100, "result")
    ;

    private final int code;
    private final String desc;

    private QuizType(int code, String desc) {
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


    /**
     * Return a string representation of this status code.
     */
    @Override
    public String toString() {
        return Integer.toString(this.code);
    }


    /**
     * Return the enum constant of this type with the specified numeric value.
     * @param requestType the numeric value of the enum to be returned
     * @return the enum constant with the specified numeric value
     * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
     */
    public static QuizType valueOf(int requestType) {
        for (QuizType status : values()) {
            if (status.code == requestType) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + requestType + "]");
    }
}
