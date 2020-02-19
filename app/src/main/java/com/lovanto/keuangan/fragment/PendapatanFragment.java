package com.lovanto.keuangan.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lovanto.keuangan.DBHelper;
import com.lovanto.keuangan.R;
import com.lovanto.keuangan.activity.DeleteActivity;
import com.lovanto.keuangan.activity.MainActivity;
import com.lovanto.keuangan.activity.TambahActivity;
import com.lovanto.keuangan.activity.UpdateActivity;
import com.lovanto.keuangan.adapter.TransaksiAdapter;
import com.lovanto.keuangan.model.Transaksi;
import com.lovanto.keuangan.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PendapatanFragment extends Fragment {

    String[] daftar;
    String[] daftar2;
    String[] daftar3;
    ListView ListView01;
    DBHelper dbcenter;
    Transaksi transaksi;

    public TransaksiAdapter adapter;
    protected Cursor cursor;

    public PendapatanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_pendapatan, container, false);

        TextView pendapatan = (TextView) rootView.findViewById(R.id.pendapatan);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.main.viewPager.setCurrentItem(1);
                Toast.makeText(getActivity(), "Refreshed",Toast.LENGTH_LONG).show();
            }
        });

        dbcenter = new DBHelper(getActivity());
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * From tb_keuangan where type LIKE '%Masuk%'", null);
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

        transaksi = new Transaksi(getActivity());
        ArrayList<HashMap<String, String>> trxList = transaksi.getList2();
        adapter = new TransaksiAdapter(getActivity(), trxList);
        ListView01 = (ListView) rootView.findViewById(R.id.list_transaksi);
        ListView01.setAdapter(adapter);
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar2[arg2]; //.getItemAtPosition(arg2).toString();
                final CharSequence[] dialogitem = {" Update Data Keuangan ", " Delete Data Keuangan "};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent i = new Intent(getActivity(), UpdateActivity.class);
                                i.putExtra("id", selection);
                                startActivity(i);
                                break;
                            case 1:
                                Intent in = new Intent(getActivity(), DeleteActivity.class);
                                in.putExtra("id", selection);
                                startActivity(in);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();

        User dataUser = new User(this.getActivity());
        JSONObject user = dataUser.getUser();
        try {
            Double balance2 = Double.parseDouble(user.getString("pendapatan"));
            pendapatan.setText("Rp " + String.format("%,.2f", balance2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
