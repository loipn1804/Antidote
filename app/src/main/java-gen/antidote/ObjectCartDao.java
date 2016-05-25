package antidote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import antidote.ObjectCart;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table OBJECT_CART.
*/
public class ObjectCartDao extends AbstractDao<ObjectCart, Long> {

    public static final String TABLENAME = "OBJECT_CART";

    /**
     * Properties of entity ObjectCart.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property ProductID = new Property(1, Long.class, "productID", false, "PRODUCT_ID");
        public final static Property ProductName = new Property(2, String.class, "productName", false, "PRODUCT_NAME");
        public final static Property Quantity = new Property(3, Integer.class, "quantity", false, "QUANTITY");
        public final static Property RegularPrice = new Property(4, Float.class, "regularPrice", false, "REGULAR_PRICE");
        public final static Property SalePrice = new Property(5, Float.class, "salePrice", false, "SALE_PRICE");
        public final static Property IdProductOption = new Property(6, Long.class, "idProductOption", false, "ID_PRODUCT_OPTION");
        public final static Property NameProductOption = new Property(7, String.class, "nameProductOption", false, "NAME_PRODUCT_OPTION");
    };


    public ObjectCartDao(DaoConfig config) {
        super(config);
    }
    
    public ObjectCartDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'OBJECT_CART' (" + //
                "'ID' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'PRODUCT_ID' INTEGER," + // 1: productID
                "'PRODUCT_NAME' TEXT," + // 2: productName
                "'QUANTITY' INTEGER," + // 3: quantity
                "'REGULAR_PRICE' REAL," + // 4: regularPrice
                "'SALE_PRICE' REAL," + // 5: salePrice
                "'ID_PRODUCT_OPTION' INTEGER," + // 6: idProductOption
                "'NAME_PRODUCT_OPTION' TEXT);"); // 7: nameProductOption
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'OBJECT_CART'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ObjectCart entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long productID = entity.getProductID();
        if (productID != null) {
            stmt.bindLong(2, productID);
        }
 
        String productName = entity.getProductName();
        if (productName != null) {
            stmt.bindString(3, productName);
        }
 
        Integer quantity = entity.getQuantity();
        if (quantity != null) {
            stmt.bindLong(4, quantity);
        }
 
        Float regularPrice = entity.getRegularPrice();
        if (regularPrice != null) {
            stmt.bindDouble(5, regularPrice);
        }
 
        Float salePrice = entity.getSalePrice();
        if (salePrice != null) {
            stmt.bindDouble(6, salePrice);
        }
 
        Long idProductOption = entity.getIdProductOption();
        if (idProductOption != null) {
            stmt.bindLong(7, idProductOption);
        }
 
        String nameProductOption = entity.getNameProductOption();
        if (nameProductOption != null) {
            stmt.bindString(8, nameProductOption);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ObjectCart readEntity(Cursor cursor, int offset) {
        ObjectCart entity = new ObjectCart( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // productID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // productName
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // quantity
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // regularPrice
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5), // salePrice
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // idProductOption
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // nameProductOption
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ObjectCart entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setProductID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setProductName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setQuantity(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setRegularPrice(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setSalePrice(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
        entity.setIdProductOption(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setNameProductOption(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ObjectCart entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ObjectCart entity) {
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
