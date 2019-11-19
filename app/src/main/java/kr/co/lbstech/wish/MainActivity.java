package kr.co.lbstech.wish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kr.co.lbstech.wish.adpter.MainListAdapter;
import kr.co.lbstech.wish.model.Schedule;

public class MainActivity extends AppCompatActivity
        implements OnDayClickListener, AdapterView.OnItemClickListener {

    CalendarView calendarView;
    ListView listView;
    MainListAdapter adapter;

    ArrayList<Schedule> schedules = new ArrayList<>();
    ArrayList<EventDay> eventDays = new ArrayList<>();

    HashMap<Long, ArrayList<Schedule>> data = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        calendarView = findViewById(R.id.calendar_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        adapter = new MainListAdapter(this, schedules, getLayoutInflater());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        calendarView.setPreviousButtonImage(getDrawable(R.drawable.ic_chevron_left_black));
        calendarView.setForwardButtonImage(getDrawable(R.drawable.ic_chevron_right_black));
        calendarView.setOnDayClickListener(this);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        Map<String, ?> savedData = pref.getAll();
        Log.d("저장 테스트", savedData.toString());
        for (String keys : savedData.keySet()){
            String jsonData = (String) savedData.get(keys);
            ArrayList<Schedule> list =
                    new Gson().fromJson(jsonData, new TypeToken<ArrayList<Schedule>>(){}.getType());
            Log.d("변환 테스트", list.size() + "");
            data.put(Long.parseLong(keys), list);
        }


        Set<Long> keys = data.keySet();
        for(Long day : keys) {
            Calendar c= Calendar.getInstance();
            c.setTimeInMillis(day);
            eventDays.add(new EventDay(c, R.drawable.ic_current_day_icon));
        }
        calendarView.setEvents(eventDays);

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
        schedules.clear();
        Calendar clickedDay = eventDay.getCalendar();
        Set<Long> keys = data.keySet();
        for(Long day : keys){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(day);
            if(isSameDay(c, clickedDay)){
                Log.d("데이터 테스트", data.get(day) + "");
                ArrayList<Schedule> s = data.get(day);
                if( s != null ) schedules.addAll(s);
                adapter.notifyDataSetChanged();
                break;
            }
        }
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

    public void clickAdd(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.main_dialog, null);
        TimePicker picker = v.findViewById(R.id.time_picker);
        EditText text = v.findViewById(R.id.et_task);

        builder.setView(v);
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hour, minute;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    hour = picker.getHour();
                    minute = picker.getMinute();
                }else{
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                Calendar selectedCalendar = calendarView.getFirstSelectedDate();
                selectedCalendar.set(selectedCalendar.get(Calendar.YEAR),
                        selectedCalendar.get(Calendar.MONTH),
                        selectedCalendar.get(Calendar.DAY_OF_MONTH),
                        hour,
                        minute);

                Schedule schedule = new Schedule(selectedCalendar.getTimeInMillis(),
                        text.getText().toString(), true);

                boolean isContain = false;
                Set<Long> keys = data.keySet();
                for(Long day : keys){
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(day);
                    if(isSameDay(c, selectedCalendar)){
                        ArrayList<Schedule> value = data.get(day);
                        if (value == null) break;
                        value.add(schedule);
                        data.put(day, value);
                        schedules.add(schedule);
                        isContain = true;
                    }
                }
                if(isContain) return;
                ArrayList<Schedule> value = new ArrayList<>();
                value.add(schedule);
                data.put(selectedCalendar.getTimeInMillis(), value);
                schedules.add(schedule);
                eventDays.add(new EventDay(selectedCalendar, R.drawable.ic_current_day_icon));
                calendarView.setEvents(eventDays);
            }
        });
        builder.setNegativeButton("취소", null);
        builder.create().show();

        Toast.makeText(this, "추가버튼", Toast.LENGTH_SHORT).show();
    }

    public boolean isSameDay(Calendar a, Calendar b){
        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.MONTH) == b.get(Calendar.MONTH) &&
                a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        Set<Long> keySet = data.keySet();
        for (Long day : keySet){
            Log.d("저장테스트", new Gson().toJson(data.get(day)));
            pref.edit().putString(day+"", new Gson().toJson(data.get(day)))
                    .apply();
        }
    }
}
