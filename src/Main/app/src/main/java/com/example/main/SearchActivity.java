package com.example.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ;
    ListView listview;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.listview);
//        list = new ArrayList<>();
//        list.add("사과");
//        list.add("배");
//        list.add("귤");
//        list.add("바나나");

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, LIST_MENU);

//        if(LIST_MENU ==null){
//            Log.d("~","NULL!!!!!!!!!!!!!!!");
//        }
//        //리스트뷰의 어댑터를 지정해준다.
//        else listview.setAdapter(adapter);
//
//
//        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
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
