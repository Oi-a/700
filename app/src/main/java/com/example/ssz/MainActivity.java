package com.example.ssz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.example.ssz.naveropenapi.ApiSecretKey;
import com.example.ssz.naveropenapi.Direction;
import com.example.ssz.naveropenapi.Geocoding;
import com.example.ssz.naveropenapi.ReverseGeocoding;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.MarkerIcons;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static NaverMap naverMap;
    private Vector<LatLng> location;
    private Vector<Marker> marker;
    private int[] markerColor;
    private PathOverlay pathOverlay;
    private MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initialize();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient(ApiSecretKey.X_NCP_APIGW_API_KEY_ID));

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        setObject();
    }

    private void initialize() {
        location = new Vector<>();
        marker = new Vector<>();
        pathOverlay = new PathOverlay();
        markerColor = new int[7];
        markerColor[0] = Color.rgb(255, 0, 0);
        markerColor[1] = Color.rgb(255, 204, 0);
        markerColor[2] = Color.rgb(255, 255, 0);
        markerColor[3] = Color.rgb(0, 255, 0);
        markerColor[4] = Color.rgb(0, 0, 255);
        markerColor[5] = Color.rgb(0, 0, 102);
        markerColor[6] = Color.rgb(100, 0, 255);
    }

    private void setObject() {
        setCreateRouteBtn();
        setDeleteAllRouteBtn();
        setDeleteAllMarkerBtn();
    }

    private void setCreateRouteBtn() {
        Button createRouteBtn = (Button) findViewById(R.id.createRouteBtn);
        createRouteBtn.setOnClickListener(view -> new Thread(() -> {
            Vector<LatLng> waypoint = Direction.getWaypoint(location);

            pathOverlay.setCoords(waypoint);
            pathOverlay.setOutlineWidth(5);
            pathOverlay.setColor(Color.RED);

            runOnUiThread(() -> {
                pathOverlay.setMap(naverMap);
            });
        }).start());
    }

    private void setDeleteAllRouteBtn() {
        Button deleteAllRouteBtn = (Button) findViewById(R.id.deleteAllRouteBtn);
        deleteAllRouteBtn.setOnClickListener(view -> new Thread(() -> {
            this.location = new Vector<>();
            runOnUiThread(() -> pathOverlay.setMap(null));
        }).start());
    }

    private void setDeleteAllMarkerBtn() {
        Button deleteAllRouteBtn = (Button) findViewById(R.id.deleteAllMarkerBtn);
        deleteAllRouteBtn.setOnClickListener(view -> new Thread(() -> {
            runOnUiThread(() -> {
                for (int i = 0; i < marker.size(); i++)
                    marker.get(i).setMap(null);
            });
        }).start());
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        //배경 지도 선택
        naverMap.setMapType(NaverMap.MapType.Navi);
        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);
        naverMap.setOnSymbolClickListener(symbol -> {
            Marker marker = new Marker();
            marker.setPosition(symbol.getPosition());
            new Thread(() -> {
                ReverseGeocoding.convertCoordinateToAddress(symbol.getPosition());
                String roadAddress = ReverseGeocoding.getRoadAddress();
                LatLng locationCoordinate = null;
                if (roadAddress != null) {
                    locationCoordinate = Geocoding.convertAddressToCoordinate(roadAddress);
                    if (locationCoordinate != null) {
                        this.location.add(locationCoordinate);
                        this.marker.add(marker);
                    } else {
                        runOnUiThread(() -> {
                            marker.setMap(null);
                        });
                        System.out.println("위치를 지정할 수 없습니다.");
                    }
                } else {
                    runOnUiThread(() -> {
                        marker.setMap(null);
                    });
                    System.out.println("위치를 지정할 수 없습니다.");
                }
            }).start();
            marker.setIcon(MarkerIcons.BLACK);
            marker.setIconTintColor(markerColor[location.size()]);
            marker.setMap(naverMap);
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}