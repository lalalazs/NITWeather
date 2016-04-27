package com.hzs.nitweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hzs on 2016/4/12.
 */
public class NitWeatherOpenHelper extends SQLiteOpenHelper {

    public NitWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Province(id integer primary key autoincrement,province_name text,province_code text)");
        db.execSQL("create table City(id integer primary key autoincrement,city_name text,city_code text,province_id integer)");
        db.execSQL("create table County(id integer primary key autoincrement,county_name text,county_code text,city_id integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
