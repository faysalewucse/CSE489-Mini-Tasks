package edu.bd.ewu.addressbookremotedb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class AddAddress extends AppCompatActivity {

    TextView cancel_btn, save_btn;
    EditText name, email, phone, address;
    String encodedImage;
    String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        cancel_btn = findViewById(R.id.cancel_btn);
        save_btn = findViewById(R.id.save_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.adrress);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        if(id != null){
            AddressesDB db = new AddressesDB(this);
            AddressData data = db.getValueByKey(id);

            name.setText(data.name);
            phone.setText(data.phone);
            email.setText(data.email);
            address.setText(data.address);
        }

        cancel_btn.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
        save_btn.setOnClickListener(v -> {
            String error_msg = "";
            if(name.getText().toString().length() == 0){
                error_msg += "Name Field is Empty\n";
            }
            else if(name.getText().toString().length() < 5){
                error_msg += "Name is too short!. Length should be more than 5\n";
            }
            if(email.getText().toString().isEmpty()){
                error_msg += "Email Field is Empty\n";
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                error_msg += "Email is not valid. Please enter a valid email address\n";
            }
            if(phone.getText().toString().isEmpty()){
                error_msg += "Phone(Home) Field is Empty\n";
            }
            else if(phone.getText().toString().length() != 11){
                error_msg += "Phone number is not valid. Please enter a valid phone number with 11 digits.\n";
            }
            if(address.getText().toString().length() == 0){
                error_msg += "Please enter your address.\n";
            }
            if(error_msg.equals("")){
                showDialogue("Do you want to save the Contact Info?", "Confirmation!", "Save", "Cancel");
            }
            else{
                showDialogue(error_msg, "Error Occured!", "Back", "OK");
            }

        });

    }

    private void showDialogue(String msg, String title, String btn1, String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, (dialog, which) -> {
                    if(btn1.equals("Save")) {

                        Calendar calendar = Calendar.getInstance();
                        if(id == null){
                            id = calendar.getTimeInMillis()+"";
                        }

                        AddressesDB db = new AddressesDB(getApplicationContext());
                        db.updateValueByKey(id, name.getText().toString(), email.getText().toString(),
                                phone.getText().toString(), address.getText().toString(), encodedImage);

                        AddressData addressData = new AddressData(id, name.getText().toString(), email.getText().toString(),
                                phone.getText().toString(), address.getText().toString());

                        @SuppressLint("HardwareIds")
                        String mobileId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(mobileId);
                        databaseReference.child(id).setValue(addressData);

                        db.close();

                        finish();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),"Successfully Saved Contact Info",Toast.LENGTH_LONG).show();
                    }
                    else dialog.cancel();
                })
                .setNegativeButton(btn2, (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}