package com.firebase.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.firebase.demo.adapter.RecyclerViewAdapter;
import com.firebase.demo.model.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private String value;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<Products> list = new ArrayList<>();

    RecyclerView recyclerView;

    RecyclerViewAdapter adapter ;

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "rest/clubs/4969/products";
    private ImageView goToScan;
    private Products products;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("productkey");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        goToScan = (ImageView) findViewById(R.id.scan);
        goToScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, ShoppingActivity.class);

                startActivity(intent);
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        progressDialog = new ProgressDialog(CartActivity.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    products = dataSnapshot.getValue(Products.class);

                    Log.d("products.getProductid()",products.getProductid() + value);
                    if(products.getProductid().equals(value) ) {
                        Log.d("products.getProductid()",value);
                        list.add(products);

                        Log.d("products.getProductid()","size" + list.size());
                    }

                }

                if (adapter == null) {
                    Log.d("products.getProductid()","adapter null" );
                    adapter = new RecyclerViewAdapter(CartActivity.this, list);
                    recyclerView.setAdapter(adapter);
                } else if (adapter != null) {
                    Log.d("products.getProductid()","adapter not null" );
                    adapter.setItems(list);
                    adapter.notifyDataSetChanged();
                }


                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });



    }

}