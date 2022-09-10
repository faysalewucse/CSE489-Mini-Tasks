package edu.bd.ewu.addressbookremotedb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AddressListAdapter extends ArrayAdapter<AddressData> {

    private final Context context;
    ArrayList<AddressData> addressData;

    public AddressListAdapter(Context context, ArrayList<AddressData> addressData) {
        super(context, -1, addressData);
        this.context = context;
        this.addressData = addressData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_template, parent, false);
        }

        TextView user_name = convertView.findViewById(R.id.user_name);
        TextView user_phone = convertView.findViewById(R.id.user_number);
        TextView user_address = convertView.findViewById(R.id.user_address);
        ImageView edit_btn = convertView.findViewById(R.id.edit_btn);

        edit_btn.setOnClickListener(v -> {
            Intent i = new Intent(context, AddAddress.class);
            i.putExtra("id", addressData.get(position).getId());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

        user_name.setText(addressData.get(position).getName());
        user_phone.setText(addressData.get(position).getPhone());
        user_address.setText(addressData.get(position).getAddress());

        return convertView;
    }
}
