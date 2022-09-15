package edu.bd.ewu.eventapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class EventHistory extends AppCompatActivity {

    ListView events_history;
    ArrayList<EventData> events = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_history);

        events_history = findViewById(R.id.events_history_list);

        CustomEventAdapter adapter = new CustomEventAdapter(this, events);
        events_history.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        String keys[] = {"action", "id", "semester"};
        String values[] = {"restore", "2019-1-60-197", "2022-2"};

        HttpReq httpReq = new HttpReq(this);
        httpReq.httpRequest(keys, values);

        KeyValueDB db = new KeyValueDB(this);
        Cursor c = db.getAllKeyValues();
        String key="",va ="";

        while(c.moveToNext()){
            key = c.getString(0);
            va = c.getString(1);

            if(!va.equals("")){
                String[] value = va.split("##");
                System.out.println("VALUE:"+ Arrays.toString(value));
                EventData e = new EventData(key, value[0], value[1],value[2],value[3],value[4],value[5],value[6],value[7],value[8]);
                events.add(e);
            }
        }
    }
}