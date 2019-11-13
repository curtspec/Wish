package kr.co.lbstech.wish;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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

        schedules.add(new Schedule(Calendar.getInstance(), "첫 할일", true));
        schedules.add(new Schedule(Calendar.getInstance(), "첫 할일", true));
        schedules.add(new Schedule(Calendar.getInstance(), "첫 할일", true));
        adapter = new MainListAdapter(this, schedules, getLayoutInflater());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        calendarView.setPreviousButtonImage(getDrawable(R.drawable.ic_chevron_left_black));
        calendarView.setForwardButtonImage(getDrawable(R.drawable.ic_chevron_right_black));
        calendarView.setOnDayClickListener(this);
    }

    @Override
    public void onDayClick(EventDay eventDay) {
        Toast.makeText(this, eventDay.getCalendar().get(Calendar.DAY_OF_MONTH)+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ImageView alarm = view.findViewById(R.id.iv_alarm);
        boolean alarmEnable = schedules.get(i).isAlarmEnable();
        if(alarmEnable) alarm.setImageResource(R.drawable.ic_notifications_off);
        else alarm.setImageResource(R.drawable.ic_notifications_active);

        schedules.get(i).setAlarmEnable(!alarmEnable);
    }
}
