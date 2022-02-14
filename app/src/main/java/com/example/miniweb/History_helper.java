package com.example.miniweb;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class History_helper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "history.db";
    // Contacts table name
    public static final String TABLE_NAME = "list";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_URL = "url";
    public static final String KEY_DATE = "DATE";

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT NOT NULL, " + KEY_URL + " TEXT, " + KEY_DATE + " INTEGER" + ")";

    // database version
    static final int DB_VERSION = 1;

    public History_helper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Logs
        String TAG = "state";
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addWebsite(Website1 website1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, website1._title); // Contact Name
        values.put(KEY_URL, website1._url); // Contact Url
        values.put(KEY_DATE, website1._date);
       // Contact Phone

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }


    // Getting All Contacts
    public List<Website1> getAllWebsite() {
        List<Website1> website1List = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.query(
                TABLE_NAME,
                new String[] {
                        KEY_ID,
                        KEY_NAME,
                        KEY_URL,
                        KEY_DATE,

                },
                null,
                null,
                null,
                null,
                KEY_ID + " desc"
        );
        // looping through all rows and adding to list
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            website1List.add(getRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return website1List;

    }

    private Website1 getRecord(Cursor cursor) {
        Website1 website1 = new Website1();
        website1.setID(Integer.parseInt(cursor.getString(0)));
        website1.setTitle(cursor.getString(1));
        website1.setUrl(cursor.getString(2));
        website1.setDate(cursor.getLong(3));
        return website1;
    }

    // Updating single contact
    public int updateWebsite(Website1 website1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, website1.getTitle());
        values.put(KEY_URL, website1.getUrl());


        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(website1.getID()) });

    }

    // Deleting single contact
    public void delete(long _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=" + _id, null);
        db.close();
    }


    // Getting contacts Count
    public int getWebsiteCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }



    public boolean checkUrl (String url, String table) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                table,
                new String[] {KEY_URL},
                KEY_URL + "=?",
                new String[] {url.trim()},
                null,
                null,
                null
        );
        if (cursor != null) {
            boolean result = cursor.moveToFirst();
            cursor.close();

            return result;
        }
        return false;
    }

    public void deleteURL (String domain, String table) {
        if (domain == null || domain.trim().isEmpty()) { return; }
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ table + " WHERE " + KEY_URL + " = " + "\"" + domain.trim() + "\"");
    }

    public void deleteAll (String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ table);
    }



}
