package antidote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import antidote.ObjectUserDao;
import antidote.ObjectProductDao;
import antidote.ObjectCategoryDao;
import antidote.ObjectCountryDao;
import antidote.ObjectCommentDao;
import antidote.ObjectOptionDao;
import antidote.ObjectProductOptionDao;
import antidote.ObjectGroupProductDao;
import antidote.ObjectGroupTimerDao;
import antidote.ObjectCartDao;
import antidote.ObjectOrderDeliveryDao;
import antidote.ObjectBannerDao;
import antidote.ObjectOrderHistoryDao;
import antidote.ObjectCouponDao;
import antidote.ObjectFaqDao;
import antidote.ObjectFaqCommentDao;
import antidote.ObjectFaqV2Dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        ObjectUserDao.createTable(db, ifNotExists);
        ObjectProductDao.createTable(db, ifNotExists);
        ObjectCategoryDao.createTable(db, ifNotExists);
        ObjectCountryDao.createTable(db, ifNotExists);
        ObjectCommentDao.createTable(db, ifNotExists);
        ObjectOptionDao.createTable(db, ifNotExists);
        ObjectProductOptionDao.createTable(db, ifNotExists);
        ObjectGroupProductDao.createTable(db, ifNotExists);
        ObjectGroupTimerDao.createTable(db, ifNotExists);
        ObjectCartDao.createTable(db, ifNotExists);
        ObjectOrderDeliveryDao.createTable(db, ifNotExists);
        ObjectBannerDao.createTable(db, ifNotExists);
        ObjectOrderHistoryDao.createTable(db, ifNotExists);
        ObjectCouponDao.createTable(db, ifNotExists);
        ObjectFaqDao.createTable(db, ifNotExists);
        ObjectFaqCommentDao.createTable(db, ifNotExists);
        ObjectFaqV2Dao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        ObjectUserDao.dropTable(db, ifExists);
        ObjectProductDao.dropTable(db, ifExists);
        ObjectCategoryDao.dropTable(db, ifExists);
        ObjectCountryDao.dropTable(db, ifExists);
        ObjectCommentDao.dropTable(db, ifExists);
        ObjectOptionDao.dropTable(db, ifExists);
        ObjectProductOptionDao.dropTable(db, ifExists);
        ObjectGroupProductDao.dropTable(db, ifExists);
        ObjectGroupTimerDao.dropTable(db, ifExists);
        ObjectCartDao.dropTable(db, ifExists);
        ObjectOrderDeliveryDao.dropTable(db, ifExists);
        ObjectBannerDao.dropTable(db, ifExists);
        ObjectOrderHistoryDao.dropTable(db, ifExists);
        ObjectCouponDao.dropTable(db, ifExists);
        ObjectFaqDao.dropTable(db, ifExists);
        ObjectFaqCommentDao.dropTable(db, ifExists);
        ObjectFaqV2Dao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(ObjectUserDao.class);
        registerDaoClass(ObjectProductDao.class);
        registerDaoClass(ObjectCategoryDao.class);
        registerDaoClass(ObjectCountryDao.class);
        registerDaoClass(ObjectCommentDao.class);
        registerDaoClass(ObjectOptionDao.class);
        registerDaoClass(ObjectProductOptionDao.class);
        registerDaoClass(ObjectGroupProductDao.class);
        registerDaoClass(ObjectGroupTimerDao.class);
        registerDaoClass(ObjectCartDao.class);
        registerDaoClass(ObjectOrderDeliveryDao.class);
        registerDaoClass(ObjectBannerDao.class);
        registerDaoClass(ObjectOrderHistoryDao.class);
        registerDaoClass(ObjectCouponDao.class);
        registerDaoClass(ObjectFaqDao.class);
        registerDaoClass(ObjectFaqCommentDao.class);
        registerDaoClass(ObjectFaqV2Dao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
