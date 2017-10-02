package com.salessticks.www.salessticks;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.salessticks.www.salessticks.util.Keys;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    public static SharedPreferences preferences;
    public static Editor editor;
    public static TelephonyManager telephonyManager = null;
    private static AppController mInstance;
    public static String locale = "en";
    Intent sharingIntent;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    String Place = "";

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    // Shared preferances
    public static void setsharedprefString(String key, String value) {
        editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getsharedprefString(String key) {
        return preferences.getString(key, "");
    }

    public static void setsharedprefInt(String key, Integer value) {
        editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static Integer getsharedprefInt(String key) {
        return preferences.getInt(key, 0);
    }

    // create folder


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        preferences = getSharedPreferences("salesstick", 0);

        telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
    }


    public String Getdistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        DecimalFormat form = new DecimalFormat("0.00");
        return ("" + form.format(dist));
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    /**
     * member function : isNetworkAvailable description : to check the network
     * status available or not
     */


    public static void setsharedprefBoolean(String key, boolean value) {
        editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // get locale

    public static boolean getsharedprefBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    // set locale

    public static String getLocale() {
        locale = Locale.getDefault().getLanguage();
        if (locale.equalsIgnoreCase("ar"))
            locale = "ar";
        return locale;

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    // to get the country id based on the user's sim
//	public static String getCountryId() {
//		return telephonyManager.getSimCountryIso();
//	}

    public void Share(Context context, String Placeurl) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Welcome to Mashaweery");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Placeurl);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


//    public String GetIMEI() {
//        return telephonyManager.getDeviceId();
//
//    }

    /*
     * member function : getRoundCornerBitmap parameter : src description :
     * change the images rounded corner
     */
    // Rounded corner image

    /*
     * member function : decodeFile parameter :f description : convert the file
	 * to bitmap
	 */


    public void Invitefriend(Context context) {
        sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Welcome to Mashaweery");

        sharingIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out Mashaweery for Android. Download it today from https://goo.gl/89XNHQ");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void showSettingsAlert(final Context mContext) {
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

    // to get the country id based on the user's sim
    public static String getCountryId() {
        return telephonyManager.getSimCountryIso();
    }

    static boolean checkPermission_WriteExternal(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestPermissionREAD_PHONE_STATE(Context context, Activity activity) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)) {

//            Toast.makeText(context, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public static String getdateformat(String date) {
        String oldFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String newFormat = "MMM dd, yyyy  hh:mm a";

        String formatedDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        formatedDate = timeFormat.format(myDate);

        return formatedDate;

    }

    public static String getdateformatOffers(String date) {
        String oldFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String newFormat = "MMM dd, yyyy";

        String formatedDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        formatedDate = timeFormat.format(myDate);

        return formatedDate;

    }


}


