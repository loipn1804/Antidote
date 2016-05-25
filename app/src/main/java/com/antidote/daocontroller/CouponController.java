package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectCoupon;
import antidote.ObjectCouponDao;

/**
 * Created by USER on 6/25/2015.
 */
public class CouponController {

    private static ObjectCouponDao getCouponDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectCouponDao();
    }

    public static void insert(Context context, ObjectCoupon objectCoupon) {
        getCouponDao(context).insert(objectCoupon);
    }

    public static ObjectCoupon getCurrentCoupon(Context context) {
        List<ObjectCoupon> list = getCouponDao(context).loadAll();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void clearAllCoupons(Context context) {
        getCouponDao(context).deleteAll();
    }
}
