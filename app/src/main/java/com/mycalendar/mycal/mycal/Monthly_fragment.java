package com.mycalendar.mycal.mycal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class Monthly_fragment extends Fragment implements View.OnClickListener {

    private TextView title;
    private GridView gCalendar;
    private ArrayList<String> days;
    private CalendarAdapter calendarAdapter;

    Calendar currentCalendar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);

        days = new ArrayList<String>();

        ImageButton prevButton = (ImageButton)view.findViewById(R.id.prev_btn);
        ImageButton nextButton = (ImageButton)view.findViewById(R.id.next_btn);

        title = (TextView)view.findViewById(R.id.month_text);
        gCalendar = (GridView)view.findViewById(R.id.calendar);

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        currentCalendar = Calendar.getInstance();
        setCalendar(currentCalendar);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.prev_btn:
                currentCalendar.add(Calendar.MONTH, -1);
                setCalendar(currentCalendar);
                break;
            case R.id.next_btn:
                currentCalendar.add(Calendar.MONTH, 1);
                setCalendar(currentCalendar);
                break;
        }
    }

    private void setCalendar(Calendar calendar) {
        int prevMonth;
        int nowDate;
        int endDate;

        int month = (calendar.get(Calendar.MONTH)) + 1;
        String tmonth;
        if (month < 10) tmonth = "0" + month;
        else tmonth = Integer.toString(month);


        title.setText(calendar.get(Calendar.YEAR) + ". " + tmonth);

        days.clear();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        nowDate = calendar.get(Calendar.DAY_OF_WEEK);

        if (nowDate == 1) nowDate = 8;

        endDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        prevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1);
        prevMonth -= nowDate - 2;

        for (int i=0; i<nowDate-1; i++) {
            int dt = prevMonth + i;
            days.add(dt+"");
        }
        for (int i=1; i <= endDate; i++)
            days.add(i+"");

        int nextMonth = 42 - endDate - nowDate + 2;

        for (int i=1; i<nextMonth; i++)
            days.add(i+"");

        calendarAdapter = new CalendarAdapter(getActivity(), R.layout.each_day, days);
        gCalendar.setAdapter(calendarAdapter);
    }
}
