package com.salessticks.www.salessticks.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.salessticks.www.salessticks.AppController;
import com.salessticks.www.salessticks.BaseActivity;
import com.salessticks.www.salessticks.R;
import com.salessticks.www.salessticks.util.Keys;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saneeb Salam
 * on 10/1/2017.
 */

public class Activity_login extends BaseActivity {

    Button login;
    ProgressBar layout_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        getSupportActionBar().hide();

        login = (Button) findViewById(R.id.login);
        layout_loading = (ProgressBar) findViewById(R.id.progressBar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                moveNextPage(MainActivity.class);
                Test();
//                Login();
            }
        });

    }

    void Test() {

        layout_loading.setVisibility(View.VISIBLE);

        AndroidNetworking.post(Keys.BaseURL + "api/Employee/login")
                .addBodyParameter("UserName", "amal")
                .addBodyParameter("Password", "123")
                .setTag("logon")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        layout_loading.setVisibility(View.GONE);

                        try {
                            if (response.getString("ErrorCode").equalsIgnoreCase("1001")) {
                                AppController.setsharedprefString(Keys.token, response.getString("Token"));
                                AppController.setsharedprefString(Keys.userId, response.getString("Id"));
                                AppController.setsharedprefString(Keys.userName, response.getString("DisplayName"));

                                moveNextPage(MainActivity.class);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        layout_loading.setVisibility(View.GONE);
                        System.out.println("Error: " + anError.getErrorBody());
                    }
                })
        ;

    }

    void Login() {

        layout_loading.setVisibility(View.VISIBLE);

        AndroidNetworking.post(Keys.BaseURL + "api/Employee/login")
                .addBodyParameter("UserName", "amal")
                .addBodyParameter("Password", "123")

                .setTag("logon")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        layout_loading.setVisibility(View.GONE);

                        try {
                            AppController.setsharedprefString(Keys.token, response.getString("Token"));
                            AppController.setsharedprefString(Keys.userId, response.getString("Id"));
                            AppController.setsharedprefString(Keys.userName, response.getString("DisplayName"));

                            moveNextPage(MainActivity.class);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        layout_loading.setVisibility(View.GONE);
                        System.out.println("Error: " + anError.getErrorBody());
                    }
                })
        ;

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }


}

