package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectGroupTimer;
import antidote.ObjectGroupTimerDao;

/**
 * Created by USER on 6/18/2015.
 */
public class GroupTimerController {

    private static ObjectGroupTimerDao getGroupTimerDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectGroupTimerDao();
    }

    public static void insert(Context context, ObjectGroupTimer objectGroupTimer) {
        getGroupTimerDao(context).insert(objectGroupTimer);
    }

    public static List<ObjectGroupTimer> getAllGroupTimers(Context context) {
        return getGroupTimerDao(context).loadAll();
    }

    public static List<ObjectGroupTimer> getGroupTimerById(Context context, Long id) {
        return getGroupTimerDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static List<ObjectGroupTimer> getGroupTimerByIdGroup(Context context, Integer id_group) {
        return getGroupTimerDao(context).queryRaw(" WHERE GROUP_ID = ?", id_group + "");
    }

    public static ObjectGroupTimer getObjectGroupTimerById(Context context, Long id) {
        List<ObjectGroupTimer> list = getGroupTimerDao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    public static ObjectGroupTimer getCurrentGroupTimerByTime(Context context, Integer id_group, Long time) {
        List<ObjectGroupTimer> list = getGroupTimerDao(context).queryRaw(" WHERE GROUP_ID = ? AND TIME < ? ORDER BY TIME DESC LIMIT 1", id_group + "", time + "");
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    public static int getNumCurrentGroupTimerByTime(Context context, Integer id_group, Long time) {
        List<ObjectGroupTimer> list = getGroupTimerDao(context).queryRaw(" WHERE GROUP_ID = ? AND TIME < ?", id_group + "", time + "");
        return list.size();
    }

    public static ObjectGroupTimer getNextGroupTimerByTime(Context context, Integer id_group, Long time) {
        List<ObjectGroupTimer> list = getGroupTimerDao(context).queryRaw(" WHERE GROUP_ID = ? AND TIME > ? ORDER BY TIME ASC LIMIT 1", id_group + "", time + "");
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    public static void clearByIdGroup(Context context, Integer id_group) {
        List<ObjectGroupTimer> list = getGroupTimerByIdGroup(context, id_group);
        for (ObjectGroupTimer objectGroupTimer : list) {
            getGroupTimerDao(context).delete(objectGroupTimer);
        }
    }

    public static void clearAllGroupTimers(Context context) {
        getGroupTimerDao(context).deleteAll();
    }
}
