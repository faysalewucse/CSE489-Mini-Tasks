package edu.bd.ewu.eventapp;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomEventAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<EventData> values;

    public CustomEventAdapter(@NonNull Context context, @NonNull ArrayList<EventData> items) {
        super(context, -1, items);
        this.context = context;
        this.values = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.event_template, parent, false);


        TextView eventName = rowView.findViewById(R.id.event_name);
        TextView eventDateTime = rowView.findViewById(R.id.event_date);
        TextView eventPlaceName = rowView.findViewById(R.id.event_place);
        //TextView eventType = rowView.findViewById(R.id.tvEventType);

        EventData e = values.get(position);
        eventName.setText(e.name);
        eventDateTime.setText(e.date);
        eventPlaceName.setText(e.place);
        //eventType.setText(e.eventType);

//        rowView.setId(position);
//        rowView.setClickable(true);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = v.getId();

                Intent i = new Intent(context, EventInfo.class);
                i.putExtra("key", values.get(position).getKey());
                context.startActivity(i);

                //Toast.makeText(context, values.get(position).getKey(), Toast.LENGTH_SHORT).show();

            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogue("Sure to Delete the event", "Confirmation", "Yes", "Cancel", values.get(position).getKey());
                return true;
            }
        });
        return rowView;
    }

    private void showDialogue(String msg, String title, String btn1, String btn2, String key){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(msg);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, (dialog, which) -> {
                    if(btn1.equals("Yes")){
                        KeyValueDB db = new KeyValueDB(context);
                        db.deleteDataByKey(key);
                        db.close();
                        context.startActivity(new Intent(context, MainActivity.class));
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