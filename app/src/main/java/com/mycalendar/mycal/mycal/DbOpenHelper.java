package com.mycalendar.mycal.mycal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "schedule.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mContext;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public DbOpenHelper (Context context) {
        this.mContext = context;
    }

    public DbOpenHelper open() throws SQLException{
        mDBHelper = new DatabaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDB.close();
    }

    //스케줄 insert
    public long insertColumn(int year, int month, int day, String schedule) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.YEAR, year);
        values.put(DataBases.CreateDB.MONTH, month);
        values.put(DataBases.CreateDB.DAY, day);
        values.put(DataBases.CreateDB.SCHEDULE, schedule);
        return mDB.insert(DataBases.CreateDB._TABLENAME, null, values);
    }

    //Monthly검색
    public Cursor selectMonthSchedule(int year, int month) {
        Cursor cursor = mDB.rawQuery("SELECT day FROM scheduletable WHERE year = " + year
                + " AND month = " + month + ";", null);
        return cursor;
    }

    //Weekly, Daily 검색
    public Cursor selectContent(int year, int month, int day) {
        Cursor cursor = mDB.rawQuery("SELECT _id, schedule FROM scheduletable WHERE year = " + year
                + " AND month = " + month + " AND day = " + day + ";", null);
        return cursor;
    }

    //스케줄 delete
    public void deleteSchedule(int id) {
        mDB.delete(DataBases.CreateDB._TABLENAME, "_id = " + id, null);
    }
}
