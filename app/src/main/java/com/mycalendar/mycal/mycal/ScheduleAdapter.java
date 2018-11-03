package com.mycalendar.mycal.mycal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleAdapter extends BaseAdapter {
    private ArrayList<ScheduleItem> scheduleItems;
    private Context contexts;
    private int laySource;
    private LayoutInflater layoutInflater;

    public ScheduleAdapter(Context context, int resource, ArrayList<ScheduleItem> schedule) {
        this.contexts = context;
        this.laySource = resource;
        this.scheduleItems = schedule;
        this.layoutInflater = (LayoutInflater)contexts.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View v = convertview;

        ScheduleItem scheduleItem = scheduleItems.get(position);
        TextView each_schedule;

        if (v == null) {
            v = layoutInflater.inflate(laySource, null);
            each_schedule = (TextView)v.findViewById(R.id.list_schedule);

            v.setTag(each_schedule);
        } else {
            each_schedule = (TextView)v.getTag();
        }

        if (scheduleItem != null)
            each_schedule.setText(scheduleItem.getSchedule());

        return v;
    }

    @Override
    public int getCount() {
        return scheduleItems.size();
    }

    @Override
    public ScheduleItem getItem(int position) {
        return scheduleItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
