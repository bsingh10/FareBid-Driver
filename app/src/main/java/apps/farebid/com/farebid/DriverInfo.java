package apps.farebid.com.farebid;



import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.location.LocationListener;


import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static apps.farebid.com.farebid.R.id.map;

public class DriverInfo extends AppCompatActivity implements OnMapReadyCallback,  ConnectionCallbacks, OnConnectionFailedListener,LocationListener {
    private FirebaseDatabase  mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mLastLocation;
    protected MapFragment mapFragment;
    //private GoogleMap mMap;
    protected static final String TAG = "Location  Fairbid";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);
        //get firebase database handle
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        //for location
        buildGoogleApiClient();

    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onMapReady(GoogleMap map) {
        Log.i(TAG,"onMapReady");
        if (mLastLocation !=null) {
            LatLng sydney = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            map.setMyLocationEnabled(true);
            map.setMapType(1);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        }
//        LatLng sydney = new LatLng(-33.867, 151.206);
//
//        //
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.i(TAG,String.valueOf(mLastLocation.getLatitude()));
            Log.i(TAG,String.valueOf(mLastLocation.getLongitude()));
        }
        mapFragment.getMapAsync(this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG,"onConnectionSuspended");
        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location){
        Log.i(TAG,location.toString());

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG,"onConnectionFailed");

    }
    /*
* Called by Google Play services if the connection to GoogleApiClient drops because of an
* error.
*/
    public void onDisconnected() {
        Log.i(TAG, "Disconnected");
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }
}
