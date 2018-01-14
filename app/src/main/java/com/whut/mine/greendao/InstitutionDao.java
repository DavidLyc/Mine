package com.whut.mine.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.whut.mine.entity.Institution;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "institution_t".
*/
public class InstitutionDao extends AbstractDao<Institution, String> {

    public static final String TABLENAME = "institution_t";

    /**
     * Properties of entity Institution.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property InstitutionNum = new Property(0, String.class, "institutionNum", true, "InstitutionNum");
        public final static Property InstitutionName = new Property(1, String.class, "institutionName", false, "InstitutionName");
        public final static Property ICategoryNum = new Property(2, int.class, "iCategoryNum", false, "ICategoryNum");
        public final static Property PeopleInCharge = new Property(3, String.class, "peopleInCharge", false, "PeopleInCharge");
        public final static Property Category = new Property(4, String.class, "category", false, "Category");
        public final static Property InstitutionPrefix = new Property(5, String.class, "institutionPrefix", false, "InstitutionPrefix");
    }


    public InstitutionDao(DaoConfig config) {
        super(config);
    }
    
    public InstitutionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"institution_t\" (" + //
                "\"InstitutionNum\" TEXT PRIMARY KEY NOT NULL ," + // 0: institutionNum
                "\"InstitutionName\" TEXT," + // 1: institutionName
                "\"ICategoryNum\" INTEGER NOT NULL ," + // 2: iCategoryNum
                "\"PeopleInCharge\" TEXT," + // 3: peopleInCharge
                "\"Category\" TEXT," + // 4: category
                "\"InstitutionPrefix\" TEXT);"); // 5: institutionPrefix
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"institution_t\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Institution entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getInstitutionNum());
 
        String institutionName = entity.getInstitutionName();
        if (institutionName != null) {
            stmt.bindString(2, institutionName);
        }
        stmt.bindLong(3, entity.getICategoryNum());
 
        String peopleInCharge = entity.getPeopleInCharge();
        if (peopleInCharge != null) {
            stmt.bindString(4, peopleInCharge);
        }
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(5, category);
        }
 
        String institutionPrefix = entity.getInstitutionPrefix();
        if (institutionPrefix != null) {
            stmt.bindString(6, institutionPrefix);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Institution entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getInstitutionNum());
 
        String institutionName = entity.getInstitutionName();
        if (institutionName != null) {
            stmt.bindString(2, institutionName);
        }
        stmt.bindLong(3, entity.getICategoryNum());
 
        String peopleInCharge = entity.getPeopleInCharge();
        if (peopleInCharge != null) {
            stmt.bindString(4, peopleInCharge);
        }
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(5, category);
        }
 
        String institutionPrefix = entity.getInstitutionPrefix();
        if (institutionPrefix != null) {
            stmt.bindString(6, institutionPrefix);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    @Override
    public Institution readEntity(Cursor cursor, int offset) {
        Institution entity = new Institution( //
            cursor.getString(offset + 0), // institutionNum
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // institutionName
            cursor.getInt(offset + 2), // iCategoryNum
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // peopleInCharge
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // category
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // institutionPrefix
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Institution entity, int offset) {
        entity.setInstitutionNum(cursor.getString(offset + 0));
        entity.setInstitutionName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setICategoryNum(cursor.getInt(offset + 2));
        entity.setPeopleInCharge(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCategory(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setInstitutionPrefix(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final String updateKeyAfterInsert(Institution entity, long rowId) {
        return entity.getInstitutionNum();
    }
    
    @Override
    public String getKey(Institution entity) {
        if(entity != null) {
            return entity.getInstitutionNum();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Institution entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
