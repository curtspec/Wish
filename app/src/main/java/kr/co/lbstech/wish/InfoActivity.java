package kr.co.lbstech.wish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class InfoActivity extends AppCompatActivity {
    private final String PREF_NAME = "information";

    private Calendar calendar = Calendar.getInstance();
    private ArrayList<String> hospitals = new ArrayList<>();
    private ArrayList<String> numbers = new ArrayList<>();

    private Toolbar toolbar;
    private TextView tvTime;
    private EditText et_etc;
    private CheckBox[] cbs = new CheckBox[7];
    private ListView hospitalListView;
    private ListView numberListView;
    private HospitalListAdapter hospitalAdapter;
    private NumberListAdapter numberAdapter;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        toolbar = findViewById(R.id.toolbar);
        tvTime = findViewById(R.id.tv_time);
        et_etc = findViewById(R.id.et_etc);
        hospitalListView = findViewById(R.id.list_view);
        numberListView = findViewById(R.id.number_list);
        cbs[0] = findViewById(R.id.rb_1);
        cbs[1] = findViewById(R.id.rb_2);
        cbs[2] = findViewById(R.id.rb_3);
        cbs[3] = findViewById(R.id.rb_4);
        cbs[4] = findViewById(R.id.rb_5);
        cbs[5] = findViewById(R.id.rb_6);
        cbs[6] = findViewById(R.id.rb_7);

        hospitalAdapter = new HospitalListAdapter();
        hospitalListView.setAdapter(hospitalAdapter);
        numberAdapter = new NumberListAdapter();
        numberListView.setAdapter(numberAdapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        getState();
    }

    // 상태 가져오기
    private void getState() {
        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        hospitals.addAll(pref.getStringSet("hospitals", new HashSet<>()));
        hospitalAdapter.notifyDataSetChanged();
        modifyListHeight();

        numbers.clear();
        numberAdapter.notifyDataSetChanged();
        if(GV.numbers != null) numbers.addAll(GV.numbers);
        else numbers.addAll(pref.getStringSet("number", new HashSet<>()));
        numberAdapter.notifyDataSetChanged();

        calendar.setTimeInMillis(pref.getLong("time", calendar.getTimeInMillis()));
        setTimeText(calendar);
        for (int i =0; i < cbs.length; i++)
            cbs[i].setChecked(pref.getBoolean("cb"+i, false));
        if(cbs[6].isChecked()) {
            et_etc.setEnabled(cbs[6].isChecked());
            et_etc.setText(pref.getString("etc", ""));
        }
        numbers.addAll(pref.getStringSet("number", new HashSet<>()));
    }

    @SuppressLint("SetTextI18n")
    private void setTimeText(Calendar day) {
        tvTime.setText(day.get(Calendar.YEAR)+ "." +
                day.get(Calendar.MONTH) + "." +
                day.get(Calendar.DAY_OF_MONTH));
    }

    public void pickDate(View view) {
        Calendar today = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                setTimeText(calendar);
            }
        }, today.get(Calendar.YEAR),
           today.get(Calendar.MONTH),
           today.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void searchHospital(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("병원 추가");
        View v = getLayoutInflater().inflate(R.layout.info_dialog, null);
        EditText et = v.findViewById(R.id.et_hospital);
        builder.setView(v);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String data = et.getText().toString();
                if(data.length() > 0) {
                    hospitals.add(et.getText().toString());
                    hospitalAdapter.notifyDataSetChanged();
                    modifyListHeight();
                }
            }
        });
        builder.setNegativeButton("취소", null);

        builder.create().show();
    }

    public void saveState(View view) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("time", calendar.getTimeInMillis());
        HashSet<String> tmp = new HashSet<>();
        tmp.addAll(hospitals);
        editor.putStringSet("hospitals", tmp);
        for(int i = 0; i< cbs.length; i++){
            editor.putBoolean("cb"+i, cbs[i].isChecked());
        }
        if(cbs[6].isChecked()) {
            editor.putString("etc", et_etc.getText().toString());
        }
        editor.apply();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("저장 완료");
        builder.setPositiveButton("확인", null);
        builder.create().show();
    }

    public void checkRadioButton(View view) {
        if (view instanceof CheckBox){
            CheckBox btn = (CheckBox) view;
            if(btn == cbs[6]){
                et_etc.setEnabled(cbs[6].isChecked());
            }
        }
    }

    private void modifyListHeight(){
        int size = hospitals.size();
        int min = 120;
        int max = 360;
        int height;
        if(size < 1)
            height = 0;
        else if(size < 3)
            height = min * size;
        else
            height = max;

        ViewGroup.LayoutParams params = hospitalListView.getLayoutParams();
        params.height = height;
        hospitalListView.setLayoutParams(params);
        hospitalListView.requestLayout();
    }

    public void addNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("연락처 추가");
        View v = getLayoutInflater().inflate(R.layout.info_dialog, null);
        EditText et = v.findViewById(R.id.et_hospital);
        et.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(v);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String data = et.getText().toString();
                if(data.length() > 0) {
                    numbers.add(et.getText().toString());
                    numberAdapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("취소", null);

        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        GV.numbers.clear();
        GV.numbers.addAll(numbers);

        HashSet<String> tmp = new HashSet<>();
        tmp.addAll(numbers);
        pref.edit().putStringSet("number", tmp).apply();
        super.onDestroy();
    }

    class HospitalListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return hospitals.size();
        }
        @Override
        public Object getItem(int i) {
            return hospitals.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.info_list_item, null);
            TextView tv = v.findViewById(R.id.tv);
            tv.setText(hospitals.get(i));
            ImageView iv = v.findViewById(R.id.iv_remove);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hospitals.remove(i);
                    modifyListHeight();
                    HospitalListAdapter.this.notifyDataSetChanged();
                }
            });
            return v;
        }
    }

    class NumberListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return numbers.size();
        }
        @Override
        public Object getItem(int i) {
            return numbers.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.info_list_item, null);
            TextView tv = v.findViewById(R.id.tv);
            tv.setText(numbers.get(i));
            ImageView iv = v.findViewById(R.id.iv_remove);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numbers.remove(i);
                    NumberListAdapter.this.notifyDataSetChanged();
                }
            });
            return v;
        }
    }

}
