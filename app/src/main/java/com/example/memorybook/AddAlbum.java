package com.example.memorybook;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddAlbum extends Fragment implements  OnMapReadyCallback{
    private ImageButton imageButton;
    private Date date;
    private ImageView imageView;
    private TextView description;
    private TextView title;
    private Button saveBtn;
    private View view;
    private Button chooseMap;
    private MarkerOptions markerOptions;

    private File photoFile;

    static final int RESULT_OK = -1  ;




    public final String APP_TAG = "MemoryBook";
    private final static String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private FusedLocationProviderClient fusedLocationClient;

    GoogleMap googleMap;
    MapView mMapView;
    LatLng currentLoc;
    private static final int CAMERA_PERMISSION_CODE = 1888;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_add_album, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageButton = (ImageButton) view.findViewById(R.id.imageButton);
        date = new Date();
        description = (TextView) view.findViewById(R.id.descriptionTxt);
        title = (TextView) view.findViewById(R.id.titleTxt);
        saveBtn = (Button) view.findViewById(R.id.saveItemBtn);
        //chooseMap = (Button) view.findViewById(R.id.chooseMap);

        mMapView=view.findViewById(R.id.mapa);



        initGoogleMap(savedInstanceState);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Item> lista = MainActivity.idb.getItemDao().getAll();
                LatLng loc = null;
                if(markerOptions != null) loc = markerOptions.getPosition();
                if(loc == null){
                    loc = currentLoc;
                }
                if(loc == null)
                    Toast.makeText(getActivity(), "We can't find your location, choose one on the map", Toast.LENGTH_LONG).show();
                else if(description.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Every memory should have a description", Toast.LENGTH_LONG).show();
                }else if(title.getText().length() == 0){
                    Toast.makeText(getActivity(), "Every memory should have a title", Toast.LENGTH_LONG).show();
                }else if(photoFile== null || !photoFile.exists()){
                    Toast.makeText(getActivity(), "Every memory should have a picture", Toast.LENGTH_LONG).show();
                }else {
                    Item item = new Item(date, description.getText().toString(), photoFile.getPath(), loc, title.getText().toString());
                    MainActivity.idb.getItemDao().insertOne(item);
                    MainActivity activity = (MainActivity) getActivity();
                    activity.updateMapFragment();

                    getFragmentManager().popBackStack();
                }
            }
        });
        return view;

    }


    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {

            googleMap = map;
            googleMap.setMyLocationEnabled(true);

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    markerOptions = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("New Memory");
                    markerOptions.draggable(true);

                    googleMap.addMarker(markerOptions);
                    googleMap.setOnMapClickListener(null);
                }
            });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }
                });

    }


    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                imageView.getLayoutParams().height =250;
                imageView.getLayoutParams().width = 250;
                imageView.setImageBitmap(takenImage);

            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = getPhotoFileUri();

        Uri fileProvider = FileProvider.getUriForFile(getContext(),BuildConfig.APPLICATION_ID + ".provider",photoFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(takePictureIntent, CAMERA_PERMISSION_CODE);
        }
    }

    public File getPhotoFileUri() {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + ".jpg";
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }




}
