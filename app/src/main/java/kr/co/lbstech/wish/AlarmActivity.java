package kr.co.lbstech.wish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    TextView tvTime;
    ImageView iv_radar;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alam);

        tvTime = findViewById(R.id.tv_time);
        iv_radar = findViewById(R.id.circle_radar);

        animation = AnimationUtils.loadAnimation(this, R.anim.fade_out_after_scale);
        animation.setRepeatCount(Animation.INFINITE);

        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        String apam = hour < 12 ? "오전" : "오후";
        tvTime.setText(
                today.get(Calendar.MONTH) + "월" +
                today.get(Calendar.DAY_OF_MONTH) + "일\n" +
                apam + " " + hour + ":" + today.get(Calendar.MINUTE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_radar.startAnimation(animation);
    }

    public void closeActivity(View view) {
        finish();
    }
}
