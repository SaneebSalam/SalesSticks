/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.salessticks.www.salessticks.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.salessticks.www.salessticks.R;


public class MainActivity extends AppCompatActivity {

    Fragment fragment_today = null, fragment_salesrout = null, fragment_settings = null;
    FrameLayout today_layout, salesroute_layout, settings_layout;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        today_layout = (FrameLayout) findViewById(R.id.todaysroute_layout);
        salesroute_layout = (FrameLayout) findViewById(R.id.salesroute_layout);
        settings_layout = (FrameLayout) findViewById(R.id.settings_layout);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        transaction = getSupportFragmentManager().beginTransaction();

                        switch (item.getItemId()) {

                            case R.id.todaysroute:

                                today_layout.setVisibility(View.VISIBLE);
                                salesroute_layout.setVisibility(View.GONE);
                                settings_layout.setVisibility(View.GONE);

                                selectedFragment = fragment_today;
                                break;
                            case R.id.salesroute:

                                today_layout.setVisibility(View.GONE);
                                salesroute_layout.setVisibility(View.VISIBLE);
                                settings_layout.setVisibility(View.GONE);

                                if (fragment_salesrout != null)
                                    selectedFragment = fragment_salesrout;
                                else {
                                    fragment_salesrout = ItemTwoFragment.newInstance();
                                    selectedFragment = fragment_salesrout;

                                    transaction.replace(R.id.salesroute_layout, selectedFragment);
                                }

                                break;
                            case R.id.settings:
                                today_layout.setVisibility(View.GONE);
                                salesroute_layout.setVisibility(View.GONE);
                                settings_layout.setVisibility(View.VISIBLE);

                                if (fragment_settings != null)
                                    selectedFragment = fragment_settings;
                                else {
                                    fragment_settings = ItemThreeFragment.newInstance();
                                    selectedFragment = fragment_settings;

                                    transaction.replace(R.id.settings_layout, selectedFragment);
                                }

                                break;
                        }
                        transaction.show(selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        fragment_today = Fragment_today.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.todaysroute_layout, fragment_today);
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }
}
