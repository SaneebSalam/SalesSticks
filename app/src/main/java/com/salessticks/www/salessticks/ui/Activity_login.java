package com.salessticks.www.salessticks.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

import org.json.JSONObject;

/**
 * Created by Saneeb Salam on 10/1/2017.
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


        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                moveNextPage(MainActivity.class);
                Login();
            }
        });

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
                        moveNextPage(MainActivity.class);
                    }

                    @Override
                    public void onError(ANError anError) {
                        layout_loading.setVisibility(View.GONE);
                        System.out.println("Error: "+anError.getErrorBody());
                    }
                })
        ;

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }


}

