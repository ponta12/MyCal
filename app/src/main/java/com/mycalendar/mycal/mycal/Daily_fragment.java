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
        day_list.setAdapter(adapter);

        //리스트 뷰의 아이템 클릭 리스너 - 클릭 시 삭제할 수 있도록 설정
        day_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                setDay();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
