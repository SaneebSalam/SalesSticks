package com.salessticks.www.salessticks.util;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;


public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location = null; // location
    public double latitude; // latitude
    public double longitude; // longitude
    public String current_location, country, place = "";

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50; // 50 meters

    // The minimum time between updates in milliseconds
    // private static final long MIN_TIME_BW_UPDATES = 30000; // 3 minute
    private static final long MIN_TIME_BW_UPDATES = 30000; // 3 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);
        getLocation();
    }

    public Location getLocation() {
        try {
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                System.out.println("no network");
            } else {
                this.canGetLocation = true;

                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);

                String provider = locationManager.getBestProvider(criteria,
                        true);

                if (location == null) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            System.out
                                    .println(".........location from gps..........." + latitude + ", " + longitude);
//                            getlocationname();
                        } else {
                            locationManager.requestLocationUpdates(provider,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                            location = locationManager
                                    .getLastKnownLocation(provider);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

//                                getlocationname();
                                System.out.println(".........location from "
                                        + provider + "...........");
                            } else {

                                locationManager.requestLocationUpdates(
                                        LocationManager.NETWORK_PROVIDER,
                                        MIN_TIME_BW_UPDATES,
                                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();

//                                    getlocationname();
                                    System.out
                                            .println(".........location from network..........." + latitude + ", " + longitude);
                                }

                            }
                        }
                    }
                }

            }
            stopUsingGPS();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


//    public void getlocationname() {
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            if (getPlace().isEmpty()) {
//                addresses = geocoder.getFromLocation(Double.parseDouble(AppController
//                        .getsharedprefString(Keys.KEY_LAT)), Double.parseDouble(AppController
//                        .getsharedprefString(Keys.KEY_LNG)), 1);
//                Address returnedAddress = addresses.get(0);
//                place = returnedAddress.getAddressLine(1);
//                country = returnedAddress.getAddressLine(2);
//                AppController.setsharedprefString(Keys.LocationPlace, place);
//                AppController.setsharedprefString(Keys.Locationcountry, country);
//                setCountry(country);
//                setPlace(place);
//            }
//
//        } catch (IOException e) {
//
//            current_location = "";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

//    public String getCountry() {
//        return country;
//    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // System.out.println("onLocationChanged.............");
    }

    @Override
    public void onProviderDisabled(String provider) {
        // System.out.println("onProviderDisabled.............");
    }

    @Override
    public void onProviderEnabled(String provider) {
        // System.out.println("onProviderEnabled.............");
        location = null;
        getLocation();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // System.out.println("onStatusChanged.............");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}