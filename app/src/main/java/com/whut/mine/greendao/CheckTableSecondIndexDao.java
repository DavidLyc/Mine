package com.whut.mine.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.whut.mine.entity.CheckTableSecondIndex;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CheckTableSecondIndex_T".
*/
public class CheckTableSecondIndexDao extends AbstractDao<CheckTableSecondIndex, Long> {

    public static final String TABLENAME = "CheckTableSecondIndex_T";

    /**
     * Properties of entity CheckTableSecondIndex.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property SecondIndexID = new Property(0, Long.class, "secondIndexID", true, "SecondIndexID");
        public final static Property FirstIndexID = new Property(1, int.class, "firstIndexID", false, "FirstIndexID");
        public final static Property SerialNum = new Property(2, String.class, "serialNum", false, "SerialNum");
        public final static Property SecondIndexName = new Property(3, String.class, "secondIndexName", false, "SecondIndexName");
        public final static Property SecondIndexDemo = new Property(4, String.class, "secondIndexDemo", false, "SecondIndexDemo");
        public final static Property HiddenDangerCategory = new Property(5, String.class, "hiddenDangerCategory", false, "HiddenDangerCategory");
        public final static Property Deleted = new Property(6, String.class, "deleted", false, "Deleted");
        public final static Property AddTime = new Property(7, String.class, "addTime", false, "AddTime");
        public final static Property DeleteTime = new Property(8, String.class, "deleteTime", false, "DeleteTime");
    }


    public CheckTableSecondIndexDao(DaoConfig config) {
        super(config);
    }
    
    public CheckTableSecondIndexDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CheckTableSecondIndex_T\" (" + //
                "\"SecondIndexID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: secondIndexID
                "\"FirstIndexID\" INTEGER NOT NULL ," + // 1: firstIndexID
                "\"SerialNum\" TEXT," + // 2: serialNum
                "\"SecondIndexName\" TEXT," + // 3: secondIndexName
                "\"SecondIndexDemo\" TEXT," + // 4: secondIndexDemo
                "\"HiddenDangerCategory\" TEXT," + // 5: hiddenDangerCategory
                "\"Deleted\" TEXT," + // 6: deleted
                "\"AddTime\" TEXT," + // 7: addTime
                "\"DeleteTime\" TEXT);"); // 8: deleteTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CheckTableSecondIndex_T\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CheckTableSecondIndex entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getSecondIndexID());
        stmt.bindLong(2, entity.getFirstIndexID());
 
        String serialNum = entity.getSerialNum();
        if (serialNum != null) {
            stmt.bindString(3, serialNum);
        }
 
        String secondIndexName = entity.getSecondIndexName();
        if (secondIndexName != null) {
            stmt.bindString(4, secondIndexName);
        }
 
        String secondIndexDemo = entity.getSecondIndexDemo();
        if (secondIndexDemo != null) {
            stmt.bindString(5, secondIndexDemo);
        }
 
        String hiddenDangerCategory = entity.getHiddenDangerCategory();
        if (hiddenDangerCategory != null) {
            stmt.bindString(6, hiddenDangerCategory);
        }
 
        String deleted = entity.getDeleted();
        if (deleted != null) {
            stmt.bindString(7, deleted);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(8, addTime);
        }
 
        String deleteTime = entity.getDeleteTime();
        if (deleteTime != null) {
            stmt.bindString(9, deleteTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CheckTableSecondIndex entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getSecondIndexID());
        stmt.bindLong(2, entity.getFirstIndexID());
 
        String serialNum = entity.getSerialNum();
        if (serialNum != null) {
            stmt.bindString(3, serialNum);
        }
 
        String secondIndexName = entity.getSecondIndexName();
        if (secondIndexName != null) {
            stmt.bindString(4, secondIndexName);
        }
 
        String secondIndexDemo = entity.getSecondIndexDemo();
        if (secondIndexDemo != null) {
            stmt.bindString(5, secondIndexDemo);
        }
 
        String hiddenDangerCategory = entity.getHiddenDangerCategory();
        if (hiddenDangerCategory != null) {
            stmt.bindString(6, hiddenDangerCategory);
        }
 
        String deleted = entity.getDeleted();
        if (deleted != null) {
            stmt.bindString(7, deleted);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(8, addTime);
        }
 
        String deleteTime = entity.getDeleteTime();
        if (deleteTime != null) {
            stmt.bindString(9, deleteTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public CheckTableSecondIndex readEntity(Cursor cursor, int offset) {
        CheckTableSecondIndex entity = new CheckTableSecondIndex( //
            cursor.getLong(offset + 0), // secondIndexID
            cursor.getInt(offset + 1), // firstIndexID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // serialNum
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // secondIndexName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // secondIndexDemo
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // hiddenDangerCategory
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // deleted
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // addTime
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // deleteTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CheckTableSecondIndex entity, int offset) {
        entity.setSecondIndexID(cursor.getLong(offset + 0));
        entity.setFirstIndexID(cursor.getInt(offset + 1));
        entity.setSerialNum(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSecondIndexName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSecondIndexDemo(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setHiddenDangerCategory(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDeleted(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAddTime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDeleteTime(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CheckTableSecondIndex entity, long rowId) {
        entity.setSecondIndexID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CheckTableSecondIndex entity) {
        if(entity != null) {
            return entity.getSecondIndexID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CheckTableSecondIndex entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
