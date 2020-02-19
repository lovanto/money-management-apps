package com.lovanto.keuangan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db_keuangan";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w("Version", "Current Version is " + db.getVersion());
        String queryBuatTable = "CREATE TABLE IF NOT EXISTS tb_keuangan (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "keperluan text," +
                "uang int," +
                "type text," +
                "tanggal text )";
        String queryBuatTableDompet = "CREATE TABLE IF NOT EXISTS tb_user (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nama text," +
                "email text," +
                "pendapatan int," +
                "pengeluaran int," +
                "uang int )";
        db.execSQL(queryBuatTable);
        db.execSQL(queryBuatTableDompet);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Update DB", "DB IS UPDATE TO " + db.getVersion());
        db.execSQL("DROP TABLE IF EXISTS tb_keuangan");
        db.execSQL("DROP TABLE IF EXISTS tb_user");
        onCreate(db);
    }
}
