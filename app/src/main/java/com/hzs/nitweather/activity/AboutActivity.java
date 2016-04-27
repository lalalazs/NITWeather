package com.hzs.nitweather.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hzs.nitweather.R;

public class AboutActivity extends AppCompatActivity {
    private PackageInfo mPackageInfo;
    private Toolbar toolbar;
    private TextView tvVersion;
    private TextView tvCheckUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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
                Toast.makeText(AboutActivity.this,"当前是最新版本，无需更新！",Toast.LENGTH_SHORT).show();
            }
        });
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
