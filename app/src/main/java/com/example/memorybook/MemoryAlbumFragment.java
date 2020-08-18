package com.example.memorybook;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class MemoryAlbumFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ItemAdapter adapterI;
    ListView listV;
    List<Item> items;
    private static final int CAMERA_PERMISSION_CODE = 1888;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int READ_PERMISSION_CODE = 102;
    AlertDialog.Builder builder;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memoryalbum, container, false);

        FloatingActionButton fab = view.findViewById(R.id.add_new_album);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                    return;
                }
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);

                    return;
                }
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

                    return;
                }
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

                    return;
                }

                    FileOutputStream fos;
                    fos.





                    AddAlbum newFragment = new AddAlbum();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

            }
        });

        Button seeMap = (Button) view.findViewById(R.id.seeMapBtn);
        if(seeMap != null) {
            seeMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapFragment mapFragment = new MapFragment();
                    FragmentTransaction t = getFragmentManager().beginTransaction();
                    t.replace(R.id.frame_layout, mapFragment);
                    t.addToBackStack(null);
                    t.commit();
                }
            });
        }
        listV = (ListView) view.findViewById(android.R.id.list);

        ItemDatabase idb = MainActivity.idb;

        //TO RESET DATABASE
        //idb.getItemDao().deleteAll();
        //idb.cleanUp();

        adapterI=new ItemAdapter(getContext(), 0);
        items = idb.getItemDao().getAll();
        Collections.reverse(items);
        adapterI.setItemsList(items);
        listV.setAdapter(adapterI);

        builder = new AlertDialog.Builder(getContext());

        listV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long pos) {
                listV.setOnItemClickListener(null);
                builder.setMessage("Do you want to Delete this memory?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.idb.getItemDao().deleteOne(items.get((int) pos));
                                MemoryAlbumFragment mem = new MemoryAlbumFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout,mem);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                getListView().setOnItemClickListener(MemoryAlbumFragment.this);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                getListView().setOnItemClickListener(MemoryAlbumFragment.this);
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.setTitle("DELETE MEMORY");
                alert.show();

                return false;
            }
        });

        return view;
    }

            // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else {
                Toast.makeText(getActivity(),
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else {
                Toast.makeText(getActivity(),"Storage Permission Denied",   Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            else {
                Toast.makeText(getActivity(),
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(adapterI);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Item it = items.get(position);
        SeeMemoryFragment seeMemoryFragment = new SeeMemoryFragment(it.getId());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,seeMemoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
