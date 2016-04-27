package com.hzs.nitweather.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Created by hzs on 2016/4/13.
 */
/**
 * 解析省的信息，并保存到数据库
 */
public class ParseUtils {
    public static void parseResponseFromeProvince(SQLiteDatabase nITWeatherDB,String response) {
        if(!TextUtils.isEmpty(response)){
            String[] allProvince = response.split(",");
            if(allProvince!=null&&allProvince.length>0){
                for(String p:allProvince){
                    String[] split = p.split("\\|");
                    ContentValues values = new ContentValues();
                    values.put("province_name",split[1]);
                    values.put("province_code",split[0]);
                    nITWeatherDB.insert("Province",null,values);
                }
            }
        }

    }
    public static void parseResponseFromeCity(SQLiteDatabase nITWeatherDB,String response,int provinceId) {
        if(!TextUtils.isEmpty(response)){
            String[] allCity = response.split(",");
            if(allCity!=null&&allCity.length>0){
                for(String c:allCity){
                    String[] split = c.split("\\|");
                    ContentValues values = new ContentValues();
                    values.put("city_name",split[1]);
                    values.put("city_code",split[0]);
                    values.put("province_id",provinceId);
                    nITWeatherDB.insert("City",null,values);
                }
            }
        }

    }

    public static void parseResponseFromeCounty(SQLiteDatabase nITWeatherDB, String response, int cityId) {
        if(!TextUtils.isEmpty(response)){
            String[] allCounty = response.split(",");
            if(allCounty!=null&&allCounty.length>0){
                for(String c:allCounty){
                    String[] split = c.split("\\|");
                    ContentValues values = new ContentValues();
                    values.put("county_name",split[1]);
                    values.put("county_code",split[0]);
                    values.put("city_id",cityId);
                    nITWeatherDB.insert("County",null,values);
                }
            }
        }
    }
}
