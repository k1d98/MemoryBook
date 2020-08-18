package com.example.memorybook;

import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SeeMemoryFragment extends Fragment {
    private int itemID;

    public SeeMemoryFragment(int id){
        this.itemID = id;
    }

    public void setItemID(int id){
        this.itemID = id;
    }
    public int getItemID(int id){
        return itemID;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.fragment_see_memory, container, false);
        TextView date = (TextView) view.findViewById(R.id.dateText);
        TextView loc = (TextView) view.findViewById(R.id.locationTxt);
        TextView description = (TextView) view.findViewById(R.id.descriptionTxt);
        TextView title = (TextView) view.findViewById(R.id.titleText);

        ImageView a1 = (ImageView) view.findViewById(R.id.imageRibbon1);
        ImageView a2 = (ImageView) view.findViewById(R.id.imageRibbon2);

        //random ribbon
        int min = 0;
        int max = 4;
        TypedArray lista = getResources().obtainTypedArray(R.array.tapeArray);
        int rnd = (int)(Math.random() * ((max - min) + 1)) + min;
        a1.setImageDrawable(lista.getDrawable(rnd));
        rnd = (int) (int)(Math.random() * ((max - min) + 1)) + min;
        a2.setImageDrawable(lista.getDrawable(rnd));

        final Item item = MainActivity.idb.getItemDao().getOne(itemID);

        date.setText(item.getDate());
        ImageView iv = (ImageView) view.findViewById(R.id.photo);
        iv.setImageURI(Uri.fromFile(new File(item.getPath())));

        title.setText(item.getTitle());
        description.setText(item.getDescription());

        Geocoder geo = new Geocoder(getContext(), Locale.getDefault());
        List<Address> adresses = null;
        try {
            adresses = geo.getFromLocation(item.getLat(), item.getLng(), 1);
        }catch (IOException e){
            Log.e("error", "map problem smth");
        }
        loc.setText(adresses.get(0).getAddressLine(0));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
