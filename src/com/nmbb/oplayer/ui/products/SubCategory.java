package com.nmbb.oplayer.ui.products;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 13.01.13
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public class SubCategory {

    private String CategoryName = "";
    private String Link = "";
    private long CategoryId = -1;

    public SubCategory(String categoryName){
        this.CategoryName = categoryName;
    }

    public long getId() {
        return CategoryId;
    }

    public void setId(long categoryId) {
        CategoryId = categoryId;
    }

    public String getSubCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
