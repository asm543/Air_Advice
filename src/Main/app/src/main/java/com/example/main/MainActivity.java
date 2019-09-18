package com.example.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class MainActivity extends FontActivity{
	
    //URL에 자주 들어가는 KEY 값을 상수화하여 사용하도록 하였다.

    static final String MYKEY = "IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D";

    private LocationManager locationManager;
    private ImageButton btnMainCloud;
    private ImageButton btn_home_set;
    private LinearLayout backgroundUp,backgroundDown;
    private TextView txtStatus;
    private TextView txtUpdate;
    private TextView txtLocation;
    private TextView txtMainStatusPm10,txtMainStatusPm25,txtMainStatusSo2,txtMainStatusO3,txtMainStatusCo,txtMainStatusNo2,txtStationName,txtComment;
    private ImageButton btn_home_map;
    double longitude = 126.930361;
    double latitude = 37.403575;
    private String accessToken = null;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    private GpsInfo gps;
    private SearchingActivity searchAdress = new SearchingActivity();
    private boolean gpsStatus = false;

    boolean initem = false, inStationName = false, inMangName = false, inDataTime = false, inSo2Value = false, inCoValue = false, inO3Value = false, inNo2Value = false, inPm10Value = false;
    boolean inPm10Value24 = false, inPm25Value = false, inPm25Value24 = false, inKhaiValue = false, inKhaiGrade = false, inSo2Grade = false;
    boolean inCoGrade = false, inO3Grade = false, inNo2Grade = false, inPm10Grade = false, inPm25Grade = false, inPm10Grade1h = false, inPm25Grade1h = false, inaddr = false;
    boolean insidoName = false,insggName=false,inumdName=false,intmX=false,intmY=false;
    boolean indmX = false,indmY = false;
    boolean inposX = false;
    boolean inposY = false;
    boolean inAccessToken = false;
    boolean inTm = false;
    int Pm10Num = 1;
    int Pm25Num = 1;

    String stationName = null, mangName = null, dataTime = null, so2Value = null, coValue = null, o3Value = null, no2Value = null, pm10Value = null;
    String pm10Value24 = null, pm25Value = null, pm25Value24 = null, khaiValue = null, khaiGrade = null, so2Grade = null;
    String coGrade = null, o3Grade = null, no2Grade = null, pm10Grade = null, pm25Grade = null, pm10Grade1h = null, pm25Grade1h = null, addr = null;
    String sidoName=null, sggName=null, umdName=null,tmX=null,tmY=null; // tm좌표
    String adress = searchAdress.tv;
    String dmX = null ,dmY = null;// tm좌표에서 위도경도로 바꾼 좌표
    String posX = null; //tm 좌표를 얻기 위해 넣는 위도경도
    String posY = null;
    String TM = "0";
    String SearchingKeyword = null;
    String ChoidetmX = null,ChoidetmY = null;
    String ChoiceStationName = null;
    String ChoicedmX=null , ChoicedmY=null;


    private void getAccessToken(){
        try{
            URL url2 = new URL("http://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.xml?consumer_key=63546047788847c9917c&consumer_secret=1698806582d740e18f5e");
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url2.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");


            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("accessToken")) {
                            inAccessToken = true;
                        }

                        if (parser.getName().equals("message")) {

                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (inAccessToken) {
                            accessToken = parser.getText();
                            inAccessToken = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("result")) {
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        }
        catch (Exception e){

        }
    }//Parse_TM 함수를 사용하기 위한 AccessToken 값을 받아오기

    private void Parse_TM(){
        try{
            URL url2 = new URL("https://sgisapi.kostat.go.kr/OpenAPI3/transformation/transcoord.xml?accessToken="+accessToken+"&src=4326&dst=5181&posX="+longitude+"&posY=" +latitude);
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url2.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");


            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("posX")) {
                            inposX = true;
                        }
                        if (parser.getName().equals("posY")) {
                            inposY = true;
                        }

                        if (parser.getName().equals("message")) {
                            txtStatus.setText(txtStatus.getText() + "에러");

                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (inposX) {
                            posX = parser.getText();
                            inposX = false;
                        }
                        if (inposY) {
                            posY = parser.getText();
                            inposY = false;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("result")) {
                            initem = false;

                        }
                        break;
                }
                parserEvent = parser.next();
            }
        }
        catch (Exception e){
            txtLocation.setText("정보 로딩 실패! 구름이를 눌러주세요.");
        }

    }//현재 위치의 위도, 경도를 TM좌표로 바꾸기

    private void Parse_getStation(){

        try{
            int count = 0;
            URL url2 = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?serviceKey=" + MYKEY +"&tmX="+posX+"&tmY="+posY);
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url2.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");


            while (parserEvent != XmlPullParser.END_DOCUMENT && count ==0) {

                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("stationName")) {
                                inStationName = true;
                            }
                            if (parser.getName().equals("addr")) {
                                inaddr = true;
                            }

                            if (parser.getName().equals("tm")) {
                                inTm = true;

                            }
                            break;

                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
                            if (inStationName) {
                                stationName = parser.getText();
                                inStationName = false;
                            }
                            if (inaddr) {
                                addr = parser.getText();
                                inaddr = false;
                            }
                            if (inTm) {
                                TM = parser.getText();
                                inTm = false;
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {
                                count++;

                            }
                            break;
                    }
                    parserEvent = parser.next();
                }

        }
        catch (Exception e){
//            txtLocation.setText("정보 로딩 실패! 구름이를 눌러주세요.");
        }
