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

import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.Dataholder> {

    RealmResults<TransactionModel> transactionModels;
    Context context;
    NumberFormat format;
    public ExpenseAdapter(Context context,RealmResults<TransactionModel> transactionModels){
        this.transactionModels = transactionModels;
        this.context = context;
        this.format = NumberFormat.getInstance(Locale.US);
    }

    @NonNull
    @Override
    public Dataholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_item_layout, parent, false);

        return new Dataholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Dataholder holder, int position) {
        TransactionModel model = transactionModels.get(position);
        assert model != null;
        ItemCategory category = model.getCategory();

        holder.amount.setText("Rs. "+format.format(Integer.parseInt(model.getAmount())));
        holder.time.setText(model.getId().substring(0,11).replace("_"," "));
        holder.note.setText(model.getNote());

        if(category!=null) {
            holder.catName.setTextColor(context.getResources().getColor(category.getTheme().getPrimaryColor()));
            holder.catName.setText(category.getCatName());

            holder.time.setTextColor(context.getResources().getColor(category.getTheme().getPrimaryColor()));

            holder.circleImageView.setImageResource(category.getTheme().getPrimaryColor());
        }
        else{
            category = new ItemCategory();
            category.setCatName("Others");
            category.setTheme(new ItemTheme(R.color.primaryBlue,R.color.primaryDarkBlue));
            category.setStyle(R.style.AddExpenceBlueTheme);
            holder.catName.setTextColor(context.getResources().getColor(category.getTheme().getPrimaryColor()));
            holder.catName.setText(category.getCatName());

            holder.time.setTextColor(context.getResources().getColor(category.getTheme().getPrimaryColor()));

            holder.circleImageView.setImageResource(category.getTheme().getPrimaryColor());
        }
    }

    @Override
    public int getItemCount() {
        return transactionModels.size();
    }

    public class Dataholder extends RecyclerView.ViewHolder {
        TextView catName,amount,time,note;
        ImageView itemImage;
        CircleImageView circleImageView;
        public Dataholder(View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.item_category);
            amount = itemView.findViewById(R.id.item_amount);
            time = itemView.findViewById(R.id.time);
            note = itemView.findViewById(R.id.item_note);
            circleImageView = itemView.findViewById(R.id.icon);
        }
    }
}
