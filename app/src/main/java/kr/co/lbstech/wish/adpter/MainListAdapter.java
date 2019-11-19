package kr.co.lbstech.wish.adpter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.lbstech.wish.R;
import kr.co.lbstech.wish.model.Schedule;

public class MainListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Schedule> schedules;
    private LayoutInflater inflater;

    public MainListAdapter(Context context, ArrayList<Schedule> schedules, LayoutInflater inflater){
        this.context = context;
        this.schedules = schedules;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Object getItem(int i) {
        return schedules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.main_list_item, null);
        TextView time = v.findViewById(R.id.time);
        ImageView alarm = v.findViewById(R.id.iv_alarm);
        TextView task = v.findViewById(R.id.tv_task);

        time.setText(schedules.get(i).getTime());
        Drawable alarmImg;
        if(schedules.get(i).isAlarmEnable())
            alarmImg = context.getDrawable(R.drawable.ic_notifications_active);
        else
            alarmImg = context.getDrawable(R.drawable.ic_notifications_off);
        alarm.setImageDrawable(alarmImg);
        task.setText(schedules.get(i).getTask());

        return v;
    }

}
