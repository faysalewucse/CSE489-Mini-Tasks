package edu.bd.ewu.addressbookremotedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AddressesDB extends SQLiteOpenHelper {
    // Contact TABLE INFORMATION
    static final String DB_NAME = "ADDRESSES.DB";
    public final String ADDRESS_INFO = "address_info";
    public final String ID = "id";
    public final String NAME = "name";
    public final String EMAIL = "email";
    public final String PHONE = "phone";
    public final String ADDRESS = "address";

    Context context;
    public AddressesDB(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("DB@OnCreate");
        createKeyValueTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createKeyValueTable(SQLiteDatabase db){
        try {
            db.execSQL("create table " + ADDRESS_INFO + " (" + ID +" TEXT ,"+ NAME
                    + " TEXT , " + EMAIL + " TEXT , "+ PHONE  + " TEXT , " + ADDRESS + " TEXT )");
        } catch (Exception e) {
            Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor execute(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        try {
            res = db.rawQuery(query, null);
        }catch (Exception e) {
            //e.printStackTrace();
            handleError(db, e);
            res = db.rawQuery(query, null);
        }
        return res;
    }

    public Boolean insertInfo(String id, String name, String email, String phone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        cv.put(NAME, name);
        cv.put(EMAIL, email);
        cv.put(PHONE, phone);
        cv.put(ADDRESS, address);
        long result;
        try{
            result = db.insert(ADDRESS_INFO, null, cv);
        }catch (Exception e){
            handleError(db, e);
            result = db.insert(ADDRESS_INFO, null, cv);
        }
        return result != -1;
    }

    private void handleError(SQLiteDatabase db, Exception e){
        String errorMsg = e.getMessage();
        assert errorMsg != null;
        if (errorMsg.contains("no such table")){
            if (errorMsg.contains(ADDRESS_INFO)){
                createKeyValueTable(db);
            }
        }
    }

    public boolean updateValueByKey(String id, String name, String email, String phone, String address, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        cv.put(NAME, name);
        cv.put(EMAIL, email);
        cv.put(PHONE, phone);
        cv.put(ADDRESS, address);
        int nr = 0;
        try{
            nr = db.update(ADDRESS_INFO, cv, ID + "=?",
                    new String[] { id });
        }catch (Exception e){
            handleError(db, e);
            try {
                nr = db.update(ADDRESS_INFO, cv, ID + "=?",
                        new String[]{id});
            }catch (Exception ex){}
        }
        if (nr == 0) {
            insertInfo(id, name, email, phone, address);
        }
        return true;
    }

    public AddressData getValueByKey(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        try{
            res = db.rawQuery("SELECT * FROM " + ADDRESS_INFO + " WHERE "
                    + ID + "='" + id + "'", null);
        }catch (Exception e){
            handleError(db, e);
            res = db.rawQuery("SELECT * FROM " + ADDRESS_INFO + " WHERE "
                    + ID + "='" + id + "'", null);
        }
        if(res.getCount()>0){
            res.moveToNext();
            AddressData data = new AddressData(res.getString(0), res.getString(1),
                    res.getString(2), res.getString(3), res.getString(4));
            return data;
        }
        return null;
    }

    public Cursor getAllKeyValues() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + ADDRESS_INFO, null);
    }
}
