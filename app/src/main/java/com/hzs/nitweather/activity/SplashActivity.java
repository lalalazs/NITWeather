package com.hzs.nitweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hzs.nitweather.R;
import com.hzs.nitweather.db.NitWeatherOpenHelper;
import com.hzs.nitweather.utils.ParseUtils;
import com.hzs.nitweather.utils.ReadUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private static final int CODE_START_MAINACTIVITY = 0;
    private static final int CODE_UPDATE_DIALOG = 1;
    private static final int CODE_URL_ERROR = 2;
    private static final int CODE_NET_ERROR = 3;
    private NitWeatherOpenHelper weatherOpenHelper;
    private SharedPreferences mSharedPreferences;
    public static SQLiteDatabase nITWeatherDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSharedPreferences = getSharedPreferences("info",MODE_PRIVATE);
        weatherOpenHelper = new NitWeatherOpenHelper(this, "nit_weather.db", null, 1);
        nITWeatherDB = weatherOpenHelper.getWritableDatabase();
        Cursor cursor = nITWeatherDB.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            cursor.close();
            sendRequestForProvince("http://www.weather.com.cn/data/list3/city.xml");
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_START_MAINACTIVITY:
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
                case CODE_UPDATE_DIALOG:break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this,"Url错误", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
            }

        }
    };

    private void sendRequestForProvince(final String address) { //从服务器读取省的信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    long time = System.currentTimeMillis();
                    URL url = new URL(address);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String response = ReadUtils.getResponseFromeHttp(inputStream).toString();
                        ParseUtils.parseResponseFromeProvince(nITWeatherDB, response);
                        long time1 = System.currentTimeMillis();
                        if ((time1 - time) >= 2000) {
                            handler.sendEmptyMessage(CODE_START_MAINACTIVITY);
                        } else {
                            handler.sendEmptyMessageDelayed(CODE_START_MAINACTIVITY, 2000 - (time - time1));
                        }
                    }
                } catch (MalformedURLException e) {
                    handler.sendEmptyMessage(CODE_URL_ERROR);
                    e.printStackTrace();
                } catch (IOException e) {
                    handler.sendEmptyMessage(CODE_NET_ERROR);
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }


}
