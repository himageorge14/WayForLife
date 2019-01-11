package com.wayforlife.wayforlife.DataHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wayforlife.wayforlife.Model.Report;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASENAME, null, Constants.DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + Constants.TABLENAME + "(" +
                Constants.KEYID + " TEXT PRIMARY KEY," +
                Constants.REPORTDESCRIPTION + " TEXT," +
                Constants.REPORTLAT + " DOUBLE," +
                Constants.REPORTLONG + " DOUBLE,"+
                Constants.REPORTIMAGE + " BLOB,"+
                Constants.REPORTDATE + " TEXT);";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLENAME);

        onCreate(db);

    }

    public int getTotalReports() {

        int ans = 0;
        String countQuery = "SELECT * FROM " + Constants.TABLENAME;
        SQLiteDatabase dbcount = this.getReadableDatabase();
        Cursor cur = dbcount.rawQuery(countQuery, null);
        ans = cur.getCount();
        cur.close();
        dbcount.close();

        return ans;

    }
    public void deleteItem(String id) {

        SQLiteDatabase dba = this.getWritableDatabase();
        dba.delete(Constants.TABLENAME, Constants.KEYID + "=?", new String[]{String.valueOf(id)});

        dba.close();

    }

    public void addItem(Report report3) {

        SQLiteDatabase dba = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(Constants.REPORTDESCRIPTION,report3.getDescription());
        val.put(Constants.REPORTLAT, report3.getLat());
        val.put(Constants.REPORTLONG, report3.getLong());
        val.put(Constants.REPORTIMAGE,report3.getImage());
        val.put(Constants.REPORTDATE, report3.getDate().toString());

        dba.insert(Constants.TABLENAME, null, val);

        Log.v("Item added", "Hurray!!");

        dba.close();

    }
    public ArrayList<Report> getAllItems() {

        ArrayList<Report> allArray = new ArrayList<>();

        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cur = dba.query(Constants.TABLENAME, new String[]{Constants.KEYID, Constants.REPORTDESCRIPTION, Constants.REPORTLAT, Constants.REPORTLONG,Constants.REPORTIMAGE,Constants.REPORTDATE}, null, null, null, null, Constants.REPORTDATE + " DESC");

        if(cur.moveToFirst()){
            do {

                Report aReport=new Report();

                aReport.setDate(Constants.REPORTDATE);
                aReport.setDescription(cur.getString(cur.getColumnIndex(Constants.REPORTDESCRIPTION)));
                aReport.setImage(cur.getBlob(cur.getColumnIndex(Constants.REPORTIMAGE)));
                aReport.setLat(cur.getDouble(cur.getColumnIndex(Constants.REPORTLAT)));
                aReport.setLong(cur.getDouble(cur.getColumnIndex(Constants.REPORTLONG)));
                aReport.setId(cur.getString(cur.getColumnIndex(Constants.KEYID)));

                allArray.add(aReport);

            }while (cur.moveToNext());
        }

        cur.close();
        dba.close();
        return allArray;
    }
}
