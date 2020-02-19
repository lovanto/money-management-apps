package com.lovanto.keuangan.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.lovanto.keuangan.R;
import com.lovanto.keuangan.activity.KeuanganActivity;
import com.lovanto.keuangan.activity.MainActivity;
import com.lovanto.keuangan.activity.PersonActivity;
import com.lovanto.keuangan.activity.TambahActivity;
import com.lovanto.keuangan.activity.TentangActivity;
import com.lovanto.keuangan.activity.Welcome;
import com.lovanto.keuangan.export.Utils;
import com.lovanto.keuangan.model.Transaksi;
import com.lovanto.keuangan.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class HomeFragment extends Fragment {

    public String username, email;
    public ImageButton pengeluaran2, pemasukan;

    Transaksi transaksi;
    com.lovanto.keuangan.DBHelper DBHelper;
    String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Keuangan/export/";
    SQLiteToExcel sqliteToExcel;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        User user = new User(getActivity());
        JSONObject dataUser = user.getUser();
        transaksi = new Transaksi(getActivity());
        TextView uangDompetText = (TextView) rootView.findViewById(R.id.rupiahku);
        try {
            Double balance = Double.parseDouble(dataUser.getString("uang"));
            uangDompetText.setText("Rp " + String.format("%,.2f", balance));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageButton keuangan = (ImageButton) rootView.findViewById(R.id.keuangan);
        ImageButton tentang = (ImageButton) rootView.findViewById(R.id.tentang);
        ImageButton tambah = (ImageButton) rootView.findViewById(R.id.tambah);
        pemasukan = (ImageButton) rootView.findViewById(R.id.pemasukan);
        pengeluaran2 = (ImageButton) rootView.findViewById(R.id.pengeluaran2);
        ImageButton person = (ImageButton) rootView.findViewById(R.id.person);
        ImageButton keluar = (ImageButton) rootView.findViewById(R.id.keluar);
        ImageButton export = (ImageButton) rootView.findViewById(R.id.export);

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                File myDir = new File(directory_path);
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                sqliteToExcel = new SQLiteToExcel(getActivity(), DBHelper.DATABASE_NAME, directory_path);
                sqliteToExcel.exportAllTables("export.xls", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted(String filePath) {
                        Utils.showSnackBar(v, "Export Berhasil. " + directory_path);
                    }

                    @Override
                    public void onError(Exception e) {
                        Utils.showSnackBar(v, e.getMessage());
                    }
                });
            }
        });

        keuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KeuanganActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TentangActivity.class);
                startActivity(intent);
            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        pemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            MainActivity.main.viewPager.setCurrentItem(0);
            }
        });

        pengeluaran2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.main.viewPager.setCurrentItem(2);
            }
        });

        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setMessage("Ingin Keluar Aplikasi ?");
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
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
        return rootView;
    }

    private void closefragment() {
        getActivity().finish();
    }
}
