package com.quiztok.kakaobot.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RestTemplateUtil {


    private final RestTemplate restTemplate;
    private final DataUtils dataUtils;

    public Object getXmlResponse(){
        return restTemplate.getForObject("http://localhost:8080/xml", Object.class);
    }

    public Object getJsonRsponse(){
        return restTemplate.getForObject("http://localhost:8098/v1/release/test", Object.class);
    }


    public ResponseEntity<String> getResponseEntityByParams(HttpHeaders httpHeaders, HttpMethod httpMethod,
                                                            String url, Map params, HttpEntity httpEntity) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap();

        if(httpHeaders == null) {
            httpHeaders = new HttpHeaders();
        }

        if(!ObjectUtils.isEmpty(params)) {
            Iterator<String> keys = params.keySet().iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                String val = String.valueOf(params.get(key));
                params.put(key,val);
            }

            body.setAll(params);
        }

        if(httpEntity == null) {
            httpEntity = new HttpEntity(httpHeaders);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(body);

        URI endUri  = builder.build().encode().toUri();

        return restTemplate.exchange(endUri, httpMethod, httpEntity, String.class);


    }

    public ResponseEntity<String> getResponseEntityByBody(HttpHeaders httpHeaders, HttpMethod httpMethod,
                                                          String url, Map params, HttpEntity httpEntity) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap();

        if(!ObjectUtils.isEmpty(params)) {
            body.setAll(params);
        }

        if(httpHeaders == null) {
            httpHeaders = new HttpHeaders();
        }

        if(httpEntity == null) {
            httpEntity = new HttpEntity(body, httpHeaders);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        URI endUri  = builder.build().encode().toUri();

        return restTemplate.exchange(endUri, httpMethod, httpEntity, String.class);
    }

}
