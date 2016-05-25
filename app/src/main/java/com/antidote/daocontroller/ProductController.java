package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectProduct;
import antidote.ObjectProductDao;

/**
 * Created by Product on 5/25/2015.
 */
public class ProductController {

    private static ObjectProductDao getProductDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectProductDao();
    }

    public static void insertOrUpdate(Context context, ObjectProduct objectProduct) {
        List<ObjectProduct> products = ProductController.getProductById(context, objectProduct.getId());
        if (products.size() == 0) {
            getProductDao(context).insert(objectProduct);
        } else {
            ObjectProduct product = products.get(0);
            product.setCategoryID(objectProduct.getCategoryID());
            product.setName(objectProduct.getName());
            product.setSlug(objectProduct.getSlug());
            product.setDescription(objectProduct.getDescription());
            product.setRegularPrice(objectProduct.getRegularPrice());
            product.setSalePrice(objectProduct.getSalePrice());
            product.setPriceRange(objectProduct.getPriceRange());
            product.setIsActive(objectProduct.getIsActive());
            product.setImage(objectProduct.getImage());

            getProductDao(context).update(product);
        }
    }

    public static List<ObjectProduct> getAllProducts(Context context) {
        return getProductDao(context).loadAll();
    }

    public static List<ObjectProduct> getProductById(Context context, Long id) {
        return getProductDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static ObjectProduct getObjectProductById(Context context, Long id) {
        List<ObjectProduct> list = getProductDao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    public static String getImageProductById(Context context, Long id) {
        List<ObjectProduct> list = getProductDao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0).getImage();
        }
        else {
            return "";
        }
    }

    public static List<ObjectProduct> getProductByIdCategory(Context context, Long id_cate) {
        return getProductDao(context).queryRaw(" WHERE CATEGORY_ID = ?", id_cate + "");
    }

    public static List<ObjectProduct> getProductBySearchName(Context context, String name) {
        return getProductDao(context).queryRaw(" WHERE NAME LIKE ?", "%" + name + "%");
    }

    public static void clearAllProducts(Context context) {
        getProductDao(context).deleteAll();
    }
}
