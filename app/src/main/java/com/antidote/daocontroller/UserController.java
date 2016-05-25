package com.antidote.daocontroller;

import android.content.Context;
import com.antidote.application.AntidoteApplication;
import java.util.List;
import antidote.ObjectUser;
import antidote.ObjectUserDao;

public class UserController {

    private static ObjectUserDao getUserDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectUserDao();
    }

    public static void insertOrUpdate(Context context, ObjectUser objectUser) {
        List<ObjectUser> users = UserController.getUserById(context, objectUser.getId());
        if (users.size() == 0) {
            getUserDao(context).insert(objectUser);
        } else {
            ObjectUser user = users.get(0);
            user.setEmail(objectUser.getEmail());
            user.setIdFacebook(objectUser.getIdFacebook());
            user.setFirstName(objectUser.getFirstName());
            user.setLastName(objectUser.getLastName());
            user.setPhone(objectUser.getPhone());
            user.setAddress(objectUser.getAddress());
            user.setCity(objectUser.getCity());
            user.setCountry(objectUser.getCountry());
            user.setPostCode(objectUser.getPostCode());
            user.setIsActive(objectUser.getIsActive());
            user.setImage(objectUser.getImage());

            getUserDao(context).update(user);
        }
    }

    public static List<ObjectUser> getAllUsers(Context context) {
        return getUserDao(context).loadAll();
    }

    public static boolean isLogin(Context context) {
        List<ObjectUser> users = getUserDao(context).loadAll();
        if (users.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static List<ObjectUser> getUserById(Context context, Long id) {
        return getUserDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static ObjectUser getCurrentUser(Context context) {
        List<ObjectUser> list = getUserDao(context).loadAll();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void clearAllUsers(Context context) {
        getUserDao(context).deleteAll();
    }
}
