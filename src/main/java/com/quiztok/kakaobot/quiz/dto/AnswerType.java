package com.quiztok.kakaobot.quiz.dto;

public enum AnswerType {
    MULTIPLE_CHOICE(1, "multipleChoice")
    , OX(2, "OX")
    , SHORT_ANSWER(3, "shortAnswer")
    , ANSWER(4, "shortAnswerQuestion")
    , RESULT(100, "result")
    ;

    private final int code;
    private final String desc;

    private AnswerType(int code, String desc) {
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
    public static AnswerType valueOf(int requestType) {
        for (AnswerType status : values()) {
            if (status.code == requestType) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + requestType + "]");
    }


}
