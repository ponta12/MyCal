package com.mycalendar.mycal.mycal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class Weekly_fragment extends Fragment implements View.OnClickListener {

    private TextView title;

    private DbOpenHelper dbOpenHelper;
    //일주일에 해당하는 7일 분량의 id와 TextView, ListView 초기화
    private int[] week_date = {R.id.week_date1, R.id.week_date2, R.id.week_date3, R.id.week_date4,
            R.id.week_date5, R.id.week_date6, R.id.week_date7};
    private int[] week_day = {R.id.week_day1, R.id.week_day2, R.id.week_day3, R.id.week_day4,
            R.id.week_day5, R.id.week_day6, R.id.week_day7};
    private int[] week_list = {R.id.week_list1, R.id.week_list2, R.id.week_list3, R.id.week_list4,
            R.id.week_list5, R.id.week_list6, R.id.week_list7};
    private TextView[] wDate = new TextView[7];
    private TextView[] wDay = new TextView[7];
    private ListView[] wList = new ListView[7];

    private Calendar currentCalendar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);

        ImageButton prevButton = (ImageButton)view.findViewById(R.id.prev_week_btn);
        ImageButton nextButton = (ImageButton)view.findViewById(R.id.next_week_btn);

        title = (TextView)view.findViewById(R.id.week_text);

        for (int i = 0; i < 7; i++) {
            wDate[i] = (TextView)view.findViewById(week_date[i]);
            wDay[i] = (TextView)view.findViewById(week_day[i]);
            wList[i] = (ListView)view.findViewById(week_list[i]);
        }

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        currentCalendar = Calendar.getInstance();
        setWeek();

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
            case R.id.prev_week_btn:
                currentCalendar.add(Calendar.DAY_OF_MONTH, -7);
                setWeek();
                break;
            case R.id.next_week_btn:
                currentCalendar.add(Calendar.DAY_OF_MONTH, 7);
                setWeek();
                break;
        }
    }

    private void setWeek() {
        Calendar calendar = (Calendar) currentCalendar.clone();
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        //한 주의 시작인 일요일이 되게 설정
        calendar.add(Calendar.DAY_OF_MONTH, -calendar.get(Calendar.DAY_OF_WEEK) + 1);

        title.setText(year + "년 " + week + "주차");

        dbOpenHelper = new DbOpenHelper(getActivity());
        dbOpenHelper.open();

        for (int i = 0; i < 7; i++) {
            year = calendar.get(Calendar.YEAR);
            int month = (calendar.get(Calendar.MONTH)) + 1;
            int weekd = calendar.get(Calendar.DAY_OF_WEEK);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //날짜 정보를 빼내었으면 다음날로 변경
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            String weekday = getWeekday(weekd);

            wDate[i].setText(year + "년 " + month + "월 " + day + "일");
            wDay[i].setText(weekday);

            //그 날의 스케줄을 확인하여 ArrayList에 저장
            ArrayList<ScheduleItem> schedule = new ArrayList<ScheduleItem>();

            Cursor cursor = dbOpenHelper.selectContent(year, month, day);
            while (cursor.moveToNext()) {
                ScheduleItem scheduleItem = new ScheduleItem();
                scheduleItem.setScheduleId(cursor.getInt(cursor.getColumnIndex("_id")));
                scheduleItem.setSchedule(cursor.getString(cursor.getColumnIndex("schedule")));
                schedule.add(scheduleItem);
            }

            //스케줄 어댑터 생성 및 set
            final ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), R.layout.each_schedule, schedule);
            wList[i].setAdapter(adapter);

            //리스트 뷰의 아이템 클릭 리스너 - 클릭 시 삭제할 수 있도록 설정
            wList[i].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final ScheduleItem scheduleItem = adapter.getItem(i);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("삭제하시겠습니까?")
                            .setNegativeButton("취소", null)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dbOpenHelper.deleteSchedule(scheduleItem.getScheduleId());
                                    setWeek();
                                }
                            })
                            .create()
                            .show();
                }
            });
        }
    }

    //String 요일을 알아내기 위한 함수
    private String getWeekday(int weekd) {
        switch (weekd) {
            case 1: return "일요일";
            case 2: return "월요일";
            case 3: return "화요일";
            case 4: return "수요일";
            case 5: return "목요일";
            case 6: return "금요일";
            case 7: return "토요일";
        }
        return "";
    }
}