//        stationName = null;
//        try{
//            URL url = new URL(//"http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?serviceKey=IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D&tmX="+posX+"&tmY="+posY);
//                    "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?tmX=193835&tmY=433779&pageNo=1&numOfRows=1&serviceKey=IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D");
//            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
//            XmlPullParser parser = parserCreator.newPullParser();
//
//            parser.setInput(url.openStream(), null);
//
//            int parserEvent = parser.getEventType();
//            System.out.println("파싱시작합니다.");
//
//
//            while (parserEvent != XmlPullParser.END_DOCUMENT) {
//
//                    switch (parserEvent) {
//                        case XmlPullParser.START_TAG:
//                            if (parser.getName().equals("stationName")) {
//                                inStationName = true;
//                            }
//                            if (parser.getName().equals("addr")) {
//                                inaddr = true;
//                            }
//                            if (parser.getName().equals("tm")) {
//
//                                inTm = true;
//                            }
//
//                            if (parser.getName().equals("message")) {
//                                //예외처리
//                            }
//                            break;
//                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
//                            if (inStationName) {
//                                if (stationName == null) {
//                                    stationName = parser.getText();
//                                } else
//                                    inStationName = false;
//                            }
//                            if (inaddr) {
//                                addr = parser.getText();
//                                inaddr = false;
//                            }
//                            if (inTm) {
//                                double temp = Double.parseDouble(parser.getText());
//                                if (TM.equals("0")) {
//                                    TM = parser.getText();
//                                } else if (Double.parseDouble(TM) > temp) {
//                                    TM = String.valueOf(temp);
//                                }
//                                inTm = false;
//                            }
//                            break;
//                        case XmlPullParser.END_TAG:
//                            if (parser.getName().equals("item")) {
//
//                            }
//                            break;
//                    }
//
//            }
//        }
//        catch (Exception e){
//            txtLocation.setText("정보를 불러오는데에 실패했습니다.");
//        }
    }//현재 위치와 가장 가까운 측정소를 찾기

    private void Parse_StationNameToAdress(){
        try{
            URL url2 = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList?serviceKey=" + MYKEY +"&numOfRows=1&pageNo=1&stationName="+stationName);
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url2.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("dmX")) {
                            indmX = true;
                        }
                        if (parser.getName().equals("dmY")) {
                            indmY = true;
                        }

                        if (parser.getName().equals("message")) {

                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (indmX) {
                            dmX = parser.getText();
                            indmX = false;
                        }
                        if (indmY) {
                            dmY = parser.getText();
                            indmY = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        }
        catch (Exception e){
//            txtLocation.setText("주소정보 호출에 실패했습니다.");
        }
    } //현재 위치와 가장 가까운 측정소의 위,경도 받아옴

    private void Parse_Adress(){
//
        Geocoder gCoder = new Geocoder(MainActivity.this);
            try {
            List<Address> addr = gCoder.getFromLocation(37.403575, 126.930361, 10);
            String subsidoName = addr.get(0).getSubAdminArea();
            sggName = addr.get(0).getLocality();
            String subsggName = addr.get(0).getSubLocality();
            umdName =  addr.get(0).getThoroughfare();
            String subumdName =  addr.get(0).getSubThoroughfare();
                //        if(sidoName.equals(null)){
//            sidoName = "";
//        }
            if(subsidoName==null){
                subsidoName = "";
            }
                if(sggName==null){
                    sggName = "";
                }
            if(subsggName==null){
                subsggName = "";
            }
                if(umdName==null){
                    umdName = "";
                }
            if(subumdName==null){
                subumdName = "";
            }
            txtLocation.setText(subsidoName + " " +  sggName + " " + subsggName + " " +    umdName +" " +  subumdName);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }




    } //내위치 지번 주소 받아오기

    private void Parse_Data(){
        try {
            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?serviceKey=" + MYKEY + "&numOfRows=1&pageNo=1&stationName="+stationName+"&dataTerm=DAILY&ver=1.3"
            );
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput( new InputStreamReader(url.openStream(), "UTF-8") );

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("stationName")) {
                            inStationName = true;
                        }
                        if (parser.getName().equals("mangName")) {
                            inMangName = true;
                        }
                        if (parser.getName().equals("dataTime")) {
                            inDataTime = true;
                        }
                        if (parser.getName().equals("so2Value")) {
                            inSo2Value = true;
                        }
                        if (parser.getName().equals("coValue")) {
                            inCoValue = true;
                        }
                        if (parser.getName().equals("o3Value")) {
                            inO3Value = true;
                        }
                        if (parser.getName().equals("no2Value")) {
                            inNo2Value = true;
                        }
                        if (parser.getName().equals("pm10Value")) {
                            inPm10Value = true;
                        }
                        if (parser.getName().equals("pm10Value24")) {
                            inPm10Value24 = true;
                        }
                        if (parser.getName().equals("pm25Value")) {
                            inPm25Value = true;
                        }
                        if (parser.getName().equals("pm25Value24")) {
                            inPm25Value24 = true;
                        }
                        if (parser.getName().equals("khaiValue")) {
                            inKhaiValue = true;
                        }
                        if (parser.getName().equals("khaiGrade")) {
                            inKhaiGrade = true;
                        }
                        if (parser.getName().equals("so2Grade")) {
                            inSo2Grade = true;
                        }
                        if (parser.getName().equals("coGrade")) {
                            inCoGrade = true;
                        }
                        if (parser.getName().equals("o3Grade")) {
                            inO3Grade = true;
                        }
                        if (parser.getName().equals("no2Grade")) {
                            inNo2Grade = true;
                        }
                        if (parser.getName().equals("pm10Grade")) {
                            inPm10Grade = true;
                        }
                        if (parser.getName().equals("pm25Grade")) {
                            inPm25Grade = true;
                        }
                        if (parser.getName().equals("pm10Grade1h")) {
                            inPm10Grade1h = true;
                        }
                        if (parser.getName().equals("pm25Grade1h")) {
                            inPm25Grade1h = true;
                        }
                        if (parser.getName().equals("message")) {
                            txtStatus.setText("정보 불러오기 실패! 다시 시도해주세요.");
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (inStationName) {
                            stationName = parser.getText();
                            inStationName = false;
                        }
                        if (inMangName) {
                            mangName = parser.getText();
                            inMangName = false;
                        }
                        if (inDataTime) {
                            dataTime = parser.getText();
                            inDataTime = false;
                        }
                        if (inSo2Value) {
                            so2Value = parser.getText();
                            inSo2Value = false;
                        }
                        if (inCoValue) {
                            coValue = parser.getText();
                            inCoValue = false;
                        }
                        if (inO3Value) {
                            o3Value = parser.getText();
                            inO3Value = false;
                        }
                        if (inNo2Value) {
                            no2Value = parser.getText();
                            inNo2Value = false;
                        }
                        if (inPm10Value) {
                            pm10Value = parser.getText();
                            inPm10Value = false;
                        }
                        if (inPm10Value24) {
                            pm10Value24 = parser.getText();
                            inPm10Value24 = false;
                        }
                        if (inPm25Value) {
                            pm25Value = parser.getText();
                            inPm25Value = false;
                        }
                        if (inPm25Value24) {
                            pm25Value24 = parser.getText();
                            inPm25Value24 = false;
                        }
                        if (inKhaiValue) {
                            khaiValue = parser.getText();
                            inKhaiValue = false;
                        }
                        if (inKhaiGrade) {
                            khaiGrade = parser.getText();
                            inKhaiGrade = false;
                        }
                        if (inSo2Grade) {
                            so2Grade = parser.getText();
                            inSo2Grade = false;
                        }
                        if (inCoGrade) {
                            coGrade = parser.getText();
                            inCoGrade = false;
                        }
                        if (inO3Grade) {
                            o3Grade = parser.getText();
                            inO3Grade = false;
                        }
                        if (inNo2Grade) {
                            no2Grade = parser.getText();
                            inNo2Grade = false;
                        }
                        if (inPm10Grade) {
                            pm10Grade = parser.getText();
                            inPm10Grade = false;
                        }
                        if (inPm25Grade) {
                            pm25Grade = parser.getText();
                            inPm25Grade = false;
                        }
                        if (inPm10Grade1h) {
                            pm10Grade1h = parser.getText();
                            inPm10Grade1h = false;
                        }
                        if (inPm25Grade1h) {
                            pm25Grade1h = parser.getText();
                            inPm25Grade1h = false;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            txtStatus.setText("통합 대기 환경 지수 : " + khaiValue +"점\n\n 측정소 명 : \n"+ addr +"\n\n 측정소와의 거리 : 약 " + TM+"Km");

                            txtMainStatusPm10.setText(pm10Value);
                            txtMainStatusPm25.setText(pm25Value);
                            txtMainStatusSo2.setText("아황산가스\n" + so2Value +"\nppm");
                            txtMainStatusCo.setText("일산화탄소\n" + coValue+"\nppm");
                            txtMainStatusO3.setText("오존\n" + o3Value+"\nppm");
                            txtMainStatusNo2.setText("이산화질소\n" + no2Value+"\nppm");
                            Pm10Grade();
                            Pm25Grade();
                            So2Grade();
                            No2Grade();
                            O3Grade();
                            CoGrade();
                            SetBackground();
                            String[] array = dataTime.split("-");
                            String[] array2 = array[2].split(" ");
                            txtUpdate.setText(array[0] + "년 " + array[1] + "월 " + array2[0] + "일 " + array2[1]);
                            initem = false;

                            //  SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.exemple.app_widget.sharedPreferences",Context.MODE_WORLD_WRITEABLE);
                            // SharedPreferences.Editor editor = sharedPreferences.edit();

                        }
                        break;


                }
                parserEvent = parser.next();
            }

        } catch (Exception e) {
            //txtStatus.setText("ERROR");
            e.printStackTrace();
        }
    }//현재 위치와 가장 가까운 측정소의 정보를 가져오기


    private void Parse_station(String sido, String sgg,String umd){
        try{
            URL url2 = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getTMStdrCrdnt?serviceKey=" +
                    "IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D&numOfRows=1&pageNo=1&sidoName="+sido+"&sggName="+sgg+"&umdName="+umd);
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url2.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");


            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("sidoName")) {
                            insidoName = true;
                        }
                        if (parser.getName().equals("sggName")) {
                            insggName = true;
                        }
                        if (parser.getName().equals("umdName")) {
                            inumdName = true;
                        }
                        if (parser.getName().equals("tmX")) {
                            intmX = true;
                        }
                        if (parser.getName().equals("tmY")) {
                            intmY = true;
                        }

                        if (parser.getName().equals("message")) {

                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (insidoName) {
                            sidoName = parser.getText();
                            insidoName = false;
                        }
                        if (insggName) {
                            sggName = parser.getText();
                            insggName = false;
                        }
                        if (inumdName) {
                            umdName = parser.getText();
                            inumdName = false;
                        }
                        if (intmX) {
                            tmX = parser.getText();
                            posX = parser.getText();
                            intmX = false;
                        }
                        if (intmY) {
                            tmY = parser.getText();
                            posY = parser.getText();
                            intmY = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            txtComment.setText(tmX+tmY);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        }
        catch (Exception e){

        }
    } // 검색결과에서 클릭한 측정소의 TM좌표를 알아오기

