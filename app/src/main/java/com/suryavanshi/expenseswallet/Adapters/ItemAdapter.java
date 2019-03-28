package com.suryavanshi.expenseswallet.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suryavanshi.expenseswallet.DataModal.ItemCategory;
import com.suryavanshi.expenseswallet.DataModal.ItemTheme;
import com.suryavanshi.expenseswallet.DataModal.TransactionModel;
import com.suryavanshi.expenseswallet.R;
import com.suryavanshi.expenseswallet.Utilities.AppPreference;

import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private RecyclerViewClickListener mListener;
    private RealmList<TransactionModel> itemModels;
    Context context;
    private NumberFormat format;

    public ItemAdapter(Context context, RealmList<TransactionModel> itemModels, RecyclerViewClickListener listener) {
        this.context = context;
        this.itemModels = itemModels;
        mListener = listener;
        this.format = NumberFormat.getInstance();

    }

    public  void updateData(RealmList<TransactionModel>itemModalList){
        this.itemModels = itemModalList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cash_item_layout,viewGroup,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int index) {
        TransactionModel model = itemModels.get(index);
        assert model != null;
        ItemCategory category = model.getCategory();

        itemHolder.amount.setText("Rs. "+format.format(Integer.parseInt(model.getAmount())));
        itemHolder.time.setText(AppPreference.date);
        itemHolder.note.setText(model.getNote());

        if(category!=null) {
            itemHolder.catName.setTextColor(context.getResources().getColor(category.getTheme().getPrimaryColor()));
            itemHolder.catName.setText(category.getCatName());

            itemHolder.time.setTextColor(context.getResources().getColor(category.getTheme().getPrimaryColor()));

            itemHolder.circleImageView.setImageResource(category.getTheme().getPrimaryColor());
        }
        else{
            category = new ItemCategory();
            category.setCatName("Others");
            category.setTheme(new ItemTheme(R.color.primaryBlue,R.color.primaryDarkBlue));
            category.setStyle(R.style.AddExpenceBlueTheme);
            itemHolder.catName.setTextColor(context.getResources().getColor(category.getTheme().getPrimaryColor()));
            itemHolder.catName.setText(category.getCatName());

            itemHolder.time.setTextColor(context.getResources().getColor(category.getTheme().getPrimaryColor()));

            itemHolder.circleImageView.setImageResource(category.getTheme().getPrimaryColor());
        }
        /*if(item.getCategory().getTheme().getPrimaryColor() != R.color.colorPrimaryDark){
            GradientDrawable drawable = (GradientDrawable) itemHolder.itemImage.getBackground();
            drawable.setColor(Color.WHITE);
        }
*/


    }

    @Override
    public int getItemCount() {
        return itemModels.size(); /*HomeActivity.realmItemModelList.size();*/
    }



    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
     TextView catName,amount,time,note;
     ImageView itemImage;
     CircleImageView circleImageView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            catName = itemView.findViewById(R.id.item_category);
            amount = itemView.findViewById(R.id.item_amount);
            time = itemView.findViewById(R.id.time);
            note = itemView.findViewById(R.id.item_note);
            circleImageView = itemView.findViewById(R.id.icon);


        }

        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.icon)
            {
                mListener.onClickImg(getAdapterPosition());

            }
            else
            mListener.onClick(getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {

        void onClick( int position);
        void onClickImg( int position);
    }
}
