package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectFaqComment;
import antidote.ObjectFaqCommentDao;

/**
 * Created by USER on 7/1/2015.
 */
public class FaqCommentController {

    private static ObjectFaqCommentDao getFaqCommentDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectFaqCommentDao();
    }

    public static void insert(Context context, ObjectFaqComment objectFaqComment) {
        getFaqCommentDao(context).insert(objectFaqComment);
    }

    public static List<ObjectFaqComment> getFaqCommentsByFaqId(Context context, Long faqid) {
        return getFaqCommentDao(context).queryRaw(" WHERE FAQS_ID = ? ORDER BY ID DESC", faqid + "");
    }

    public static void clearAllFaqComments(Context context) {
        getFaqCommentDao(context).deleteAll();
    }

    public static void clearAllFaqCommentsByFaqId(Context context, Long faqid) {
        List<ObjectFaqComment> list = getFaqCommentsByFaqId(context, faqid);
        for (ObjectFaqComment faqComment : list) {
            getFaqCommentDao(context).deleteByKey(faqComment.getId());
        }
    }
}
