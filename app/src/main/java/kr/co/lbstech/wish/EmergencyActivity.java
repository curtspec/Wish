package kr.co.lbstech.wish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    public void callEmergency(View view) {
        Intent intent = new Intent(this, CallingActivity.class);
        startActivity(intent);
    }

    public void getLocation(View view) {
    }

    public void howToCPR(View view) {
        Intent intent = new Intent(this, CprActivity.class);
        startActivity(intent);
    }

    public void getUsage(View view) {
        Intent intent = new Intent(this, AEDActivity.class);
        startActivity(intent);
    }
}
