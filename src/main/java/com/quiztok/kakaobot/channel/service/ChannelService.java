package com.quiztok.kakaobot.channel.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quiztok.kakaobot.quiz.dto.PPL;
import com.quiztok.kakaobot.quiz.dto.Tag;
import com.quiztok.kakaobot.util.DataUtils;
import com.quiztok.kakaobot.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChannelService {

    @Value(value = "${quiztok.channel.url}")
    private String channelUrl;

    private final RestTemplateUtil restTemplateUtil;

    private final DataUtils dataUtils;

    public List<Tag> getRandomTag(String searchKeyword) {

        String url = channelUrl + "/tag/bot/random" ;

        List<Tag> randomTagList = null;

        Map params = new HashMap();

        params.put("searchKeyword", searchKeyword);

        params = dataUtils.removeEmptyValueFromMap(params);


        try {

            String jsonString = restTemplateUtil.getResponseEntityByParams(null, HttpMethod.GET, url, params, null).getBody();

            Gson gson = new Gson();

            if(!ObjectUtils.isEmpty(jsonString)) {
                randomTagList =  gson.fromJson(jsonString, new TypeToken<List<Tag>>(){}.getType());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return randomTagList;
    }
}
