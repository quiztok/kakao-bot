package com.quiztok.kakaobot.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quiztok.kakaobot.channel.service.ChannelService;
import com.quiztok.kakaobot.dto.*;
import com.quiztok.kakaobot.dto.common.Button;
import com.quiztok.kakaobot.dto.common.ContextControl;
import com.quiztok.kakaobot.dto.common.Link;
import com.quiztok.kakaobot.dto.common.Thumbnail;
import com.quiztok.kakaobot.point.service.PointService;
import com.quiztok.kakaobot.quiz.dto.*;
import com.quiztok.kakaobot.quiz.service.QuizService;
import com.quiztok.kakaobot.util.DataUtils;
import com.quiztok.kakaobot.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;


@Service
@RequiredArgsConstructor
public class KakaoBotService {

    private final DataUtils dataUtils;

    private final RestTemplateUtil restTemplateUtil;

    private final HttpSession httpSession;

    @Value(value = "${quiztok.sso.url}")
    private String ssoUrl;

    @Value(value = "${quiztok.channel.url}")
    private String quizChannelUrl;

    @Value(value = "${quiztok.quiz-cache.url}")
    private String quizCacheUrl;

    private final PointService pointService;

    private final QuizService quizService;

    private final ChannelService channelService;

    private final RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOps;

    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        this.valueOps = redisTemplate.opsForValue();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final String botPrefixKey = "bot";

//    private Map pointPolicyMap;

//    @PostConstruct
//    public void initialize() {
//        pointPolicyMap = pointService.getPointPolicy(valueOps, mapper);
//    }

