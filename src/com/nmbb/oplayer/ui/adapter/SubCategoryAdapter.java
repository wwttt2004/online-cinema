package com.nmbb.oplayer.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.nmbb.oplayer.ui.products.Category;
import com.nmbb.oplayer.ui.products.SubCategory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 3/11/13
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubCategoryAdapter implements SpinnerAdapter {

    private ArrayList<SubCategory> mObjects;
    private Context mContext;

    public SubCategoryAdapter(Context context, ArrayList<SubCategory> objects){
         mObjects = objects;
        mContext = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        label.setText(mObjects.get(position).getSubCategoryName());

        return label;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return mObjects.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int position) {
        return position;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasStableIds() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(mObjects.get(position).getSubCategoryName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getViewTypeCount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isEmpty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
