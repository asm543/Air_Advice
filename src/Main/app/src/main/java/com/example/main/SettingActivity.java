package com.example.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

import java.util.Set;

public class SettingActivity extends AppCompatActivity {
private ImageButton btnSetSearch, btnMySet, btnSetMain;
public Switch btnSetNotice;
private GpsInfo gps;

    private void CheckState(){

        if(btnSetNotice.isChecked())   {
            if (gps.isGetLocation()) {

            } else {
                // GPS 를 사용할수 없으므로
                showSettingsAlert(); // 설정창으로 이동 , 온으로 설정해주세요 부탁한다
            }
        }
        else{
            if (gps.isGetLocation()) {
                showSettingsAlert(); // 설정창으로 이동 , 오프로 설정해주세요 부탁한다
            } else {
            }
        }
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 사용 권한을 직접 설정하셔야 합니다.\n 설정창으로 가시겠습니까?");

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
                        btnSetNotice.setChecked(!btnSetNotice.isChecked());
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        btnSetSearch = (ImageButton)findViewById(R.id.btnSetSearch);
        btnMySet = (ImageButton)findViewById(R.id.btnMySet);
        btnSetMain =(ImageButton)findViewById(R.id.btnSetMain);
        btnSetNotice = (Switch)findViewById(R.id.btnSetNotice);

        gps = new GpsInfo(SettingActivity.this);

        if (gps.isGetLocation()) {
            btnSetNotice.setChecked(true);
        }
        else btnSetNotice.setChecked(false);

//        btnSetSearch.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent_toSearch = new Intent(SettingActivity.this,SearchingActivity.class);
//                startActivity(intent_toSearch);
//            }
//        });
        btnMySet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_toMySet = new Intent(SettingActivity.this,SetMyPageActivity.class);
                startActivity(intent_toMySet);
            }
        });
        btnSetMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_toMain = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent_toMain);
            }
        });
        btnSetNotice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//
//                CheckState();
                if(btnSetNotice.isChecked())   {
                    if (gps.isGetLocation()) {

                    } else {
                        // GPS 를 사용할수 없으므로
                        showSettingsAlert(); // 설정창으로 이동 , 온으로 설정해주세요 부탁한다
                    }
                }
                else{
                    if (gps.isGetLocation()) {
                        showSettingsAlert(); // 설정창으로 이동 , 오프로 설정해주세요 부탁한다
                    } else {
                    }
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

//        btnSetSearch = (ImageButton)findViewById(R.id.btnSetSearch);
        btnMySet = (ImageButton)findViewById(R.id.btnMySet);
        btnSetMain =(ImageButton)findViewById(R.id.btnSetMain);
        btnSetNotice = (Switch)findViewById(R.id.btnSetNotice);

        gps = new GpsInfo(SettingActivity.this);

        if (gps.isGetLocation()) {
            btnSetNotice.setChecked(true);
        }
        else btnSetNotice.setChecked(false);

//        btnSetSearch.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent_toSearch = new Intent(SettingActivity.this,SearchingActivity.class);
//                startActivity(intent_toSearch);
//            }
//        });
        btnMySet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_toMySet = new Intent(SettingActivity.this,SetMyPageActivity.class);
                startActivity(intent_toMySet);
            }
        });
        btnSetMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_toMain = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent_toMain);
            }
        });
        btnSetNotice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//
//                CheckState();
                if(btnSetNotice.isChecked())   {
                    if (gps.isGetLocation()) {

                    } else {
                        // GPS 를 사용할수 없으므로
                        showSettingsAlert(); // 설정창으로 이동 , 온으로 설정해주세요 부탁한다
                    }
                }
                else{
                    if (gps.isGetLocation()) {
                        showSettingsAlert(); // 설정창으로 이동 , 오프로 설정해주세요 부탁한다
                    } else {
                    }
                }
            }
        });
    }
}


