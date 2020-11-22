package com.example.matchup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    FusedLocationProviderClient fusedLocationProviderClient;
    Marker markYouAreHere, blueMarker;
    Location currentLocation;
    private GoogleMap mMap;
    LocationManager locationManager;
    LatLng newTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);*/

        //HARD-CODED CAMERA LOCATION
        LatLng userLocation = new LatLng(40.5218356,-74.4675954);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //EXTERNAL FUNCTIONS
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (blueMarker == null) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Let's Meet Here");
                    //markerOptions.snippet(newTitle.latitude + " " + newTitle.longitude);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    markerOptions.draggable(true);
                    blueMarker = mMap.addMarker(markerOptions);
                    newTitle = blueMarker.getPosition();
                }
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //blueMarker = marker;
                newTitle = marker.getPosition();
                marker.setTitle("Let's Meet Here");
                marker.setSnippet(newTitle.latitude + " " + newTitle.longitude);

                Toast.makeText(MapActivity.this, "Currently at: " + newTitle.latitude + ", " + newTitle.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnThisLocation = (Button) findViewById(R.id.btnThisLocation);
        btnThisLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GETS ADDRESS
                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(newTitle.latitude, newTitle.longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    //Toast.makeText(MapActivity.this, address, Toast.LENGTH_SHORT).show();

                    //Returns address to chat
                    Intent intent = new Intent(MapActivity.this, MessageActivity.class);
                    intent.putExtra("address", address);
                    setResult(RESULT_OK, intent);
                    finish();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}