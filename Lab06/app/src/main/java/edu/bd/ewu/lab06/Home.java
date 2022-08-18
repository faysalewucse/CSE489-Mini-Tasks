package edu.bd.ewu.lab06;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    TextView welcome;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent i = getIntent();

        welcome = findViewById(R.id.welcome);
        logout = findViewById(R.id.logout_btn);

        welcome.setText(i.getStringExtra("name"));
        logout.setOnClickListener(v->{
            SharedPreferences pref= getSharedPreferences("AuthInfo", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = pref.edit();

            prefEditor.putBoolean("remember_pass", false);
            prefEditor.putBoolean("remember_user_id", false);

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
    }
}