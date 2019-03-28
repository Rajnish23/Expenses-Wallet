package com.suryavanshi.expenseswallet.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suryavanshi.expenseswallet.Activities.AddCategoryActivity;
import com.suryavanshi.expenseswallet.Adapters.CategoryAdapter;
import com.suryavanshi.expenseswallet.DataModal.ItemCategory;
import com.suryavanshi.expenseswallet.R;
import com.suryavanshi.expenseswallet.Utilities.AppPreference;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {


    private TextView addCategory;
    private CategoryAdapter categoryAdapter;
    private ArrayList<ItemCategory> categoryList;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View categoryView = inflater.inflate(R.layout.fragment_category, container, false);
         categoryList = AppPreference.realmHelper.retrieveCategory();
        RecyclerView categoryRecycler = categoryView.findViewById(R.id.category_recycler);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRecycler.setHasFixedSize(true);
        CategoryAdapter.RecyclerViewClickListener listener = (view,position) ->{
            Intent categoryAddIntent = new Intent(getActivity(),AddCategoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryName",categoryList.get(position).getCatName());
            categoryAddIntent.putExtras(bundle);
            startActivity(categoryAddIntent);
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        };
         categoryAdapter = new CategoryAdapter(categoryList, listener);
        categoryRecycler.setAdapter(categoryAdapter);

        addCategory = (TextView)categoryView.findViewById(R.id.add_category);
        addCategory.setOnClickListener(this);
        return categoryView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_category:
                Intent categoryAddIntent = new Intent(getActivity(),AddCategoryActivity.class);
                startActivity(categoryAddIntent);
                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        categoryList = AppPreference.realmHelper.retrieveCategory();
        categoryAdapter.updateData(AppPreference.realmHelper.retrieveCategory());
    }
}
