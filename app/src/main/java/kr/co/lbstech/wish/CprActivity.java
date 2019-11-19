package kr.co.lbstech.wish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CprActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Manual> manuals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpr);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manuals.add(new Manual(R.string.cpr_sequence_1, R.string.cpr_description_1, R.drawable.cpr1));
        manuals.add(new Manual(R.string.cpr_sequence_2, R.string.cpr_description_2, R.drawable.cpr2));
        manuals.add(new Manual(R.string.cpr_sequence_3, R.string.cpr_description_3, R.drawable.cpr3));
        manuals.add(new Manual(R.string.cpr_sequence_4, R.string.cpr_description_4, R.drawable.cpr4));
        manuals.add(new Manual(R.string.cpr_sequence_5, R.string.cpr_description_5, R.drawable.cpr5));
        manuals.add(new Manual(R.string.cpr_sequence_6, R.string.cpr_description_6, R.drawable.cpr6));

        listView = findViewById(R.id.list_view);
        listView.setAdapter(new ListAdapter());

    }

    private class Manual {
        int sequence;
        int description;
        int image;

        private Manual(int sequence, int description, int image){
            this.sequence = sequence;
            this.description = description;
            this.image = image;
        }
    }

    private class ListAdapter extends BaseAdapter{
        @Override
        public int getCount() { return manuals.size(); }
        @Override
        public Object getItem(int i) { return manuals.get(i); }
        @Override
        public long getItemId(int i) { return i; }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.cpr_list_item, null);

            TextView tvNumber = v.findViewById(R.id.tv_num);
            TextView tvSequence = v.findViewById(R.id.tv_sequence);
            ImageView ivImage = v.findViewById(R.id.iv_image);
            TextView tvDescription = v.findViewById(R.id.tv_description);

            tvNumber.setText((i+1)+"");
            tvSequence.setText(manuals.get(i).sequence);
            Glide.with(CprActivity.this).load(manuals.get(i).image).into(ivImage);
            tvDescription.setText(manuals.get(i).description);
            return v;
        }
    }
}
