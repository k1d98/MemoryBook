package com.example.memorybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.memorybook.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class ItemAdapter extends ArrayAdapter {
    private List<Item> itemsList;

    public ItemAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void setItemsList(List<Item> items) {
        itemsList = items;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView opis, datum;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
        }
        opis = (TextView) convertView.findViewById(R.id.opis);
        datum = (TextView) convertView.findViewById(R.id.time);
        opis.setText(itemsList.get(position).getTitle());

        datum.setText(itemsList.get(position).getDate());

        return convertView;
    }
    public List<Item>  getItemsList(){
        return itemsList;
    }
}
