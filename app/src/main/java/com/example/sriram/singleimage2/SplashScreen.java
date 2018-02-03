package com.example.sriram.singleimage2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.PixelCopy;

import android.view.Window;
import android.graphics.PixelFormat;
import android.animation.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.Window;
import android.view.SurfaceHolder;
import android.graphics.ImageFormat;

public class SplashScreen extends AppCompatActivity {

   /* public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

   // Thread splashThread;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        //startAnimation();
    }

    /*private void startAnimation() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView imgView = (ImageView) findViewById(R.id.splash);
        imgView.clearAnimation();
        imgView.startAnimation(anim);

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 1000) {
                        sleep(100);
                        waited += 100;
                    }
                    splashThread.start();
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashScreen.this.finish();
                } catch (InterruptedException e) {

                } finally {
                    SplashScreen.this.finish();
                }
            }
        };

    }*/
}

