package com.mycalendar.mycal.mycal;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class Daily_fragment extends Fragment implements View.OnClickListener {

    private TextView title;
    private DbOpenHelper dbOpenHelper;
    private ListView day_list;

    private Calendar currentCalendar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        ImageButton prevButton = (ImageButton)view.findViewById(R.id.prev_day_btn);
        ImageButton nextButton = (ImageButton)view.findViewById(R.id.next_day_btn);

        title = (TextView)view.findViewById(R.id.day_text);
        day_list = (ListView)view.findViewById(R.id.day_list);

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        currentCalendar = Calendar.getInstance();
        setDay();

        return view;
    }

    @Override
    public void onDestroy() {
        dbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_day_btn:
                currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
                setDay();
                break;
            case R.id.next_day_btn:
                currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
                setDay();
                break;
        }
    }

    private void setDay() {
        int year = currentCalendar.get(Calendar.YEAR);
        int month = (currentCalendar.get(Calendar.MONTH)) + 1;
        int day = currentCalendar.get(Calendar.DAY_OF_MONTH);

        dbOpenHelper = new DbOpenHelper(getActivity());
        dbOpenHelper.open();

        title.setText(year + "년 " + month + "월 " + day + "일");

        ArrayList<String> schedule = new ArrayList<String>();

        Cursor cursor = dbOpenHelper.selectContent(year, month, day);
        while (cursor.moveToNext()) {
            schedule.add(cursor.getString(cursor.getColumnIndex("schedule")));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, schedule);
        day_list.setAdapter(adapter);

    }
}
