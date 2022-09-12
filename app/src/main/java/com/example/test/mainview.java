package com.example.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class mainview extends Fragment {

    private View view;
    String year, month, day;
    TextView textView;
    Button btn_previous, btn_next;


    // 메인화면 날짜 지정 관련 소스
    Calendar calendar = new GregorianCalendar();

    public void dateSet() {
        year = calendar.get(Calendar.YEAR) + "년";
        month = (calendar.get(Calendar.MONTH)+1) + "월";
        day = calendar.get(Calendar.DAY_OF_MONTH) + "일";
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mainview, container, false); // Frag 레이아웃에서는 findViewById 단독 사용 X, v라는 변수로 뷰 지정

        dateSet();

        TextView textView = (TextView)v.findViewById(R.id.date);
        textView.setText(year + month + day); // 텍스트뷰에 정의해놓은 현재 날짜 뿌리기
        textView.setOnClickListener(new View.OnClickListener() { // 날짜 클릭했을때 실행되는 이벤트
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "날짜를 조회합니다.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btn_previous = (Button)v.findViewById(R.id.btn_previous);
        Button btn_next = (Button)v.findViewById(R.id.btn_next);

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                dateSet();
                textView.setText(year + month + day);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                dateSet();
                textView.setText(year + month + day);
            }
        });




        return v;
    };
}
