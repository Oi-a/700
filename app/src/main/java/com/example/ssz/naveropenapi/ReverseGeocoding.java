package com.example.ssz.naveropenapi;

import com.naver.maps.geometry.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReverseGeocoding {

    private static StringBuilder roadAddress;

    public static void convertCoordinateToAddress(LatLng location) {
        String locationStr = location.longitude + "," + location.latitude;
        String API_URI = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=" +
                locationStr + "&orders=roadaddr&output=json";

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-NCP-APIGW-API-KEY-ID", ApiSecretKey.X_NCP_APIGW_API_KEY_ID);
        requestHeader.put("X-NCP-APIGW-API-KEY", ApiSecretKey.X_NCP_APIGW_API_KEY);
        String responseBody = OpenAPIHelper.get(API_URI, requestHeader);

        parseData(responseBody);
    }

    private static void parseData(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.toString());

            JSONObject results = jsonObject.getJSONArray("results").getJSONObject(0);

            setRoadAddress(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void setRoadAddress(JSONObject results) throws JSONException {
        roadAddress = new StringBuilder();
        for (int i = 1; i <= 2; i++) {
            JSONObject area = results.getJSONObject("region").getJSONObject("area"+i);
            roadAddress.append(area.get("name")).append(" ");
        }
        roadAddress.append(results.getJSONObject("land").get("name"));
        roadAddress.append(results.getJSONObject("land").get("number1"));
        // 건물 이름
        roadAddress.append(results.getJSONObject("land").getJSONObject("addition0").get("value"));
    }

    public static String getRoadAddress() {
        if (roadAddress != null) return roadAddress.toString();
        else return null;
    }
}
