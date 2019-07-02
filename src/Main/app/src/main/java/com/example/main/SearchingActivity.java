package com.example.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;
import java.util.ArrayList;

public class SearchingActivity extends FontActivity {

    ListView listview;
    String sidoName=null,sggName=null,umdName=null,tmX=null,tmY=null;
    boolean insidoName = false,insggName = false,inumdName = false,intmX = false,intmY = false;
    String SearchingKeyword = "";
    ArrayList<Listviewitem> list=new ArrayList<>();
    EditText edtSearch;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
          list.clear();
          SearchingKeyword =edtSearch.getText().toString();
          Parse_station();

        }

        @Override
        public void afterTextChanged(Editable editable) {
            list.clear();
            SearchingKeyword =editable.toString();
            Parse_station();
        }
    };

    String tv=null;



    private void Parse_station(){
        try{
            URL url2 = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getTMStdrCrdnt?serviceKey=" +
                    "IPbBi9DbtpkIHLLYxiEdNhiPoe%2B2ZzZWPHoag%2FeAOimpSX%2FCAZW4%2FU8CmowZTEuFFzgXP3%2FRAuH%2FZYJQ2fQgxQ%3D%3D&numOfRows=6000&pageNo=1&umdName="+SearchingKeyword);
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
                            intmX = false;
                        }
                        if (intmY) {
                            tmY = parser.getText();
                            intmY = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            String adress = sidoName + " " + sggName + " " + umdName;
                            Listviewitem item=new Listviewitem(adress, tmX,tmY);

                            list.add(item);

                        }
                        break;
                }
                parserEvent = parser.next();
            }
        }
        catch (Exception e){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchingpage);

        listview = (ListView) findViewById(R.id.listview);
        edtSearch = (EditText)findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(textWatcher);

        list.clear();


        edtSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(edtSearch.getText().toString().equals("위치를 주소로 입력해주세요!")) {

                    edtSearch.setText("");
                }
            }
        });

        AdapterView.OnItemClickListener listViewExampleClickListener = new AdapterView.OnItemClickListener()

        {

            public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id)

            {
                Intent intent = new Intent(SearchingActivity.this, MainActivity.class);
                tv = (String)parentView.getAdapter().getItem(position);
                String[] TV = tv.split(" ");
                Toast.makeText(getApplicationContext(), tv + "의 정보를 불러옵니다.", Toast.LENGTH_SHORT).show();

                intent.putExtra("sido",TV[0]);
                intent.putExtra("sgg",TV[1]);
                intent.putExtra("umd",TV[2]);


                //String toastMessage =  sidoName + " " + sggName + " " +  umdName + " " + " is selected. position is " + ""+position + ", and id is " + tmY;

                //Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }

        };
        ListviewAdapter adapter = new ListviewAdapter(SearchingActivity.this,R.layout.item, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(listViewExampleClickListener);





        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터

        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView,
//                                    View view, int position, long id) {
//
//                //클릭한 아이템의 문자열을 가져옴
//                String selected_item = (String)adapterView.getItemAtPosition(position);
//
//                //텍스트뷰에 출력
//            }
//        });
    }



}