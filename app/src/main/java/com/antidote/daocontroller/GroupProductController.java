package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectGroupProduct;
import antidote.ObjectGroupProductDao;

/**
 * Created by user on 6/17/2015.
 */
public class GroupProductController {

    private static ObjectGroupProductDao getGroupProductDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectGroupProductDao();
    }

    public static void insertOrUpdate(Context context, ObjectGroupProduct objectGroupProduct) {
        List<ObjectGroupProduct> groupProducts = GroupProductController.getGroupProductById(context, objectGroupProduct.getId());
        if (groupProducts.size() == 0) {
            getGroupProductDao(context).insert(objectGroupProduct);
        } else {
            ObjectGroupProduct groupProduct = groupProducts.get(0);
            groupProduct.setGroupID(objectGroupProduct.getGroupID());
            groupProduct.setName(objectGroupProduct.getName());
            groupProduct.setProductID(objectGroupProduct.getProductID());
            groupProduct.setTimer(objectGroupProduct.getTimer());
            groupProduct.setImage(objectGroupProduct.getImage());
            groupProduct.setRepeat_day(objectGroupProduct.getRepeat_day());
            groupProduct.setIngredient(objectGroupProduct.getIngredient());
            groupProduct.setDescription(objectGroupProduct.getDescription());

            getGroupProductDao(context).update(groupProduct);
        }
    }

    public static void insert(Context context, ObjectGroupProduct objectGroupProduct) {
        getGroupProductDao(context).insert(objectGroupProduct);
    }

    public static List<ObjectGroupProduct> getAllGroupProducts(Context context) {
        return getGroupProductDao(context).loadAll();
    }

    public static List<ObjectGroupProduct> getGroupProductById(Context context, Long id) {
        return getGroupProductDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static List<ObjectGroupProduct> getGroupProductByIdGroup(Context context, Integer id_group) {
        return getGroupProductDao(context).queryRaw(" WHERE GROUP_ID = ?", id_group + "");
    }

    public static ObjectGroupProduct getObjectGroupProductById(Context context, Long id) {
        List<ObjectGroupProduct> list = getGroupProductDao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    public static void clearAllGroupProducts(Context context) {
        getGroupProductDao(context).deleteAll();
    }
}
