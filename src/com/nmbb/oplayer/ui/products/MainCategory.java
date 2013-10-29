package com.nmbb.oplayer.ui.products;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 3/12/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainCategory {
    String mName;
    ArrayList<Category> mCategoriesList;

    public MainCategory(){}

    public MainCategory(String name){
        this.mName = name;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public ArrayList<Category> getCategoriesList() {
        return this.mCategoriesList;
    }

    public void setCategoriesList(ArrayList<Category> mCategoriesList) {
        this.mCategoriesList = mCategoriesList;
    }
}
