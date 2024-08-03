package com.example.somcoco.etc;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somcoco.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

public class Calendar extends AppCompatActivity {

    final String tableName = "checklist";
    SQLiteDatabase database;
    String curDate;
    Dialog dialog;
    CalendarDay selectDate;
    Date date;
    RecyclerView recyclerView;
    CheckListAdapter adapter;
    long mNow = System.currentTimeMillis();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    FloatingActionButton floatingActionButton;
    MaterialCalendarView materialCalendarView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.checkadd_dialog);

        date = new Date(mNow);
        curDate = simpleDateFormat.format(date);
        back = (ImageView) findViewById(R.id.calendar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView textView = (TextView) findViewById(R.id.date);
        recyclerView = (RecyclerView) findViewById(R.id.checklist_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckListAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.check_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());

        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator());
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectDate = date;
                curDate = String.format("%d-%d-%d", date.getYear(), date.getMonth() + 1, date.getDay());
                textView.setText(String.format("%d년 %d월 %d일", date.getYear(), date.getMonth() + 1, date.getDay()));
                selectData(tableName, curDate);
            }
        });

        DBHelper helper = new DBHelper(this, tableName, null, 1);
        database = helper.getWritableDatabase();
    }

    class SaturdayDecorator implements DayViewDecorator {

        private final java.util.Calendar calendar = java.util.Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(java.util.Calendar.DAY_OF_WEEK);
            return weekDay == java.util.Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    class SundayDecorator implements DayViewDecorator {

        private final java.util.Calendar calendar = java.util.Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(java.util.Calendar.DAY_OF_WEEK);
            return weekDay == java.util.Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));

        }
    }

    public void showDialog() {
        dialog.show();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView selDate = dialog.findViewById(R.id.select_date);
        EditText checkContent = dialog.findViewById(R.id.check_content);
        Button checkSubmit = dialog.findViewById(R.id.check_submit);

        if (curDate != null) {
            selDate.setText(curDate);
        } else {
            selDate.setText(simpleDateFormat.format(date));
        }

        checkSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = checkContent.getText().toString();

                if (content.equals("") || content == null) {
                    Toast.makeText(getApplicationContext(), "일정 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    insertData(content, curDate);
                    dialog.dismiss();
                    checkContent.setText(null);
                    selectData(tableName, curDate);
                    Toast.makeText(getApplicationContext(), "일정을 등록하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void selectData(String tableName, String curDate) {
        if (database != null) {
            adapter.items.clear();

            String sql = "select _id, content, date from " + tableName + " where date = '" + curDate + "' ORDER BY _id ASC";
            Cursor cursor = database.rawQuery(sql, null);

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                int id = cursor.getInt(0);
                String content = cursor.getString(1);
                String date = cursor.getString(2);

                adapter.addItem(new CheckListItem(id, content, date));
            }

            if (cursor.getCount() > 0) {
                materialCalendarView.addDecorator(new EventDecorator(Color.RED, Collections.singleton(selectDate)));
            }

            cursor.close();

            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

    public void insertData(String content, String date) {
        if (database != null) {
            String sql = "insert into checklist(content,date) values(?,?)";
            Object[] params = {content, date};

            database.execSQL(sql, params);
        }
    }

    public void updateData(int id, String content) {
        if (database != null) {
            String sql = "update " + tableName + " set content = " + content + " where _id = " + id;

            database.execSQL(sql);
            adapter.notifyDataSetChanged();
        }
    }

    public void deleteData(int id) {
        if (database != null) {
            String sql = "delete from " + tableName + " where _id = " + id;

            database.execSQL(sql);
            adapter.notifyDataSetChanged();
        }
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, content text, date text)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
