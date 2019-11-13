package kr.co.lbstech.wish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout logoType;
    private Animation animation;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoType = findViewById(R.id.logotype);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
            }
        };

        animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                handler.postDelayed(runnable, 500);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        logoType.startAnimation(animation);
    }
}
