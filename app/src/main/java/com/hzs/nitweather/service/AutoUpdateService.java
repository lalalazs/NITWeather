package com.hzs.nitweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.hzs.nitweather.activity.MainActivity;
import com.hzs.nitweather.adapter.MyRvAdapter;
import com.hzs.nitweather.broadcast.AutoUpdateReceiver;
import com.hzs.nitweather.domain.WeatherInfo;

public class AutoUpdateService extends Service {
    private SharedPreferences mSharedPreferences;
    private RequestQueue mQueue;
    private WeatherInfo mWeatherInfo;
    private int delayTime;
    private String s;
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSharedPreferences.getString("titleName", null);
        getDataFromServer();
        startService(new Intent(this,NotificationService.class));
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        s = mSharedPreferences.getString("updateRate","禁止刷新");
        if(s.equals("禁止刷新")){
            stopSelf();
        }else if(s.equals("一小时")){
            delayTime = 60*60*1000;
        }
        else if(s.equals("两小时")){
            delayTime = 2*60*60*1000;
        }
        else if(s.equals("三小时")){
            delayTime = 3*60*60*1000;
        }
        else if(s.equals("四小时")){
            delayTime = 4*60*60*1000;
        }
        else if(s.equals("五小时")){
            delayTime = 5*60*60*1000;
        }
        long triggerAtTime = SystemClock.elapsedRealtime()+delayTime;
        Intent i = new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,i,0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }
    private void getDataFromServer() {
        StringRequest stringRequest = new StringRequest("https://api.heweather.com/x3/weather?city=" +  mSharedPreferences.getString("titleName", null) + "&key=3f3d67620f9d4f1f877c70a925830b0f", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(stringRequest);
    }
    private void parseData(String response) {
        Gson gson = new Gson();
        mWeatherInfo = gson.fromJson(response, WeatherInfo.class);
        addWeatherInfoCache();
        if (mSharedPreferences.getBoolean("isShowStatus", true)) {
            startService(new Intent(this, NotificationService.class));
        }
    }
    private void addWeatherInfoCache() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("condTxt", mWeatherInfo.HeWeatherDataService3.get(0).now.cond.txt);
        editor.putString("tempFlu", mWeatherInfo.HeWeatherDataService3.get(0).now.tmp);
        editor.putString("tempMax", mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(0).tmp.max);
        editor.putString("tempMin", mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(0).tmp.min);
        editor.putString("tempPm", mWeatherInfo.HeWeatherDataService3.get(0).aqi.city.pm25);
        editor.putString("tempQuality", mWeatherInfo.HeWeatherDataService3.get(0).aqi.city.qlty);
        for (int i = 0; i < mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.size(); i++) {
            editor.putString("mDate" + i, mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.get(i).date);
            editor.putString("mTemp" + i, mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.get(i).tmp);
            editor.putString("mHumidity" + i, mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.get(i).hum);
            editor.putString("mWind" + i, mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.get(i).wind.spd);
        }
        editor.putString("clothBrief", mWeatherInfo.HeWeatherDataService3.get(0).suggestion.drsg.brf);
        editor.putString("clothTxt", mWeatherInfo.HeWeatherDataService3.get(0).suggestion.drsg.txt);
        editor.putString("sportBrief", mWeatherInfo.HeWeatherDataService3.get(0).suggestion.sport.brf);
        editor.putString("sportTxt", mWeatherInfo.HeWeatherDataService3.get(0).suggestion.sport.txt);
        editor.putString("travelBrief", mWeatherInfo.HeWeatherDataService3.get(0).suggestion.trav.brf);
        editor.putString("travelTxt", mWeatherInfo.HeWeatherDataService3.get(0).suggestion.trav.txt);
        editor.putString("fluBrief", mWeatherInfo.HeWeatherDataService3.get(0).suggestion.flu.brf);
        editor.putString("fluTxt", mWeatherInfo.HeWeatherDataService3.get(0).suggestion.flu.txt);
        for (int i = 0; i < mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.size(); i++) {
            if (i > 1) {
                editor.putString("forecastDate" + i, mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).date);
            }
            editor.putString("forecastIcon" + i, mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).cond.txt_d);
            editor.putString("tmpmin" + i, mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).tmp.min);
            editor.putString("tmpmax" + i, mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).tmp.max);
            editor.putString("condtxt" + i, mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).cond.txt_d);
            editor.putString("windsc" + i, mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).wind.sc);
            editor.putString("winddir" + i, mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).wind.dir);
            editor.putString("windspd" + i, mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).wind.spd);
            editor.putString("pop" + i, mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).pop);
        }
        editor.putInt("hourlySize", mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.size());
        editor.putInt("dailySize", mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.size());
        editor.commit();
    }
}
