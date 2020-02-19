package com.lovanto.keuangan.activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lovanto.keuangan.R;
import com.lovanto.keuangan.model.Transaksi;
import com.lovanto.keuangan.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class TambahActivity extends AppCompatActivity {

    TextInputEditText uangText, keperluanText;
    RadioButton radiopendapatan, radiopengeluaran;
    Transaksi transaksi;
    Transaksi dataTransaksi = new Transaksi();
    TextInputLayout textInputLayoutKeperluan, textInputLayoutUang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        uangText = (TextInputEditText) findViewById(R.id.editTextUang);
        keperluanText = (TextInputEditText) findViewById(R.id.editTextKeperluan);
        radiopendapatan = findViewById(R.id.radiopendapatan);
        radiopengeluaran = findViewById(R.id.radiopengeluaran);
        transaksi = new Transaksi(this);

        View btn = findViewById(R.id.buttonSimpan);
        View batal = findViewById(R.id.textViewBatal);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keperluan = keperluanText.getText().toString();
                if (validate()) {
                    String type = "";
                    if (radiopendapatan.isChecked()) {
                        type = "Masuk";
                    } else if (radiopengeluaran.isChecked()) {
                        type = "Keluar";
                    } else {
                        type = "type";
                    }

                    if (type.equals("type")){
                        Toast.makeText(TambahActivity.this, "Operasional belum dipilih",Toast.LENGTH_LONG).show();
                    } else {
                        Integer uang = Integer.parseInt(uangText.getText().toString());
                        dataTransaksi.keperluan = keperluan;
                        dataTransaksi.uang = uang;
                        dataTransaksi.type = type;
                        transaksi.insertTable(dataTransaksi);

                        User dataUser = new User(TambahActivity.this);
                        JSONObject user = dataUser.getUser();

                        Integer balance = 0;
                        Integer balance2 = 0;
                        Integer balance3 = 0;
                        try {
                            if (type.equals("Keluar")) {
                                balance3 = uang + user.getInt("pengeluaran");
                                balance2 = user.getInt("pendapatan");
                                balance = user.getInt("uang") - uang;
                            } else {
                                balance3 = user.getInt("pengeluaran");
                                balance2 = uang + user.getInt("pendapatan");
                                balance = uang + user.getInt("uang");
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

                        uangText.setText("");
                        keperluanText.setText("");
                        uangText.requestFocus();
                        Toast.makeText(TambahActivity.this, "Data anda berhasil disimpan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TambahActivity.this, "Data anda gagal disimpan", Toast.LENGTH_SHORT).show();
                }
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TambahActivity.this, KeuanganActivity.class)); finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TambahActivity.this, KeuanganActivity.class)); finish();
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
