package edu.bd.ewu.eventapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HttpReq {
    Context context;
    public HttpReq(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    public void httpRequest(final String keys[], final String values[]){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... param) {
                try {
                    List<NameValuePair> params = new ArrayList<>();
                    for(int i=0;i<keys.length;i++){
                        params.add(new BasicNameValuePair(keys[i], values[i]));
                    }
                    String data = JSONParser.getInstance().makeHttpRequest("http://www.muthosoft.com/univ/cse489/index.php", "POST", params);
                    return data;
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data != null){
                    try{
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray events = jsonObject.getJSONArray("events");
                        for (int i = 0; i < events.length(); i++) {
                            JSONObject e = events.getJSONObject(i);
                            String k = e.getString("key");
                            String v = e.getString("value");
                            KeyValueDB db = new KeyValueDB(context);
                            db.updateValueByKey(k, v);
                        }
                        System.out.println("Data:"+jsonObject.getString("events"));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
