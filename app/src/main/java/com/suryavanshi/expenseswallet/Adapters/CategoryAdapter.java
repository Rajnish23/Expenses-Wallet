package com.suryavanshi.expenseswallet.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suryavanshi.expenseswallet.DataModal.ItemCategory;
import com.suryavanshi.expenseswallet.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private RecyclerViewClickListener mListener;
    private ArrayList<ItemCategory> categories;

    public CategoryAdapter(ArrayList<ItemCategory> categories ,RecyclerViewClickListener listener){
        this.categories = categories;
        this.mListener = listener;
    }


    public  void updateData(ArrayList<ItemCategory> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.category_item_layout, parent, false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCategory category = categories.get(position);
        holder.itemColor.setImageResource(category.getTheme().getPrimaryColor());
        holder.itemName.setText(category.getCatName());
    }

    @Override
    public int getItemCount() {
        return categories.size();/* CategoryActivity.categoryList.size();*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView itemColor;
        TextView itemName;
        private RecyclerViewClickListener mListener;
        public ViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);
            mListener = clickListener;
            itemView.setOnClickListener(this);
            itemColor = (CircleImageView)itemView.findViewById(R.id.item_color);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }
}
