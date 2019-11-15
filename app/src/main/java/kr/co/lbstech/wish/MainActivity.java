package kr.co.lbstech.wish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.lbstech.wish.adpter.MainListAdapter;
import kr.co.lbstech.wish.model.Schedule;

public class MainActivity extends AppCompatActivity
        implements OnDayClickListener, AdapterView.OnItemClickListener {

    CalendarView calendarView;
    ListView listView;
    MainListAdapter adapter;

    ArrayList<Schedule> schedules = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        calendarView = findViewById(R.id.calendar_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        schedules.add(new Schedule(Calendar.getInstance(), "첫 할일", true));
        schedules.add(new Schedule(Calendar.getInstance(), "첫 할일", true));
        schedules.add(new Schedule(Calendar.getInstance(), "첫 할일", true));
        adapter = new MainListAdapter(this, schedules, getLayoutInflater());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        calendarView.setPreviousButtonImage(getDrawable(R.drawable.ic_chevron_left_black));
        calendarView.setForwardButtonImage(getDrawable(R.drawable.ic_chevron_right_black));
        calendarView.setOnDayClickListener(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int result = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if(result != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.emerency, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_emergency :
                Intent intent = new Intent(this, EmergencyActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDayClick(EventDay eventDay) {
        Toast.makeText(this, eventDay.getCalendar().get(Calendar.DAY_OF_MONTH)+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        boolean alarmEnable = schedules.get(i).isAlarmEnable();
        schedules.get(i).setAlarmEnable(!alarmEnable);
        adapter.notifyDataSetChanged();
    }

    public void goToInfo(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public void goToAlarm(View view) {
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
