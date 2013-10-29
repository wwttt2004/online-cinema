package com.nmbb.oplayer.ui.products;

import com.nmbb.oplayer.ui.search.SearchKeywordsConverter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 13.01.13
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class Category{
    private String CategoryName = "";
    private ArrayList<SubCategory> SubCategories = new ArrayList<SubCategory>();
    private int mId = -1;
    private String mUrl = "";

    public String getCategoryName() {
        return CategoryName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public ArrayList<SubCategory> getSubCategories() {
        return SubCategories;
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories) {
        SubCategories = subCategories;
    }

    public Category(String name) {
        this.setCategoryName(name);
    }

    public Category(){}

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
