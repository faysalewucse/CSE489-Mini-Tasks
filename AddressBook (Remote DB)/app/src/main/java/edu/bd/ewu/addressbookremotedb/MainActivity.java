package edu.bd.ewu.addressbookremotedb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView address_list;
    TextView add_address_btn;
    ArrayList<AddressData> addressData = new ArrayList<>();
    ArrayList<AddressData> allAddress = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        address_list = findViewById(R.id.address_list);
        add_address_btn = findViewById(R.id.addAddressBtn);

        System.out.println(addressData);
        AddressListAdapter adapter=new AddressListAdapter(getApplicationContext(), addressData);
        address_list.setAdapter(adapter);

        add_address_btn.setOnClickListener(v->{
            startActivity(new Intent(this, AddAddress.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AddressesDB db = new AddressesDB(this);
        Cursor c = db.getAllKeyValues();
        String id="", name="", email="", phone="", address="";
        while(c.moveToNext()){
            id = c.getString(0);
            name = c.getString(1);
            email = c.getString(2);
            phone = c.getString(3);
            address = c.getString(4);

            AddressData addressData = new AddressData(id, name, email, phone, address);
            this.addressData.add(addressData);
        }
        @SuppressLint("HardwareIds")
        String mobileId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(mobileId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allAddress.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String id = dataSnapshot.child("id").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String address = dataSnapshot.child("address").getValue().toString();

                    AddressData addressData = new AddressData(id, name, email, phone, address);
                    allAddress.add(addressData);
                }

                if(c.getCount() == 0 && allAddress.size() > 0){
                    for (int i = 0; i < allAddress.size(); i++) {
                        AddressesDB db = new AddressesDB(getApplicationContext());
                        db.updateValueByKey(allAddress.get(i).id,allAddress.get(i).name , allAddress.get(i).email,
                                allAddress.get(i).phone, allAddress.get(i).address, "");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}