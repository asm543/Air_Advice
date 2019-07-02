package com.example.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SetMyPageActivity extends AppCompatActivity {

    private EditText edtAge,edtJob;
    private boolean saveLoginData,inout;
    private String age;
    private Switch InOutSwitch;
    private ImageButton btnBack,btnSave,btnSetMySearch;
    private TextView txtAge;

    private void load() {

        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
        saveLoginData =  sf.getBoolean("SAVE_LOGIN_DATA", false);
        age = sf.getString("textAge", "나이를 입력해주세요!");
        inout = sf.getBoolean("inout",true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_my_page);

        edtAge = (EditText)findViewById(R.id.edtAge);
        edtJob = (EditText)findViewById(R.id.edtJob);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnSetMySearch = (ImageButton)findViewById(R.id.btnSetMySearch);
        btnSave = (ImageButton)findViewById(R.id.btnSave);
        InOutSwitch = (Switch)findViewById(R.id.InOutSwitch);
        txtAge = (TextView)findViewById(R.id.txtAge);

        load();

        txtAge.setText("나이 : "+age);
        InOutSwitch.setChecked(inout);

        InOutSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        edtAge.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                edtAge.setText("");
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
                boolean inoutchecked = InOutSwitch.isChecked();
                if(!edtAge.getText().equals("")){
                    editor.putString("textAge",textAge); // key, value를 이용하여 저장하는 형태
                }
                editor.putBoolean("inout",inoutchecked);
                editor.commit();
                Toast.makeText(SetMyPageActivity.this, "정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent_toMain = new Intent(SetMyPageActivity.this, MainActivity.class);
                startActivity(intent_toMain);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_toSetting = new Intent(SetMyPageActivity.this,SettingActivity.class);
                startActivity(intent_toSetting);
            }
        });
        btnSetMySearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent_toSearch = new Intent(SetMyPageActivity.this, SearchingActivity.class);
                startActivity(intent_toSearch);
            }
        });
    }

}