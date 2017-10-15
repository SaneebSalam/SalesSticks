package com.salessticks.www.salessticks.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.salessticks.www.salessticks.AppController;
import com.salessticks.www.salessticks.BaseActivity;
import com.salessticks.www.salessticks.R;
import com.salessticks.www.salessticks.adapter.POJO_Customer;
import com.salessticks.www.salessticks.db.DB_Cart;
import com.salessticks.www.salessticks.util.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saneeb Salam
 * on 10/15/2017.
 */

public class Activity_Category extends BaseActivity {

    RecyclerView recyclerView, recycler_view_sub;
    ContentAdapter adapter;
    ContentAdapterSub adaptersub;
    JSONArray Listarray;
    JSONObject obj_catdata;
    public List<POJO_Customer> feedItems, feedItemsSub;
    ProgressBar layout_loading;
    int selectedPosition = 0;
    AlertDialog.Builder builder;
    DB_Cart db_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_category);

        db_cart = new DB_Cart(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recycler_view_sub = (RecyclerView) findViewById(R.id.my_recycler_view_sub);

        layout_loading = (ProgressBar) findViewById(R.id.progressBar);

        feedItems = new ArrayList<>();
        adapter = new ContentAdapter(recyclerView.getContext(), feedItems);
        recyclerView.setHasFixedSize(true);
        //        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        GetCategory();


        feedItemsSub = new ArrayList<>();
        adaptersub = new ContentAdapterSub(recycler_view_sub.getContext(), feedItemsSub);
        recycler_view_sub.setHasFixedSize(true);
        recycler_view_sub.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_sub.setLayoutManager(new GridLayoutManager(this, 3));
        recycler_view_sub.setAdapter(adaptersub);


    }


    public void GetCategory() {
        layout_loading.setVisibility(View.VISIBLE);

        AndroidNetworking.post(Keys.BaseURL + "api/Product/GetAllCategory/")
                //                .addBodyParameter("RouteID", "2")
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
                        parseJsonFeed_customer(response);

                        layout_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getErrorCode() + " :" + error.getErrorBody());

                        layout_loading.setVisibility(View.GONE);
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

            if (selectedPosition == position) {
                holder.layout.setSelected(true);
                holder.name.setTextColor(ContextCompat.getColor(Activity_Category.this, R.color.white));
            } else {
                holder.layout.setSelected(false);
                holder.name.setTextColor(ContextCompat.getColor(Activity_Category.this, R.color.colorPrimary));
            }


        }

        @Override
        public int getItemCount() {
            return feedItems.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RelativeLayout layout;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_category, parent, false));
            name = itemView.findViewById(R.id.name);
            layout = itemView.findViewById(R.id.layout);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //                    Intent intent = new Intent(Activity_Category.this, Activity_Sub_Category.class);
                    //                    intent.putExtra(Keys.productid, String.valueOf(feedItems.get(getAdapterPosition()).getId()));
                    //                    startActivity(intent);

                    GetSubCategory(String.valueOf(feedItems.get(getAdapterPosition()).getId()));

                    selectedPosition = getAdapterPosition();

                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    void parseJsonFeed_customer(JSONObject response) {
        feedItems.clear();

        POJO_Customer items_cat = new POJO_Customer();
        items_cat.setName("Recent");
        items_cat.setId("");
        feedItems.add(items_cat);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
            }
        });


        try {
            Listarray = response.getJSONArray("List");
            if (Listarray.length() != 0) {
                for (int i = 0; i < Listarray.length(); i++) {
                    obj_catdata = (JSONObject) Listarray.get(i);
                    items_cat = new POJO_Customer();
                    //                        items.setId(obj_catdata.getString("CustomerId"));
                    items_cat.setName(obj_catdata.getString("ProductCategoryName"));
                    items_cat.setId(obj_catdata.getString("ProductCategoryId"));


                    feedItems.add(items_cat);
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    adapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    public void GetSubCategory(String catid) {
        layout_loading.setVisibility(View.VISIBLE);

        AndroidNetworking.post(Keys.BaseURL + "api/Product/GetProductByCategoryId")
                .addBodyParameter("CategoryId", catid)
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

        }

        @Override
        public int getItemCount() {
            return feedItems.size();
        }
    }

    class ViewHolderSub extends RecyclerView.ViewHolder {
        TextView name;

        ViewHolderSub(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_subcategory, parent, false));
            name = itemView.findViewById(R.id.name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    builder = new AlertDialog.Builder(Activity_Category.this);

                    // Get the layout inflater
                    LayoutInflater inflater = Activity_Category.this.getLayoutInflater();
                    @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_cart, null);
                    TextView item_name = view.findViewById(R.id.item_name);
                    Button addtocart = view.findViewById(R.id.addtocart);

                    item_name.setText(feedItemsSub.get(getAdapterPosition()).getName());


                    builder
                            .setCancelable(true)
                            .setView(view);
                    final AlertDialog dialog = builder.create();
                    //        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                    dialog.show();


                    addtocart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db_cart.adddata(new POJO_Customer(feedItemsSub.get(getAdapterPosition()).getId(), feedItemsSub.get(getAdapterPosition()).getName()));
                            dialog.dismiss();
                        }
                    });

                }
            });
        }
    }

    void parseJsonFeed_subcat(JSONObject response) {
        feedItemsSub.clear();
        try {
            Listarray = response.getJSONArray("List");
            if (Listarray.length() != 0) {
                for (int i = 0; i < Listarray.length(); i++) {
                    obj_catdata = (JSONObject) Listarray.get(i);
                    POJO_Customer items = new POJO_Customer();
                    items.setId(obj_catdata.getString("CategoryId"));
                    items.setName(obj_catdata.getString("ProductName"));


                    feedItemsSub.add(items);
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


}
