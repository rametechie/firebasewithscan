package com.firebase.demo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.demo.R;
import com.firebase.demo.adapter.ProductListAdapter;
import com.firebase.demo.model.Product;
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
public class OrderListFragment extends Fragment  {

    private static final String TAG = OrderListFragment.class.getSimpleName();

    public OrderListFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity app = (AppCompatActivity) getActivity();
        app.getSupportActionBar().setIcon(null);
        app.getSupportActionBar().setTitle("CheckOut");
        View view = inflater.inflate(R.layout.fragment_product, container, false);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onStop() {
        super.onStop();

    }


}
