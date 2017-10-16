package com.salessticks.www.salessticks.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.salessticks.www.salessticks.BaseActivity;
import com.salessticks.www.salessticks.R;
import com.salessticks.www.salessticks.adapter.POJO_Customer;
import com.salessticks.www.salessticks.db.DB_Cart;
import com.salessticks.www.salessticks.util.Keys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saneeb Salam
 * on 10/16/2017.
 */

public class Activity_Cart extends BaseActivity {


    RecyclerView recyclerView;
    ContentAdapter adaptercart;
    public List<POJO_Customer> feedItems;
    POJO_Customer items;
    ProgressBar layout_loading;
    TextView customer_name, grandtotal;
    DB_Cart db_cart;
    Double GrandTotal = 0.00;
    String CustomerName, CustomerID;
    List<POJO_Customer> db_Cart_List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cart);

        CustomerName = getIntent().getStringExtra(Keys.customername);
        CustomerID = getIntent().getStringExtra(Keys.customerid);

        getSupportActionBar().setTitle("My Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customer_name = (TextView) findViewById(R.id.customername);
        grandtotal = (TextView) findViewById(R.id.grandtotal);
        //        layout_loading = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        customer_name.setText(CustomerName);

        db_cart = new DB_Cart(this);


        feedItems = new ArrayList<>();
        adaptercart = new ContentAdapter(recyclerView.getContext(), feedItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adaptercart);

        GetCart();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return false;
        }
    }


    public void GetCart() {
//        layout_loading.setVisibility(View.VISIBLE);

        db_Cart_List = db_cart.getAlldetails(CustomerID);
        GrandTotal = 0.00;
        feedItems.clear();

        for (int i = db_Cart_List.size() - 1; i >= 0; i--) {
            items = new POJO_Customer();
            items.setId(db_Cart_List.get(i).getId());
            items.setName(db_Cart_List.get(i).getName());
            items.setQuantity(db_Cart_List.get(i).getQuantity());
            items.setPrice(db_Cart_List.get(i).getPrice());

            GrandTotal = GrandTotal + db_Cart_List.get(i).getQuantity() * db_Cart_List.get(i).getPrice();
            feedItems.add(items);
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adaptercart.notifyDataSetChanged();
            }
        });

        grandtotal.setText(" TOTAL: RS " + GrandTotal);
//        layout_loading.setVisibility(View.GONE);

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
            holder.quantity.setText(String.valueOf(feedItems.get(position).getQuantity()));
            holder.price.setText(String.valueOf(feedItems.get(position).getPrice()));
            holder.totalprice.setText(String.valueOf(feedItems.get(position).getQuantity() * feedItems.get(position).getPrice()));

        }

        @Override
        public int getItemCount() {
            return feedItems.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, price, totalprice, edit, remove;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_cart, parent, false));
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            totalprice = itemView.findViewById(R.id.totalprice);
            edit = itemView.findViewById(R.id.edit);
            remove = itemView.findViewById(R.id.remove);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db_cart.removeitem(feedItems.get(getAdapterPosition()).getId());

                    GetCart();
//                    adaptercart.notifyDataSetChanged();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Alert_Edit(getAdapterPosition());
                }
            });

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


    public void Alert_Edit(final int position) {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(feedItems.get(position).getQuantity()));
        input.setSelection(String.valueOf(feedItems.get(position).getQuantity()).length());
        new AlertDialog.Builder(this)
                .setMessage("Enter Quantity")
                .setView(input)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db_cart.Update_Item(new POJO_Customer(CustomerID, feedItems.get(position).getId(), feedItems.get(position).getName(),
                                Integer.parseInt(input.getText().toString()), feedItems.get(position).getPrice()));
                        dialog.dismiss();
                        showtoast("Cart updated");
                        GetCart();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }
}