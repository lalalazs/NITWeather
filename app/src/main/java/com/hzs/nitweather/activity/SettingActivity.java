package com.hzs.nitweather.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.hzs.nitweather.R;
import com.hzs.nitweather.service.NotificationService;

public class SettingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Switch isShow;
    private TextView tvUpdate;
    private TextView tvTime;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }
    private void initView() {
        tvUpdate = (TextView) findViewById(R.id.tv_update);
        tvTime = (TextView) findViewById(R.id.tv_time);
        isShow = (Switch) findViewById(R.id.switch_show);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("设置");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
    }
    private void initData() {
        mSharedPreferences = getSharedPreferences("info",MODE_PRIVATE);
        tvTime.setText(mSharedPreferences.getString("updateRate","禁止刷新"));
        final String[] times = new String[]{"禁止刷新","一小时","两小时","三小时","四小时","五小时"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(times, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:tvTime.setText(times[0]);mSharedPreferences.edit().putString("updateRate",times[0]).commit();break;
                    case 1:tvTime.setText(times[1]);mSharedPreferences.edit().putString("updateRate",times[1]).commit();break;
                    case 2:tvTime.setText(times[2]);mSharedPreferences.edit().putString("updateRate",times[2]).commit();break;
                    case 3:tvTime.setText(times[3]);mSharedPreferences.edit().putString("updateRate",times[3]).commit();break;
                    case 4:tvTime.setText(times[4]);mSharedPreferences.edit().putString("updateRate",times[4]).commit();break;
                    case 5:tvTime.setText(times[5]);mSharedPreferences.edit().putString("updateRate",times[5]).commit();break;
                }
            }
        });
        if(!mSharedPreferences.getBoolean("isShowStatus",true)){
            isShow.setChecked(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        isShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSharedPreferences.edit().putBoolean("isShowStatus",true).commit();
                    Intent startIntent = new Intent(SettingActivity.this, NotificationService.class);
                    startService(startIntent);
                } else {
                    mSharedPreferences.edit().putBoolean("isShowStatus",false).commit();
                    Intent stopIntent = new Intent(SettingActivity.this, NotificationService.class);
                    stopService(stopIntent);
                }
            }
        });
    }
}
