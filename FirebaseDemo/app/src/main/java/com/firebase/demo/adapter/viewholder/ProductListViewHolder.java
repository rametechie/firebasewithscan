package com.firebase.demo.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.demo.R;
import com.firebase.demo.model.Product;

/**
 * Created by vn08fwk on 2/7/2018.
 */

public class ProductListViewHolder extends RecyclerView.ViewHolder {



    private static final String TAG = "ProductListViewHolder";
    private TextView titleView;

    private TextView count;

    private ImageButton plusBtn;

    private ImageButton removeBtn;

    private TextView totlCount;

    private TextView priceValue;

    private ImageView icon;


    public ProductListViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.product_title);
        totlCount = itemView.findViewById(R.id.available_count);
        count = itemView.findViewById(R.id.count);
        plusBtn = itemView.findViewById(R.id.add);
        removeBtn = itemView.findViewById(R.id.remove);
        priceValue = itemView.findViewById(R.id.price_value);
        icon = itemView.findViewById(R.id.icon);
    }

    public final void bindToPost(Product product, View.OnClickListener plusClickListener, View.OnClickListener minusClickListener, String usetId) {

        titleView.setText(product.name);
        count.setText("0");
//        totlCount.setText("" + product.availableCount);
        plusBtn.setEnabled(true);

        Log.d(TAG, "product type" + product.category);


        if ("bulb".contains(product.category.toLowerCase())) {
            if ("sony".contains(product.category.toLowerCase())) {
                icon.setBackgroundResource(R.drawable.bulb1);
            } else {
                icon.setBackgroundResource(R.drawable.bulb2);
            }
        } else {
            icon.setBackgroundResource(R.drawable.tube1);
        }


        priceValue.setText("" + product.price);

        count.setText("" + product.count);

//            Integer countValue =0;
//            if(countValue != null) {
//                int value = countValue.intValue();
//                count.setText("" + value);
////                if(value > 0) {
//////                    int newCount = product.availableCount - value;
//////                    if(newCount <= 0) {
//////                        plusBtn.setEnabled(false);
//////                    } else {
//////                        plusBtn.setEnabled(true);
//////                    }
////
////                   // totlCount.setText("" + (product.availableCount - value));
////                } else {
////                   // totlCount.setText("" + product.availableCount);
////                }
//            }

            plusBtn.setOnClickListener(plusClickListener);
            removeBtn.setOnClickListener(minusClickListener);
        }

    }

