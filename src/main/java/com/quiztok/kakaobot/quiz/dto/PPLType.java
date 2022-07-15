package com.quiztok.kakaobot.quiz.dto;

/**
 * The enum Ppl type.
 */
public enum PPLType {
	/**
	 * Basic ppl type.
	 */
	BASIC(0, "basic")
	,
	/**
	 * Cpm ppl type.
	 */
	CPM(100, "cpm")
	,
	/**
	 * Cpc ppl type.
	 */
	CPC(200, "cpc");
	
	
	private final int code;
	private final String desc;
	
	private PPLType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	/**
	 * Value int.
	 *
	 * @return the int
	 */
	public int value() {
		return this.code;
	}

	/**
	 * Desc string.
	 *
	 * @return the string
	 */
	public String desc() {
		return this.desc;
	}

	@Override
	public String toString() {
		return Integer.toString(this.code);
	}

	/**
	 * Value of ppl type.
	 *
	 * @param requestType the request type
	 * @return the ppl type
	 */
	public static PPLType valueOf(int requestType) {
		for (PPLType status : values()) {
			if (status.code == requestType) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + requestType + "]");
	}


}
