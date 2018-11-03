package com.mycalendar.mycal.mycal;

import android.database.Cursor;
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
    private ArrayList<DayItem> days;
    private CalendarAdapter calendarAdapter;
    private DbOpenHelper dbOpenHelper;

    private Calendar currentCalendar;
    private int nowDay, nowMonth, nowYear;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);

        days = new ArrayList<DayItem>();

        ImageButton prevButton = (ImageButton)view.findViewById(R.id.prev_month_btn);
        ImageButton nextButton = (ImageButton)view.findViewById(R.id.next_month_btn);

        title = (TextView)view.findViewById(R.id.month_text);
        gCalendar = (GridView)view.findViewById(R.id.calendar);

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        currentCalendar = Calendar.getInstance();
        nowDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        nowMonth = (currentCalendar.get(Calendar.MONTH)) + 1;
        nowYear = currentCalendar.get(Calendar.YEAR);
        setCalendar(currentCalendar);

        return view;
    }

    @Override
    public void onDestroy() {
        dbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.prev_month_btn:
                currentCalendar.add(Calendar.MONTH, -1);
                setCalendar(currentCalendar);
                break;
            case R.id.next_month_btn:
                currentCalendar.add(Calendar.MONTH, 1);
                setCalendar(currentCalendar);
                break;
        }
    }

    private void setCalendar(Calendar calendar) {
        //변수 초기화
        int prevMonth;
        int nowDate;
        int endDate;
        int year = calendar.get(Calendar.YEAR);
        int month = (calendar.get(Calendar.MONTH)) + 1;
        String tmonth;
        if (month < 10) tmonth = "0" + month;
        else tmonth = Integer.toString(month);
        ArrayList<Integer> schedule_day = new ArrayList<Integer>();

        //DB에서 스케줄이 있는 날짜를 가져옴
        dbOpenHelper = new DbOpenHelper(getActivity());
        dbOpenHelper.open();

        Cursor cursor = dbOpenHelper.selectMonthSchedule(year, month);
        while(cursor.moveToNext()) {
            int tempday = cursor.getInt(cursor.getColumnIndex("day"));
            schedule_day.add(tempday);
        }

        //제목 설정(년, 월)
        title.setText(year + ". " + tmonth);

        //days ArrayList 초기화 및 날짜 변수 설정
        days.clear();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        nowDate = calendar.get(Calendar.DAY_OF_WEEK);
        if (nowDate == 1) nowDate = 8;
        endDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, -1);
        prevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1);

        //이전 달 날짜 수 = 요일번호(2~8) - 1
        //이전 달 시작 날짜 = 이전 달 마지막 날짜 - 이전 달 날짜 수 + 1
        prevMonth -= nowDate - 2;

        //날짜 ArrayList 추가
        for (int i=0; i<nowDate-1; i++) {
            DayItem dayItem = new DayItem();
            int dt = prevMonth + i;
            dayItem.setDay(dt+"");
            dayItem.setInMonth(false);
            dayItem.setSchedule(false);
            dayItem.setToday(false);
            days.add(dayItem);
        }
        for (int i=1; i <= endDate; i++) {
            DayItem dayItem = new DayItem();
            dayItem.setDay(i+"");
            dayItem.setInMonth(true);
            if (schedule_day.contains(i)) dayItem.setSchedule(true);
            else dayItem.setSchedule(false);
            if (year == nowYear && month == nowMonth && i == nowDay) {
                dayItem.setToday(true);
            }
            else dayItem.setToday(false);
            days.add(dayItem);
        }

        // 6 * 7 = 42 - 현재 한 달 날짜 - 요일 번호(2~8) + 2 = 다음달의 마지막 날짜 + 1
        int nextMonth = 42 - endDate - nowDate + 2;

        for (int i=1; i<nextMonth; i++) {
            DayItem dayItem = new DayItem();
            dayItem.setDay(i+"");
            dayItem.setInMonth(false);
            dayItem.setSchedule(false);
            days.add(dayItem);
        }

        //Adapter 설정
        calendarAdapter = new CalendarAdapter(getActivity(), R.layout.each_day, days);
        gCalendar.setAdapter(calendarAdapter);
    }
}
