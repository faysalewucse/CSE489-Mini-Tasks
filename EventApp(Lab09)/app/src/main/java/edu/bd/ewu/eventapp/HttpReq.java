package edu.bd.ewu.eventapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HttpReq {
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
                        JSONArray jsonArray = new JSONArray();

                        System.out.println("Data:"+jsonObject.getString("events"));
//                        Log.d("EVENTDATA", "Data: "+data);
                        //webView.loadData(data, "text/html", "UTF-8");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
