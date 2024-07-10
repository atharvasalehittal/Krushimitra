package com.mitra.krushi.krushimitra;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.FileObserver;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    ZoomControls zoom;
    Button markBt,GeoLocation;
    Double MyLatitude = null;
    Double MyLongitude = null;
    private GoogleApiClient GoogleApiClient;
    private LocationRequest LocationRequest;
    private Task<Location> location;
    protected final static String TAG = "MapsActivity";
    List<Address> addresses;
    String postalCode,city,address,state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //I added these
        GoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //I added these
        LocationRequest = new LocationRequest();
        LocationRequest.setInterval(15*1000);
        LocationRequest.setFastestInterval(5*1000);
        LocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //I added these all methods down below
        zoom = (ZoomControls) findViewById(R.id.btZoom);
        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        markBt = (Button) findViewById(R.id.getLocation);
        markBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng MyLocation = new LatLng(MyLatitude, MyLongitude);
                mMap.addMarker(new MarkerOptions().position(MyLocation).title("MY Location"));
                getAddress();

                startActivity(new Intent(MapsActivity.this,SignUp.class).putExtra("name",postalCode));
            }
        });

        GeoLocation = (Button) findViewById(R.id.btSearch);
        GeoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText) findViewById(R.id.locationEntry);
                String search = searchText.getText().toString();
                if (search != null && !search.equals("")) {
                    List<Address> addressList = null;
                    Geocoder geoC = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geoC.getFromLocationName(search, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address add = addressList.get(0);
                    LatLng latLng = new LatLng(add.getLatitude(), add.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My Position"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    MyLatitude = add.getLatitude();
                    MyLongitude = add.getLongitude();
                    getAddress();
                }
            }
        });
    }

    void getAddress()
    {
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            addresses = geocoder.getFromLocation(MyLatitude, MyLongitude, 1);
            postalCode = addresses.get(0).getPostalCode();
            city = addresses.get(0).getLocality();
            address = addresses.get(0).getAddressLine(0);
            state = addresses.get(0).getAdminArea();
            //country = addresses.get(0).getCountryName();
            //knownName = addresses.get(0).getFeatureName();
            //Log.e("Location ", postalCode + " " + city + " " + state + " " + address);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //I added this if and else
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]
                        {
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    //i added this method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case MY_PERMISSION_FINE_LOCATION:
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"This application requires location permissions to be granted",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocation();
    }

    private void requestLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(LocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },
                    Looper.myLooper());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.i(TAG,"Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Log.i(TAG,"Connection Failed: "+ connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        MyLatitude = location.getLatitude();
        MyLongitude = location.getLongitude();
        //Log.i(TAG,"Latitude " + Double.toString(MyLatitude));
        //Log.i(TAG,"Longitude " + Double.toString(MyLongitude));
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(LocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },
                    Looper.myLooper());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(GoogleApiClient.isConnected())
        {
            requestLocation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleApiClient.disconnect();
    }
}
