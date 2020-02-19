package com.lovanto.keuangan.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lovanto.keuangan.DBHelper;
import com.lovanto.keuangan.R;
import com.lovanto.keuangan.adapter.TransaksiAdapter;
import com.lovanto.keuangan.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteActivity extends AppCompatActivity {

    TextInputEditText uangText, uangText2, keperluanText, tanggalText, IDText, typeText;
    RadioButton radiopendapatan, radiopengeluaran;
    RadioGroup radioGroup;
    TextView batal;
    protected Cursor cursor;
    DBHelper dbHelper;
    Button hapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        dbHelper = new DBHelper(this);
        typeText = (TextInputEditText) findViewById(R.id.editTextType2);
        uangText = (TextInputEditText) findViewById(R.id.editTextUang);
        keperluanText = (TextInputEditText) findViewById(R.id.editTextKeperluan);
        tanggalText = (TextInputEditText) findViewById(R.id.editTextTanggal);
        IDText = (TextInputEditText) findViewById(R.id.editTextID);
        uangText2 = (TextInputEditText) findViewById(R.id.editTextUang2);
        radiopendapatan = (RadioButton) findViewById(R.id.radiopendapatan2);
        radiopengeluaran = (RadioButton) findViewById(R.id.radiopengeluaran2);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        this.setTitle("Delete Data Keuangan");

        IDText.setEnabled(false);
        tanggalText.setEnabled(false);
        uangText.setEnabled(false);
        keperluanText.setEnabled(false);
        radiopendapatan.setEnabled(false);
        radiopengeluaran.setEnabled(false);
        typeText.setVisibility(View.GONE);
        uangText2.setVisibility(View.GONE);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tb_keuangan WHERE id= '" +
                getIntent().getStringExtra("id") + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            IDText.setText(cursor.getString(0).toString());
            keperluanText.setText(cursor.getString(1).toString());
            uangText.setText(cursor.getString(2).toString());
            uangText2.setText(cursor.getString(2).toString());
            typeText.setText(cursor.getString(3).toString());
            tanggalText.setText(cursor.getString(4).toString());
        }

        if (cursor.getString(3).toString().equals("Masuk")) {
            radiopendapatan.setChecked(true);
        } else {
            radiopengeluaran.setChecked(true);
        }

        hapus = (Button) findViewById(R.id.buttonHapus);
        batal = (TextView) findViewById(R.id.textViewBatal);
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Yakin ingin menghapus data ?");
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idku = IDText.getText().toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("delete from tb_keuangan where id = '" + idku + "'");
                        User dataUser = new User(DeleteActivity.this);
                        JSONObject user = dataUser.getUser();

                        Integer balance = 0;
                        Integer balance2 = 0;
                        Integer balance3 = 0;

                        String type = "";
                        if (radiopendapatan.isChecked()) {
                            type = "Masuk";
                        } else if (radiopengeluaran.isChecked()) {
                            type = "Keluar";
                        } else {
                            type = "type";
                        }

                        String typeku = typeText.getText().toString();
                        Integer uangku = Integer.parseInt(uangText2.getText().toString());
                        try {
                            if (type.equals("Keluar") && typeku.equals("Keluar")) {
                                balance3 = user.getInt("pengeluaran") - uangku;
                                balance2 = user.getInt("pendapatan");
                                balance = uangku + user.getInt("uang");
                            } else {
                                balance3 = user.getInt("pengeluaran");
                                balance2 = user.getInt("pendapatan") - uangku;
                                balance = user.getInt("uang") - uangku;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Balance2", balance2.toString());
                        Log.d("Balance3", balance3.toString());
                        Log.d("Balance", balance.toString());
                        dataUser.updateBalance2(balance2);
                        dataUser.updateBalance3(balance3);
                        dataUser.updateBalance(balance);
                        finish();

                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }


        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Yakin ingin kembali ?");
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Yakin ingin kembali ?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
