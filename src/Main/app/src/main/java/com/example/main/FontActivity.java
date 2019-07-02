package com.example.main;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontActivity extends AppCompatActivity {
    public Typeface mTypeface = null;

    public void setContentView(int layoutResID){
        super.setContentView(layoutResID);
        if(mTypeface == null){
            mTypeface = Typeface.createFromAsset(this.getAssets(),"fonts/JejuGothic.ttf");
        }
        setGlobalFont(getWindow().getDecorView());
    }

    private void setGlobalFont(View view) {
        if(view != null){
            if(view instanceof ViewGroup){
                ViewGroup vg = (ViewGroup) view;
                int vgCount = vg.getChildCount();
                for (int i = 0; i<vgCount;i++){
                    View v = vg.getChildAt(i);
                    if(v instanceof TextView){
                        ((TextView) v).setTypeface(mTypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }
}
