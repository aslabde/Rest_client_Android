package tk.ebalsa.rest1.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ebalsa.gmail.com on 23/02/14.
 */
public class DBCache {
    static final String KEY_ROWID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_BODY  = "body";
    static final String PUB_DATE  = "pubDate";
    static final String END_DATE  = "endDate";
    static final String USER_ACTIVE = "userActive";
    static final String DATABASE_NAME = "local_cache";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_TABLE = "resources";
    static final String DATABASE_CREATE =
    " create table resurces (id integer primary key , title text, body text " +
            ",pubDate integer, endDate integer, userActive text)";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBCache(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            //NOT IMPLEMENTED YET
        }

    }

    //---opens the database---
    public DBCache open() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close(){
        DBHelper.close();
    }

    //---insert a resource into the database---
    public long insertResource(long id, String title, String body, long pubDate,
                               long endDate, String userActive ){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(PUB_DATE, (int)pubDate);
        initialValues.put(END_DATE, (int)endDate );
        initialValues.put(USER_ACTIVE,userActive );


        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular resource---
    public boolean deleteResource(long rowId){
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the resources---
    public Cursor getAllContacts(){
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, PUB_DATE, END_DATE, USER_ACTIVE}, null, null, null, null, null);
    }


    //---retrieves a particular contact---
    public Cursor getContact(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                        KEY_BODY, PUB_DATE, END_DATE, USER_ACTIVE}, KEY_ROWID + "=" + rowId, null,
            null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

   //---updates a contact---
    public boolean updateContact(long id, String title, String body, long pubDate,
                                 long endDate, String userActive )
    {
        ContentValues args = new ContentValues();
        args.put(KEY_ROWID, id);
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put(PUB_DATE, (int)pubDate);
        args.put(END_DATE, (int)endDate );
        args.put(USER_ACTIVE,userActive );

        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
    }



}
