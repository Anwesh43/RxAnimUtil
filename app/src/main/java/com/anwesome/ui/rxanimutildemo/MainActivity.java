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
        ImageView imageView2 = (ImageView)findViewById(R.id.im_view2);
        final ImageView imageView3 = (ImageView)findViewById(R.id.im_view3);
        ImageView imageView4 = (ImageView)findViewById(R.id.im_view4);
        RxAnimUtil.rotateView(imageView,40,270,100,null);
        RxAnimUtil.translateView(imageView2,300,700, RxAnimUtil.RxAnimTranslation.XY,100,null);
        RxAnimUtil.scaleView(imageView3, 0.2f, 1.5f, 100, new RxAnimUtil.RxAnimationListener() {
            @Override
            public void onAnimationEnd() {
                RxAnimUtil.fadeOutView(imageView3,300,null);
            }

            @Override
            public void onAnimationRunning() {

            }

            @Override
            public void onAnimationStart() {

            }
        });
        RxAnimUtil.fadeInView(imageView4,300,null);
    }
    public void onPause() {
        super.onPause();
        RxAnimUtil.pause();

    }
    public void onResume() {
        super.onResume();
    }
}
