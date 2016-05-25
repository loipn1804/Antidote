package antidote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import antidote.ObjectProduct;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table OBJECT_PRODUCT.
*/
public class ObjectProductDao extends AbstractDao<ObjectProduct, Long> {

    public static final String TABLENAME = "OBJECT_PRODUCT";

    /**
     * Properties of entity ObjectProduct.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property CategoryID = new Property(1, Long.class, "categoryID", false, "CATEGORY_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Slug = new Property(3, String.class, "slug", false, "SLUG");
        public final static Property Description = new Property(4, String.class, "description", false, "DESCRIPTION");
        public final static Property RegularPrice = new Property(5, Float.class, "regularPrice", false, "REGULAR_PRICE");
        public final static Property SalePrice = new Property(6, Float.class, "salePrice", false, "SALE_PRICE");
        public final static Property PriceRange = new Property(7, String.class, "priceRange", false, "PRICE_RANGE");
        public final static Property IsActive = new Property(8, String.class, "isActive", false, "IS_ACTIVE");
        public final static Property Image = new Property(9, String.class, "image", false, "IMAGE");
    };


    public ObjectProductDao(DaoConfig config) {
        super(config);
    }
    
    public ObjectProductDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'OBJECT_PRODUCT' (" + //
                "'ID' INTEGER PRIMARY KEY ," + // 0: id
                "'CATEGORY_ID' INTEGER," + // 1: categoryID
                "'NAME' TEXT," + // 2: name
                "'SLUG' TEXT," + // 3: slug
                "'DESCRIPTION' TEXT," + // 4: description
                "'REGULAR_PRICE' REAL," + // 5: regularPrice
                "'SALE_PRICE' REAL," + // 6: salePrice
                "'PRICE_RANGE' TEXT," + // 7: priceRange
                "'IS_ACTIVE' TEXT," + // 8: isActive
                "'IMAGE' TEXT);"); // 9: image
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'OBJECT_PRODUCT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ObjectProduct entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long categoryID = entity.getCategoryID();
        if (categoryID != null) {
            stmt.bindLong(2, categoryID);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String slug = entity.getSlug();
        if (slug != null) {
            stmt.bindString(4, slug);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(5, description);
        }
 
        Float regularPrice = entity.getRegularPrice();
        if (regularPrice != null) {
            stmt.bindDouble(6, regularPrice);
        }
 
        Float salePrice = entity.getSalePrice();
        if (salePrice != null) {
            stmt.bindDouble(7, salePrice);
        }
 
        String priceRange = entity.getPriceRange();
        if (priceRange != null) {
            stmt.bindString(8, priceRange);
        }
 
        String isActive = entity.getIsActive();
        if (isActive != null) {
            stmt.bindString(9, isActive);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(10, image);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ObjectProduct readEntity(Cursor cursor, int offset) {
        ObjectProduct entity = new ObjectProduct( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // categoryID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // slug
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // description
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5), // regularPrice
            cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6), // salePrice
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // priceRange
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // isActive
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // image
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ObjectProduct entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCategoryID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSlug(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDescription(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRegularPrice(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
        entity.setSalePrice(cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6));
        entity.setPriceRange(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIsActive(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setImage(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ObjectProduct entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ObjectProduct entity) {
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