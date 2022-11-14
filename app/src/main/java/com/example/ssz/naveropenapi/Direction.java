package com.example.ssz.naveropenapi;

import com.naver.maps.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

public class Direction {

    public static Vector<LatLng> getWaypoint(Vector<LatLng> location) {
        String DIRECTION_API_URI = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving" +
                getAPIParameter(location);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-NCP-APIGW-API-KEY-ID", ApiSecretKey.X_NCP_APIGW_API_KEY_ID);
        requestHeader.put("X-NCP-APIGW-API-KEY", ApiSecretKey.X_NCP_APIGW_API_KEY);
        String responseBody = OpenAPIHelper.get(DIRECTION_API_URI, requestHeader);

        return parseData(responseBody);
    }

    private static String getAPIParameter(Vector<LatLng> location) {
        String startCoordinate = getStartCoordinate(location.get(0));
        String waypointCoordinate = getWaypointCoordinate(location);
        String goalCoordinate = getGoalCoordinate(location.get(location.size()-1));

        return startCoordinate + waypointCoordinate  + goalCoordinate;
    }

    private static String getStartCoordinate(LatLng startLocation) {
        return "?start=" + startLocation.latitude + "," + startLocation.longitude;
    }

    private static String getWaypointCoordinate(Vector<LatLng> location) {
        String waypoint = "";
        for (int i = 1; i < location.size()-1; i++) {
            if (i == 1) waypoint = "&waypoints=";
            waypoint += location.get(i).latitude + "," + location.get(i).longitude + "|";
        }
        return waypoint;
    }

    private static String getGoalCoordinate(LatLng goalLocation) {
        return "&goal=" + goalLocation.latitude + "," + goalLocation.longitude;
    }

    private static Vector<LatLng> parseData(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.toString());

            JSONObject route = jsonObject.getJSONObject("route");
            JSONArray traoptimal = route.getJSONArray("traoptimal");
            JSONArray path = traoptimal.getJSONObject(0).getJSONArray("path");

            Vector<LatLng> resultRoute = new Vector<>();
            HashSet<LatLng> set = new HashSet<>();
            for (int i = 0; i < path.length(); i++) {
                StringTokenizer st = new StringTokenizer(path.get(i).toString(), "[,]");
                double latitude = Double.parseDouble(st.nextToken());
                double longitude = Double.parseDouble(st.nextToken());
                if (i == 0 || !set.contains(new LatLng(longitude, latitude))) {
                    resultRoute.add(new LatLng(longitude, latitude));
                    set.add(new LatLng(longitude, latitude));
                }
            }
            return resultRoute;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
