package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectCategory;
import antidote.ObjectCategoryDao;

/**
 * Created by USER on 5/26/2015.
 */
public class CategoryController {

    private static ObjectCategoryDao getCategoryDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectCategoryDao();
    }

    public static void insertOrUpdate(Context context, ObjectCategory objectCategory) {
        List<ObjectCategory> categories = CategoryController.getCategoryById(context, objectCategory.getId());
        if (categories.size() == 0) {
            getCategoryDao(context).insert(objectCategory);
        } else {
            ObjectCategory category = categories.get(0);
            category.setName(objectCategory.getName());
            category.setSlug(objectCategory.getSlug());
            category.setParentID(objectCategory.getParentID());
            category.setShortDescription(objectCategory.getShortDescription());
            category.setFullDescription(objectCategory.getFullDescription());
            category.setThumbnail(objectCategory.getThumbnail());
            category.setImage(objectCategory.getImage());
            category.setIsActive(objectCategory.getIsActive());

            getCategoryDao(context).update(category);
        }
    }

    public static List<ObjectCategory> getAllCategories(Context context) {
        return getCategoryDao(context).loadAll();
    }

    public static List<ObjectCategory> getCategoryById(Context context, Long id) {
        return getCategoryDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static ObjectCategory getObjectCategoryById(Context context, Long id) {
        List<ObjectCategory> list = getCategoryDao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    public static void clearAllCategories(Context context) {
        getCategoryDao(context).deleteAll();
    }
}
