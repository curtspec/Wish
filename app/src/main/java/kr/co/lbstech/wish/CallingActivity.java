package kr.co.lbstech.wish;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CallingActivity extends AppCompatActivity {
    private final String PREF_NAME = "information";

    ImageView iv_radar;
    Animation animation;
    TextView tvTitle;
    GestureDetector gestureListener;
    CardView cancelBtn;
    private ArrayList<String> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        getGlobalVariable();

        tvTitle = findViewById(R.id.tv_title);
        cancelBtn = findViewById(R.id.btn_cancel);
        iv_radar = findViewById(R.id.circle_radar);

        animation = AnimationUtils.loadAnimation(this, R.anim.fade_out_after_scale);
        animation.setRepeatCount(Animation.INFINITE);
        cancelBtn.setVisibility(GV.isEmerency ? View.VISIBLE : View.INVISIBLE);

        gestureListener = new GestureDetector(this, new GestureListener());
    }

    private void getGlobalVariable() {
        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Set tmp = pref.getStringSet("number", new HashSet<>());
        numbers.addAll(tmp);
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
        @SuppressLint("IntentReset")
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            if(!GV.isEmerency){
                GV.isEmerency = true;
                tvTitle.setText(R.string.calling_title_emerency);
                iv_radar.startAnimation(animation);
                cancelBtn.setVisibility(View.VISIBLE);

                if(!numbers.isEmpty()){
                    StringBuffer buffer = new StringBuffer();
                    for (String number : numbers){
                        buffer.append(number).append(",");
                    }

                    try{
                        Intent intent =
                                new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + buffer.toString()));
                        intent.putExtra(Intent.EXTRA_TEXT, "긴급한 상황으로 응급호출을 시작했습니다.\n연락을 취해 도움을 요청해주세요.");
                        CallingActivity.this.startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            return super.onDoubleTap(event);
        }
    }
}
