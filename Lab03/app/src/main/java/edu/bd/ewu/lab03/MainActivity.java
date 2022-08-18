package edu.bd.ewu.lab03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText name, place, datetime, capacity, budget, email, phone, description;
    RadioGroup radio_grp;

    TextView cancel, share, save, error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().equals("") || place.getText().equals("") || datetime.getText().equals("")
                        || capacity.getText().equals("")|| budget.getText().equals("")|| email.getText().equals("")
                        || phone.getText().equals("")|| description.getText().equals("") || radio_grp.getCheckedRadioButtonId() == -1){

                    error.setText("Please Fill all the field");
                }
                else{
                    error.setText("");
                    System.out.println("Name:" +name.getText().toString());
                    System.out.println("Place:" +place.getText().toString());
                    int id = radio_grp.getCheckedRadioButtonId();
                    if(id == 1){
                        System.out.println("Type: Indoor");
                    }else if(id == 2){
                        System.out.println("Type: Outdoor");
                    }else{
                        System.out.println("Type: Online");
                    }
                    System.out.println("Date & Time:" +datetime.getText().toString());
                    System.out.println("Capacity:" +capacity.getText().toString());
                    System.out.println("Budget:" +budget.getText().toString());
                    System.out.println("Email:" +email.getText().toString());
                    System.out.println("Phone:" +phone.getText().toString());
                    System.out.println("Description:" +description.getText().toString());
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}