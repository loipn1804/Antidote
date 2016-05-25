package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectComment;
import antidote.ObjectCommentDao;

/**
 * Created by user on 5/31/2015.
 */
public class CommentController {

    private static ObjectCommentDao getCommentDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectCommentDao();
    }

    public static void insertOrUpdate(Context context, ObjectComment objectComment) {
        List<ObjectComment> comments = CommentController.getCommentById(context, objectComment.getId());
        if (comments.size() == 0) {
            getCommentDao(context).insert(objectComment);
        } else {
            ObjectComment comment = comments.get(0);
            comment.setProductID(objectComment.getProductID());
            comment.setUserID(objectComment.getUserID());
            comment.setComment(objectComment.getComment());
            comment.setCreateDate(objectComment.getCreateDate());
            comment.setFirstName(objectComment.getFirstName());
            comment.setLastName(objectComment.getLastName());
            comment.setImage(objectComment.getImage());
            comment.setIdFacebook(objectComment.getIdFacebook());

            getCommentDao(context).update(comment);
        }
    }

    public static List<ObjectComment> getAllComments(Context context) {
        return getCommentDao(context).loadAll();
    }

    public static List<ObjectComment> getCommentById(Context context, Long id) {
        return getCommentDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static List<ObjectComment> getCommentByIdProduct(Context context, Long id_product) {
        return getCommentDao(context).queryRaw(" WHERE PRODUCT_ID = ? ORDER BY ID DESC", id_product + "");
    }

    public static void clearAllComments(Context context) {
        getCommentDao(context).deleteAll();
    }
}
