package com.lovanto.keuangan.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lovanto.keuangan.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String nama;
    public String email;
    public Integer uang;
    public Integer pendapatan;
    public Integer pengeluaran;
    private DBHelper dbHelper;

    public User() {

    }

    public User(String nama, String email, Integer uang, Integer pengeluaran, Integer pendapatan) {
        this.nama = nama;
        this.email = email;
        this.uang = uang;
        this.pendapatan = pendapatan;
        this.pengeluaran = pengeluaran;
    }

    public User(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insertUser(User data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama", data.nama);
        values.put("email", data.email);
        values.put("uang", data.uang);
        values.put("pendapatan", data.pendapatan);
        values.put("pengeluaran", data.pengeluaran);
        long user_id = db.insert("tb_user", null, values);
        db.close();
        return (int) user_id;
    }

    public int updateBalance(Integer balance) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("uang", balance);
        String selection = "id=?";
        String[] selectionArg = {String.valueOf(1)};
        long id = db.update("tb_user", cValues, selection, selectionArg);
        return (int) id;
    }

    public int updateBalance2(Integer balance2) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("pendapatan", balance2);
        String selection = "id=?";
        String[] selectionArg = {String.valueOf(1)};
        long id = db.update("tb_user", cValues, selection, selectionArg);
        return (int) id;
    }

    public int updateBalance3(Integer balance3) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("pengeluaran", balance3);
        String selection = "id=?";
        String[] selectionArg = {String.valueOf(1)};
        long id = db.update("tb_user", cValues, selection, selectionArg);
        return (int) id;
    }

    public JSONObject getUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from tb_user where id=1", null);
        JSONObject usernya = new JSONObject();
        if (cursor.moveToFirst()) {
            try {
                usernya.put("nama", cursor.getString(cursor.getColumnIndex("nama")));
                usernya.put("email", cursor.getString(cursor.getColumnIndex("email")));
                usernya.put("uang", cursor.getString(cursor.getColumnIndex("uang")));
                usernya.put("pendapatan", cursor.getString(cursor.getColumnIndex("pendapatan")));
                usernya.put("pengeluaran", cursor.getString(cursor.getColumnIndex("pengeluaran")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return usernya;
    }
}
