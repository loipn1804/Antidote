package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectCart;
import antidote.ObjectCartDao;

/**
 * Created by user on 6/20/2015.
 */
public class CartController {

    private static ObjectCartDao getCartDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectCartDao();
    }

    public static void update(Context context, ObjectCart objectCart) {
        List<ObjectCart> carts = CartController.getCartById(context, objectCart.getId());
        if (carts.size() > 0) {
            ObjectCart cart = carts.get(0);
            cart.setProductID(objectCart.getProductID());
            cart.setProductName(objectCart.getProductName());
            cart.setQuantity(objectCart.getQuantity());
            cart.setRegularPrice(objectCart.getRegularPrice());
            cart.setSalePrice(objectCart.getSalePrice());
            cart.setIdProductOption(objectCart.getIdProductOption());

            getCartDao(context).update(cart);
        }
    }

    public static void insert(Context context, ObjectCart objectCart) {
        getCartDao(context).insert(objectCart);
    }

    public static List<ObjectCart> getAllCarts(Context context) {
        return getCartDao(context).loadAll();
    }

    public static List<ObjectCart> getCartById(Context context, Long id) {
        return getCartDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static ObjectCart getObjectCartByIdProductAndIdProductOption(Context context, Long id_product, Long id_product_option) {
        List<ObjectCart> list = getCartDao(context).queryRaw(" WHERE PRODUCT_ID = ? AND ID_PRODUCT_OPTION = ?", id_product + "", id_product_option + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void deleteCartById(Context context, Long id) {
        getCartDao(context).deleteByKey(id);
    }

    public static void clearAllCarts(Context context) {
        getCartDao(context).deleteAll();
    }
}
