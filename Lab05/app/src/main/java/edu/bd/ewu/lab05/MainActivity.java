package edu.bd.ewu.lab05;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDialogue(String msg, String title, String btn1, String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Util.getInstance().deleteByKey(MainActivity.this, key);
                        dialog.cancel();
                        //loadData();
                        //adapter.notifyDataSetChanged()
                    }
                })
                .setNegativeButton(btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}