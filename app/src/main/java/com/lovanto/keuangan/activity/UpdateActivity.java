package com.lovanto.keuangan.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import com.lovanto.keuangan.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateActivity extends AppCompatActivity {

    protected Cursor cursor;

    TextInputEditText uangText, uangText2, keperluanText, tanggalText, IDText, typeText;
    TextInputLayout textInputLayoutKeperluan, textInputLayoutUang;
    RadioButton radiopendapatan, radiopengeluaran;
    RadioGroup radioGroup;
    TextView batal;
    DBHelper dbHelper;
    Button simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

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

        this.setTitle("Update Data Keuangan");

        IDText.setEnabled(false);
        tanggalText.setEnabled(false);
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

        batal = (TextView) findViewById(R.id.textViewBatal);
        simpan = (Button) findViewById(R.id.buttonSimpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Yakin ingin memperbarui data ?");
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (validate()) {
                            String type = "";
                            if (radiopendapatan.isChecked()) {
                                type = "Masuk";
                            } else if (radiopengeluaran.isChecked()) {
                                type = "Keluar";
                            } else {
                                type = "type";
                            }

                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            db.execSQL("update tb_keuangan set keperluan='" +
                                    keperluanText.getText().toString() + "', uang='" +
                                    uangText.getText().toString() + "', type='" +
                                    type + "', tanggal='" +
                                    tanggalText.getText().toString() + "' where id='" +
                                    IDText.getText().toString() + "'");

                            User dataUser = new User(UpdateActivity.this);
                            JSONObject user = dataUser.getUser();
                            Integer balance = 0;
                            Integer balance2 = 0;
                            Integer balance3 = 0;
                            String typeku = typeText.getText().toString();
                            Integer uangku = Integer.parseInt(uangText.getText().toString());
                            Integer uangku2 = Integer.parseInt(uangText2.getText().toString());
                            try {
                                if (type.equals("Keluar") && typeku.equals("Keluar")) {
                                    balance3 = (user.getInt("pengeluaran") - uangku2) + uangku;
                                    balance2 = user.getInt("pendapatan");
                                    balance = (user.getInt("uang") + uangku2) - uangku;
                                } else if (type.equals("Masuk") && typeku.equals("Masuk")) {
                                    balance3 = user.getInt("pengeluaran");
                                    balance2 = (user.getInt("pendapatan") - uangku2) + uangku;
                                    balance = (user.getInt("uang") - uangku2) + uangku;
                                } else if (type.equals("Keluar") && typeku.equals("Masuk")) {
                                    balance3 = user.getInt("pengeluaran") + uangku;
                                    balance2 = (user.getInt("pendapatan") - uangku2);
                                    balance = (user.getInt("uang") - uangku2) - uangku;
                                } else {
                                    balance3 = (user.getInt("pengeluaran") - uangku2);
                                    balance2 = user.getInt("pendapatan") + uangku;
                                    balance = (user.getInt("uang") + uangku2) + uangku;
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
                            Toast.makeText(getApplicationContext(), "Data anda berhasil disimpan", Toast.LENGTH_LONG).show();
                            KeuanganActivity.ma.RefreshList();
                            finish();
                        } else {
                            Toast.makeText(UpdateActivity.this, "Data anda gagal disimpan", Toast.LENGTH_LONG).show();
                        }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
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

    public boolean validate() {
        boolean valid = false;

        uangText = (TextInputEditText) findViewById(R.id.editTextUang);
        keperluanText = (TextInputEditText) findViewById(R.id.editTextKeperluan);

        textInputLayoutKeperluan = (TextInputLayout) findViewById(R.id.textInputLayoutKeperluan);
        textInputLayoutUang = (TextInputLayout) findViewById(R.id.textInputLayoutUang);

        String uang = uangText.getText().toString();
        String keperluan = keperluanText.getText().toString();
        if (uang.isEmpty()) {
            valid = false;
            textInputLayoutUang.setError("Masukan nominal uang");
        } else {
            if (uang.length() >= 1) {
                valid = true;
                textInputLayoutUang.setError(null);
            } else {
                valid = false;
                textInputLayoutUang.setError("Uang tidak sesuai ketentuan");
            }
        }

        if (keperluan.isEmpty()) {
            valid = false;
            textInputLayoutKeperluan.setError("Keperluan masih kosong");
        } else {
            if (keperluan.length() >= 1) {
                valid = true;
                textInputLayoutKeperluan.setError(null);
            } else {
                valid = false;
                textInputLayoutKeperluan.setError("Keperluan anda tidak sesuai ketentuan");
            }
        }
        return valid;
    }
}
