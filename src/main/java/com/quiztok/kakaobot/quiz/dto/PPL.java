package com.quiztok.kakaobot.quiz.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * The type Ppl.
 */
@Data
public class PPL {
    private PPLType type;

    private Integer id;
    private Company company;
    private String title;
    private Double point;
    private Integer cash;
    private String url;
    private String description;
    private Long line;
    private String lineUrl;
    private Long kakaotalk;
    private String kakaotalkUrl;
    private Long telegram;
    private String telegramUrl;
    private Long inhouse;
    private String inhouseUrl;
    private Integer status = 100;

    private String userId; //대상 사용자 아이디

/*    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date timeStart = new Date();
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date timeEnd = new Date();*/

//    private Date timeStartStr;
//    private Date timeEndStr;

//    private Date createTime;
//    private Date updateTime;
    private String createId;
    private String updateId;
//    private String query;

    private Integer page = 1;
    private Integer limit = 20;

    private Integer lastId;

    /**
     * Gets from.
     *
     * @return the from
     */
    public Integer getFrom() {
        return (page - 1) * 20;
    }

//    private Integer ad_id;
//    private Integer group_id;
//    private Integer campaign_id;
    //new ppl 서버
    private String ad_id;
    private String group_id;
    private String campaign_id;
    private String q_id;

    private String at_id;

    private String linkType;
    private String linkPrm;
    private String linkTitle;

    /**
     * Instantiates a new Ppl.
     */
    public PPL() {
    }

    /**
     * Instantiates a new Ppl.
     *
     * @param id      the id
     * @param type    the type
     * @param company the company
     */
    public PPL(Integer id, PPLType type, Company company) {
        this.id = id;
        this.type = type;
        this.company = company;
    }

    /**
     * Instantiates a new Ppl.
     *
     * @param id           the id
     * @param type         the type
     * @param title        the title
     * @param point        the point
     * @param cash         the cash
     * @param url          the url
     * @param description  the description
     * @param lineUrl      the line url
     * @param kakaotalkUrl the kakaotalk url
     * @param telegramUrl  the telegram url
     * @param inhouseUrl   the inhouse url
     */
    public PPL(Integer id, PPLType type, String title, Double point, Integer cash, String url, String description, String lineUrl, String kakaotalkUrl, String telegramUrl, String inhouseUrl) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.point = point;
        this.cash = cash;
        this.url = url;
        this.description = description;
        this.lineUrl = lineUrl;
        this.kakaotalkUrl = kakaotalkUrl;
        this.telegramUrl = telegramUrl;
        this.inhouseUrl = inhouseUrl;
    }

}
