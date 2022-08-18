package edu.bd.ewu.lab06;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText name,email, phone, userId, password, confirm_pass;
    TableRow name_row,email_row, phone_row, userId_row, password_row, confirm_pass_row;
    CheckBox remember_user_id, remember_pass;
    TextView login, go, exit, title, already;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        userId = findViewById(R.id.user_id);
        password = findViewById(R.id.password);
        confirm_pass = findViewById(R.id.repassword);

        name_row = findViewById(R.id.name_row);
        email_row = findViewById(R.id.email_row);
        phone_row = findViewById(R.id.phone_row);
        userId_row = findViewById(R.id.user_id_row);
        password_row = findViewById(R.id.password_row);
        confirm_pass_row = findViewById(R.id.remember_pass_row);

        remember_user_id = findViewById(R.id.remember_user_id);
        remember_pass = findViewById(R.id.remember_pass);

        login = findViewById(R.id.login_txt);
        exit = findViewById(R.id.exit_btn);
        go = findViewById(R.id.go_btn);
        title = findViewById(R.id.title);
        already = findViewById(R.id.already);

        exit.setOnClickListener(v->{
            finish();
            System.exit(0);
        });

        SharedPreferences pref = getSharedPreferences("AuthInfo", MODE_PRIVATE);
        System.out.println(pref.getString("name", null));
        if(pref.getBoolean("remember_user_id", false)){
            name_row.setVisibility(View.INVISIBLE);
            confirm_pass_row.setVisibility(View.INVISIBLE);
            phone_row.setVisibility(View.INVISIBLE);
            email_row.setVisibility(View.INVISIBLE);
            title.setText("Login");
            remember_user_id.setVisibility(View.INVISIBLE);
            remember_pass.setVisibility(View.INVISIBLE);
            login.setText(" Sign Up");
            userId.setText(pref.getString("user_id", null));
        }
        else{
            login.setText(" Login");
            name_row.setVisibility(View.VISIBLE);
            confirm_pass_row.setVisibility(View.VISIBLE);
            phone_row.setVisibility(View.VISIBLE);
            email_row.setVisibility(View.VISIBLE);
            title.setText("Sign Up");
            remember_user_id.setVisibility(View.VISIBLE);
            remember_pass.setVisibility(View.VISIBLE);
            already.setText("Already have an account?");
        }

        go.setOnClickListener(v->{
            if(pref.getBoolean("remember_user_id", false)){
                if(pref.getString("user_id", null).equals(userId.getText().toString())){
                    String name = pref.getString("name", null);
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    i.putExtra("name", name);
                    startActivity(i);
                }
                else{
                    showDialogue("Invalid Password", "Invalid", "OK", "Back");
                }
            }
            else{
                String error_msg = "";
                if(name.getText().toString().length() == 0){
                    error_msg += "Name field is Empty!\n";
                }
                else if(name.getText().toString().length() < 6){
                    error_msg += "Name is too short. Length should be 6 character long\n";
                }
                if(email.getText().toString().length() == 0){
                    error_msg += "Email field is empty!\n";
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    error_msg += "Invalid EMail address. Enter a valid email address\n";
                }
                if(phone.getText().toString().length() == 0){
                    error_msg += "Phone Number field is empty!\n";
                }
                else if(phone.getText().toString().length() < 11){
                    error_msg += "Invalid Phone number. Enter a valid email address\n";
                }
                if(password.getText().toString().length() == 0){
                    error_msg += "Password field is empty!\n";
                }
                else if(password.getText().toString().length() < 8){
                    error_msg += "Password is too short. Length should be 8 character long or more\n";
                }
                if(confirm_pass.getText().toString().length() == 0){
                    error_msg += "Re-Password field is empty!\n";
                }
                else if(!password.getText().toString().equals(confirm_pass.getText().toString())){
                    error_msg += "Password doesn't match. Plase enter a same password\n";
                }
                if(!error_msg.equals("")){
                    showDialogue(error_msg, "Validation Error!", "Ok", "Back");
                }
                else{
                    showDialogue("Do you want to Sign Up", "Confirmation!", "Yes", "Cancel");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("AuthInfo", MODE_PRIVATE);
        System.out.println(pref.getBoolean("remember_pass", false));
        if(pref.getBoolean("remember_pass", false)){
            Intent home =new Intent(getApplicationContext(), Home.class);
            home.putExtra("name", pref.getString("name", null));
            startActivity(home);
        }
    }

    private void showDialogue(String msg, String title, String btn1, String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setCancelable(false)
                .setPositiveButton(btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(btn1.equals("Yes")){
                            SharedPreferences pref = getSharedPreferences("AuthInfo", MODE_PRIVATE);
                            SharedPreferences.Editor prefEditor = pref.edit();
                            System.out.println(name.getText().toString());
                            prefEditor.putBoolean("remember_pass", remember_pass.isChecked());
                            prefEditor.putBoolean("remember_user_id", remember_user_id.isChecked());
                            prefEditor.putString("name", name.getText().toString());
                            prefEditor.putString("email", email.getText().toString());
                            prefEditor.putString("phone", phone.getText().toString());
                            prefEditor.putString("user_id", userId.getText().toString());
                            prefEditor.putString("password", password.getText().toString());

                            prefEditor.apply();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            dialog.cancel();
                        }
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