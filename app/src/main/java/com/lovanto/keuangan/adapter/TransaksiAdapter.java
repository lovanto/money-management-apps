package com.lovanto.keuangan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.database.Cursor;

import com.lovanto.keuangan.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TransaksiAdapter extends ArrayAdapter<HashMap<String, String>> {
    private final ArrayList data;
    private LayoutInflater mInflater;

    public TransaksiAdapter(Context context, ArrayList<HashMap<String, String>> trxList) {
        super(context, 0, trxList);
        data = new ArrayList();
        data.addAll(trxList);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, String> tr = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_images, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView uang = (TextView) convertView.findViewById(R.id.uang);
        TextView tglTrx = (TextView) convertView.findViewById(R.id.tglTrx);
        TextView type = (TextView) convertView.findViewById(R.id.type_trx);

        int gambar = R.drawable.rupiah_g;
        int gambar2 = R.drawable.rupiah_r;
        if (tr.get("type").equals("Masuk")) {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_image);
            imageView.setImageResource(gambar);
            title.setTextColor(Color.parseColor("#388E3C"));
            uang.setTextColor(Color.parseColor("#388E3C"));
            tglTrx.setTextColor(Color.parseColor("#388E3C"));
            type.setTextColor(Color.parseColor("#388E3C"));
        } else {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_image);
            imageView.setImageResource(gambar2);
            title.setTextColor(Color.RED);
            uang.setTextColor(Color.RED);
            tglTrx.setTextColor(Color.RED);
            type.setTextColor(Color.RED);
        }

        title.setText(tr.get("keperluan").toString());
        Double am = Double.parseDouble(tr.get("uang"));
        uang.setText("Rp " + String.format("%,.2f", am));
        tglTrx.setText(tr.get("tanggal"));
        type.setText(tr.get("type"));

        return convertView;
    }
}
