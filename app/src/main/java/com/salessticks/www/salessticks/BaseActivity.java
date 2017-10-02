package com.salessticks.www.salessticks;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.salessticks.www.salessticks.util.GPSTracker;
import com.salessticks.www.salessticks.util.Keys;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/*
 * Class Name 	: BaseActivity
 * Description 	: For adapters
 * Author 		: Saneeb salam
 */

public abstract class BaseActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected static AppController application;
    protected Context context;
    protected Activity activity;
    //    ImageLoader imageLoader;
    protected Animation animMove_up, animRotate, slideleft, slideright;
    //    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    GPSTracker gps;
    String lat, lng;
    View layout;
    LayoutInflater inflater;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private static final long INTERVAL = 3000 * 10;
    private static final long FASTEST_INTERVAL = 3000 * 5;

    private static final int REQUEST_LOCATION = 0;
    public Typeface myTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = this;
        application = (AppController) getApplication();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

//        animMove_up = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.move_up);
//
//        animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.rotate_anim);
//
//        slideleft = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.anim_slide_in_left_slow);
//
//        slideright = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.anim_slide_in_right_slow);


        createLocationRequest();


//        if (!imageLoader.isInited()) {
//            imageLoader.init(ImageLoaderConfiguration
//                    .createDefault(getApplicationContext()));
//        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }


    public void showtoast(String toast) {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG)
                .show();
    }


    public final void moveNextPagewitoutfinish(Class<? extends Activity> class1) {
        startActivity(new Intent(this, class1));
//        overridePendingTransition(R.anim.anim_slide_in_left,
//                R.anim.anim_slide_out_left);

    }

    public final void moveNextPage(Class<? extends Activity> class1) {
        startActivity(new Intent(this, class1));

        finish();
//        overridePendingTransition(R.anim.anim_slide_in_left,
//                R.anim.anim_slide_out_left);

    }

    public final void moveNextPageback() {

        finish();
//        overridePendingTransition(R.anim.anim_slide_in_right,
//                R.anim.anim_slide_out_right);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            System.out.println("Lat..........: " + location.getLatitude());
            lat = Double.toString(location.getLatitude());
            lng = Double.toString(location.getLongitude());
            AppController.setsharedprefString(Keys.KEY_LAT, lat);
            AppController.setsharedprefString(Keys.KEY_LNG, lng);
            getlocationname();
            if (lat.isEmpty()) {
                if (gps.canGetLocation()) {
                    gps = new GPSTracker(context);
                    lat = Double.toString(gps.getLatitude());
                    lng = Double.toString(gps.getLongitude());

                    if (!lat.isEmpty() && !lat.equalsIgnoreCase("0.0")) {
                        AppController.setsharedprefString(Keys.KEY_LAT, lat);
                        AppController.setsharedprefString(Keys.KEY_LNG, lng);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (checkPermission_location()) {
            startLocationUpdates();
        } else {
            requestAppPermissions(new
                    String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }


    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                        this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        } catch (Exception ignored) {

        }
    }

    public void getlocationname() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            if (application.getPlace().isEmpty()) {
                addresses = geocoder.getFromLocation(Double.parseDouble(AppController
                        .getsharedprefString(Keys.KEY_LAT)), Double.parseDouble(AppController
                        .getsharedprefString(Keys.KEY_LNG)), 1);
                Address returnedAddress = addresses.get(0);
                String place = returnedAddress.getAddressLine(1);
                String country = returnedAddress.getAddressLine(2);
                AppController.setsharedprefString(Keys.LocationPlace, place);
                AppController.setsharedprefString(Keys.Locationcountry, country);

                application.setPlace(place);
            }

        } catch (IOException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    public boolean checkPermission_location() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

//    public void requestPermission_location() {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
////        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
////            Toast.makeText(context, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
////        } else {
////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
////        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_LOCATION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                    Snackbar.make(mLayout, "Permission Granted, Now you can access location data.", Snackbar.LENGTH_LONG).show();
//                    showtoast("Permission Granted, Now you can access location data.");
//                } else {
////                    Snackbar.make(mLayout, "Permission Denied, You cannot access location data.", Snackbar.LENGTH_LONG).show();
//                    showtoast("Permission Denied, You cannot access location data.");
//                }
//                break;
//        }
//    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int requestCode) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                ActivityCompat.requestPermissions(BaseActivity.this, requestedPermissions, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    public abstract void onPermissionsGranted(int requestCode);

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        }
    }


    public void alert_Success(String text) {
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

//                        overridePendingTransition(R.anim.anim_slide_in_right,
//                                R.anim.anim_slide_out_right);
                    }
                })
                .show();

    }

    public void alert_ok(String text) {
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }


}
