package edu.bd.ewu.eventapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

public class EventInfo extends AppCompatActivity {

    EditText name, place, datetime, capacity, budget, email, phone, description;
    RadioGroup radio_grp;
    RadioButton indoor, outdoor, online;
    TextView cancel, share, save, error;
    String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        error = findViewById(R.id.error_msg);
        error.setText("");
        name = findViewById(R.id.name);
        place = findViewById(R.id.place);
        datetime = findViewById(R.id.datetime);
        capacity = findViewById(R.id.capacity);
        budget = findViewById(R.id.budget);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.description);
        radio_grp = findViewById(R.id.radio_grp);
        cancel = findViewById(R.id.cancel_button);
        share = findViewById(R.id.share_button);
        save = findViewById(R.id.save_button);

        indoor= findViewById(R.id.indoor);
        outdoor= findViewById(R.id.outdoor);
        online= findViewById(R.id.online);

        KeyValueDB db = new KeyValueDB(this);
        Intent i = getIntent();
        key = i.getStringExtra("key");
        if(key != null){
            String values = db.getValueByKey(key);
            String value[] = values.split("##");

            name.setText(value[0]);
            place.setText(value[1]);
            if(value[2].contains("Indoor")) indoor.setChecked(true);
            else if(value[2].contains("Outdoor")) outdoor.setChecked(true);
            else online.setChecked(true);

            datetime.setText(value[3]);
            capacity.setText(value[4]);
            budget.setText(value[5]);
            email.setText(value[6]);
            phone.setText(value[7]);
            description.setText(value[8]);
        }

        save.setOnClickListener(v -> {
            String errorMsg = "";
            if(name.getText().toString().length() < 5){
                errorMsg += "Name is not Valid\n";
            }
            if(place.getText().toString().length() < 6){
                errorMsg += "Place is not Valid\n";
            }
            if(radio_grp.getCheckedRadioButtonId() == -1){
                errorMsg += "Select Type\n";
            }
            if(datetime.getText().length() == 0){
                errorMsg += "Date filed is empty\n";
            }
            if(budget.getText().length()  == 0){
                errorMsg += "Budget field is Empty\n";
            }
            else if(Integer.parseInt(budget.getText().toString()) < 10000){
                errorMsg += "Budget must be greater then 10000\n";
            }
            if(capacity.getText().length()  == 0){
                errorMsg += "Capacity field is Empty\n";
            }
            else if(Integer.parseInt(capacity.getText().toString()) < 10){
                errorMsg += "Capacity must be greater then 10\n";
            }
            if(!email.getText().toString().contains("@") || !email.getText().toString().contains(".")){
                errorMsg += "Email is not valid\n";
            }
            if(phone.getText().length() < 11){
                errorMsg += "Phone Number is not valid\n";
            }

            if(errorMsg.length() == 0){

                showDialogue("Do you want to save the event info?\n", "Info", "Yes", "No");
            }
            else{
                showDialogue(errorMsg, "Error!", "OK", "Back");
            }
        });

        cancel.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
    }

    private void showDialogue(String msg, String title, String btn1, String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, (dialog, which) -> {
                    if(btn1.equals("Yes")){
                        Calendar calendar = Calendar.getInstance();
                        if(key == null)
                        {
                            key = name.getText()+"##"+calendar.getTimeInMillis();
                        }

                        int selectedId = radio_grp.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        String radio = radioButton.getText().toString();

                        String value = name.getText()+"##"+place.getText()+"##"+radio+"##"+datetime.getText()+"##"+
                                capacity.getText()+"##"+budget.getText()+"##"+email.getText()+"##"+phone.getText()+"##"+
                                description.getText();

                        KeyValueDB db = new KeyValueDB(getApplicationContext());
                        db.updateValueByKey(key, value);
                        db.close();

                        String keys[] = {"action", "id", "semester", "key", "event"};
                        String values[] = {"backup", "2019-1-60-197", "2022-2", key, value};

                        HttpReq httpReq = new HttpReq(this);
                        httpReq.httpRequest(keys, values);

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(btn2, (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}