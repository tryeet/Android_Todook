package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView listView;
    private View view;
    String year, month, day;
    TextView textView;
    Button btn_previous, btn_next;

    private CheckBox list_checkbox;
    private TextView list_textview;


    // 메인화면 날짜 지정 관련 소스
    Calendar calendar = new GregorianCalendar();

    public void dateSet() {
        year = calendar.get(Calendar.YEAR) + "년";
        month = (calendar.get(Calendar.MONTH)+1) + "월";
        day = calendar.get(Calendar.DAY_OF_MONTH) + "일";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView)findViewById(R.id.date);

        dateSet();// 텍스트뷰에 정의해놓은 현재 날짜 뿌리기
        textView.setText(year + month + day);

        textView.setOnClickListener(new View.OnClickListener() { // 날짜 클릭했을때 실행되는 이벤트
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "날짜를 조회합니다.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button btn_previous = (Button)findViewById(R.id.btn_previous); // 이전 날짜 버튼
        Button btn_next = (Button)findViewById(R.id.btn_next); // 다음 날짜 버튼

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1); // 날짜 -1
                dateSet();
                textView.setText(year + month + day);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1); // 날짜 +1
                dateSet();
                textView.setText(year + month + day);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        List<String> data = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item, data);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        data.add("TestText");

        adapter.notifyDataSetChanged();

        //길게 눌렀을 때 수정, 복사, 삭제가 나타나는 팝업 메뉴가 나타남
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_change:
                                Snackbar.make(view, "일정을 수정합니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                adapter.notifyDataSetChanged();
                                break;
                            case R.id.action_modify:
                                Snackbar.make(view, "일정을 복제했습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                data.add("복제된 일정");
                                adapter.notifyDataSetChanged();
                                break;
                            case R.id.action_delete:
                                Snackbar.make(view, "일정을 지웠습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                data.remove(position);
                                adapter.notifyDataSetChanged();
                                break;
                            default:
                                adapter.notifyDataSetChanged();

                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });

        //짧게 눌렀을 때 상호작용할 것
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "체크(해제)했습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });


        // FAB 온클릭리스너
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, input_Activity.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //하단 네비게이션 바 관련 소스
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home: //리스트 버튼
//                        Intent intent1 = new Intent(SubActivity.class, MainActivity.class);
//                        startActivity(intent1);
                        break;
                    case R.id.action_settings: //설정 버튼
                        Intent intent2 = new Intent(MainActivity.this, SubActivity.class);
                        intent2.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });

    }

}