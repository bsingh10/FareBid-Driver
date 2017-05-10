package apps.farebid.com.farebid;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener{




    //for firebase
    private FirebaseDatabase  mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mRequestDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //private GoogleMap mMap;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    // The geographical location where the device is currently located.
    private LatLng mCurrentLocation;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    protected SupportMapFragment mapFragment;
    private CameraPosition mCameraPosition;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;


    protected static final String TAG = "Fairbid";

    //user credentials
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private String mUsername;
    private String mUID;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL_IN_MILLISECONDS, 0, locationListener);

                    }

                }

            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();


        mDatabaseReference = mFirebaseDatabase.getReference();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG,"onAuthStateChanged+");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                   onSignedInInitialize(user.getDisplayName(), user.getUid());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
                Log.i(TAG,"onAuthStateChanged-");
            }
        };

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mRequestDatabaseReference = mFirebaseDatabase.getReference().child("request");

        ChildEventListener mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RideRequest riderequest = dataSnapshot.getValue(RideRequest.class);
                if (riderequest != null && riderequest.getRidestatus().compareTo("R") == 0) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date date = null;
                    try {
                        date = format.parse(riderequest.getRequesttime().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(date);
                    Date cdt = new Date();
                    Long dif = cdt.getTime() - date.getTime();
                    if (dif < 300000) {
                        Log.i("bhupendra  date diff", String.valueOf(dif));

                        //mMessageAdapter.add(friendlyMessage);
                        //if (riderequest !=null && riderequest.getRidestatus().compareTo("requested")==0){
//                Log.i("DataValue Called "," I dont know how many time ");
//                Log.i("DataValue Key",dataSnapshot.getKey());
//                Log.i("dataValue",riderequest.getRidestatus());
//                Log.i("dataValue",riderequest.getDestinationAddress());
//                Log.i("dataValue",riderequest.getPassengerid());
//                Log.i("dataValue",riderequest.getSourceAddress());
//                Log.i("dataValue",riderequest.getRidedatetime());
//                //}

                        Dialog d = CreateDialog(riderequest, dataSnapshot.getKey());
                        d.show();
                    }
                }

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mRequestDatabaseReference.addChildEventListener(mChildEventListener);
        buildGoogleApiClient();
    }
    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mCurrentLocation);
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    @Override
    public void onMapReady(GoogleMap map) {
        Log.i(TAG, "+onMapReady");
        mMap = map;
/*        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(mCurrentLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation,15));
                //mMap.animateCamera(CameraUpdateFactory.zoomIn());

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL_IN_MILLISECONDS, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL_IN_MILLISECONDS, 0, locationListener);

                Log.i(TAG, " requesting location update locationManager");
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation !=null)
                {
                    LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(userLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                }
            }


        }
        */
        Log.i(TAG, "-onMapReady");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSignedInInitialize(String username, String uid) {
        Log.i(TAG,"onSignedInInitialize+");
        mUsername = username;
        mUID=uid;
        Driver lDriver= new Driver();
        lDriver.setDname(mUsername);
        try {
            mDatabaseReference.child("driver").child(mUID).setValue(lDriver) ;
        }
        catch (Exception e) {
            Log.i(TAG,e.getMessage());
        }


        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        Log.i(TAG,"onSignedInInitialize-");
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;

}
    public void onLogin(View view){
        Intent i = new Intent(this, DriverInfo.class);
        startActivity(i);
    }

    public Dialog CreateDialog(RideRequest riderequest , String key) {

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.ride_request);
        dialog.setTitle("Bid Alert");

        // set the custom dialog components - text, image and button
        TextView rid=(TextView)dialog.findViewById(R.id.lblrequestID);
        rid.setText(key);
        TextView ts=(TextView)dialog.findViewById(R.id.txtSource);
        ts.setText(riderequest.getSourceAddress());
        TextView td=(TextView)dialog.findViewById(R.id.txtDestination);
        td.setText(riderequest.getDestinationAddress());
        TextView tr=(TextView)dialog.findViewById(R.id.txtRequestTime);
        tr.setText(riderequest.getRidedatetime());
        Button ignoreButton = (Button) dialog.findViewById(R.id.btnIgnore);
        // if button is clicked, close the custom dialog
        ignoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.i(TAG,"bid Ignore");
            }
        });
        Button bidButton = (Button) dialog.findViewById(R.id.btnPlaceBid);
        // if button is clicked, close the custom dialog
        bidButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et=(EditText)dialog.findViewById(R.id.txtBidValue);
                if (et.length()==0)
                    Toast.makeText(MainActivity.this,"Bib Amount can be blank",Toast.LENGTH_SHORT).show();
                TextView rid=(TextView)dialog.findViewById(R.id.lblrequestID);
                Log.i(TAG,"bid done"+rid.getText());
                BidObject bid= new BidObject();
                bid.setBidvalue(et.getText().toString());
                bid.setDriverid(mUID);
                bid.setBidstatus("biddingInProgress");
                bid.setRequestid(rid.getText().toString());
                DatabaseReference mMessagesRequestReference=mFirebaseDatabase.getReference().child("bid");
                DatabaseReference temp=mMessagesRequestReference.push();
                temp.setValue(bid);
                dialog.dismiss();
            }
        });

//        dialog.show();



        return dialog;
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        // Get the layout inflater
//        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.ride_request, null))
//                // Add action buttons
//                .setPositiveButton(R.string.placeBid , new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                    }
//                })
//                .setNegativeButton(R.string.ignoreBid, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //DialogFragment.this.getDialog().cancel();
//                        //LoginDialogFragment.this.getDialog().cancel();
//                    }
//                });
//
//        Dialog dlg= builder.create();
//
//
//        TextView ts=(TextView)dlg.findViewById(R.id.txtSource);
//        ts.setText(riderequest.getSourceAddress());
//        TextView td=(TextView)dlg.findViewById(R.id.txtDestination);
//        td.setText(riderequest.getDestinationAddress());
//        TextView tr=(TextView)dlg.findViewById(R.id.txtRequestTime);
//        tr.setText(riderequest.getRidedatetime());
//        return dlg;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
    public void onDisconnected() {
        Log.i(TAG, "Disconnected");
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
    public void onConnected(Bundle connectionHint) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, location.toString());
        if (mMap !=null) {
            mMap.clear();
            mCurrentLocation= new LatLng(location.getLatitude(),location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(mCurrentLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 15));
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            Log.i(TAG, "Location update removed");

        }
        //txtOutput.setText(location.toString());

        //mLatitudeText.setText(String.valueOf(location.getLatitude()));
        //mLongitudeText.setText(String.valueOf(location.getLongitude()));
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "+buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.i(TAG, "-buildGoogleApiClient");
    }

}
