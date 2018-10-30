package com.mycalendar.mycal.mycal;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MONTHLY = 19940208;
    private final int WEEKLY = 19940209;
    private final int DAILY = 19940210;

    private TextView btn_monthly, btn_weekly, btn_daily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btn_monthly = (TextView)findViewById(R.id.monthly_btn);
        btn_weekly = (TextView)findViewById(R.id.weekly_btn);
        btn_daily = (TextView)findViewById(R.id.daily_btn);

        btn_monthly.setOnClickListener(this);
        btn_weekly.setOnClickListener(this);
        btn_daily.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monthly_btn:
                callFragment(MONTHLY);
                break;
            case R.id.weekly_btn:
                callFragment(WEEKLY);
                break;
            case R.id.daily_btn:
                callFragment(DAILY);
                break;
        }
    }

    private void callFragment(int frag_no) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frag_no) {
            case MONTHLY:
                transaction.replace(R.id.fragment_container, new Monthly_fragment());
                transaction.commit();
                break;
            case WEEKLY:
                transaction.replace(R.id.fragment_container, new Weekly_fragment());
                transaction.commit();
                break;
            case DAILY:
                transaction.replace(R.id.fragment_container, new Daily_fragment());
                transaction.commit();
                break;
        }
    }
}