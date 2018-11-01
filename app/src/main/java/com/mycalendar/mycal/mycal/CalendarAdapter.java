package com.mycalendar.mycal.mycal;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter {
    private ArrayList<String> days;
    private Context contexts;
    private int laySource;
    private LayoutInflater layoutInflater;

    public CalendarAdapter(Context context, int resource, ArrayList<String> day) {
        this.contexts = context;
        this.laySource = resource;
        this.days = day;
        this.layoutInflater = (LayoutInflater)contexts.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View v = convertview;

        String day = days.get(position);
        Each_day_flame each_day_flame;

        if (v == null) {
            v = layoutInflater.inflate(laySource, null);

            DisplayMetrics displayMetrics = contexts.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels / 7;
            int restWidth = displayMetrics.widthPixels % 7;
            int height = 840 / 6;

            if (position % 7 == 6)
                v.setLayoutParams(new GridView.LayoutParams(width+restWidth, height));
            else
                v.setLayoutParams(new GridView.LayoutParams(width, height));

            each_day_flame = new Each_day_flame();

            each_day_flame.dBackground = (LinearLayout) v.findViewById(R.id.day_background);
            each_day_flame.each_day = (TextView)v.findViewById(R.id.each_day);

            v.setTag(each_day_flame);
        } else {
            each_day_flame = (Each_day_flame)v.getTag();
        }

        if (day != null)
            each_day_flame.each_day.setText(day);

        return v;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public String getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Each_day_flame {
        public LinearLayout dBackground;
        public TextView each_day;
    }
}
