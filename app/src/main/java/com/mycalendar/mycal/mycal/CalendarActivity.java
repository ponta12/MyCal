package com.mycalendar.mycal.mycal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MONTHLY = 19940208;
    private final int WEEKLY = 19940209;
    private final int DAILY = 19940210;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    int mode;

    private TextView btn_monthly, btn_weekly, btn_daily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btn_monthly = (TextView)findViewById(R.id.monthly_btn);
        btn_weekly = (TextView)findViewById(R.id.weekly_btn);
        btn_daily = (TextView)findViewById(R.id.daily_btn);

        //스케줄 추가 버튼
        FloatingActionButton addSchedule = (FloatingActionButton)findViewById(R.id.add_schedule);

        //마지막에 사용한 탭 저장
        setting = getSharedPreferences("setting", MODE_PRIVATE);
        editor = setting.edit();
        mode = setting.getInt("mode", MONTHLY);

        btn_monthly.setOnClickListener(this);
        btn_weekly.setOnClickListener(this);
        btn_daily.setOnClickListener(this);
        addSchedule.setOnClickListener(this);

        callFragment(mode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monthly_btn:
                btn_weekly.setBackgroundResource(R.drawable.click_background);
                btn_daily.setBackgroundResource(R.drawable.click_background);
                callFragment(MONTHLY);
                editor.putInt("mode", MONTHLY);
                editor.commit();
                mode = MONTHLY;
                break;
            case R.id.weekly_btn:
                btn_monthly.setBackgroundResource(R.drawable.click_background);
                btn_daily.setBackgroundResource(R.drawable.click_background);
                callFragment(WEEKLY);
                editor.putInt("mode", WEEKLY);
                editor.commit();
                mode = WEEKLY;
                break;
            case R.id.daily_btn:
                btn_weekly.setBackgroundResource(R.drawable.click_background);
                btn_monthly.setBackgroundResource(R.drawable.click_background);
                callFragment(DAILY);
                editor.putInt("mode", DAILY);
                editor.commit();
                mode = DAILY;
                break;
            case R.id.add_schedule:
                Intent intent = new Intent(CalendarActivity.this, AddScheduleActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void callFragment(int frag_no) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frag_no) {
            case MONTHLY:
                btn_monthly.setBackgroundResource(R.color.pink);
                transaction.replace(R.id.fragment_container, new Monthly_fragment());
                transaction.commit();
                break;
            case WEEKLY:
                btn_weekly.setBackgroundResource(R.color.pink);
                transaction.replace(R.id.fragment_container, new Weekly_fragment());
                transaction.commit();
                break;
            case DAILY:
                btn_daily.setBackgroundResource(R.color.pink);
                transaction.replace(R.id.fragment_container, new Daily_fragment());
                transaction.commit();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callFragment(mode);
    }
}