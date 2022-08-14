package edu.bd.ewu.lab08_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView create_new, history, exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create_new = findViewById(R.id.create_new_btn);
        history = findViewById(R.id.history_btn);
        exit = findViewById(R.id.exit_btn);

        create_new.setOnClickListener(v->{
            startActivity(new Intent(this, EventInfo.class));
        });

        exit.setOnClickListener(v->{
            finish();
        });
    }
}