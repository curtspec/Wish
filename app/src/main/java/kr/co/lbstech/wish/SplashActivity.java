package kr.co.lbstech.wish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SplashActivity extends AppCompatActivity {
    private final String PREF_NAME = "information";

    private RelativeLayout logoType;
    private Animation animation;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getGlobalVariable();

        logoType = findViewById(R.id.logotype);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // 애니메이션 끝나고 실행되는 영역
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

    private void getGlobalVariable() {
        if(GV.numbers == null) {
            GV.numbers = new ArrayList<>();
            SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            Set tmp = pref.getStringSet("number", new HashSet<>());
            GV.numbers.addAll(tmp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logoType.startAnimation(animation);
    }
}
