package edu.bd.ewu.addressbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView address_list;
    TextView add_address_btn;
    ArrayList<AddressData> addressData = new ArrayList<>();
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
    }
}