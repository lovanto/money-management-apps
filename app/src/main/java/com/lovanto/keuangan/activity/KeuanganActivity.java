package com.lovanto.keuangan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lovanto.keuangan.DBHelper;
import com.lovanto.keuangan.R;
import com.lovanto.keuangan.adapter.TransaksiAdapter;
import com.lovanto.keuangan.model.Transaksi;
import com.lovanto.keuangan.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class KeuanganActivity extends AppCompatActivity {
    String[] daftar;
    String[] daftar2;
    String[] daftar3;
    ListView ListView01;
    DBHelper dbcenter;
    Transaksi transaksi;

    protected Cursor cursor;
    public static KeuanganActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keuangan);

        ma = this;
        dbcenter = new DBHelper(this);
        RefreshList();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KeuanganActivity.this, TambahActivity.class)); finish();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KeuanganActivity.this, KeuanganActivity.class)); finish();
                Toast.makeText(getApplication(), "Refreshed", Toast.LENGTH_LONG).show();
            }
        });

        TextView uangDompetText = (TextView) findViewById(R.id.uang_dompet);
        User dataUser = new User(this);
        JSONObject user = dataUser.getUser();
        try {
            Double balance = Double.parseDouble(user.getString("uang"));
            uangDompetText.setText("Rp " + String.format("%,.2f", balance));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(KeuanganActivity.this, MainActivity.class));
        finish();
    }

    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tb_keuangan order by id desc", null);
        daftar = new String[cursor.getCount()];
        daftar2 = new String[cursor.getCount()];
        daftar3 = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar3[cc] = cursor.getString(3).toString();
            daftar2[cc] = cursor.getString(0).toString();
            daftar[cc] = cursor.getString(1).toString();
        }

        transaksi = new Transaksi(this);
        ArrayList<HashMap<String, String>> trxList = transaksi.getList();
        TransaksiAdapter adapter = new TransaksiAdapter(this, trxList);

        ListView01 = (ListView) findViewById(R.id.list_transaksi);
        ListView01.setAdapter(adapter);
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar2[arg2];
                final CharSequence[] dialogitem = {" Update Data Keuangan ", " Delete Data Keuangan "};
                AlertDialog.Builder builder = new AlertDialog.Builder(KeuanganActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent i = new Intent(getApplicationContext(), UpdateActivity.class);
                                i.putExtra("id", selection);
                                startActivity(i);
                                break;
                            case 1:
                                Intent in = new Intent(getApplicationContext(), DeleteActivity.class);
                                in.putExtra("id", selection);
                                startActivity(in);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();
    }
}
