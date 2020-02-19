package com.lovanto.keuangan.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lovanto.keuangan.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Transaksi {
    public String keperluan;
    public String tanggal;
    public Integer uang;
    private DBHelper dbHelper;
    public String type;
    Context contextApp;

    public  Transaksi() {

    }

    public Transaksi (String keperluan, Integer uang, String tanggal, String type) {
        this.keperluan = keperluan;
        this.tanggal = tanggal;
        this.type = type;
        this.uang = uang;
    }

    public Transaksi(Context context) {
        dbHelper = new DBHelper(context);
        contextApp = context;
    }

    public int insertTable(Transaksi data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());

        values.put("uang", data.uang);
        values.put("keperluan", data.keperluan);
        values.put("type", data.type);
        values.put("tanggal", strDate);

        long transaksi_id = db.insert("tb_keuangan",null,values);
        db.close();
        return (int) transaksi_id;
    }

    public ArrayList<HashMap<String, String>> getList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String querySelect = "SELECT * From tb_keuangan order by id desc";
        ArrayList<HashMap<String, String>> trxList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(querySelect, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> trx = new HashMap<String, String>();
                trx.put("id", cursor.getString(cursor.getColumnIndex("id")));
                trx.put("keperluan", cursor.getString(cursor.getColumnIndex("keperluan")));
                trx.put("uang", cursor.getString(cursor.getColumnIndex("uang")));
                trx.put("type", cursor.getString(cursor.getColumnIndex("type")));
                trx.put("tanggal", cursor.getString(cursor.getColumnIndex("tanggal")));
                trxList.add(trx);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return trxList;
    }

    public ArrayList<HashMap<String, String>> getList2() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String querySelect = "SELECT * From tb_keuangan where type LIKE '%Masuk%'";
        ArrayList<HashMap<String, String>> trxList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(querySelect, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> trx = new HashMap<String, String>();
                trx.put("id", cursor.getString(cursor.getColumnIndex("id")));
                trx.put("keperluan", cursor.getString(cursor.getColumnIndex("keperluan")));
                trx.put("uang", cursor.getString(cursor.getColumnIndex("uang")));
                trx.put("type", cursor.getString(cursor.getColumnIndex("type")));
                trx.put("tanggal", cursor.getString(cursor.getColumnIndex("tanggal")));
                trxList.add(trx);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return trxList;
    }

    public ArrayList<HashMap<String, String>> getList3() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String querySelect = "SELECT * From tb_keuangan where type LIKE '%Keluar%'";
        ArrayList<HashMap<String, String>> trxList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(querySelect, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> trx = new HashMap<String, String>();
                trx.put("id", cursor.getString(cursor.getColumnIndex("id")));
                trx.put("keperluan", cursor.getString(cursor.getColumnIndex("keperluan")));
                trx.put("uang", cursor.getString(cursor.getColumnIndex("uang")));
                trx.put("type", cursor.getString(cursor.getColumnIndex("type")));
                trx.put("tanggal", cursor.getString(cursor.getColumnIndex("tanggal")));
                trxList.add(trx);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return trxList;
    }
}
