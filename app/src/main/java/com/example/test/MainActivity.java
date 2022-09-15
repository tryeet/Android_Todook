package com.example.test;

import static android.content.ContentValues.TAG;

import static java.lang.System.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView listView;
    private View view;
    String year, month, day, dayOutput;
    TextView textView, time, date;
    static int holdPosition;
//    CustomChoiceListViewAdapter adapter; // 추가한 코드

    ArrayAdapter<String> adapter;
    ArrayList<String> array = new ArrayList<>();

    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";
    // 메인화면 날짜 지정 관련 소스
    Calendar calendar = new GregorianCalendar();

    public void getDate() {
        year = calendar.get(Calendar.YEAR) + "년";
        month = (calendar.get(Calendar.MONTH)+1) + "월";
        day = calendar.get(Calendar.DAY_OF_MONTH) + "일";
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOutput = "요일";
        switch (dayOfWeek) {
            case 0:
                dayOutput = "일";
                break;
            case 1:
                dayOutput = "월";
                break;
            case 2:
                dayOutput = "화";
                break;
            case 3:
                dayOutput = "수";
                break;
            case 4:
                dayOutput = "목";
                break;
            case 5:
                dayOutput = "금";
                break;
            case 6:
                dayOutput = "토";
                break;
        }
        TextView textView = (TextView)findViewById(R.id.date);
        textView.setText(year + month + day + "(" + dayOutput + ")");
    }
    public void setDate(int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            setDate(year, month, dayOfMonth);
            getDate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 임시로 받아오는 코드
        time = (TextView)findViewById(R.id.tititi);
        date = (TextView)findViewById(R.id.dadada);

        TextView textView = (TextView)findViewById(R.id.date); // 날짜 표시되는 뷰
        Button btn_previous = (Button)findViewById(R.id.btn_previous); // 이전 날짜 버튼
        Button btn_next = (Button)findViewById(R.id.btn_next); // 다음 날짜 버튼
        listView = (ListView) findViewById(R.id.listView);

//        adapter = new CustomChoiceListViewAdapter(); // 추가한 코드
//        listView.setAdapter(adapter); // 추가한 코드
//        adapter.addItem("20220715", "study"); // 추가한코드

        FloatingActionButton fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavi);


        array = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);

        getDate(); // 현재 정의된 날짜 뿌리기

        textView.setOnClickListener(new View.OnClickListener() { // 날짜 텍스트뷰 클릭 시 팝업형태의 캘린더 출력하여 날짜 선택
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, myDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() { // 이전 버튼 리스너
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1); // 날짜 -1
                getDate();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() { // 다음 버튼 리스너
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1); // 날짜 +1
                getDate();
            }
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item, array);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        Intent input_intent = getIntent();
        String setTodo = input_intent.getStringExtra("todo");
        String setTime = input_intent.getStringExtra("time");
        String setDate = input_intent.getStringExtra("date");
        int modify = input_intent.getIntExtra("modify", 0);

        if (setTodo != null) {
            if (modify == 1) {
                array.remove(holdPosition);
            }
            array.add(setTodo);
            time.setText(setTime);
            date.setText(setDate);
        }
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
                                Intent modify_intent = new Intent(MainActivity.this, input_Activity.class);
                                String todoGet = (String) parent.getAdapter().getItem(position);
                                modify_intent.putExtra("itemTodo", todoGet);
                                holdPosition = position;
                                startActivity(modify_intent);
                                break;
//                            case R.id.action_modify:
//                                Snackbar.make(view, "일정을 복제했습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
//                                array.add("복제된 일정");
//                                array.add(position);
//                                adapter.notifyDataSetChanged();
//                                break;
                            case R.id.action_delete:
                                Snackbar.make(view, "일정을 삭제했습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                array.remove(position);
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
                Snackbar.make(view, position + "번째 포지션", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        // FAB 온클릭리스너
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, input_Activity.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //하단 네비게이션 바 관련 소스
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
    // 앱 종료 또는 다른 액티비티 이동 시에 리스트뷰 추가된 아이템들 editor에 저장, a에는 리스트 개수 저장
    private void setStringArrayPref(Context context, String key, ArrayList<String> value) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < value.size(); i++) {
            a.put(value.get(i));
        }
        if (!value.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    // 저장한 배열을 JSONArray를 통해 불러오기 함
    private ArrayList getStringArrayPref(Context context, String key) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();

        if (json != null) {
            try{
                JSONArray a = new JSONArray(json);

                for (int i = 0; i<a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    @Override
    protected void onPause() {
        super.onPause();

        setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, array);
        Log.d(TAG, "Put json");
    }

}