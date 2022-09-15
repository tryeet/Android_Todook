package com.example.test;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class input_Activity extends AppCompatActivity {

    private EditText editTextDate, editTextTodo;
    private EditText timeSet;
    private Switch toggle_switch;
    private Button btn_clear, btn_add;
    private Boolean addState = true; // true일 때 추가하기 기능으로 사용, false 일 때 아이템 삭제 -> 추가

    Calendar calendar = new GregorianCalendar(); // 날짜 지정 시에 사용하는 캘린더

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_list);

        timeSet = (EditText) findViewById(R.id.input_time);
        toggle_switch = (Switch) findViewById(R.id.toggle_switch);

        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_add = (Button) findViewById(R.id.btn_add);

        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextTodo = (EditText)findViewById(R.id.editTextTodo);

        Intent modify_intent = getIntent();
        String loadTodo = modify_intent.getStringExtra("itemTodo");
        if (loadTodo != null) {
            editTextTodo.setText(loadTodo);
            toggle_switch.setChecked(true);
            btn_add.setText("수정하기");
            addState = false;

        } else {
            btn_add.setText("추가하기");
            timeSet.setVisibility(View.INVISIBLE); // 시간지정 칸 기본 투명 설정
        }

        // 아래 vislbleTimeSwitch 참고
        toggle_switch.setOnCheckedChangeListener(new visibleTimeSwitch());
        btn_clear.setOnClickListener(new View.OnClickListener() { // X 버튼 눌렀을 때 이전 액티비티로 돌아감
            @Override
            public void onClick(View v) {
                input_Activity.super.onBackPressed();
            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(input_Activity.this, myDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 알림 시간지정 클릭 리스너

        Calendar mCurrentTime = Calendar.getInstance();
        int C_hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int C_minute = mCurrentTime.get(Calendar.MINUTE);
        timeSet = (EditText) findViewById(R.id.input_time);
        String state = "오전";
        if (C_hour > 12) {
            C_hour -= 12;
            state = "오후";
        }
        timeSet.setText(state + " " + C_hour + "시 " + C_minute + "분");
        timeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(input_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                        String state = "오전";
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            state = "오후";
                        }
                        timeSet.setText(state + " " + hourOfDay + "시 " + minuteOfHour + "분");
                    }
                }, hour, minute, false); //true로 바꾸면 24시간제 형식 사용
                mTimePicker.setTitle("시간 설정");
                mTimePicker.show();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent input_intent = new Intent(getApplicationContext(), MainActivity.class);

                if (addState) { // 일반적인 일정추가일때
                    input_intent.putExtra("todo", editTextTodo.getText().toString());
                    input_intent.putExtra("date", editTextDate.getText().toString());
                    input_intent.putExtra("time", timeSet.getText().toString());
                } else { // 수정하기로 들어왔을때
                    input_intent.putExtra("todo", editTextTodo.getText().toString());
                    input_intent.putExtra("date", editTextDate.getText().toString());
                    input_intent.putExtra("time", timeSet.getText().toString());
                    input_intent.putExtra("modify", 1); // 변수 보내서 기존 리스트 지움
                }

                startActivity(input_intent);
                finish();
            }
        });


    }
    class visibleTimeSwitch implements CompoundButton.OnCheckedChangeListener { // 리스트 입력화면에서 알림 스위치 켰을 때 시간지정칸 보여줌
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                timeSet.setVisibility(View.VISIBLE);
            } else {
                timeSet.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void updateLabel() {
        String myFormat = "yyyy년 M월 d일 (E)";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText input_date = (EditText) findViewById(R.id.editTextDate);
        input_date.setText(sdf.format(calendar.getTime()));
    }
}

