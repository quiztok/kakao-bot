package com.quiztok.kakaobot.quiz.dto;

import lombok.Data;

/**
 * The type Company.
 */
@Data
public class Company {
	private String id;
	private String name;
	private String brandName;
	private Link banner;
	private Link brand;

	/**
	 * Instantiates a new Company.
	 */
	public Company(){}

	/**
	 * Instantiates a new Company.
	 *
	 * @param id        the id
	 * @param name      the name
	 * @param brandName the brand name
	 * @param banner    the banner
	 * @param brand     the brand
	 */
	public Company(String id, String name, String brandName, Link banner, Link brand) {
		this.id = id;
		this.name = name;
		this.brandName = brandName;
		this.banner = banner;
		this.brand = brand;
	}
}
