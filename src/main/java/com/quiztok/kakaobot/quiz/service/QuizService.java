package com.quiztok.kakaobot.quiz.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quiztok.kakaobot.quiz.dto.ExternalQuizPlayLog;
import com.quiztok.kakaobot.quiz.dto.PPL;
import com.quiztok.kakaobot.quiz.dto.PPLType;
import com.quiztok.kakaobot.util.DataUtils;
import com.quiztok.kakaobot.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {

    @Value(value = "${quiztok.quiz-write.url}")
    private String quizWriteUrl;

    @Value(value = "${quiztok.ppl.url}")
    private String pplUrl;

    @Value(value = "${quiztok.point.url}")
    private String pointUrl;

    private final RestTemplateUtil restTemplateUtil;

    private final DataUtils dataUtils;

    public void addPlayLog(ExternalQuizPlayLog externalQuizPlayLog) {

        String url = quizWriteUrl + "/external/play-log" ;

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(new Gson().toJson(externalQuizPlayLog), httpHeaders);

        try {
            restTemplateUtil.getResponseEntityByBody(httpHeaders, HttpMethod.POST, url, null, httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity getPpl(String tag, String userId) {

        ResponseEntity responseEntity = null;

        PPL result = null;

        String url = pplUrl + "/api/ajax_ad_list.do" ;

        Map params = new HashMap();

        params.put("keyword" , tag);

        params.put("user_id", userId);

        params = dataUtils.removeEmptyValueFromMap(params);

        try {

            String jsonString = restTemplateUtil.getResponseEntityByParams(null, HttpMethod.GET, url, params, null).getBody();

            Gson gson = new Gson();

            result = gson.fromJson(jsonString, new TypeToken<PPL>(){}.getType());

            result.setType(PPLType.BASIC);
            result.setCash(0);

            responseEntity = new ResponseEntity(result, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result = new PPL(0, null, "default", 0.0, null, "https://www.quiztok.com/ko/",
                    "default", null, null, null, "https://quiztok.s3.ap-northeast-2.amazonaws.com/QuizTokAD/2019/11/12/6ee3df3d2dc64ab19c16e40f339433b5.png");
            responseEntity = new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }


        return responseEntity;
    }

    public ResponseEntity viewPpl(PPL ppl) {

        ResponseEntity responseEntity = null;


        String url = pplUrl + "/api/ajax_ad_view.do" ;

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("at_id", ppl.getAt_id());

        Map params = new HashMap();

        params.put("ad_id" , ppl.getAd_id());

        params.put("user_id", "kakaoBot");

        params = dataUtils.removeEmptyValueFromMap(params);

        try {

            String jsonString = restTemplateUtil.getResponseEntityByParams(httpHeaders, HttpMethod.GET, url, params, null).getBody();

            responseEntity = new ResponseEntity(HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


        return responseEntity;
    }

    public void addExternalPlayCount(String externalCode) {

        String url = pointUrl + "/external/point/play/quiz/count" ;

        Map params = new HashMap();

        params.put("externalCode" , externalCode);

        params = dataUtils.removeEmptyValueFromMap(params);

        try {
            restTemplateUtil.getResponseEntityByBody(null, HttpMethod.POST, url, params, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
