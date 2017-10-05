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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.salessticks.www.salessticks.R;
import com.salessticks.www.salessticks.adapter.POJO_history;
import com.salessticks.www.salessticks.util.Keys;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Fragment_today extends Fragment {

    RecyclerView recyclerView;
    public static List<POJO_history> feedItems;
    ContentAdapter adapter;

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

        feedItems = new ArrayList<>();

        adapter = new ContentAdapter(recyclerView.getContext(), feedItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(adapter);

        GetHistory();


        return rootView;
    }

    private class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<POJO_history> feedItems;
        Context mContext;
        private int lastPosition = -1;

        ContentAdapter(Context context, List<POJO_history> feedItems) {
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


        }

        @Override
        public int getItemCount() {
            return feedItems.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, time, date, amount, status;
//        CardView cardview;
        LinearLayout mainlayout;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_today, parent, false));
//            frame = (FrameLayout) itemView.findViewById(frame);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            date = (TextView) itemView.findViewById(R.id.date);
            amount = (TextView) itemView.findViewById(R.id.amount);
            status = (TextView) itemView.findViewById(R.id.status);
//            cardview = (CardView) itemView.findViewById(R.id.cardview);
            mainlayout = (LinearLayout) itemView.findViewById(R.id.mainlayout);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(Activity_Categories.this, Activity_Vouchers.class);
//                    intent.putExtra(Keys.CatID, String.valueOf(feedItems.get(getAdapterPosition()).getId()));
//                    intent.putExtra(Keys.CardName, feedItems.get(getAdapterPosition()).getName());
//                    Activity_Categories.this.startActivity(intent);
//                    overridePendingTransition(R.anim.anim_slide_in_left,
//                            R.anim.anim_slide_out_left);
//
//                }
//            });
        }
    }

    public void GetHistory() {
//        layout_loading.setVisibility(View.VISIBLE);

        AndroidNetworking.post(Keys.BaseURL + "api/Route/GetSalePersonCustomers")
                .addBodyParameter("salePersonId", "6")
                .addBodyParameter("Date", "2017-10-04T11:23:15.9835306-07:00")
                .addBodyParameter("Token", "EAAAAIZElWEdXXyLTuuju61CoLrAd5QJcDETp58YsmuKMmAUEfa6nzRTk2aJUAFZ0EvcO5zS7pac+PRYWBJ0jSBibgp3DsAgyN48rJNqgRPXUzgWpFADj0k3\\/KFZrvYL\\/pSQ2GXRPotB2FRw\\/DlXtu6+64TY9PX4+kQYsz3R664wXdtAdcwZNh+BGUKV6Tod8h3cOl4AIuXvVfZ7HBPnYrlx7fVfxs+ML4GzNpuyWEfmFe1AlYJZB6liILEWeCKnd2\\/QvvQUwqbl5To+Dmnbj+0HtQKk81z+5pL4Lzjfu+NyeUmUlxtoSTjjR387sSPD66qeIVrgvAJj1ULt0+hbUCIlqJ0=")
                .addBodyParameter("DeviceId", "1")

                .setTag("getticketdetails")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("response: " + response.toString());


//                        layout_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getErrorCode() + " :" + error.getErrorBody());

//                        layout_loading.setVisibility(View.GONE);
                    }
                });
    }
}
