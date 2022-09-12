package com.example.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;


public class input_Activity extends AppCompatActivity {



    private EditText input_date, input_todo;
    private EditText timeSet;
    private Switch toggle_switch;
    private Button btn_clear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_list);
        timeSet = (EditText) findViewById(R.id.input_time);
        toggle_switch = (Switch) findViewById(R.id.toggle_switch);
        btn_clear = (Button) findViewById(R.id.btn_clear);

        timeSet.setVisibility(View.INVISIBLE); // 시간지정 칸 기본 투명 설정

        toggle_switch.setOnCheckedChangeListener(new visibleTimeSwitch()); // 아래 vislbleTimeSwitch 참고
        btn_clear.setOnClickListener(new View.OnClickListener() { // X모양 버튼 눌렀을 때 이전 액티비티로 돌아감
            @Override
            public void onClick(View v) {
                input_Activity.super.onBackPressed();
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
}

