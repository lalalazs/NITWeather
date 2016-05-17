package com.hzs.nitweather.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hzs.nitweather.R;
import com.hzs.nitweather.adapter.MyRvAdapter;
import com.hzs.nitweather.domain.WeatherInfo;
import com.hzs.nitweather.service.AutoUpdateService;
import com.hzs.nitweather.service.NotificationService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AMapLocationListener, SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {
    private String mCityNameFalse;
    private String mCityNameTrue;
    private String mCityNameFromLocation;
    private String requestUrl;
    private Toolbar mToolbar;
    private WeatherInfo mWeatherInfo;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private RequestQueue mQueue;
    private MyRvAdapter myRvAdapter;
    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;
    private long exitTime = 0;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    //public AMapLocationListener mLocationListener = new AMapLocationListener();
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        initView();
        initData();
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("未知", R.mipmap.none);
        editor.putInt("晴", R.mipmap.type_one_sunny);
        editor.putInt("阴", R.mipmap.type_one_cloudy);
        editor.putInt("少云", R.mipmap.type_one_cloudy);
        editor.putInt("多云", R.mipmap.type_one_cloudy);
        editor.putInt("晴间多云", R.mipmap.type_one_cloudytosunny);
        editor.putInt("毛毛雨/细雨", R.mipmap.type_one_light_rain);
        editor.putInt("小雨", R.mipmap.type_one_light_rain);
        editor.putInt("中雨", R.mipmap.type_one_middle_rain);
        editor.putInt("大雨", R.mipmap.type_one_heavy_rain);
        editor.putInt("阵雨", R.mipmap.type_one_thunderstorm);
        editor.putInt("雷阵雨", R.mipmap.type_one_thunderstorm);
        editor.putInt("霾", R.mipmap.type_one_fog);
        editor.putInt("雾", R.mipmap.type_one_fog);
        editor.commit();
        if (mSharedPreferences.getBoolean("isFirst", true)) {
            startActivityForResult(new Intent(MainActivity.this, ChoiceCityActivity.class), 2);
        } else {
            if (mSharedPreferences.getBoolean("isCache", false)) {
                mToolbar.setTitle(mSharedPreferences.getString("titleName", null));
                myRvAdapter = new MyRvAdapter(this, mSharedPreferences);
                mRecyclerView.setAdapter(myRvAdapter);
                if (mSharedPreferences.getBoolean("isShowStatus", true)) {
                    startService(new Intent(MainActivity.this, NotificationService.class));
                    startService(new Intent(MainActivity.this, AutoUpdateService.class));
                }
            }
        }

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
//        mToolbar.setNavigationIcon(R.mipmap.ic_toolbar_list);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        mSwipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright, android.R.color.holo_green_light);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
//        mSwipeRefreshLayout.setProgressViewEndTarget(true, 100);
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    mCityNameTrue = data.getStringExtra("city_name_true");
                    mCityNameFalse = data.getStringExtra("city_name_false");
                    mToolbar.setTitle(mCityNameTrue);
                    requestUrl = "https://api.heweather.com/x3/weather?city=" + mCityNameFalse + "&key=3f3d67620f9d4f1f877c70a925830b0f";
                    getDataFromServer();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    mSharedPreferences.edit().putBoolean("isFirst", false).commit();
                    mCityNameTrue = data.getStringExtra("city_name_true");
                    mCityNameFalse = data.getStringExtra("city_name_false");
                    mToolbar.setTitle(mCityNameTrue);
                    requestUrl = "https://api.heweather.com/x3/weather?city=" + mCityNameFalse + "&key=3f3d67620f9d4f1f877c70a925830b0f";
                    getDataFromServer();
                } else {
                    finish();
                }

        }
    }

    /**
     * 从和风api请求天气信息
     */
    private void getDataFromServer() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }

    /**
     * 解析返回的天气信息
     */
    private void parseData(String response) {
        Gson gson = new Gson();
        mWeatherInfo = gson.fromJson(response, WeatherInfo.class);
        addWeatherInfoCache();
        myRvAdapter = new MyRvAdapter(this, mSharedPreferences);
        mRecyclerView.setAdapter(myRvAdapter);
        if (mSharedPreferences.getBoolean("isShowStatus", true)) {
            startService(new Intent(MainActivity.this, NotificationService.class));
            startService(new Intent(MainActivity.this, AutoUpdateService.class));
        }
        closeProgressDialog();
    }

    //添加缓存
    private void addWeatherInfoCache() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("titleName", mToolbar.getTitle().toString());
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

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
//                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                amapLocation.getLatitude();//获取纬度
//                amapLocation.getLongitude();//获取经度
//                amapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//                df.format(date);//定位时间
//                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                amapLocation.getCountry();//国家信息
//                amapLocation.getProvince();//省信息
                mCityNameFromLocation = amapLocation.getCity().replace("市", "")
                        .replace("省", "")
                        .replace("自治区", "")
                        .replace("特别行政区", "")
                        .replace("地区", "")
                        .replace("盟", "");
                ;//城市信息o
                mToolbar.setTitle(mCityNameFromLocation);
                requestUrl = "https://api.heweather.com/x3/weather?city=" + mCityNameFromLocation + "&key=3f3d67620f9d4f1f877c70a925830b0f";
                getDataFromServer();
//                amapLocation.getDistrict();//城区信息
//                amapLocation.getStreet();//街道信息
//                amapLocation.getStreetNum();//街道门牌号信息
//                amapLocation.getCityCode();//城市编码
//                amapLocation.getAdCode();//地区编码
//                amapLocation.getAoiName();//获取当前定位点的AOI信息
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onRefresh() {
        requestUrl = "https://api.heweather.com/x3/weather?city=" + mToolbar.getTitle().toString() + "&key=3f3d67620f9d4f1f877c70a925830b0f";
        getDataFromServer();
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
    }

    /*
     监听侧滑菜单中的按钮*
      *
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_choice_city:
                startActivityForResult(new Intent(MainActivity.this, ChoiceCityActivity.class), 1);
                break;
            case R.id.nav_location_city:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    } else {
                        initLocation();
                    }
                } else {
                    initLocation();
                }
                break;
            case R.id.nav_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, "http://www.wandoujia.com/apps/com.hzs.nitweather");
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation();
            }else{
                Toast.makeText(MainActivity.this,"缺少定位权限",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("loading...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
