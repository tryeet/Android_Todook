package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class SubActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TextView setting_notice, setting_version, setting_developer, setting_qna, setting_announcement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        setting_announcement = (TextView)findViewById(R.id.setting_announcement);
        setting_notice = (TextView)findViewById(R.id.setting_notice);
        setting_version = (TextView)findViewById(R.id.setting_version);
        setting_developer = (TextView)findViewById(R.id.setting_developer);
        setting_qna = (TextView)findViewById(R.id.setting_qna);

        //공지사항
        setting_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SubActivity.this, webViewer.class);
                intent1.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent1);
            }
        });
        //알림
        setting_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                startActivityForResult(intent, 0);
            }
        });
        //버전
        setting_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), versionactivity.class);
                startActivity(intent);
            }
        });
        //개발자
        setting_developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DevActivity.class);
                startActivity(intent);
            }
        });
        //문의하기
        TextView email = (TextView) findViewById(R.id.setting_qna);
        setting_qna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                //이메일 보내는 대상
                String[] address = {"email@address.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                //이메일 제목
                email.putExtra(Intent.EXTRA_SUBJECT, "문의제목");
                //이메일 내용
                email.putExtra(Intent.EXTRA_TEXT, "문의내용들");
                startActivity(email);
            }
        });




        //하단 네비게이션 바 관련 소스
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home: //리스트 버튼
                        Intent intent1 = new Intent(SubActivity.this, MainActivity.class);
                        intent1.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        break;
                    case R.id.action_settings: //설정 버튼
//                        Intent intent2 = new Intent(MainActivity.this, SubActivity.class);
//                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });

    }
}
