package com.firebase.demo.adapter;

/**
 * Created by Juned on 8/9/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.demo.R;
import com.firebase.demo.model.Products;
import com.firebase.demo.model.StudentDetails;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Products> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, List<Products> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    public void setItems(List<Products> TempList) {
        this.MainImageUploadInfoList = TempList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_holder_product_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Products product = MainImageUploadInfoList.get(position);

        holder.titleView.setText(product.name);


    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;

        private TextView count;

        private ImageButton plusBtn;

        private ImageButton removeBtn;

        private TextView totlCount;

        private TextView priceValue;

        private ImageView icon;

        public ViewHolder(View itemView) {

            super(itemView);

            titleView = itemView.findViewById(R.id.product_title);
            totlCount = itemView.findViewById(R.id.available_count);
            count = itemView.findViewById(R.id.count);
            plusBtn = itemView.findViewById(R.id.add);
            removeBtn = itemView.findViewById(R.id.remove);
            priceValue = itemView.findViewById(R.id.price_value);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}