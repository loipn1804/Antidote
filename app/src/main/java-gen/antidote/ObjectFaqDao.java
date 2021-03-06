package antidote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import antidote.ObjectFaq;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table OBJECT_FAQ.
*/
public class ObjectFaqDao extends AbstractDao<ObjectFaq, Long> {

    public static final String TABLENAME = "OBJECT_FAQ";

    /**
     * Properties of entity ObjectFaq.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property UserID = new Property(1, Long.class, "userID", false, "USER_ID");
        public final static Property Question = new Property(2, String.class, "question", false, "QUESTION");
        public final static Property Answer = new Property(3, String.class, "answer", false, "ANSWER");
    };


    public ObjectFaqDao(DaoConfig config) {
        super(config);
    }
    
    public ObjectFaqDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'OBJECT_FAQ' (" + //
                "'ID' INTEGER PRIMARY KEY ," + // 0: id
                "'USER_ID' INTEGER," + // 1: userID
                "'QUESTION' TEXT," + // 2: question
                "'ANSWER' TEXT);"); // 3: answer
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'OBJECT_FAQ'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ObjectFaq entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long userID = entity.getUserID();
        if (userID != null) {
            stmt.bindLong(2, userID);
        }
 
        String question = entity.getQuestion();
        if (question != null) {
            stmt.bindString(3, question);
        }
 
        String answer = entity.getAnswer();
        if (answer != null) {
            stmt.bindString(4, answer);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ObjectFaq readEntity(Cursor cursor, int offset) {
        ObjectFaq entity = new ObjectFaq( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // userID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // question
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // answer
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ObjectFaq entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setQuestion(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAnswer(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ObjectFaq entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ObjectFaq entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
