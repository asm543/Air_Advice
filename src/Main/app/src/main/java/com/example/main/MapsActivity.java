package com.example.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.URL;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static final String MYKEY = "IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D";


    private GoogleMap mMap;
    private String stationName, addr;
    private double dmX,dmY;
    boolean inStationName=false, inAddr=false, inDmX=false, inDmY=false;
    private void Station_Addr(){
        try{
            URL url2 = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList?serviceKey=" + MYKEY + "&numOfRows=500&pageNo=1");
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url2.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");


            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("stationName")) {
                            inStationName = true;
                        }
                        if (parser.getName().equals("addr")) {
                            inAddr = true;
                        }
                        if (parser.getName().equals("dmX")) {
                            inDmX = true;
                        }
                        if (parser.getName().equals("dmY")) {
                            inDmY = true;
                        }

                        if (parser.getName().equals("message")) {
                            //예외처리
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (inStationName) {
                            stationName = parser.getText();
                            inStationName = false;
                        }
                        if (inAddr) {
                            addr = parser.getText();
                            inAddr = false;
                        }
                        if (inDmX) {
                            dmX = Double.parseDouble(parser.getText());
                            inDmX = false;
                        }
                        if (inDmY) {
                            dmY = Double.parseDouble(parser.getText());
                            inDmY = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            LatLng StationLocation = new LatLng(dmX,dmY);
                            mMap.addMarker(new MarkerOptions().position(StationLocation).title("측정소 명 : " + stationName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                        }
                        break;
                }
                parserEvent = parser.next();
            }
        }
        catch (Exception e){

        }
    } // 전국 측정소의 위치를 마커로 찍는다

    private GpsInfo gps;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    double latitude = 37.403575;
        double longitude = 126.930361;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        callPermission();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gps = new GpsInfo(MapsActivity.this);
        if (isPermission) {
            if (gps.isGetLocation()) {
//                latitude = gps.getLatitude();
//                longitude = gps.getLongitude();
            } else {
                // GPS 를 사용할수 없으므로
                showSettingsAlert();
            }
        }

        // GPS 사용유무 가져오기

        LatLng myLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myLocation).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));

        Station_Addr();
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 사용 권한이 필요합니다.\n 설정창으로 가시겠습니까?");

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MapsActivity.this,"현재 위치를 불러올 수 없습니다. 권한 설정을 해주세요.",Toast.LENGTH_LONG);
                        LatLng myLocation = new LatLng(37.403575, 126.930361);
                        mMap.addMarker(new MarkerOptions().position(myLocation).title(""));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,7));
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

}
