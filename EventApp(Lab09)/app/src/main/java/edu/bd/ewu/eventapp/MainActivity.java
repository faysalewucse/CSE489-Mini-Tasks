package edu.bd.ewu.eventapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView create_new, history, exit;
    ListView event_list;
    ArrayList<EventData> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        event_list = findViewById(R.id.event_list);
        create_new = findViewById(R.id.create_new_btn);
        history = findViewById(R.id.history_btn);
        exit = findViewById(R.id.exit_btn);

        CustomEventAdapter adapter = new CustomEventAdapter(this, events);
        event_list.setAdapter(adapter);

        create_new.setOnClickListener(v->{
            startActivity(new Intent(this, EventInfo.class));
        });

        exit.setOnClickListener(v->{
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        KeyValueDB db = new KeyValueDB(this);
        Cursor c = db.getAllKeyValues();
        String key="",va ="";
        while(c.moveToNext()){
            key = c.getString(0);
            va = c.getString(1);

            String value[] = va.split("##");
            EventData e = new EventData(key, value[0], value[1],value[2],value[3],value[4],value[5],value[6],value[7],value[8]);
            events.add(e);
        }
        String keys[] = {"action", "id", "semester"};
        String values[] = {"restore", "2019-1-60-197", "2022-2"};

        HttpReq httpReq = new HttpReq();
        httpReq.httpRequest(keys, values);
    }
}