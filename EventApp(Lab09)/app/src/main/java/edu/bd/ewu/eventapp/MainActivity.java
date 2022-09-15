package edu.bd.ewu.eventapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        history.setOnClickListener(v->{
            startActivity(new Intent(this, EventHistory.class));
        });
        exit.setOnClickListener(v->{
            finish();
            System.exit(0);
        });
    }

    @SuppressLint("CommitPrefEdits")
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

        SharedPreferences pref = getSharedPreferences("DELETEDKEYS", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        String deleted_keys = pref.getString("KEYS", "");
        System.out.println("DELETEDKEYS: "+deleted_keys);

        while(c.moveToNext()){
            key = c.getString(0);
            va = c.getString(1);

            if(!va.equals("")){
                if(deleted_keys.length() != 0){
                    String[] splitted_keys = deleted_keys.split("@@");
                    String noSpaceStr = key.replaceAll("\\s", "");
                    if(!isKeyContains(splitted_keys, noSpaceStr)){
                        String[] value = va.split("##");
                        System.out.println("VALUE:"+ Arrays.toString(value));
                        EventData e = new EventData(key, value[0], value[1],value[2],value[3],value[4],value[5],value[6],value[7],value[8]);
                        events.add(e);
                    }
                }
                else{
                    String[] value = va.split("##");
                    System.out.println("VALUE:"+ Arrays.toString(value));
                    EventData e = new EventData(key, value[0], value[1],value[2],value[3],value[4],value[5],value[6],value[7],value[8]);
                    events.add(e);
                }
            }
        }
    }
    public boolean isKeyContains(String[] deletedKeys, String key){
        for (String splitted_key : deletedKeys) {
            if(splitted_key.equals(key)){
                return true;
            }
        }
        return false;
    }
}