    public ResponseEntity getRandomQuiz(String tag) {

        ResponseEntity responseEntity = null;

        String url = quizCacheUrl + "/quizzes/bot/random-quiz";

        Map params = new HashMap();

        params.put("tag", tag);

        params = dataUtils.removeEmptyValueFromMap(params);

        try {

            String jsonString = restTemplateUtil.getResponseEntityByParams(null, HttpMethod.GET, url, params, null).getBody();

            Gson gson = new Gson();

            QuizDto result = gson.fromJson(jsonString, new TypeToken<QuizDto>(){}.getType());

            responseEntity = new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return  responseEntity;
    }

/*    public SkillResponse saveQuizTag(Map<String, Object> params) {

        Map<String, Object> userRequest = (Map) params.get("userRequest");

        Map<String, Object> user = (Map) userRequest.get("user");

        Map<String, Object> properties = (Map) user.get("properties");

        String utterance = userRequest.get("utterance").toString();

        String plusFriendUserKey = properties.get("plusfriendUserKey").toString();

        List<QuickReply> quickReplies = new ArrayList<>();

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();
        Component component = new Component();
        components.add(component);
        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);

        SimpleText simpleText = new SimpleText();

        String externalCode = null;

        Map actionMap = (Map) params.get("action");

        if(!ObjectUtils.isEmpty(actionMap)) {
            if(((Map)actionMap.get("params")).containsKey("externalCode")) {
                externalCode = ((Map)actionMap.get("params")).get("externalCode").toString();
            }
        }

        String quizTagCacheKey = botPrefixKey + ":" + "userId:" + plusFriendUserKey + ":externalCode:" + externalCode + ":quizTag";

        try {
            valueOps.set(quizTagCacheKey, utterance);

            simpleText.setText(utterance + "????????? ??????????????????.");
            component.setSimpleText(simpleText);

            QuickReply quickReply = QuickReply.builder()
                    .messageText("????????????")
                    .label("????????????")
                    .action("message")
                    .blockId("61e6600704e36e2c78ce7e4b")
                    .build();
            quickReplies.add(quickReply);
            quickReply = QuickReply.builder()
                    .messageText("????????????")
                    .label("????????????")
                    .action("message")
                    .blockId("61e6600704e36e2c78ce7e4b")
                    .build();
            quickReplies.add(quickReply);
        } catch (Exception e) {
            e.printStackTrace();
            simpleText.setText("?????? ?????? ??? ????????? ??????????????????.\n?????? ????????? ?????? ??????????????????.");
            component.setSimpleText(simpleText);

            QuickReply quickReply = QuickReply.builder()
                    .messageText("????????????")
                    .label("????????????")
                    .action("message")
                    .blockId("61e6600704e36e2c78ce7e4b")
                    .build();
            quickReplies.add(quickReply);
        }

        skillTemplate.setQuickReplies(quickReplies);



        return skillResponse;
    }*/

    public SkillResponse getQuiz(Map<String, Object> params) {

        Map<String, Object> userRequest = (Map) params.get("userRequest");

        Map<String, Object> user = (Map) userRequest.get("user");

        Map<String, Object> properties = (Map) user.get("properties");

        String plusFriendUserKey = properties.get("plusfriendUserKey").toString();

        String externalCode = null;

        Map actionMap = (Map) params.get("action");

        if(!ObjectUtils.isEmpty(actionMap)) {
            if(((Map)actionMap.get("params")).containsKey("externalCode")) {
                externalCode = ((Map)actionMap.get("params")).get("externalCode").toString();
            }
        }

        String quizTagCacheKey = botPrefixKey + ":" + "userId:" + plusFriendUserKey + ":externalCode:" + externalCode + ":" + "quizTag";

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();
        Component component = new Component();
        components.add(component);
        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);
        List<QuickReply> quickReplies = null;
        String quizTag = null;

        try {
            quizTag = valueOps.get(quizTagCacheKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(ObjectUtils.isEmpty(quizTag)) {
//            return this.tagRegisterInfo();
            SimpleText simpleText = new SimpleText();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("????????? ?????? ???????????? ????????? ????????? ?????????!");
            stringBuffer.append("\n");
            stringBuffer.append("?????? ???????????? ???????????? ????????? ????????? ???????????? ????????? ???~");
            simpleText.setText(stringBuffer.toString());
            component.setSimpleText(simpleText);

            skillResponse = this.recommendTagQuickReplies(skillResponse);

            return skillResponse;
        }

        QuizDto quiz = null;

        Object result = this.getRandomQuiz(quizTag).getBody();

        if(result != null) {
            quiz = (QuizDto) result;
        }

        if(quiz == null) {
            SimpleText simpleText = new SimpleText();
            BasicCard basicCard = new BasicCard();

            Button button = new Button();

            StringBuffer stringBuffer = new StringBuffer();

            List<Button> buttonList = new ArrayList<>();

//            button.setMessageText("????????????");
            button.setLabel("????????? ??????");
            button.setAction("block");
            button.setBlockId("61e924698bb70d34fcf091cd");

            buttonList.add(button);

            basicCard.setDescription("????????? ???????????? ?????? ????????? ?????? ??????\uD83E\uDD72\n ????????? ???????????? ????????? ???!");
            basicCard.setButtons(buttonList);

            component.setBasicCard(basicCard);
            /*simpleText.setText("???????????? ????????? ?????? ????????? ????????????.\n????????? ????????? ??????????????????.");
            component.setSimpleText(simpleText);
            quickReplies = new ArrayList<>();
            QuickReply quickReply = QuickReply.builder()
                    .messageText("????????????")
                    .label("????????????")
                    .action("message")
                    .blockId("61e6600704e36e2c78ce7e4b")
                    .build();
            quickReplies.add(quickReply);
            skillTemplate.setQuickReplies(quickReplies);*/

            return skillResponse;
        } else {

            String titleThumbnail = quiz.getLink().getUrl();

            if(ObjectUtils.isEmpty(titleThumbnail)) {
                SimpleText simpleText = new SimpleText();
                simpleText.setText(quiz.getTitle());
                component.setSimpleText(simpleText);

            } else {
                BasicCard basicCard = new BasicCard();
                basicCard.setDescription(quiz.getTitle());
                Thumbnail thumbnail = new Thumbnail();
                basicCard.setThumbnail(thumbnail);
                if(titleThumbnail.startsWith("/")) {
                    titleThumbnail.substring(1);
                    quiz.getLink().setUrl(titleThumbnail.substring(1));
                }
                thumbnail.setImageUrl("https://s3.ap-northeast-2.amazonaws.com/quiztok/quiz/" + quiz.getLink().getUrl());
                thumbnail.setFixedRatio(true);
                thumbnail.setWidth(300);
                thumbnail.setHeight(300);
                component.setBasicCard(basicCard);
            }

            quickReplies = new ArrayList<>();
            for(int i = 0 ; i < quiz.getExamples().size(); i++) {
                QuickReply quickReply = QuickReply.builder()
                        .messageText(quiz.getExamples().get(i).getBody())
                        .label(quiz.getExamples().get(i).getBody())
                        .action("block")
                        .blockId("61ee3469e3907f6a2b56644e")
                        .build();
                quickReplies.add(quickReply);
            }

            quiz.setPpl((PPL)quizService.getPpl(quizTag, botPrefixKey + ":" + "userId:" + plusFriendUserKey + ":externalCode:" + externalCode).getBody());

            quiz.setPoint(this.setQuizPoint(quiz));

            skillTemplate.setQuickReplies(quickReplies);

            String botQuizCacheKey =  botPrefixKey + ":" + "userId:" + plusFriendUserKey + ":externalCode:" + externalCode + ":quiz";

            try {
                valueOps.set(botQuizCacheKey, mapper.writeValueAsString(quiz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return skillResponse;
    }

    public SkillResponse tagRegisterInfo() {
        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();
        Component component = new Component();
        SimpleText simpleText = new SimpleText();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("????????? ?????? ???????????? ????????? ????????? ?????????!");
        stringBuffer.append("\n");
        stringBuffer.append("?????? ???????????? ???????????? ????????? ????????? ???????????? ????????? ???~");
        simpleText.setText(stringBuffer.toString());
        component.setSimpleText(simpleText);
        components.add(component);
        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);
        skillResponse = this.recommendTagQuickReplies(skillResponse);

        return skillResponse;
    }

    public SkillResponse getQuizAnswer(Map params) {

        SkillResponse skillResponse = null;

        Map<String, Object> userRequest = (Map) params.get("userRequest");

        Map<String, Object> user = (Map) userRequest.get("user");

        Map<String, Object> properties = (Map) user.get("properties");

        String utterance = userRequest.get("utterance").toString();

        String plusFriendUserKey = properties.get("plusfriendUserKey").toString();

        skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();
        Component component = new Component();
        BasicCard basicCard = new BasicCard();
        SimpleText simpleText = new SimpleText();
        component.setSimpleText(simpleText);
        components.add(component);
        component = new Component();
        component.setBasicCard(basicCard);
        components.add(component);


        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);

        List<Button> buttonList = new ArrayList<>();

        String externalCode = null;

        Map actionMap = (Map) params.get("action");

        if(!ObjectUtils.isEmpty(actionMap)) {
            if(((Map)actionMap.get("params")).containsKey("externalCode")) {
                externalCode = ((Map)actionMap.get("params")).get("externalCode").toString();
            }
        }

        String botQuizCacheKey = botPrefixKey + ":" + "userId:" + plusFriendUserKey + ":externalCode:" + externalCode + ":quiz";

        QuizDto botQuizCache = null;
        try {
            botQuizCache =  mapper.readValue(valueOps.get(botQuizCacheKey), QuizDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(botQuizCache == null) {
            basicCard.setDescription("?????? ?????? ??? ????????? ???????????????.\n????????? ????????? ?????????????????????.");
//            simpleText.setText("?????? ?????? ??? ????????? ???????????????.\n????????? ????????? ?????????????????????.");
//            component.setSimpleText(simpleText);
           /* List<QuickReply> quickReplies = new ArrayList<>();
            QuickReply quickReply = QuickReply.builder()
                    .label("????????????")
                    .messageText("????????????")
                    .action("message")
                    .build();
            quickReplies.add(quickReply);
            skillTemplate.setQuickReplies(quickReplies);*/
            Button button = new Button();
            button.setLabel("????????????");
            button.setAction("message");
            button.setMessageText("????????????");
            buttonList.add(button);
            basicCard.setButtons(buttonList);
        } else {

//            String botUserPointCacheKey = botPrefixKey + ":" + "userId:" + plusFriendUserKey + ":externalCode:" + externalCode + ":totalPoint";

            String botUserDailyPointCacheKey = botPrefixKey + ":" + "userId:" + plusFriendUserKey + ":externalCode:" + externalCode + ":dailyPoint";

            StringBuffer isCorrectSb = new StringBuffer();

            int botUserDailyPoint = 0;

            int quizPoint = 0;

            ExternalQuizPlayLog externalQuizPlayLog = new ExternalQuizPlayLog();

            externalQuizPlayLog.setUserId(plusFriendUserKey);

            externalQuizPlayLog.setPlayQuiz(botQuizCache);

            Date date = new Date();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            externalQuizPlayLog.setCreateTime(simpleDateFormat.format(date));

            Choice choice = new Choice();

            choice.setText(utterance);

            externalQuizPlayLog.setChoice(choice);

            //????????? ???????????? ????????? ????????? ????????? ??????
            if(botQuizCache.getAnswer().getText().trim().equals(utterance)) {

                isCorrectSb.append("???????????????.");

                choice.setIsCorrect("y");

                quizPoint = botQuizCache.getPoint();

            } else {
                isCorrectSb.append("???????????????.");
                String answerText  = botQuizCache.getAnswer().getText();
                isCorrectSb.append("\n????????? \'" + answerText + "\' ?????????.");

                choice.setIsCorrect("n");

                quizPoint = 0;
            }

            if(externalCode != null) {

                if(valueOps.get(botUserDailyPointCacheKey) == null) {
                    //POINT API ???????????? ?????? ????????? ??????
//                    botUserPoint = pointService.getExternalTotalPoint(plusFriendUserKey, externalCode);
                    //POINT API ???????????? ?????? ????????? ??????
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    botUserDailyPoint = pointService.getExternalDailyPoint(plusFriendUserKey, externalCode, simpleDateFormat.format(date));
//                            botUserDailyPoint =+ quizPoint;
                    Calendar cnow = Calendar.getInstance();

                    Calendar cend = Calendar.getInstance();

                    Date now = new Date();

                    cnow.setTime(now);

                    cend.add(cnow.DATE,1);  //????????? ?????????.

                    cend.set(cend.HOUR_OF_DAY, 0);

                    cend.set(cend.MINUTE, 0);

                    cend.set(cend.SECOND, 0);

                    cend.set(cend.MILLISECOND, 0);

                    long dailyRemainSecond = (cend.getTimeInMillis() - cnow.getTimeInMillis()) / 1000;

                    valueOps.set(botPrefixKey + ":" + "userId:" + plusFriendUserKey + ":externalCode:" + externalCode + ":dailyPoint", Integer.toString(botUserDailyPoint),  Duration.ofSeconds(dailyRemainSecond));
                } else {
                    //redis ?????? ?????? ????????? ??????
                    botUserDailyPoint = Integer.parseInt(valueOps.get(botUserDailyPointCacheKey));
                }


                Map pointPolicyMap = pointService.getPointPolicy(valueOps, mapper);

                //????????? ?????? ???????????? ?????????
                if(pointPolicyMap != null) {

                    externalQuizPlayLog.setExternalCode(externalCode);

                    externalQuizPlayLog.setDailyPoint(botUserDailyPoint);

                    try {

                        // ????????? ?????? ???
                        if(botQuizCache.getPpl() != null) {

                            //?????? ???????????? API ??????
                            quizService.viewPpl(botQuizCache.getPpl());
                            //????????? ??????
                            pointService.addExternalPoint(externalQuizPlayLog);
                            //?????? ?????? ??????
                            quizService.addPlayLog(externalQuizPlayLog);
                            //?????? ?????? ??????
                            quizService.addExternalPlayCount(externalCode);

                            basicCard.setTitle("????????????");
                            basicCard.setDescription(botQuizCache.getExplanation());
                            Thumbnail thumbnail = new Thumbnail();
                            basicCard.setThumbnail(thumbnail);
                            thumbnail.setImageUrl(botQuizCache.getPpl().getInhouseUrl());
                            thumbnail.setFixedRatio(true);
                            Link link = new Link();
                            link.setWeb(botQuizCache.getPpl().getUrl());
                            link.setPc(botQuizCache.getPpl().getUrl());
                            link.setMobile(botQuizCache.getPpl().getUrl());
                            thumbnail.setLink(link);
                            thumbnail.setWidth(800);
                            thumbnail.setHeight(300);
                        } else {
                            quizPoint = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        basicCard.setDescription("????????? ????????? ????????? ???????????????.");
//                        simpleText.setText("????????? ????????? ????????? ??????????????????.");
                        return skillResponse;
                    }



                    //????????? ????????????????????? ?????? ???
                    if(botUserDailyPoint < (double) pointPolicyMap.get("limitDailyPoint")) {
                        if(externalQuizPlayLog.getChoice().getIsCorrect().equals("y")) {
                            valueOps.increment(botUserDailyPointCacheKey, quizPoint);
                        }
                    } else {
                        quizPoint = 0;
                    }

                } else {
                    quizPoint = 0;
                }
            }
            isCorrectSb.append("\n" + quizPoint + "???????????? ???????????????.");
            simpleText.setText(isCorrectSb.toString());

            Button button = new Button();
            button.setLabel("????????? ??????");
            button.setMessageText("????????? ??????");
            button.setAction("message");
            buttonList.add(button);

            button = new Button();
            button.setLabel("?????? ??????");
            button.setMessageText("?????? ??????");
            button.setAction("message");
            buttonList.add(button);

            basicCard.setButtons(buttonList);
        }

        return skillResponse;
    }

    public int setQuizPoint(QuizDto quizDto) {

        Map pointPolicyMap = pointService.getPointPolicy(valueOps, mapper);

        int quizPoint;

        PPL ppl = quizDto.getPpl();

        double oxPointPercentage = (double)pointPolicyMap.get("playerOxPercentage");
        double otherPointPercentage = (double)pointPolicyMap.get("playerPercentage");

        if(quizDto.getFormat().getAnswerType().desc().equals("OX")) {
            quizPoint = (int) (ppl.getPoint() * oxPointPercentage);
        } else {
            quizPoint = (int) (ppl.getPoint() * otherPointPercentage);
        }
        return quizPoint;
    }


    public String getQuiztokIdByUserId(String userId,
                                                      String externalCode,
                                                      ValueOperations<String, String> valueOps,
                                                      ObjectMapper mapper) {
        String quiztokId = null;


        /*try {
            quiztokId = valueOps.get(botPrefixKey + ":" + "userId:" + userId + ":externalCode:" + externalCode + ":quiztokId");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

//        if(quiztokId == null) {

            ResponseEntity responseEntity = null;

            String url = ssoUrl + "/integration/userMapping";

            Map params = new HashMap();

            params.put("otherId", userId);
            params.put("companyCode", externalCode);

            params = dataUtils.removeEmptyValueFromMap(params);

            try {
                Gson gson = new Gson();

                String jsonString = restTemplateUtil.getResponseEntityByBody(null, HttpMethod.POST, url, params, null).getBody();


                Map result = gson.fromJson(jsonString, new TypeToken<Map>(){}.getType());

                if(result.get("code").toString().equals("100")) {
                    if(result.get("value") != null) {
                        quiztokId = result.get("value").toString();

//                        valueOps.set(botPrefixKey + ":" + "userId:" + userId + ":externalCode:" + externalCode + ":quiztokId", quiztokId);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
        return quiztokId;
    }

    public Map getUserInfoByQuiztokId(String userId) {

        Map result = null;

        if(userId != null) {

            ResponseEntity responseEntity = null;

            String url = ssoUrl + "/users/find";

            Map params = new HashMap();

            params.put("id", userId);

            params = dataUtils.removeEmptyValueFromMap(params);

            try {
                Gson gson = new Gson();

                String jsonString = restTemplateUtil.getResponseEntityByBody(null, HttpMethod.POST, url, params, null).getBody();

                result = gson.fromJson(jsonString, new TypeToken<Map>(){}.getType());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  result;
    }


    public SkillResponse checkEarnPoint(Map params) {

        //System.out.println("????????? ???????????? ?????? : " + params.toString());

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

        StringBuffer stringBuffer = new StringBuffer();

        Button button = new Button();

        String externalCode = null;

        Map actionMap = (Map) params.get("action");

        if(!ObjectUtils.isEmpty(actionMap)) {
            if(((Map)actionMap.get("params")).containsKey("externalCode")) {
                externalCode = ((Map)actionMap.get("params")).get("externalCode").toString();
            }
        }

        String quizTokId = null;


        if (plusFriendUserKey != null && externalCode != null) {
            quizTokId = this.getQuiztokIdByUserId(plusFriendUserKey, externalCode, valueOps, mapper);
        }

        if (quizTokId == null) {

            stringBuffer.append("????????? ???????????? ???????????? ????????? ?????????.");
            stringBuffer.append("\n");
            stringBuffer.append("???????????? ???????????? ???????????? ????????? ????????? ???????");
            button.setAction("webLink");
            button.setLabel("????????? ?????? ????????????");
            String webUrl = ssoUrl + "/integration/login?" + "otherId=" + plusFriendUserKey + "&" + "companyCode=" + externalCode;
            button.setWebLinkUrl(webUrl);
            buttonList.add(button);

        } else {

            Map userInfo = this.getUserInfoByQuiztokId(quizTokId);

            ExternalPointLog externalPointLog = new ExternalPointLog();

            externalPointLog.setUserId(plusFriendUserKey);
            externalPointLog.setExternalCode(externalCode);

            Date date = new Date();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            externalPointLog.setCreateTime(simpleDateFormat.format(date));

            int externalDailyPoint = pointService.getExternalDailyPoint(plusFriendUserKey, externalCode, simpleDateFormat.format(date));

            int quiztokTotalPoint = pointService.getQuiztokTotalPoint(quizTokId);

            stringBuffer.append(userInfo.get("email").toString() + "????????? ????????? ???????????? ?????? ????????? ????????????");
            stringBuffer.append("\n");
            stringBuffer.append("\n");
            stringBuffer.append("?????? ????????? ????????? :" + externalDailyPoint + " POINT");
            stringBuffer.append("\n");
            stringBuffer.append("??? ????????? ???????????? : " + quiztokTotalPoint + " QPOINT");
            stringBuffer.append("\n");
            stringBuffer.append("??? ????????? ??????????????? ?????? ?????? 1~2??? ????????? ???????????????.");
        }

        basicCard.setButtons(buttonList);
        component.setBasicCard(basicCard);
        components.add(component);
        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);

        basicCard.setDescription(stringBuffer.toString());

        return skillResponse;
    }

    public SkillResponse loginQuiztok(Map params) {

        //System.out.println("???????????? ???????????? : " + params.toString());

        Map<String, Object> userRequest = (Map) params.get("userRequest");

        Map<String, Object> user = (Map) userRequest.get("user");

        Map<String, Object> properties = (Map) user.get("properties");

        String plusFriendUserKey = properties.get("plusfriendUserKey").toString();

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");

        String externalCode = null;

        Map actionMap = (Map) params.get("action");

        if(!ObjectUtils.isEmpty(actionMap)) {
            if(((Map)actionMap.get("params")).containsKey("externalCode")) {
                externalCode = ((Map)actionMap.get("params")).get("externalCode").toString();
            }
        }

            String userId = null;


            if (plusFriendUserKey != null && externalCode != null) {
                userId = this.getQuiztokIdByUserId(plusFriendUserKey, externalCode, valueOps, mapper);
            }

            SkillTemplate skillTemplate = new SkillTemplate();
            List<Component> components = new ArrayList<>();
            List<Button> buttonList = new ArrayList<>();

            Component component = new Component();

            BasicCard basicCard = new BasicCard();

            StringBuffer stringBuffer = new StringBuffer();

            Button button = new Button();

            stringBuffer.append("?????? ???????????? ?????? ????????? ????????? ???????????? ?????? ????????? ????????? ???????????? ????????? ??????!");
            stringBuffer.append("\n");
            stringBuffer.append("?????? ????????? ?????? ????????? ???????????? ????????? ??? ??? ??????\uD83D\uDE22");
            stringBuffer.append("\n\n");
            stringBuffer.append("*?????? ????????? 1?????? ???????????? ?????? ????????? info@quiztok.com?????? ?????? ??????????????? ?????????.");

            button.setLabel("????????? ?????? ????????????");

        String webUrl = ssoUrl + "/integration/login?" + "otherId=" + plusFriendUserKey + "&" + "companyCode=" + externalCode;
        button.setAction("webLink");
        button.setWebLinkUrl(webUrl);

/*
            if(userId == null) {
                String webUrl = ssoUrl + "/integration/join?" + "otherId=" + plusFriendUserKey + "&" + "companyCode=" + externalCode;
                button.setAction("webLink");
                button.setWebLinkUrl(webUrl);
            } else {
                button.setAction("block");
                button.setBlockId("61ee3469e3907f6a2b56644e");
            }
*/

                buttonList.add(button);


/*            if (userId == null) {

                stringBuffer.append("?????? ???????????? ?????? ???????????? ??????????????? ?????? ????????? ????????? ???????????? ????????? ??????!");
                stringBuffer.append("??????????????? ????????? ???????????? ????????? ??? ??? ??????\uD83D\uDE22");
                button.setAction("webLink");
                button.setLabel("????????? ?????? ????????????");
                String webUrl = ssoUrl + "/integration/join?" + "otherId=" + plusFriendUserKey + "&" + "companyCode=" + externalCode;
                button.setWebLinkUrl(webUrl);
                buttonList.add(button);

            } else {

                stringBuffer.append("?????? ????????? ????????? ???????????????!???????????? ????????? ???????????? ???????????? ??? ???????????? ??????!");
                stringBuffer.append("\n");
                stringBuffer.append("\n");
                stringBuffer.append("?????? ?????? ????????? ???????????? info@quiztok.com?????? ?????? ?????? ????????? ?????????~");
//                button.setAction("message");
//                button.setLabel("????????? ????????????");
//                buttonList.add(button);

            }*/

            basicCard.setButtons(buttonList);
            component.setBasicCard(basicCard);
            components.add(component);
            skillTemplate.setOutputs(components);
            skillResponse.setTemplate(skillTemplate);

            basicCard.setDescription(stringBuffer.toString());

        return skillResponse;
    }


    public SkillResponse tagRegister(Map params) {

        //System.out.println("?????? ?????? ?????? : " + params.toString());

        Map<String, Object> userRequest = (Map) params.get("userRequest");

        Map<String, Object> user = (Map) userRequest.get("user");

        Map<String, Object> properties = (Map) user.get("properties");

        String utterance = userRequest.get("utterance").toString();

        String plusFriendUserKey = properties.get("plusfriendUserKey").toString();

        String externalCode = null;

        Map actionMap = (Map) params.get("action");

        if(!ObjectUtils.isEmpty(actionMap)) {
            if(((Map)actionMap.get("params")).containsKey("externalCode")) {
                externalCode = ((Map)actionMap.get("params")).get("externalCode").toString();
            }
        }

        String quizTagCacheKey = botPrefixKey + ":" + "userId:" + plusFriendUserKey + ":externalCode:" + externalCode + ":quizTag";

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();
        Component component = new Component();
        components.add(component);
        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);

        BasicCard basicCard = new BasicCard();

        Button button = new Button();

        StringBuffer stringBuffer = new StringBuffer();

        List<Button> buttonList = new ArrayList<>();

        try {
            valueOps.set(quizTagCacheKey, utterance);

            stringBuffer.append("\'" + utterance + "\' ???????????? ????????????.");
            stringBuffer.append("\n");
            stringBuffer.append("????????? ???????????? ????????? ?????????????");
            stringBuffer.append("\n");
            stringBuffer.append("????????? ??????!???");

            button.setMessageText("?????? ??????!");
            button.setLabel("?????? ??????!");
            button.setAction("message");

        } catch (Exception e) {
            e.printStackTrace();
            stringBuffer.append("????????? ?????? ??? ????????? ????????????\n????????? ????????? ?????? ?????? ????????????!");
            button.setMessageText("????????? ??????");
            button.setLabel("????????? ??????");
            button.setAction("message");
        }

        buttonList.add(button);

        basicCard.setDescription(stringBuffer.toString());
        basicCard.setButtons(buttonList);

        component.setBasicCard(basicCard);
        
        return skillResponse;
    }

    public SkillResponse setSearchTagKeyword(Map<String, Object> params) {
        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();
        Component component = new Component();
        SimpleText simpleText = new SimpleText();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("???????????? ????????? ???????????? ????????? ???!");
        simpleText.setText(stringBuffer.toString());
        component.setSimpleText(simpleText);
        components.add(component);
        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);

        List<ContextValue> contextValues = new ArrayList<>();
        ContextValue contextValue = new ContextValue();
        contextValues.add(contextValue);
        ContextControl contextControl = new ContextControl();
        contextControl.setValues(contextValues);
        contextValue.setName("TAG_SEARCH");
        contextValue.setLifeSpan(1);
        skillResponse.setContext(contextControl);
        return skillResponse;
    }

    public SkillResponse getTagBySearchKeyword(Map<String, Object> params) {

        Map<String, Object> userRequest = (Map) params.get("userRequest");

        Map<String, Object> user = (Map) userRequest.get("user");

        Map<String, Object> properties = (Map) user.get("properties");

        String utterance = userRequest.get("utterance").toString();

        List<Tag> searchTagList = null;

        searchTagList = channelService.getRandomTag(utterance);

        /*try {

            String url = quizChannelUrl + "/v1/channel/search/tag";

                String jsonString = restTemplateUtil.getResponseEntityByParams(null, HttpMethod.GET, url, null, null).getBody();

                Gson gson = new Gson();

                searchTagList = gson.fromJson(jsonString, new TypeToken<List<Map>>(){}.getType());

        } catch (Exception e) {

        }*/

        List<QuickReply> quickReplies = new ArrayList<>();

        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setVersion("2.0");
        SkillTemplate skillTemplate = new SkillTemplate();
        List<Component> components = new ArrayList<>();
        Component component = new Component();
        components.add(component);
        skillTemplate.setOutputs(components);
        skillResponse.setTemplate(skillTemplate);

        SimpleText simpleText = new SimpleText();

            if(searchTagList != null && searchTagList.size() != 0) {
                simpleText.setText(utterance + " ????????? ??????????????? ????????????. ???????????? ????????? ????????? ?????? ???!");
                for(Tag searchTag : searchTagList) {
                    QuickReply quickReply = QuickReply.builder()
                            .messageText(searchTag.getName())
                            .label(searchTag.getName())
                            .action("block")
                            .blockId("6274c91716b99e0c3380e1d3")
                            .build();
                    quickReplies.add(quickReply);
                }

                skillTemplate.setQuickReplies(quickReplies);

            } else {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("?????? ???????????? ?????? ????????????\uD83D\uDE05 ?????? ???????????? ????????? ???");
                simpleText.setText(stringBuffer.toString());
                skillResponse = this.recommendTagQuickReplies(skillResponse);

            }

        component.setSimpleText(simpleText);

        return skillResponse;
    }

    public SkillResponse recommendTagQuickReplies(SkillResponse skillResponse) {

        List<Tag> recommendTags ;

        recommendTags = channelService.getRandomTag(null);

        /*if(ObjectUtils.isEmpty(recommendTags)) {

            recommendTags = new ArrayList<String>();

            recommendTags.add("?????????");
            recommendTags.add("??????");
            recommendTags.add("??????");
            recommendTags.add("??????");
            recommendTags.add("?????????");
            recommendTags.add("??????");
            recommendTags.add("??????");
            recommendTags.add("??????");
            recommendTags.add("??????");
        }*/

        List quickReplies = new ArrayList<>();

        QuickReply quickReply = QuickReply.builder()
                .messageText("????????? ??????")
                .label("????????? ??????")
                .action("message")
                .build();

        quickReplies.add(quickReply);


        for(int i = 0; i < recommendTags.size(); i++) {
            quickReply = QuickReply.builder()
                    .messageText(recommendTags.get(i).getName())
                    .label(recommendTags.get(i).getName())
                    .action("block")
                    .blockId("6274c91716b99e0c3380e1d3")
                    .build();
            quickReplies.add(quickReply);
        }

        /*List<ContextValue> contextValues = new ArrayList<>();
        ContextValue contextValue = new ContextValue();
        contextValues.add(contextValue);
        ContextControl contextControl = new ContextControl();
        contextControl.setValues(contextValues);
        contextValue.setName("TAG_SEARCH_KEYWORD");
        contextValue.setLifeSpan(1);
        skillResponse.setContext(contextControl);*/

        skillResponse.getTemplate().setQuickReplies(quickReplies);

        return skillResponse;
    }

}
