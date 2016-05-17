package com.hzs.nitweather.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hzs.nitweather.R;
import com.hzs.nitweather.domain.VersionInfo;
import com.hzs.nitweather.domain.WeatherInfo;

public class AboutActivity extends AppCompatActivity {
    private PackageInfo mPackageInfo;
    private Toolbar toolbar;
    private TextView tvVersion;
    private TextView tvCheckUpdate;
    private RequestQueue mQueue;
    private VersionInfo mVersionInfo;
    private int firVersionCode;
    private String firVersionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mQueue = Volley.newRequestQueue(this);
        initView();
        initData();
    }


    private void initView() {
        tvCheckUpdate = (TextView) findViewById(R.id.tv_checkupdate);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("关于");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
    }
    private void initData() {
        try {
            mPackageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvVersion.setText("v"+getVersionName());
        tvCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest("http://api.fir.im/apps/latest/5732b2f900fc74170d00000e?api_token=55aeb51eeb855414c90331215f22a43e", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseData(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
                mQueue.add(stringRequest);
            }
        });
    }

    private void parseData(String response) {
        Gson gson = new Gson();
        mVersionInfo = gson.fromJson(response, VersionInfo.class);
        firVersionCode = Integer.parseInt(mVersionInfo.version);
        firVersionName = mVersionInfo.versionShort;
        if(firVersionCode>getVersionCode()){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("发现新版本："+firVersionName);
            dialog.setMessage(mVersionInfo.changelog);
            dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mVersionInfo.update_url));
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton("以后再说",null );

            dialog.show();
        }else{
            Toast.makeText(this,"当前是最新版本！",Toast.LENGTH_SHORT).show();
        }
    }

    private String getVersionName(){
            String versionName = mPackageInfo.versionName;
            return versionName;
    }
    private int getVersionCode(){
            int versionCode = mPackageInfo.versionCode;
            return versionCode;
    }

}
