package com.firebase.demo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.demo.MainActivity;
import com.firebase.demo.R;

import com.firebase.demo.ShoppingActivity;
import com.firebase.demo.adapter.ProductListAdapter;

import com.firebase.demo.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class ProductListFragment extends Fragment implements ProductListAdapter.OnActionListener {

    private static final String TAG = ProductListFragment.class.getSimpleName();
    private RecyclerView productRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabaseRef;
    private ProductListAdapter mAdapter;
    private FirebaseAuth mAuth;
    private ProductListAdapter.OnActionListener mListener;
    private ProductListFragment.OnActionListener mOrderListCallbacks;


    private Button goToCart;
    private MainActivity mActivity;
    private ImageView goToScan;

    public ProductListFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity app = (AppCompatActivity) getActivity();
        app.getSupportActionBar().setIcon(null);
        app.getSupportActionBar().setTitle("Cart");
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        Log.d(TAG, "ProductListFragment");
        String strtext = getArguments().getString("productid");
        Log.d(TAG, "product Scanned" +strtext);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        productRecyclerview = (RecyclerView)view.findViewById(R.id.product_list);
        productRecyclerview.setHasFixedSize(true);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();



       Query query = mDatabaseRef.child("rest/clubs/4969/products").orderByChild("productid").equalTo(strtext);;
       // Query query = mDatabaseRef.child("rest/clubs/4969/products");


        FirebaseRecyclerOptions<Product> options =  new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();;


        mAuth = FirebaseAuth.getInstance();
     //   mAuth.getCurrentUser().getUid()


        mAdapter = new ProductListAdapter(options,"ramesh" , mDatabaseRef, mListener,getContext());
        productRecyclerview.setLayoutManager(linearLayoutManager);
        productRecyclerview.setAdapter(mAdapter);

        goToCart = (Button) view.findViewById(R.id.check_out);
        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderListCallbacks.openCart();

            }
        });
        goToScan = (ImageView) view.findViewById(R.id.scan);
        goToScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOrderListCallbacks.openScan();

            }
        });


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

            mActivity = (MainActivity) activity;

            mOrderListCallbacks = (MainActivity) activity;



        } catch (ClassCastException e) {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onAddedProduct(Product product) {

    }

    @Override
    public void onRemovedProduct(Product product) {

    }



    public interface OnActionListener {
        int onAddedProduct(Product productAdded);

        int onRemovedProduct(Product productRemoved);

        void openCart();
        void openScan();

    }



}
