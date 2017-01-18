package com.anwesome.ui.rxanimutildemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.anwesome.ui.rxanimutil.RxAnimUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView)findViewById(R.id.im_view);
        RxAnimUtil.rotateView(imageView,40,270,100);
    }
    public void onPause() {
        super.onPause();
        RxAnimUtil.pause();

    }
    public void onResume() {
        super.onResume();
    }
}
