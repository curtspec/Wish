package kr.co.lbstech.wish;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CallingActivity extends AppCompatActivity {

    ImageView iv_radar;
    Animation animation;
    TextView tvTitle;
    GestureDetector gestureListener;
    CardView cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        tvTitle = findViewById(R.id.tv_title);
        cancelBtn = findViewById(R.id.btn_cancel);
        iv_radar = findViewById(R.id.circle_radar);

        animation = AnimationUtils.loadAnimation(this, R.anim.fade_out_after_scale);
        animation.setRepeatCount(Animation.INFINITE);
        cancelBtn.setVisibility(GV.isEmerency ? View.VISIBLE : View.INVISIBLE);

        gestureListener = new GestureDetector(this, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureListener.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(GV.isEmerency){
            iv_radar.startAnimation(animation);
            tvTitle.setText(R.string.calling_title_emerency);
        }
    }

    public void cancelCalling(View view) {
        iv_radar.clearAnimation();
        GV.isEmerency = false;
        tvTitle.setText(R.string.calling_title_normal);
        cancelBtn.setVisibility(View.INVISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("호출 취소");
        builder.setMessage("긴급 호출이 취소되었습니다.");
        builder.setPositiveButton("확인", null);
        builder.create().show();
    }

    public void closeActivity(View view) {
        finish();
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(!GV.isEmerency){
                GV.isEmerency = true;
                tvTitle.setText(R.string.calling_title_emerency);
                iv_radar.startAnimation(animation);
                cancelBtn.setVisibility(View.VISIBLE);
            }
            return super.onDoubleTap(e);
        }
    }
}
