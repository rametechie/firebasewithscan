package com.firebase.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.demo.MainActivity;
import com.firebase.demo.R;
import com.firebase.demo.adapter.viewholder.ProductListViewHolder;
import com.firebase.demo.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.MutableData;
import com.google.firebase.database.*;
import com.google.firebase.database.Transaction;

import java.util.zip.Inflater;

/**
 * Created by vn08fwk on 2/7/2018.
 */

public class ProductListAdapter extends FirebaseRecyclerAdapter<Product, ProductListViewHolder>{


    private static final String TAG = ProductListAdapter.class.getSimpleName();
    private String UserId;
    private Context mApplicationContext;
    private DatabaseReference DatabaseReference;

    private ProductListAdapter.OnActionListener OnActionListener;


    public ProductListAdapter(FirebaseRecyclerOptions<Product> options,
                              String userId,
                              DatabaseReference databaseReference,
                              OnActionListener  onActionListener ,
                              Context context ) {
        super(options);

        this.UserId = userId;
        this.DatabaseReference = databaseReference;
        this.OnActionListener = onActionListener;
        this.mApplicationContext = context;

        Log.d(TAG, "ProductListAdapterconstructor"+ options+ UserId);
    }



    @Override
    protected void onBindViewHolder(@NonNull ProductListViewHolder holder, int position, @NonNull Product model) {
        DatabaseReference = getRef(position);

        String postKey = DatabaseReference.getKey();


        holder.bindToPost(model, (View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                updateItem(true, postKey,model);
            }
        }), (View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                updateItem(false, postKey,model);
            }
        }), UserId);

    }

    @Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mApplicationContext);
        return new ProductListViewHolder(inflater.inflate(R.layout.layout_holder_product_list, parent, false));
    }

    public interface OnActionListener {
        void onAddedProduct( Product product);

        void onRemovedProduct(Product product);
    }


    public final void updateItem(final boolean add,  String key,Product product) {

        if(add)
        {

          //  OnActionListener.onAddedProduct(product);
        }else
        {
           // OnActionListener.onRemovedProduct(product);
        }


        DatabaseReference globalPostRef = DatabaseReference.child("rest/clubs/4969/products").child(key);
//        if(add) {
//            Integer count = product.count;
//            Integer value = count++;
//            product.count = value;
//
//        }else
//        {
//            Integer count = product.count;
//            Integer value = count--;
//            product.count = value;
//
//        }
        globalPostRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {



                Product p = (Product)mutableData.getValue(Product.class);
          if(add)
          {


          Integer   count = p.count;
          if (count == null) {
           count = 0;
           }
              Integer value = count++;
              p.count = value;


          }else
          {
              Integer   count = p.count;
              if (count == null) {
                  count = 0;
              }
              Integer value = count--;
              p.count = value;


          }

                  mutableData.setValue(p);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {


            }
        });


    }
}
