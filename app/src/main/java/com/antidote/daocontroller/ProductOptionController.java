package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectProductOption;
import antidote.ObjectProductOptionDao;

/**
 * Created by USER on 6/17/2015.
 */
public class ProductOptionController {

    private static ObjectProductOptionDao getProductOptionDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectProductOptionDao();
    }

    public static void insertOrUpdate(Context context, ObjectProductOption objectProductOption) {
        List<ObjectProductOption> productOptions = ProductOptionController.getProductOptionById(context, objectProductOption.getId());
        if (productOptions.size() == 0) {
            getProductOptionDao(context).insert(objectProductOption);
        } else {
            ObjectProductOption productOption = productOptions.get(0);
            productOption.setProductID(objectProductOption.getProductID());
            productOption.setOptionID(objectProductOption.getOptionID());
            productOption.setOption_index(objectProductOption.getOption_index());
            productOption.setValue(objectProductOption.getValue());
            productOption.setRegularPrice(objectProductOption.getRegularPrice());
            productOption.setSalePrice(objectProductOption.getSalePrice());

            getProductOptionDao(context).update(productOption);
        }
    }

    public static List<ObjectProductOption> getAllProductOptions(Context context) {
        return getProductOptionDao(context).loadAll();
    }

    public static List<ObjectProductOption> getProductOptionById(Context context, Long id) {
        return getProductOptionDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static List<ObjectProductOption> getProductOptionByIdProduct(Context context, Long product_id) {
        return getProductOptionDao(context).queryRaw(" WHERE PRODUCT_ID = ? ORDER BY OPTION_INDEX ASC", product_id + "");
    }

    public static ObjectProductOption getObjectProductOptionById(Context context, Long id) {
        List<ObjectProductOption> list = getProductOptionDao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    public static void clearAllProductOptions(Context context) {
        getProductOptionDao(context).deleteAll();
    }
}
