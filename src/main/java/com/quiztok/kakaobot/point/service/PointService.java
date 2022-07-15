package com.quiztok.kakaobot.point.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quiztok.kakaobot.quiz.dto.ExternalPointLog;
import com.quiztok.kakaobot.quiz.dto.ExternalQuizPlayLog;
import com.quiztok.kakaobot.quiz.dto.QuizDto;
import com.quiztok.kakaobot.util.DataUtils;
import com.quiztok.kakaobot.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PointService {

    @Value(value = "${quiztok.point.url}")
    private String pointUrl;

    private final RestTemplateUtil restTemplateUtil;

    private final DataUtils dataUtils;

    public int getExternalTotalPoint(String userId,
                                    String externalCode) {


        int externalTotalPoint = 0;

        String url = pointUrl + "/external/point/total" ;

        Map params = new HashMap();

        params.put("userId" , userId);

        params.put("externalCode", externalCode);

        params = dataUtils.removeEmptyValueFromMap(params);

        try {

            String jsonString = restTemplateUtil.getResponseEntityByParams(null, HttpMethod.GET, url, params, null).getBody();

            Gson gson = new Gson();

            if(!ObjectUtils.isEmpty(jsonString)) {
                externalTotalPoint =  gson.fromJson(jsonString, new TypeToken<Integer>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return externalTotalPoint;

    }

    public void addExternalPoint(ExternalQuizPlayLog externalQuizPlayLog) {

        String url = pointUrl + "/external/point" ;

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(new Gson().toJson(externalQuizPlayLog), httpHeaders);

        restTemplateUtil.getResponseEntityByBody(httpHeaders, HttpMethod.POST, url, null, httpEntity).getBody();

    }

    public int getExternalDailyPoint(String userId,
                                    String externalCode,
                                    String date) {

        int externalTotalPoint = 0;

        String url = pointUrl + "/external/point/daily" ;

        Map params = new HashMap();

        params.put("userId" , userId);

        params.put("externalCode", externalCode);

        params.put("date", date);

        params = dataUtils.removeEmptyValueFromMap(params);

        try {

            String jsonString = restTemplateUtil.getResponseEntityByParams(null, HttpMethod.GET, url, params, null).getBody();

            Gson gson = new Gson();

            if(!ObjectUtils.isEmpty(jsonString)) {
                externalTotalPoint =  gson.fromJson(jsonString, new TypeToken<Integer>(){}.getType());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return externalTotalPoint;

    }

    public int getQuiztokTotalPoint(String userId) {

        int totalPoint = 0;

        try {

            String url = pointUrl + "/points/" + userId;

            String jsonString = restTemplateUtil.getResponseEntityByParams(null, HttpMethod.GET, url, null , null).getBody();

            Gson gson = new Gson();

            totalPoint = gson.fromJson(jsonString, new TypeToken<Integer>(){}.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalPoint;
    }

    public Map getPointPolicy(ValueOperations<String, String> valueOps, ObjectMapper mapper) {

        Map<String, Object> pointPolicy = null;

        String pointPolicyCacheKey = "point:policy";

        try {

            if(valueOps.get(pointPolicyCacheKey) != null) {
                pointPolicy = mapper.readValue(valueOps.get(pointPolicyCacheKey), Map.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(pointPolicy == null) {
            try {

                String url = pointUrl + "/external/point/policy";

                String jsonString = restTemplateUtil.getResponseEntityByParams(null, HttpMethod.GET, url, null , null).getBody();

                Gson gson = new Gson();

                pointPolicy = gson.fromJson(jsonString, new TypeToken<Map>(){}.getType());

                valueOps.set(pointPolicyCacheKey, mapper.writeValueAsString(pointPolicy));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pointPolicy;
    }



}
