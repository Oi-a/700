package com.example.ssz.naveropenapi;

import com.naver.maps.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Geocoding {
    public static LatLng convertAddressToCoordinate(String address) {
        String API_URI = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-NCP-APIGW-API-KEY-ID", ApiSecretKey.X_NCP_APIGW_API_KEY_ID);
        requestHeader.put("X-NCP-APIGW-API-KEY", ApiSecretKey.X_NCP_APIGW_API_KEY);
        String responseBody = OpenAPIHelper.get(API_URI, requestHeader);

        return parseData(responseBody);
    }

    private static LatLng parseData(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.toString());
            JSONArray address = jsonObject.getJSONArray("addresses");
            double x = Double.parseDouble(String.valueOf(address.getJSONObject(0).get("x")));
            double y = Double.parseDouble(String.valueOf(address.getJSONObject(0).get("y")));

            return new LatLng(x, y);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
