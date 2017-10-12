package com.salessticks.www.salessticks.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.salessticks.www.salessticks.AppController;
import com.salessticks.www.salessticks.BaseActivity;
import com.salessticks.www.salessticks.R;
import com.salessticks.www.salessticks.adapter.POJO_Customer;
import com.salessticks.www.salessticks.util.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity_Sub_Category extends BaseActivity {

    RecyclerView recyclerView;
    ContentAdapterSub adaptersub;
    JSONArray Listarray;
    JSONObject obj_catdata;
    String CategoryId;
    public  List<POJO_Customer> feedItems;
    ProgressBar layout_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_category);

        CategoryId = getIntent().getStringExtra(Keys.productid);
        layout_loading = (ProgressBar) findViewById(R.id.progressBar);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        feedItems = new ArrayList<>();
        adaptersub = new ContentAdapterSub(recyclerView.getContext(), feedItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adaptersub);

        GetSubCategory();


    }


    public void GetSubCategory() {
        layout_loading.setVisibility(View.VISIBLE);

        AndroidNetworking.post(Keys.BaseURL + "api/Product/GetProductByCategoryId")
                .addBodyParameter("CategoryId", CategoryId)
                .addBodyParameter("Token", AppController.getsharedprefString(Keys.token))
//                .addQueryParameter("Date", "2017-10-06T23:43:50.7161287-07:00")
                .setTag("GetCustomer")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("response: " + response.toString());
                        parseJsonFeed_subcat(response);

                        layout_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getErrorCode() + " :" + error.getErrorBody());

                        layout_loading.setVisibility(View.GONE);
                    }
                });
    }

    private class ContentAdapterSub extends RecyclerView.Adapter<ViewHolderSub> {

        private List<POJO_Customer> feedItems;
        Context mContext;

        ContentAdapterSub(Context context, List<POJO_Customer> feedItems) {
            this.feedItems = feedItems;
            this.mContext = context;
        }

        @Override
        public ViewHolderSub onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolderSub(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(final ViewHolderSub holder, int position) {

//            holder.frame.setBackgroundColor(feedItems.get(position).getColor());
            holder.name.setText(feedItems.get(position).getName());
            holder.subtext.setText(feedItems.get(position).getRoutarea());

        }

        @Override
        public int getItemCount() {
            return feedItems.size();
        }
    }

    class ViewHolderSub extends RecyclerView.ViewHolder {
        TextView name, subtext;

        ViewHolderSub(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_today, parent, false));
            name = itemView.findViewById(R.id.name);
            subtext = itemView.findViewById(R.id.subtext);


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

    void parseJsonFeed_subcat(JSONObject response) {
        feedItems.clear();
        try {
            Listarray = response.getJSONArray("List");
            if (Listarray.length() != 0) {
                for (int i = 0; i < Listarray.length(); i++) {
                    obj_catdata = (JSONObject) Listarray.get(i);
                    POJO_Customer items = new POJO_Customer();
//                        items.setId(obj_catdata.getString("CustomerId"));
                    items.setName(obj_catdata.getString("ProductName"));


                    feedItems.add(items);
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    adaptersub.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode) {

    }

}
