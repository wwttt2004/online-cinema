package com.nmbb.oplayer.ui.helper.Utils;

import com.nmbb.oplayer.ui.products.SubCategory;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 16.01.13
 * Time: 22:50
 * To change this template use File | Settings | File Templates.
 */
public class ListUtil {
    public static void sortSubCategoryObjects(ArrayList<SubCategory> objects){
        String[] names = new String[objects.size()];
        for(int i = 0; i < names.length; i ++){
            names[i] = objects.get(i).getSubCategoryName();
        }
        Arrays.sort(names);
        ArrayList<SubCategory> newObjects = new ArrayList<SubCategory>(){};

        for (String name : names){
            for(SubCategory object : objects){
                if(name.equals(object.getSubCategoryName())){
                    newObjects.add(object);
                    objects.remove(object);
                    break;
                }
            }
        }
        objects.clear();
        for(int i = newObjects.size() - 1; i >= 0; i--){
            objects.add(newObjects.get(i));
        }
    }
}
