package com.quiztok.kakaobot.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@Component
public class DataUtils {

    /**
     * Map을 json으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return JSONObject.
     */
    public JSONObject getJsonStringFromMap( Map<String, Object> map )
    {
        JSONObject jsonObject = new JSONObject();
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonObject.put(key, value);
        }

        return jsonObject;
    }

    /**
     * List<Map>을 jsonArray로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return JSONArray.
     */
    public JSONArray getJsonArrayFromList( List<Map<String, Object>> list )
    {
        JSONArray jsonArray = new JSONArray();
        for( Map<String, Object> map : list ) {
            jsonArray.put( getJsonStringFromMap( map ) );
        }

        return jsonArray;
    }

    /**
     * List<Map>을 jsonString으로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return String.
     */
    public String getJsonStringFromList( List<Map<String, Object>> list )
    {
        JSONArray jsonArray = getJsonArrayFromList( list );
        return jsonArray.toString();
    }

    /**
     * JsonObject를 Map<String, String>으로 변환한다.
     *
     * @param jsonObj JSONObject.
     * @return Map<String, Object>.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMapFromJsonObject( JSONObject jsonObj )
    {
        Map<String, Object> map = null;

        try {

            map = new ObjectMapper().readValue(jsonObj.toString(), Map.class) ;

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * JsonArray를 List<Map<String, String>>으로 변환한다.
     *
     * @param jsonArrayString String.
     * @return List<Map<String, Object>>.
     */
    public List<Map> getListMapFromJsonArrayString(String jsonArrayString )
    {
        List<Map> list = new ArrayList<>();

        try {
            Type collectionType = new TypeToken<List<Map<String,Object>>>() {
            }.getType();
            list = new Gson().fromJson(jsonArrayString, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * JsonString을 <Map<String, String>으로 변환한다.
     *
     * @param jsonString String
     * @return List<Map<String, Object>>.
     */
    public Map getMapFromJsonString(String jsonString)
    {
        Map map = new HashMap();

        try {
            Type collectionType = new TypeToken<Map<String,Object>>() {
            }.getType();
            map = new Gson().fromJson(jsonString, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public Object getObjectFromJsonString(String jsonString) {

        Object obj = new Object();

        try {
            Type collectionType = new TypeToken<Map<String,Object>>() {
            }.getType();
            obj = new Gson().fromJson(jsonString, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public Map removeEmptyValueFromMap(Map params) {

        if(!ObjectUtils.isEmpty(params)) {
            Iterator<String> keys = params.keySet().iterator();

            while(keys.hasNext()) {
                String key = keys.next();
                Object val = params.get(key);
                if(val == null || val.equals("")) {
                    keys.remove();
                }
            }
        }
        return params;
    }

    public Map getMapFromObject(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map result = objectMapper.convertValue(obj, Map.class);

        if(!ObjectUtils.isEmpty(result)) {
            Iterator<String> keys = result.keySet().iterator();

            while(keys.hasNext()) {
                String key = keys.next();
                Object val = result.get(key);
                if(val instanceof List) {
                    List tmpList = (List) val;
                    String strList = String.join(",", tmpList);
                    result.put(key, strList);
                }
            }
        }

        return result;
    }

}
