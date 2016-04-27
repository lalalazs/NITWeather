package com.hzs.nitweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzs.nitweather.R;
import com.hzs.nitweather.domain.City;
import com.hzs.nitweather.domain.County;
import com.hzs.nitweather.domain.Province;
import com.hzs.nitweather.utils.ParseUtils;
import com.hzs.nitweather.utils.ReadUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzs on 2016/4/15.
 */
public class ChoiceCityActivity extends AppCompatActivity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;
    private List<Province> provinces;
    private List<City> cities;
    private List<County> counties;
    private ListView listView;
    private List<String> names;
    private Toolbar toolbar;
    private int currentLevel;
    private MyAdapter myAdapter;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choice_city);
        initView();
        initData();
        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //listview条目的点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) { //当前点击的条目等级为省
                    showProgressDialog();
                    selectedProvince = provinces.get(position-listView.getHeaderViewsCount()); //加了头部导致position移位，要减去所加的头部数量
                    showCities();
                } else if (currentLevel == LEVEL_CITY) { //当前点击的条目等级为市
                    showProgressDialog();
                    selectedCity = cities.get(position-listView.getHeaderViewsCount());
                    showCounties();
                } else if(currentLevel==LEVEL_COUNTY){
                    Intent intent = new Intent();
                    intent.putExtra("city_name_true",counties.get(position-listView.getHeaderViewsCount()).getCountyName());
                    intent.putExtra("city_name_false",counties.get(0).getCountyName());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

    }



    /*显示省级列表*

     */
    private void showProvinces() {
        if (provinces.size() > 0) {
            names.clear();
            for(Province p:provinces){
                names.add(p.getProvinceName());
            }
        } else {
            Cursor cursor = SplashActivity.nITWeatherDB.query("Province", null, null, null, null, null, null);
            names.clear();
            while (cursor.moveToNext()) {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province);
                names.add(province.getProvinceName());
            }
            cursor.close();
        }
        toolbar.setTitle("选择城市");
        currentLevel = LEVEL_PROVINCE;
        myAdapter.notifyDataSetChanged();
        closeProgressDialog();
    }

    /*
    * 显示城市列表
    **/
    private void showCities() {
        Cursor cursor = SplashActivity.nITWeatherDB.query("City", null, "province_id=?", new String[]{String.valueOf(selectedProvince.getId())}, null, null, null);
        if (cursor.moveToFirst()) { //城市数据库存在，加载数据库数据
            loadCitiesFromDB(cursor);

        } else { //当前省的数据库不存在，请求服务器
            sendRequestForCity("http://www.weather.com.cn/data/list3/city" + selectedProvince.getProvinceCode() + ".xml", selectedProvince.getId());

        }
        cursor.close();
        toolbar.setTitle(selectedProvince.getProvinceName());
        currentLevel = LEVEL_CITY;
    }
    /**
     * 显示区/县级列表
     */
    private void showCounties() {
        Cursor cursor = SplashActivity.nITWeatherDB.query("County", null, "city_id=?", new String[]{String.valueOf(selectedCity.getId())}, null, null, null);
        if (cursor.moveToFirst()) { //县/区数据库存在，加载数据库数据
            loadCountiesFromDB(cursor);

        } else { //当前市的数据库不存在，请求服务器
            sendRequestForCounty("http://www.weather.com.cn/data/list3/city" + selectedCity.getCityCode() + ".xml", selectedCity.getId());

        }
        cursor.close();
        toolbar.setTitle(selectedCity.getCityName());
        currentLevel = LEVEL_COUNTY;
    }


    private Handler handler = new Handler() {
        Cursor cursor;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    cursor = SplashActivity.nITWeatherDB.query("City", null, "province_id=?", new String[]{String.valueOf(selectedProvince.getId())}, null, null, null);
                    if (cursor.moveToFirst()) {
                        loadCitiesFromDB(cursor);
                    }
                    break;
                case 2:
                    cursor = SplashActivity.nITWeatherDB.query("County", null, "city_id=?", new String[]{String.valueOf(selectedCity.getId())}, null, null, null);
                    if (cursor.moveToFirst()) {
                        loadCountiesFromDB(cursor);
                    }
                    break;
            }
        }
    };


    /**
     * 向服务器请求市列表
     */
    private void sendRequestForCity(final String adress, final int provinceId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(adress);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String response = ReadUtils.getResponseFromeHttp(inputStream).toString();
                        ParseUtils.parseResponseFromeCity(SplashActivity.nITWeatherDB, response, provinceId); //解析并保存到数据库
                        handler.sendEmptyMessage(1);
                    }
                } catch (MalformedURLException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChoiceCityActivity.this,"url错误",Toast.LENGTH_SHORT).show();
                        }
                    });

                    e.printStackTrace();
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChoiceCityActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }finally{
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }
    /**
     * 向服务器请求县级列表
     */
    private void sendRequestForCounty(final String adress, final int cityId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(adress);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String response = ReadUtils.getResponseFromeHttp(inputStream).toString();
                        ParseUtils.parseResponseFromeCounty(SplashActivity.nITWeatherDB, response, cityId); //解析并保存到数据库
                        handler.sendEmptyMessage(2);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("选择城市");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        listView = (ListView) findViewById(R.id.list_view);
    }

    private void initData() {
        myAdapter = new MyAdapter();
        provinces = new ArrayList<Province>();
        cities = new ArrayList<City>();
        counties = new ArrayList<County>();
        names = new ArrayList<String>();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        showProgressDialog();
        showProvinces();
    }
    /*
    /listview的适配器*
    */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            View view;
            if (convertView == null) {
                view = View.inflate(ChoiceCityActivity.this, R.layout.listview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) view.findViewById(R.id.tv_item);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.textView.setText(names.get(position));
            return view;
//            TextView textView = new TextView(ChoiceCityActivity.this);
//            textView.setText(names.get(position));
//            textView.setPadding(20,20,20,20);
//            textView.setTextSize(25);
//            return textView;
        }

        class ViewHolder {
            TextView textView;
        }
    }
    /**
     * 从数据库读取市名
     */
    private void loadCitiesFromDB(Cursor cursor) {
        names.clear();
        cities.clear();
        do {
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
            cities.add(city);
            names.add(city.getCityName());
        } while (cursor.moveToNext());
        cursor.close();
        myAdapter.notifyDataSetChanged();
        closeProgressDialog();
    }
    /**
     * 从数据库读取县/区名
     */
    private void loadCountiesFromDB(Cursor cursor) {
        names.clear();
        counties.clear();
        do {
            County county = new County();
            county.setId(cursor.getInt(cursor.getColumnIndex("id")));
            county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
            county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
            county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
            if(county.getCountyName().equals("鄞县")){
                county.setCountyName("鄞州");
            }
            counties.add(county);
            names.add(county.getCountyName());
        } while (cursor.moveToNext());
        cursor.close();
        myAdapter.notifyDataSetChanged();
        closeProgressDialog();
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            showCities();
        } else if (currentLevel == LEVEL_CITY) {
            showProvinces();
        } else {
            finish();
        }
    }

    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog = new ProgressDialog(ChoiceCityActivity.this);
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}
