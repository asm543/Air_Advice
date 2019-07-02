package com.example.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private TextView txtLocation;
    private EditText edtAge,edtJob;
    private ImageButton btnSave,btnJump,btnStartSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);

        edtAge = (EditText) findViewById(R.id.edtAge);
        edtJob = (EditText) findViewById(R.id.edtJob);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        btnStartSearch = (ImageButton) findViewById(R.id.btnStartSearch);
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnJump = (ImageButton) findViewById(R.id.btnJump);

        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String text = sf.getString("text", "");

        edtAge.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                edtAge.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Activity가 종료되기 전에 저장한다.
                //SharedPreferences를 sFile이름, 기본모드로 설정
                SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);

                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = sf.edit();
                String textAge = edtAge.getText().toString().trim(); // 사용자가 입력한 저장할 데이터
                editor.putString("textAge",textAge); // key, value를 이용하여 저장하는 형태
                String textJob = edtJob.getText().toString().trim(); // 사용자가 입력한 저장할 데이터
                editor.putString("textJob",textJob); // key, value를 이용하여 저장하는 형태


                editor.commit();
                Toast.makeText(StartActivity.this, "정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent_toMain = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent_toMain);
            }
        });
        btnJump.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(StartActivity.this, "정보입력을 건너뜁니다.", Toast.LENGTH_SHORT).show();
                Intent intent_toMain = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent_toMain);
            }
        });
        btnStartSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent_toSearch = new Intent(StartActivity.this, SearchingActivity.class);
                startActivity(intent_toSearch);
            }
        });
    }
}
