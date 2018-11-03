package com.mycalendar.mycal.mycal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    TextView sYear, sMonth, sDay;
    int year, month, day;
    EditText sContent;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addschedule);

        sYear = (TextView)findViewById(R.id.schedule_year);
        sMonth = (TextView)findViewById(R.id.schedule_month);
        sDay = (TextView)findViewById(R.id.schedule_day);
        sContent = (EditText)findViewById(R.id.schedule_content);

        Button btn_change = (Button)findViewById(R.id.change_schedule_date);
        Button btn_register = (Button)findViewById(R.id.register_schedule);
        Button btn_cancel = (Button)findViewById(R.id.cancel_schedule);

        btn_change.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //현재 날짜로 초기화
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = ((calendar.get(Calendar.MONTH)) + 1);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        sYear.setText(year + "년");
        sMonth.setText(month + "월");
        sDay.setText(day + "일");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_schedule_date:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, year, month-1, day);
                datePickerDialog.show();
                break;
            case R.id.register_schedule:
                regiSchedule();
                break;
            case R.id.cancel_schedule:
                finish();
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            //변경 날짜로 저장
            year = i;
            month = i1 + 1;
            day = i2;
            sYear.setText(year + "년");
            sMonth.setText(month + "월");
            sDay.setText(day + "일");
        }
    };

    private void regiSchedule() {
        //스케줄 내용 변수
        String content = sContent.getText().toString();

        //스케줄의 내용이 없으면 다시 확인
        if (TextUtils.isEmpty(content)) {
            sContent.setError("내용을 입력하세요.");
            sContent.requestFocus();
            return;
        }

        //스케줄을 DB에 저장
        DbOpenHelper dbOpenHelper = new DbOpenHelper(this);
        dbOpenHelper.open();
        dbOpenHelper.insertColumn(year, month, day, content);
        dbOpenHelper.close();

        //확인 창 표시
        AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this);
        builder.setMessage("스케줄이 등록되었습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create()
                .show();
    }
}
