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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.salessticks.www.salessticks.AppController;
import com.salessticks.www.salessticks.R;
import com.salessticks.www.salessticks.adapter.POJO_Customer;
import com.salessticks.www.salessticks.util.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Fragment_today extends Fragment {

    RecyclerView recyclerView;
    ImageView logo;
    TextView name;
    public static List<POJO_Customer> feedItems;
    ContentAdapter adapter;
    JSONArray Listarray;
    JSONObject obj_catdata;

    public static Fragment_today newInstance() {
        Fragment_today fragment = new Fragment_today();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        recyclerView = rootView.findViewById(R.id.my_recycler_view);
        name = rootView.findViewById(R.id.name);
        logo = rootView.findViewById(R.id.logo);

        name.setText(AppController.getsharedprefString(Keys.userName));

//        Glide.with(getActivity()).load(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher))
//                .apply(RequestOptions.circleCropTransform()).into(logo);

        feedItems = new ArrayList<>();
        adapter = new ContentAdapter(recyclerView.getContext(), feedItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(adapter);

        GetRouteByDate();

        return rootView;
    }


    public void GetRouteByDate() {
//        layout_loading.setVisibility(View.VISIBLE);
                AndroidNetworking.post(Keys.BaseURL + "api/Route/GetSalePersonRouteByDate")
//                .addBodyParameter("salePersonId", AppController.getsharedprefString(Keys.userId))
                .addBodyParameter("Date", AppController.getCurrentDate())
                .addBodyParameter("Token", AppController.getsharedprefString(Keys.token))
//                .addBodyParameter("DeviceId", "1")

                .setTag("GetRouteByDate")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("response: " + response.toString());
                        parseJsonFeed_rout(response);

//                        layout_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getErrorCode() + " :" + error.getErrorBody());

//                        layout_loading.setVisibility(View.GONE);
                    }
                });
    }

    private class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<POJO_Customer> feedItems;
        Context mContext;

        ContentAdapter(Context context, List<POJO_Customer> feedItems) {
            this.feedItems = feedItems;
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

//            holder.frame.setBackgroundColor(feedItems.get(position).getColor());
            holder.name.setText(feedItems.get(position).getName());
            holder.subtext.setText(feedItems.get(position).getRoutarea());

        }

        @Override
        public int getItemCount() {
            return feedItems.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, subtext;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_today, parent, false));
            name = itemView.findViewById(R.id.name);
            subtext = itemView.findViewById(R.id.subtext);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), Activity_customers.class);
//                    intent.putExtra(Keys.routeid, String.valueOf(feedItems.get(getAdapterPosition()).getId()));
                    getActivity().startActivity(intent);

                }
            });
        }
    }

    void parseJsonFeed_rout(JSONObject response) {
        feedItems.clear();
        try {
            Listarray = response.getJSONArray("List");
            if (Listarray.length() != 0) {
                for (int i = 0; i < Listarray.length(); i++) {
                    obj_catdata = (JSONObject) Listarray.get(i);
                    POJO_Customer items = new POJO_Customer();
//                        items.setId(obj_catdata.getString("CustomerId"));
                    items.setName(obj_catdata.getString("RouteName"));
                    items.setRoutarea(obj_catdata.getString("RouteArea"));

                    feedItems.add(items);
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    adapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