//    private void Parse_getChoiceStation(){
//        int i = 0;
//        try{
//            URL url2 = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?serviceKey=IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D&tmX="+tmX+"&tmY="+tmY);
//            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
//            XmlPullParser parser = parserCreator.newPullParser();
//
//            parser.setInput(url2.openStream(), null);
//
//            int parserEvent = parser.getEventType();
//            System.out.println("파싱시작합니다.");
//
//
//            while (parserEvent != XmlPullParser.END_DOCUMENT) {
//                if( i==0) {
//                    switch (parserEvent) {
//                        case XmlPullParser.START_TAG:
//                            if (parser.getName().equals("stationName")) {
//                                inStationName = true;
//                            }
//                            if (parser.getName().equals("addr")) {
//                                inaddr = true;
//                            }
//
//                            if (parser.getName().equals("tm")) {
//                                inTm = true;
//
//                            }
//                            break;
//
//                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
//                            if (inStationName) {
//                                stationName = parser.getText();
//                                inStationName = false;
//                            }
//                            if (inaddr) {
//                                addr = parser.getText();
//                                inaddr = false;
//                            }
//                            if (inTm) {
//                                TM = parser.getText();
//                                inTm = false;
//                            }
//
//                            break;
//                        case XmlPullParser.END_TAG:
//                            if (parser.getName().equals("result")) {
//
//                                i++;
//                            }
//                            break;
//                    }
//                    parserEvent = parser.next();
//                }
//            }
//        }
//        catch (Exception e){
////            txtLocation.setText("정보 로딩 실패! 구름이를 눌러주세요.");
//        }
////        stationName = null;
////        try{
////            URL url = new URL(//"http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?serviceKey=IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D&tmX="+posX+"&tmY="+posY);
////                    "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?tmX=193835&tmY=433779&pageNo=1&numOfRows=1&serviceKey=IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D");
////            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
////            XmlPullParser parser = parserCreator.newPullParser();
////
////            parser.setInput(url.openStream(), null);
////
////            int parserEvent = parser.getEventType();
////            System.out.println("파싱시작합니다.");
////
////
////            while (parserEvent != XmlPullParser.END_DOCUMENT) {
////
////                    switch (parserEvent) {
////                        case XmlPullParser.START_TAG:
////                            if (parser.getName().equals("stationName")) {
////                                inStationName = true;
////                            }
////                            if (parser.getName().equals("addr")) {
////                                inaddr = true;
////                            }
////                            if (parser.getName().equals("tm")) {
////
////                                inTm = true;
////                            }
////
////                            if (parser.getName().equals("message")) {
////                                //예외처리
////                            }
////                            break;
////                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
////                            if (inStationName) {
////                                if (stationName == null) {
////                                    stationName = parser.getText();
////                                } else
////                                    inStationName = false;
////                            }
////                            if (inaddr) {
////                                addr = parser.getText();
////                                inaddr = false;
////                            }
////                            if (inTm) {
////                                double temp = Double.parseDouble(parser.getText());
////                                if (TM.equals("0")) {
////                                    TM = parser.getText();
////                                } else if (Double.parseDouble(TM) > temp) {
////                                    TM = String.valueOf(temp);
////                                }
////                                inTm = false;
////                            }
////                            break;
////                        case XmlPullParser.END_TAG:
////                            if (parser.getName().equals("item")) {
////
////                            }
////                            break;
////                    }
////
////            }
////        }
////        catch (Exception e){
////            txtLocation.setText("정보를 불러오는데에 실패했습니다.");
////        }
//    }// 검색결과에서 클릭한 측정소 이름 가져오기
//
//    private void Parse_ChoiceStationNameToAdress(){
//        try{
//            URL url2 = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList?serviceKey=IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D&numOfRows=1&pageNo=1&stationName="+stationName);
//            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
//            XmlPullParser parser = parserCreator.newPullParser();
//
//            parser.setInput(url2.openStream(), null);
//
//            int parserEvent = parser.getEventType();
//            System.out.println("파싱시작합니다.");
//
//            while (parserEvent != XmlPullParser.END_DOCUMENT) {
//                switch (parserEvent) {
//                    case XmlPullParser.START_TAG:
//                        if (parser.getName().equals("dmX")) {
//                            indmX = true;
//                        }
//                        if (parser.getName().equals("dmY")) {
//                            indmY = true;
//                        }
//
//                        if (parser.getName().equals("message")) {
//
//                        }
//                        break;
//
//                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
//                        if (indmX) {
//                            ChoicedmX = parser.getText();
//                            indmX = false;
//                        }
//                        if (indmY) {
//                            ChoicedmY = parser.getText();
//                            indmY = false;
//                        }
//                        break;
//                    case XmlPullParser.END_TAG:
//                        if (parser.getName().equals("item")) {
//                        }
//                        break;
//                }
//                parserEvent = parser.next();
//            }
//        }
//        catch (Exception e){
////            txtLocation.setText("주소정보 호출에 실패했습니다.");
//        }
//    } //검색결과에서 클릭한 측정소의 위,경도 받아오기
//
//    private void Parse_ChoiceData(){
//        try {
//            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?serviceKey=IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D&numOfRows=1&pageNo=1&stationName="+stationName+"&dataTerm=DAILY&ver=1.3"
//            );
//            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
//            XmlPullParser parser = parserCreator.newPullParser();
//
//            parser.setInput( new InputStreamReader(url.openStream(), "UTF-8") );
//
//            int parserEvent = parser.getEventType();
//            System.out.println("파싱시작합니다.");
//
//            while (parserEvent != XmlPullParser.END_DOCUMENT) {
//                switch (parserEvent) {
//                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
//                        if (parser.getName().equals("stationName")) {
//                            inStationName = true;
//                        }
//                        if (parser.getName().equals("mangName")) {
//                            inMangName = true;
//                        }
//                        if (parser.getName().equals("dataTime")) {
//                            inDataTime = true;
//                        }
//                        if (parser.getName().equals("so2Value")) {
//                            inSo2Value = true;
//                        }
//                        if (parser.getName().equals("coValue")) {
//                            inCoValue = true;
//                        }
//                        if (parser.getName().equals("o3Value")) {
//                            inO3Value = true;
//                        }
//                        if (parser.getName().equals("no2Value")) {
//                            inNo2Value = true;
//                        }
//                        if (parser.getName().equals("pm10Value")) {
//                            inPm10Value = true;
//                        }
//                        if (parser.getName().equals("pm10Value24")) {
//                            inPm10Value24 = true;
//                        }
//                        if (parser.getName().equals("pm25Value")) {
//                            inPm25Value = true;
//                        }
//                        if (parser.getName().equals("pm25Value24")) {
//                            inPm25Value24 = true;
//                        }
//                        if (parser.getName().equals("khaiValue")) {
//                            inKhaiValue = true;
//                        }
//                        if (parser.getName().equals("khaiGrade")) {
//                            inKhaiGrade = true;
//                        }
//                        if (parser.getName().equals("so2Grade")) {
//                            inSo2Grade = true;
//                        }
//                        if (parser.getName().equals("coGrade")) {
//                            inCoGrade = true;
//                        }
//                        if (parser.getName().equals("o3Grade")) {
//                            inO3Grade = true;
//                        }
//                        if (parser.getName().equals("no2Grade")) {
//                            inNo2Grade = true;
//                        }
//                        if (parser.getName().equals("pm10Grade")) {
//                            inPm10Grade = true;
//                        }
//                        if (parser.getName().equals("pm25Grade")) {
//                            inPm25Grade = true;
//                        }
//                        if (parser.getName().equals("pm10Grade1h")) {
//                            inPm10Grade1h = true;
//                        }
//                        if (parser.getName().equals("pm25Grade1h")) {
//                            inPm25Grade1h = true;
//                        }
//                        if (parser.getName().equals("message")) {
//                            txtStatus.setText("정보 불러오기 실패! 다시 시도해주세요.");
//                        }
//                        break;
//
//                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
//                        if (inStationName) {
//                            stationName = parser.getText();
//                            inStationName = false;
//                        }
//                        if (inMangName) {
//                            mangName = parser.getText();
//                            inMangName = false;
//                        }
//                        if (inDataTime) {
//                            dataTime = parser.getText();
//                            inDataTime = false;
//                        }
//                        if (inSo2Value) {
//                            so2Value = parser.getText();
//                            inSo2Value = false;
//                        }
//                        if (inCoValue) {
//                            coValue = parser.getText();
//                            inCoValue = false;
//                        }
//                        if (inO3Value) {
//                            o3Value = parser.getText();
//                            inO3Value = false;
//                        }
//                        if (inNo2Value) {
//                            no2Value = parser.getText();
//                            inNo2Value = false;
//                        }
//                        if (inPm10Value) {
//                            pm10Value = parser.getText();
//                            inPm10Value = false;
//                        }
//                        if (inPm10Value24) {
//                            pm10Value24 = parser.getText();
//                            inPm10Value24 = false;
//                        }
//                        if (inPm25Value) {
//                            pm25Value = parser.getText();
//                            inPm25Value = false;
//                        }
//                        if (inPm25Value24) {
//                            pm25Value24 = parser.getText();
//                            inPm25Value24 = false;
//                        }
//                        if (inKhaiValue) {
//                            khaiValue = parser.getText();
//                            inKhaiValue = false;
//                        }
//                        if (inKhaiGrade) {
//                            khaiGrade = parser.getText();
//                            inKhaiGrade = false;
//                        }
//                        if (inSo2Grade) {
//                            so2Grade = parser.getText();
//                            inSo2Grade = false;
//                        }
//                        if (inCoGrade) {
//                            coGrade = parser.getText();
//                            inCoGrade = false;
//                        }
//                        if (inO3Grade) {
//                            o3Grade = parser.getText();
//                            inO3Grade = false;
//                        }
//                        if (inNo2Grade) {
//                            no2Grade = parser.getText();
//                            inNo2Grade = false;
//                        }
//                        if (inPm10Grade) {
//                            pm10Grade = parser.getText();
//                            inPm10Grade = false;
//                        }
//                        if (inPm25Grade) {
//                            pm25Grade = parser.getText();
//                            inPm25Grade = false;
//                        }
//                        if (inPm10Grade1h) {
//                            pm10Grade1h = parser.getText();
//                            inPm10Grade1h = false;
//                        }
//                        if (inPm25Grade1h) {
//                            pm25Grade1h = parser.getText();
//                            inPm25Grade1h = false;
//                        }
//
//                        break;
//                    case XmlPullParser.END_TAG:
//                        if (parser.getName().equals("item")) {
//                            txtStatus.setText("통합 대기 환경 지수 : " + khaiValue +"점\n\n 측정소 명 : \n"+ addr +"\n\n 측정소와의 거리 : 약 " + TM+"Km");
//
//                            txtMainStatusPm10.setText(pm10Value);
//                            txtMainStatusPm25.setText(pm25Value);
//                            txtMainStatusSo2.setText("아황산가스\n" + so2Value +"\nppm");
//                            txtMainStatusCo.setText("일산화탄소\n" + coValue+"\nppm");
//                            txtMainStatusO3.setText("오존\n" + o3Value+"\nppm");
//                            txtMainStatusNo2.setText("이산화질소\n" + no2Value+"\nppm");
//                            Pm10Grade();
//                            Pm25Grade();
//                            So2Grade();
//                            No2Grade();
//                            O3Grade();
//                            CoGrade();
//                            SetBackground();
//                            String[] array = dataTime.split("-");
//                            String[] array2 = array[2].split(" ");
//                            txtUpdate.setText(array[0] + "년 " + array[1] + "월 " + array2[0] + "일 " + array2[1]);
//                            initem = false;
//
//                            //  SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.exemple.app_widget.sharedPreferences",Context.MODE_WORLD_WRITEABLE);
//                            // SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                        }
//                        break;
//
//
//                }
//                parserEvent = parser.next();
//            }
//
//        } catch (Exception e) {
//            //txtStatus.setText("ERROR");
//            e.printStackTrace();
//        }
//    }//선택한 측정소의 정보를 가져오기

    private void Pm10Grade(){
        if(pm10Value.equals("-")) {txtMainStatusPm10.append("\n 데이터가 없습니다."); Pm10Num =0;}
        else if(0.0<=Double.parseDouble(pm10Value) &&Double.parseDouble(pm10Value)<15.0){
//            txtMainStatusPm10.setBackgroundColor(Color.rgb(154,181,250));

//            txtMainStatusPm10.append("\n매우 좋음");
            Pm10Num =1;
        }
        else if(15.0<=Double.parseDouble(pm10Value) && Double.parseDouble(pm10Value)<30.0){
//            txtMainStatusPm10.setBackgroundColor(Color.rgb(99,221,255));

            //txtMainStatusPm10.append("\n좋음");
            Pm10Num =2;
        }
        else if(30.0<=Double.parseDouble(pm10Value) && Double.parseDouble(pm10Value)<40.0){
//            txtMainStatusPm10.setBackgroundColor(Color.rgb(95,235,178));

            //txtMainStatusPm10.append("\n양호");
            Pm10Num =3;
        }
        else if(40.0<=Double.parseDouble(pm10Value) && Double.parseDouble(pm10Value)<50.0){
//            txtMainStatusPm10.setBackgroundColor(Color.rgb(116,241,128));

            //txtMainStatusPm10.append("\n보통");
            Pm10Num =4;
        }
        else if(50.0<=Double.parseDouble(pm10Value) && Double.parseDouble(pm10Value)<75.0){
//            txtMainStatusPm10.setBackgroundColor(Color.rgb(241,206,116));

            //txtMainStatusPm10.append("\n나쁨");
            Pm10Num =5;
        }
        else if(75.0<=Double.parseDouble(pm10Value) && Double.parseDouble(pm10Value)<100.0){
//            txtMainStatusPm10.setBackgroundColor(Color.rgb(255,146,72));

            //txtMainStatusPm10.append("\n상당히 나쁨");
            Pm10Num =6;
        }
        else if(100.0<=Double.parseDouble(pm10Value) && Double.parseDouble(pm10Value)<150.0){
//            txtMainStatusPm10.setBackgroundColor(Color.rgb(255,99,72));
            //txtMainStatusPm10.append("\n매우 나쁨");
            Pm10Num =7;
        }
        else if(151.0<=Double.parseDouble(pm10Value)){
//            txtMainStatusPm10.setBackgroundColor(Color.rgb(192,19,5));
            //txtMainStatusPm10.append("\n최악의 대기질");
            Pm10Num =8;
        }

    }
    private void Pm25Grade(){
        if(pm25Value.equals("-")){txtMainStatusPm25.append("\n 데이터가 없습니다."); Pm25Num =0;}
        else if(0.0<=Double.parseDouble(pm25Value) &&Double.parseDouble(pm25Value)<8.0){
//            txtMainStatusPm25.setBackgroundColor(Color.rgb(154,181,250));
            //txtMainStatusPm25.append("\n매우 좋음");
            Pm25Num =1;
        }
        else if(8.0<=Double.parseDouble(pm25Value) && Double.parseDouble(pm25Value)<15.0){
//            txtMainStatusPm25.setBackgroundColor(Color.rgb(99,221,255));
            //txtMainStatusPm25.append("\n좋음");
            Pm25Num =2;
        }
        else if(15.0<=Double.parseDouble(pm25Value) && Double.parseDouble(pm25Value)<20.0){
//            txtMainStatusPm25.setBackgroundColor(Color.rgb(95,235,178));
            //txtMainStatusPm25.append("\n양호");
            Pm25Num =3;
        }
        else if(20.0<=Double.parseDouble(pm25Value) && Double.parseDouble(pm25Value)<25.0){
//            txtMainStatusPm25.setBackgroundColor(Color.rgb(116,241,128));
            //txtMainStatusPm25.append("\n보통");
            Pm25Num =4;
        }
        else if(25.0<=Double.parseDouble(pm25Value) && Double.parseDouble(pm25Value)<37.0){
//            txtMainStatusPm25.setBackgroundColor(Color.rgb(241,206,116));
            //txtMainStatusPm25.append("\n나쁨");
            Pm25Num =5;
        }
        else if(37.0<=Double.parseDouble(pm25Value) && Double.parseDouble(pm25Value)<50.0){
//            txtMainStatusPm25.setBackgroundColor(Color.rgb(255,146,72));
            //txtMainStatusPm25.append("\n상당히 나쁨");
            Pm25Num =6;
        }
        else if(50.0<=Double.parseDouble(pm25Value) && Double.parseDouble(pm25Value)<75.0){
//            txtMainStatusPm25.setBackgroundColor(Color.rgb(255,99,72));
            //txtMainStatusPm25.append("\n매우 나쁨");
            Pm25Num =7;
        }
        else if(75.0<=Double.parseDouble(pm25Value)){
//            txtMainStatusPm25.setBackgroundColor(Color.rgb(192,19,5));
            //txtMainStatusPm25.append("\n최악의 대기질");
            Pm25Num =8;
        }
    }
    private void O3Grade(){
        if(0.0<=Double.parseDouble(o3Value) &&Double.parseDouble(o3Value)<0.02){
//            txtMainStatusO3.setBackgroundColor(Color.rgb(154,181,250));
            //txtMainStatusO3.append("\n매우 좋음");
        }
        else if(0.02<=Double.parseDouble(o3Value) && Double.parseDouble(o3Value)<0.03){
//            txtMainStatusO3.setBackgroundColor(Color.rgb(99,221,255));
            //txtMainStatusO3.append("\n좋음");
        }
        else if(0.03<=Double.parseDouble(o3Value) && Double.parseDouble(o3Value)<0.06){
//            txtMainStatusO3.setBackgroundColor(Color.rgb(95,235,178));
            //txtMainStatusO3.append("\n양호");
        }
        else if(0.06<=Double.parseDouble(o3Value) && Double.parseDouble(o3Value)<0.09){
//            txtMainStatusO3.setBackgroundColor(Color.rgb(116,241,128));
//            txtMainStatusO3.append("\n보통");
        }
        else if(0.09<=Double.parseDouble(o3Value) && Double.parseDouble(o3Value)<0.12){
//            txtMainStatusO3.setBackgroundColor(Color.rgb(241,206,116));
           // txtMainStatusO3.append("\n나쁨");
        }
        else if(0.12<=Double.parseDouble(o3Value) && Double.parseDouble(o3Value)<0.15){
//            txtMainStatusO3.setBackgroundColor(Color.rgb(255,146,72));
            //txtMainStatusO3.append("\n상당히 나쁨");
        }
        else if(0.15<=Double.parseDouble(o3Value) && Double.parseDouble(o3Value)<0.38){
//            txtMainStatusO3.setBackgroundColor(Color.rgb(255,99,72));
            //txtMainStatusO3.append("\n매우 나쁨");
        }
        else if(0.38<=Double.parseDouble(o3Value)){
//            txtMainStatusO3.setBackgroundColor(Color.rgb(192,19,5));
            //txtMainStatusO3.append("\n최악의 대기질");
        }
        else txtMainStatusO3.append("\n 데이터가 없습니다.");
    }
    private void So2Grade(){
        if(0.0<=Double.parseDouble(no2Value) &&Double.parseDouble(no2Value)<0.02){
//            txtMainStatusNo2.setBackgroundColor(Color.rgb(154,181,250));
            //txtMainStatusNo2.append("\n매우 좋음");
        }
        else if(0.02<=Double.parseDouble(no2Value) && Double.parseDouble(no2Value)<0.03){
//            txtMainStatusNo2.setBackgroundColor(Color.rgb(99,221,255));
            //txtMainStatusNo2.append("\n좋음");
        }
        else if(0.03<=Double.parseDouble(no2Value) && Double.parseDouble(no2Value)<0.05){
//            txtMainStatusNo2.setBackgroundColor(Color.rgb(95,235,178));
           // txtMainStatusNo2.append("\n양호");
        }
        else if(0.05<=Double.parseDouble(no2Value) && Double.parseDouble(no2Value)<0.06){
//            txtMainStatusNo2.setBackgroundColor(Color.rgb(116,241,128));
           // txtMainStatusNo2.append("\n보통");
        }
        else if(0.06<=Double.parseDouble(no2Value) && Double.parseDouble(no2Value)<0.13){
//            txtMainStatusNo2.setBackgroundColor(Color.rgb(241,206,116));
           // txtMainStatusNo2.append("\n나쁨");
        }
        else if(0.13<=Double.parseDouble(no2Value) && Double.parseDouble(no2Value)<0.2){
//            txtMainStatusNo2.setBackgroundColor(Color.rgb(255,146,72));
           // txtMainStatusNo2.append("\n상당히 나쁨");
        }
        else if(0.2<=Double.parseDouble(no2Value) && Double.parseDouble(no2Value)<1.1){
//            txtMainStatusNo2.setBackgroundColor(Color.rgb(255,99,72));
           // txtMainStatusNo2.append("\n매우 나쁨");
        }
        else if(1.1<=Double.parseDouble(no2Value)){
//            txtMainStatusNo2.setBackgroundColor(Color.rgb(192,19,5));
           // txtMainStatusNo2.append("\n최악의 대기질");
        }
        else txtMainStatusNo2.append("\n 데이터가 없습니다.");
    }
    private void CoGrade(){
        if(0.0<=Double.parseDouble(coValue) &&Double.parseDouble(coValue)<1.0){
//            txtMainStatusCo.setBackgroundColor(Color.rgb(154,181,250));
           // txtMainStatusCo.append("\n매우 좋음");
        }
        else if(1.0<=Double.parseDouble(coValue) && Double.parseDouble(coValue)<2.0){
//            txtMainStatusCo.setBackgroundColor(Color.rgb(99,221,255));
           // txtMainStatusCo.append("\n좋음");
        }
        else if(2.0<=Double.parseDouble(coValue) && Double.parseDouble(coValue)<5.5){
//            txtMainStatusCo.setBackgroundColor(Color.rgb(95,235,178));
           // txtMainStatusCo.append("\n양호");
        }
        else if(5.5<=Double.parseDouble(coValue) && Double.parseDouble(coValue)<9.0){
//            txtMainStatusCo.setBackgroundColor(Color.rgb(116,241,128));
           // txtMainStatusCo.append("\n보통");
        }
        else if(9.0<=Double.parseDouble(coValue) && Double.parseDouble(coValue)<12.0){
//            txtMainStatusCo.setBackgroundColor(Color.rgb(241,206,116));
           // txtMainStatusCo.append("\n나쁨");
        }
        else if(12.0<=Double.parseDouble(coValue) && Double.parseDouble(coValue)<15.0){
//            txtMainStatusCo.setBackgroundColor(Color.rgb(255,146,72));
           // txtMainStatusCo.append("\n상당히 나쁨");
        }
        else if(15.0<=Double.parseDouble(coValue) && Double.parseDouble(coValue)<32.0){
//            txtMainStatusCo.setBackgroundColor(Color.rgb(255,99,72));
            //txtMainStatusCo.append("\n매우 나쁨");
        }
        else if(32.0<=Double.parseDouble(coValue)){
//            txtMainStatusCo.setBackgroundColor(Color.rgb(192,19,5));
           // txtMainStatusCo.append("\n최악의 대기질");
        }
        else txtMainStatusCo.append("\n 데이터가 없습니다.");
    }
    private void No2Grade(){
        if(0.0<=Double.parseDouble(so2Value) &&Double.parseDouble(so2Value)<0.01){
//            txtMainStatusSo2.setBackgroundColor(Color.rgb(154,181,250));
            //txtMainStatusSo2.append("\n매우 좋음");
        }
        else if(0.01<=Double.parseDouble(so2Value) && Double.parseDouble(so2Value)<0.02){
//            txtMainStatusSo2.setBackgroundColor(Color.rgb(99,221,255));
            //txtMainStatusSo2.append("\n좋음");
        }
        else if(0.02<=Double.parseDouble(so2Value) && Double.parseDouble(so2Value)<0.04){
//            txtMainStatusSo2.setBackgroundColor(Color.rgb(95,235,178));
            //txtMainStatusSo2.append("\n양호");
        }
        else if(0.04<=Double.parseDouble(so2Value) && Double.parseDouble(so2Value)<0.05){
//            txtMainStatusSo2.setBackgroundColor(Color.rgb(116,241,128));
            //txtMainStatusSo2.append("\n보통");
        }
        else if(0.05<=Double.parseDouble(so2Value) && Double.parseDouble(so2Value)<0.1){
//            txtMainStatusSo2.setBackgroundColor(Color.rgb(241,206,116));
            //txtMainStatusSo2.append("\n나쁨");
        }
        else if(0.1<=Double.parseDouble(so2Value) && Double.parseDouble(so2Value)<0.15){
//            txtMainStatusSo2.setBackgroundColor(Color.rgb(255,146,72));
            //txtMainStatusSo2.append("\n상당히 나쁨");
        }
        else if(0.15<=Double.parseDouble(so2Value) && Double.parseDouble(so2Value)<0.6){
//            txtMainStatusSo2.setBackgroundColor(Color.rgb(255,99,72));
           // txtMainStatusSo2.append("\n매우 나쁨");
        }
        else if(0.6<=Double.parseDouble(so2Value)){
//            txtMainStatusSo2.setBackgroundColor(Color.rgb(192,19,5));
            //txtMainStatusSo2.append("\n최악의 대기질");
        }
        else txtMainStatusSo2.append("\n 데이터가 없습니다.");
    }
    private void SetBackground() {
        Random random = new Random();
        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
        String age = sf.getString("textAge", "나이를 입력해주세요!");
        if(age == "나이를 입력해주세요!"){
            age = "0";
        }
        int intage = Integer.parseInt(age);
        boolean inout = sf.getBoolean("inout",true);
        if (Pm10Num >= Pm25Num) {
            switch (Pm10Num) {
                case 0://Toast.makeText(MainActivity.this, "초기값입니다", Toast.LENGTH_SHORT).show();
                     break;
                case 1:
                    backgroundUp.setBackgroundResource(R.drawable.background_firstgood);
                    backgroundDown.setBackgroundResource(R.drawable.background_firstgood2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud1);
                    txtStationName.setText("\t\t매우 좋음");

                    break;
                case 2:
                    backgroundUp.setBackgroundResource(R.drawable.background_secondgood);
                    backgroundDown.setBackgroundResource(R.drawable.background_secondgood2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud2);
                    txtStationName.setText("\t\t\t\t\t\t\t좋음");
                    break;
                case 3:
                    backgroundUp.setBackgroundResource(R.drawable.background_thirdgood);
                    backgroundDown.setBackgroundResource(R.drawable.background_thridgood2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud3);
                    txtStationName.setText("\t\t\t\t\t\t\t양호");
                    break;
                case 4:
                    backgroundUp.setBackgroundResource(R.drawable.background_firstsoso);
                    backgroundDown.setBackgroundResource(R.drawable.background_firstsoso2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud4);
                    txtStationName.setText("\t\t\t\t\t\t\t보통");
                    break;
                case 5:
                    backgroundUp.setBackgroundResource(R.drawable.background_secondsoso);
                    backgroundDown.setBackgroundResource(R.drawable.background_secondsoso2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud5);
                    txtStationName.setText("\t\t\t\t\t\t\t나쁨");
                    break;
                case 6:
                    backgroundUp.setBackgroundResource(R.drawable.background_thirdsoso);
                    backgroundDown.setBackgroundResource(R.drawable.background_thirdsoso2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud6);
                    txtStationName.setText("상당히 나쁨");
                    break;
                case 7:
                    backgroundUp.setBackgroundResource(R.drawable.background_bad);
                    backgroundDown.setBackgroundResource(R.drawable.background_bad2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud7);
                    txtStationName.setText("\t\t매우 나쁨");
                    break;
                case 8:
                    backgroundUp.setBackgroundResource(R.drawable.background_verybad);
                    backgroundDown.setBackgroundResource(R.drawable.background_verybad2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud8);
                    txtStationName.setText("\t\t\t\t\t\t\t최악");
                    break;
            }
            if(1<=Pm10Num && Pm10Num<=3){
                //if 문에 따른 코멘트
                if(inout){ // 실외
                    if(intage>=20){
                        txtComment.setText("산책하기 좋은 공기네요.");
                    }
                    else{
                        txtComment.setText("신선한 공기를 많이 마시세요~");
                    }
                }
                else{
                    if(intage>=20){ //실내

                        txtComment.setText("좋은 날씨. 좋은 하루 보내세요~");
                    }
                    else{
                        String[] txtGoodArray = {"야외활동을 즐기세요~","좋은 공기에 환기는 필수!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtGoodArray[r]);
                    }

                }
            }

        else if(4<=Pm10Num && Pm10Num<=5){
            //if 문에 따른 코멘트
            if(inout){//실외
                if(intage>=20){
                    txtComment.setText("꾸준한 수분 섭취!!!");
                }
                else{
                    txtComment.setText("공기가 쏘쏘하네요^^");
                }
            } //실외끝

            else{ //실내
                if(intage>=20){
                    txtComment.setText("잠깐의 스트레칭은 어떠세요?");
                }
                else{
                    txtComment.setText("청소하기 좋은 날씨에요.");
                }
            }
        } // 보통의 코멘트

        else if(6<=Pm10Num && Pm10Num<=8) { //나쁨
                //if 문에 따른 코멘트
                if (inout) { //실외
                    if (intage >= 20) {
                        String[] txtBadArray = {"노폐물 배출에 좋은 과일을 섭취!!", "건강을 위해!! 마스크 착용!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtBadArray[r]);

                    } else {
                        String[] txtBadArray1 = {"공기가 탁하네요. 조심하세요~", "나쁜 공기엔 마스크 필수!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtBadArray1[r]);
                    }
                } else { //실내
                    if (intage >= 20) {
                        String[] txtBadArray2 = {"수분 섭취를 많이 해주세요!!", "외출시 마스크. 외출후 손씻기!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtBadArray2[r]);
                    } else {
                        String[] txtBadArray3 = {"외출은 가급적 하지마세요~", "밖은 위험해요!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtBadArray3[r]);
                    }
                }
            }
        } else if (Pm10Num < Pm25Num) {
            switch (Pm25Num) {
                case 0://Toast.makeText(MainActivity.this, "초기값입니다", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    backgroundUp.setBackgroundResource(R.drawable.background_firstgood);
                    backgroundDown.setBackgroundResource(R.drawable.background_firstgood2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud1);
                    txtStationName.setText("\t\t매우 좋음");
                    break;
                case 2:
                    backgroundUp.setBackgroundResource(R.drawable.background_secondgood);
                    backgroundDown.setBackgroundResource(R.drawable.background_secondgood2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud2);
                    txtStationName.setText("\t\t\t\t\t\t\t좋음");
                    break;
                case 3:
                    backgroundUp.setBackgroundResource(R.drawable.background_thirdgood);
                    backgroundDown.setBackgroundResource(R.drawable.background_thridgood2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud3);
                    txtStationName.setText("\t\t\t\t\t\t\t양호");
                    break;
                case 4:
                    backgroundUp.setBackgroundResource(R.drawable.background_firstsoso);
                    backgroundDown.setBackgroundResource(R.drawable.background_firstsoso2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud4);
                    txtStationName.setText("\t\t\t\t\t\t\t보통");
                    break;
                case 5:
                    backgroundUp.setBackgroundResource(R.drawable.background_secondsoso);
                    backgroundDown.setBackgroundResource(R.drawable.background_secondsoso2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud5);
                    txtStationName.setText("\t\t\t\t\t\t\t나쁨");
                    break;
                case 6:
                    backgroundUp.setBackgroundResource(R.drawable.background_thirdsoso);
                    backgroundDown.setBackgroundResource(R.drawable.background_thirdsoso2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud6);
                    txtStationName.setText("상당히 나쁨");
                    break;
                case 7:
                    backgroundUp.setBackgroundResource(R.drawable.background_bad);
                    backgroundDown.setBackgroundResource(R.drawable.background_bad2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud7);
                    txtStationName.setText("\t\t매우 나쁨");
                    break;
                case 8:
                    backgroundUp.setBackgroundResource(R.drawable.background_verybad);
                    backgroundDown.setBackgroundResource(R.drawable.background_verybad2);
                    btnMainCloud.setBackgroundResource(R.drawable.cloud8);
                    txtStationName.setText("\t\t\t\t\t\t\t최악");
                    break;


            }

            if(1<=Pm25Num && Pm25Num<=3){
                //if 문에 따른 코멘트
                if(inout){ // 실외
                    if(intage>=20){
                        txtComment.setText("산책하기 좋은 공기네요.");
                    }
                    else{
                        txtComment.setText("신선한 공기를 많이 마시세요~");
                    }
                }
                else{
                    if(intage>=20){ //실내

                        txtComment.setText("좋은 날씨. 좋은 하루 보내세요~");
                    }
                    else{
                        String[] txtGoodArray = {"야외활동을 즐기세요~","좋은 공기에 환기는 필수!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtGoodArray[r]);
                    }

                }
            }

            else if(4<=Pm25Num && Pm25Num<=5){
                //if 문에 따른 코멘트
                if(inout){//실외
                    if(intage>=20){
                        txtComment.setText("꾸준한 수분 섭취!!!");
                    }
                    else{
                        txtComment.setText("공기가 쏘쏘하네요^^");
                    }
                } //실외끝

                else{ //실내
                    if(intage>=20){
                        txtComment.setText("잠깐의 스트레칭은 어떠세요?");
                    }
                    else{
                        txtComment.setText("청소하기 좋은 날씨에요.");
                    }
                }
            } // 보통의 코멘트

            else if(6<=Pm25Num && Pm25Num<=8) { //나쁨
                //if 문에 따른 코멘트
                if (inout) { //실외
                    if (intage >= 20) {
                        String[] txtBadArray = {"노폐물 배출에 좋은 과일을 섭취!!", "건강을 위해!! 마스크 착용!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtBadArray[r]);

                    } else {
                        String[] txtBadArray1 = {"공기가 탁하네요. 조심하세요~", "나쁜 공기엔 마스크 필수!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtBadArray1[r]);
                    }
                } else { //실내
                    if (intage >= 20) {
                        String[] txtBadArray2 = {"수분 섭취를 많이 해주세요!!", "외출시 마스크. 외출후 손씻기!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtBadArray2[r]);
                    } else {
                        String[] txtBadArray3 = {"외출은 가급적 하지마세요~", "밖은 위험해요!!"};
                        int r = random.nextInt(1) ;
                        txtComment.setText(txtBadArray3[r]);
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        StrictMode.enableDefaults();

        backgroundUp = (LinearLayout) findViewById(R.id.backgroundUp);
        backgroundDown = (LinearLayout) findViewById(R.id.backgroundDown);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtUpdate = (TextView) findViewById(R.id.txtUpdate);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        btnMainCloud = (ImageButton) findViewById(R.id.btnMainCloud);
        txtMainStatusPm10 = (TextView) findViewById(R.id.txtMainStatusPm10);
        txtMainStatusPm25 = (TextView) findViewById(R.id.txtMainStatusPm25);
        txtMainStatusNo2 = (TextView) findViewById(R.id.txtMainStatusNo2);
        txtMainStatusSo2 = (TextView) findViewById(R.id.txtMainStatusSo2);
        txtMainStatusO3 = (TextView) findViewById(R.id.txtMainStatusO3);
        txtMainStatusCo = (TextView) findViewById(R.id.txtMainStatusCo);
        txtComment = (TextView)findViewById(R.id.txtComment) ;
        btn_home_map = (ImageButton) findViewById(R.id.btn_home_map);
        btn_home_set=findViewById(R.id.btn_home_set);

        final String sido = intent.getStringExtra("sido");
        final String sgg = intent.getStringExtra("sgg");
        final String umd = intent.getStringExtra("umd");

        SearchingKeyword = sido +  sgg   + umd;


        if(sido == null || sgg == null || umd == null) {
            getAccessToken();
            Parse_TM();
            Parse_getStation();
            Parse_StationNameToAdress();
            Parse_Adress();
            Parse_Data();

        }
        else{
            txtLocation.setText(SearchingKeyword);
            Parse_station(sido,sgg,umd);
            Parse_getStation();
            Parse_StationNameToAdress();
            Parse_Data();

        }

        btnMainCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (!isPermission) {
//                    callPermission();
//                    return;
//                }
//                gps = new GpsInfo(MainActivity.this);
//                // GPS 사용유무 가져오기
//                if (gps.isGetLocation()) {

//                    latitude = gps.getLatitude();
//                    longitude = gps.getLongitude();

                if(sido == null || sgg == null || umd == null) {
                    getAccessToken();
                    Parse_TM();
                    Parse_getStation();
                    Parse_StationNameToAdress();
                    Parse_Adress();
                    Parse_Data();

                }
                else{
                    txtLocation.setText(SearchingKeyword);
                    Parse_station(sido,sgg,umd);
                    Parse_getStation();
                    Parse_StationNameToAdress();
                    Parse_Data();

                }
//                } else {
//                    // GPS 를 사용할수 없으므로
//                    gps.showSettingsAlert();
//                }
            }
        });
        callPermission();
        btn_home_map.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent_toMap = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent_toMap);
            }
        });
        btn_home_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_toSet = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent_toSet);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        gps = new GpsInfo(MainActivity.this);
        if(isPermission==true){

            if (gps.isGetLocation()) {

//                latitude = gps.getLatitude();
//                longitude = gps.getLongitude();

                String sido = intent.getStringExtra("sido");
                String sgg = intent.getStringExtra("sgg");
                String umd = intent.getStringExtra("umd");

                SearchingKeyword = sido + " " + sgg + " " + umd;
                if(sido == null || sgg == null || umd == null) {
                    getAccessToken();
                    Parse_TM();
                    Parse_getStation();
                    Parse_StationNameToAdress();
                    Parse_Adress();
                    Parse_Data();

                }
                else{
                    txtLocation.setText(SearchingKeyword);
                    Parse_station(sido,sgg,umd);
                    Parse_getStation();
                    Parse_StationNameToAdress();
                    Parse_Data();

                }
            }
            else {
                // GPS 를 사용할수 없으므로
                gps.showSettingsAlert();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        SharedPreferences pref = getSharedPreferences("isFirst", MainActivity.MODE_PRIVATE);
//        boolean first = pref.getBoolean("isFirst", false);
//        if(first==false){
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putBoolean("isFirst",true);
//            editor.commit();
//            //앱 최초 실행시 StartPage 가 나오게 한다.
//        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        StrictMode.enableDefaults();

        backgroundUp = (LinearLayout) findViewById(R.id.backgroundUp);
        backgroundDown = (LinearLayout) findViewById(R.id.backgroundDown);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtUpdate = (TextView) findViewById(R.id.txtUpdate);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        btnMainCloud = (ImageButton) findViewById(R.id.btnMainCloud);
        txtMainStatusPm10 = (TextView) findViewById(R.id.txtMainStatusPm10);
        txtMainStatusPm25 = (TextView) findViewById(R.id.txtMainStatusPm25);
        txtMainStatusNo2 = (TextView) findViewById(R.id.txtMainStatusNo2);
        txtMainStatusSo2 = (TextView) findViewById(R.id.txtMainStatusSo2);
        txtMainStatusO3 = (TextView) findViewById(R.id.txtMainStatusO3);
        txtMainStatusCo = (TextView) findViewById(R.id.txtMainStatusCo);
        txtStationName = (TextView) findViewById(R.id.txtMainStatus);
        btn_home_map = (ImageButton) findViewById(R.id.btn_home_map);
        btn_home_set=findViewById(R.id.btn_home_set);

        Intent intent = getIntent();


        final String sido = intent.getStringExtra("sido");
        final String sgg = intent.getStringExtra("sgg");
        final String umd = intent.getStringExtra("umd");

        SearchingKeyword = sido + " " + sgg + " " + umd;
        if(sido == null || sgg == null || umd == null) {
            getAccessToken();
            Parse_TM();
            Parse_getStation();
            Parse_StationNameToAdress();
            Parse_Adress();
            Parse_Data();

        }
        else{
            txtLocation.setText(SearchingKeyword);
            Parse_station(sido,sgg,umd);
            Parse_getStation();
            Parse_StationNameToAdress();
            Parse_Data();

        }
        btnMainCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPermission) {
                    callPermission();
                    return;
                }
                gps = new GpsInfo(MainActivity.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {

//                    latitude = gps.getLatitude();
//                    longitude = gps.getLongitude();


                    Toast.makeText(
                            getApplicationContext(),
                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                            Toast.LENGTH_LONG).show();
                    if(sido == null || sgg == null || umd == null) {
                        getAccessToken();
                        Parse_TM();
                        Parse_getStation();
                        Parse_StationNameToAdress();
                        Parse_Adress();
                        Parse_Data();

                    }
                    else{
                        txtLocation.setText(SearchingKeyword);
                        Parse_station(sido,sgg,umd);
                        Parse_getStation();
                        Parse_StationNameToAdress();
                        Parse_Data();

                    }
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });

        btn_home_map.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent_toMap = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent_toMap);
            }
        });
        btn_home_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_toSet = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent_toSet);
            }
        });
    } // 다른화면에서 메인으로 돌아왔을때 다시 실행시키기

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
    private  void callPermission() {
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