package com.quiztok.kakaobot.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.quiztok.kakaobot.dto.*;
import com.quiztok.kakaobot.dto.common.Button;
import com.quiztok.kakaobot.service.KakaoBotService;
import com.quiztok.kakaobot.util.DataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/kakao")
public class KakaoBotController {

    private final KakaoBotService kakaoBotService;

    private final DataUtils dataUtils;

    @PostMapping(value = "/fallback")
    public SkillResponse fallback(@RequestBody Map<String, Object> params, HttpServletRequest req, HttpServletResponse res) {
        //System.out.printf("폴백알림 : " + params.toString());

        List<ContextValue> contextValues = (List<ContextValue>) params.get("contexts");

        SkillResponse skillResponse = null;

        if(contextValues.size() == 1) {

            Gson gson = new Gson();

            Map contextValue = (Map) contextValues.get(0);

            String contextValueName = contextValue.get("name").toString();

            /*if(contextValueName.equals("TAG_REGISTER")) {
                skillResponse = kakaoBotService.saveQuizTag(params);
            } else if(contextValueName.equals("SOLVE_QUIZ_NO_TAG")) {

            } else*/ if(contextValueName.equals("TAG_SEARCH")) {
                skillResponse = kakaoBotService.getTagBySearchKeyword(params);
            }



        }

        return skillResponse;
    }


    /**
     * 태그 등록 알림 스킬 (카카오 챗봇 추천 태그와 태그검색 퀵플라이를 생성함)
     * @param params
     * @param req
     * @param res
     * @return
     */
    //TODO 추천 태그를 조회하는 API 가 필요함
    @PostMapping(value = "/tag-register-info")
    public SkillResponse tagRegisterInfo(@RequestBody Map<String, Object> params, HttpServletRequest req, HttpServletResponse res) {
        //System.out.printf("태그등록알림 : " + params.toString());

        SkillResponse skillResponse = kakaoBotService.tagRegisterInfo();

        return skillResponse;
    }

    /**
     * 설정한 태그를 저장함
     * @param params
     * @return
     */
    @PostMapping(value = "/tag-register")
    public SkillResponse tagRegister(@RequestBody Map<String, Object> params) {
        //System.out.println("태그 등록 ::::::::");
        SkillResponse skillResponse = kakaoBotService.tagRegister(params);
        return skillResponse;
    }

    /**
     * 태그 검색 발화 API
     * @param params
     * @return
     */
    @PostMapping(value = "/tag-search")
    public SkillResponse tagSearch(@RequestBody Map<String, Object> params) {
        SkillResponse skillResponse = kakaoBotService.setSearchTagKeyword(params);
        return skillResponse;
    }

/*    @PostMapping(value = "/tag")
    public SkillResponse saveTag(@RequestBody Map<String, Object> params, HttpServletRequest req, HttpServletResponse res) {

        SkillResponse skillResponse = kakaoBotService.saveQuizTag(params, valueOps, mapper);

        return skillResponse;
    }*/

    /**
     * 퀴즈 정답 확인
     * @param params
     * @return
     */
    @PostMapping(value = "/quiz-answer")
    public SkillResponse getQuizAnswer(@RequestBody Map params) {

        SkillResponse skillResponse = kakaoBotService.getQuizAnswer(params);

        return skillResponse;

/*        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();
        Component component = new Component();
        ListCard listCard = new ListCard();
        components.add(component);
        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);

        QuizDto quiz = (QuizDto) kakaoBotService.getRandomQuiz("둘리");
        ListItem listItem = new ListItem();
        listItem.setTitle(quiz.getTitle());
        if(!StringUtils.isEmpty(quiz.getLink().getUrl())) {
            listItem.setImageUrl(quiz.getLink().getUrl());
        }
        listCard.setHeader(listItem);
        List<ListItem> listItems = new ArrayList<>();

        for(int i = 0; i < quiz.getExamples().size(); i++) {
            listItem = new ListItem();
            listItem.setTitle(quiz.getExamples().get(i).getBody());
            listItems.add(listItem);
        }
        listCard.setItems(listItems);
        component.setListCard(listCard);

        List<Button> buttons = new ArrayList<>();
        Button button = new Button();
        button.setLabel("연결");
        button.setAction("message");
        button.setMessageText("연결");
        buttons.add(button);
        listCard.setButtons(buttons);*/

    }

    /**
     * 퀴즈 불러오기
     * @param params
     * @param req
     * @param res
     * @return
     */
    @PostMapping(value = "/quiz-question")
    public SkillResponse getQuizQuestion(@RequestBody Map params, HttpServletRequest req, HttpServletResponse res) {
//            System.out.println("퀴즈불러오기 : " + params.toString());
        SkillResponse skillResponse = kakaoBotService.getQuiz(params);
        return skillResponse;
    }

    @PostMapping(value = "/multi-outputs")
    public SkillResponse getMultiOutputs(@RequestBody Map params, HttpServletRequest req, HttpServletResponse res) {

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();

        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);

        for(int i = 0; i < 3 ; i++) {
            SimpleText simpleText = new SimpleText();
            simpleText.setText(i + "111");
            Component component = new Component();
            component.setSimpleText(simpleText);
            components.add(component);
        }

//        System.out.println("params1 : " + params.toString());

        return skillResponse;
    }

    /**
     * 인기 컨텐츠 불러오기
     * @param params
     * @return
     */
    @PostMapping(value = "/popular-content")
    public SkillResponse getPopularContent(@RequestBody Map params) {

        Map<String, Object> userRequest = (Map) params.get("userRequest");

        Map<String, Object> user = (Map) userRequest.get("user");

        Map<String, Object> properties = (Map) user.get("properties");

        String plusFriendUserKey = properties.get("plusfriendUserKey").toString();

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();
        List<Button> buttonList = new ArrayList<>();

        Component component = new Component();

        BasicCard basicCard = new BasicCard();
        basicCard.setDescription("지금 퀴즈톡에서 인기있는 콘텐츠를 만나보자! \n\n신박하고 재미있는 퀴즈가 가득 심심한 시간을 알차게 채워줄게~");
        Button button = new Button();
//        button.setMessageText("보러가기");
        button.setAction("webLink");
        button.setLabel("보러가기");
        String webUrl = "https://www.quiztok.com?user-key=" + plusFriendUserKey;
        button.setWebLinkUrl(webUrl);
        buttonList.add(button);
        basicCard.setButtons(buttonList);
        component.setBasicCard(basicCard);
        components.add(component);
        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);

        return skillResponse;
    }

/*    @PostMapping(value = "/point/purchase/goods")
    public SkillResponse purchaseGoodsByPoint(@RequestBody Map params) {

        SkillResponse skillResponse = kakaoBotService.purchaseGoodsByPoint(params,valueOps, mapper);
        return skillResponse;
    }*/

    /**
     * 적립된 포인트 확인
     * @param params
     * @return
     */
    @PostMapping(value = "/point/check")
    public SkillResponse checkEarnPoint(@RequestBody Map params) {
        SkillResponse skillResponse = kakaoBotService.checkEarnPoint(params);
        return skillResponse;
    }

    /**
     * 퀴즈톡 계정 로그인
     * @param params
     * @return
     */
    @PostMapping(value = "/login/quiztok")
    public SkillResponse loginQuiztok(@RequestBody Map params) {
        SkillResponse skillResponse = kakaoBotService.loginQuiztok(params);
        return skillResponse;
    }







}

