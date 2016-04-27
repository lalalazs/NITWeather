package com.hzs.nitweather.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hzs.nitweather.R;
import com.hzs.nitweather.domain.WeatherInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hzs on 2016/4/22.
 */
public class MyRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_ONE = 0;
    private final int TYPE_TWO = 1;
    private final int TYPE_THREE = 2;
    private final int TYPE_FORE = 3;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    public MyRvAdapter(Context context, SharedPreferences sharedPreferences) {
        mContext = context;
        mSharedPreferences = sharedPreferences;
        editor = mSharedPreferences.edit();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            return new NowWeatherViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item1, parent, false));
        }
        if (viewType == TYPE_TWO) {
            return new HoursWeatherViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item2, parent, false));
        }
        if (viewType == TYPE_THREE) {
            return new SuggestionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item3, parent, false));
        }
        if (viewType == TYPE_FORE) {
            return new ForecastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item4, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NowWeatherViewHolder) {
//            if (mSharedPreferences.getBoolean("isReadCache", false)==true) {
                int i = mSharedPreferences.getInt(mSharedPreferences.getString("condTxt", null), R.mipmap.none);
                ((NowWeatherViewHolder) holder).weatherIcon.setImageResource(i);
                ((NowWeatherViewHolder) holder).tempFlu.setText(mSharedPreferences.getString("tempFlu", null) + "℃");
                ((NowWeatherViewHolder) holder).tempMax.setText("↑ " + mSharedPreferences.getString("tempMax", null) + "°");
                ((NowWeatherViewHolder) holder).tempMin.setText("↓ " + mSharedPreferences.getString("tempMin", null) + "°");
                ((NowWeatherViewHolder) holder).tempPm.setText("PM25： " + mSharedPreferences.getString("tempPm", null));
                ((NowWeatherViewHolder) holder).tempQuality.setText("空气质量： " + mSharedPreferences.getString("tempQuality", null));
//            } else {
//                int i = mSharedPreferences.getInt(mWeatherInfo.HeWeatherDataService3.get(0).now.cond.txt, R.mipmap.none);
//                ((NowWeatherViewHolder) holder).weatherIcon.setImageResource(i);
//                ((NowWeatherViewHolder) holder).tempFlu.setText(mWeatherInfo.HeWeatherDataService3.get(0).now.tmp + "℃");
//                ((NowWeatherViewHolder) holder).tempMax.setText("↑ " + mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(0).tmp.max + "°");
//                ((NowWeatherViewHolder) holder).tempMin.setText("↓ " + mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(0).tmp.min + "°");
//                ((NowWeatherViewHolder) holder).tempPm.setText("PM25： " + mWeatherInfo.HeWeatherDataService3.get(0).aqi.city.pm25);
//                ((NowWeatherViewHolder) holder).tempQuality.setText("空气质量： " + mWeatherInfo.HeWeatherDataService3.get(0).aqi.city.qlty);
//            }

        }
        if (holder instanceof HoursWeatherViewHolder) {
//            if (mSharedPreferences.getBoolean("isReadCache", false)==true) {
                for (int i = 0; i < mSharedPreferences.getInt("hourlySize", 0); i++) {
                    //s.subString(s.length-3,s.length);
                    //第一个参数是开始截取的位置，第二个是结束位置。
                    String mDate = mSharedPreferences.getString("mDate" + i, null);
                    ((HoursWeatherViewHolder) holder).mClock[i].setText(mDate.substring(mDate.length() - 5, mDate.length()));
                    ((HoursWeatherViewHolder) holder).mTemp[i].setText(mSharedPreferences.getString("mTemp" + i, null) + "°");
                    ((HoursWeatherViewHolder) holder).mHumidity[i].setText(mSharedPreferences.getString("mHumidity" + i, null) + "%");
                    ((HoursWeatherViewHolder) holder).mWind[i].setText(mSharedPreferences.getString("mWind" + i, null) + "Km");
                }
//            } else {
//                for (int i = 0; i < mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.size(); i++) {
//                    //s.subString(s.length-3,s.length);
//                    //第一个参数是开始截取的位置，第二个是结束位置。
//                    String mDate = mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.get(i).date;
//                    ((HoursWeatherViewHolder) holder).mClock[i].setText(mDate.substring(mDate.length() - 5, mDate.length()));
//                    ((HoursWeatherViewHolder) holder).mTemp[i].setText(mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.get(i).tmp + "°");
//                    ((HoursWeatherViewHolder) holder).mHumidity[i].setText(mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.get(i).hum + "%");
//                    ((HoursWeatherViewHolder) holder).mWind[i].setText(mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.get(i).wind.spd + "Km");
//                }
//            }
        }
        if (holder instanceof SuggestionViewHolder) {
//            if (mSharedPreferences.getBoolean("isReadCache", false)==true) {
                ((SuggestionViewHolder) holder).clothBrief.setText("穿衣指数---" + mSharedPreferences.getString("clothBrief", null));
                ((SuggestionViewHolder) holder).clothTxt.setText(mSharedPreferences.getString("clothTxt", null));

                ((SuggestionViewHolder) holder).sportBrief.setText("运动指数---" + mSharedPreferences.getString("sportBrief", null));
                ((SuggestionViewHolder) holder).sportTxt.setText(mSharedPreferences.getString("sportTxt", null));

                ((SuggestionViewHolder) holder).travelBrief.setText("旅游指数---" + mSharedPreferences.getString("travelBrief", null));
                ((SuggestionViewHolder) holder).travelTxt.setText(mSharedPreferences.getString("travelTxt", null));

                ((SuggestionViewHolder) holder).fluBrief.setText("感冒指数---" + mSharedPreferences.getString("fluBrief", null));
                ((SuggestionViewHolder) holder).fluTxt.setText(mSharedPreferences.getString("fluTxt", null));
//            } else {
//                ((SuggestionViewHolder) holder).clothBrief.setText("穿衣指数---" + mWeatherInfo.HeWeatherDataService3.get(0).suggestion.drsg.brf);
//                ((SuggestionViewHolder) holder).clothTxt.setText(mWeatherInfo.HeWeatherDataService3.get(0).suggestion.drsg.txt);
//
//                ((SuggestionViewHolder) holder).sportBrief.setText("运动指数---" + mWeatherInfo.HeWeatherDataService3.get(0).suggestion.sport.brf);
//                ((SuggestionViewHolder) holder).sportTxt.setText(mWeatherInfo.HeWeatherDataService3.get(0).suggestion.sport.txt);
//
//                ((SuggestionViewHolder) holder).travelBrief.setText("旅游指数---" + mWeatherInfo.HeWeatherDataService3.get(0).suggestion.trav.brf);
//                ((SuggestionViewHolder) holder).travelTxt.setText(mWeatherInfo.HeWeatherDataService3.get(0).suggestion.trav.txt);
//
//                ((SuggestionViewHolder) holder).fluBrief.setText("感冒指数---" + mWeatherInfo.HeWeatherDataService3.get(0).suggestion.flu.brf);
//                ((SuggestionViewHolder) holder).fluTxt.setText(mWeatherInfo.HeWeatherDataService3.get(0).suggestion.flu.txt);
//            }

        }

        if (holder instanceof ForecastViewHolder) {
            //今日 明日
            ((ForecastViewHolder) holder).forecastDate[0].setText("今日");
            ((ForecastViewHolder) holder).forecastDate[1].setText("明日");
            int icon_id;
//            if (mSharedPreferences.getBoolean("isReadCache", false)==true) {
                for (int i = 0; i < mSharedPreferences.getInt("dailySize", 0); i++) {
                    if (i > 1) {
                        try {
                            ((ForecastViewHolder) holder).forecastDate[i].setText(dayForWeek(mSharedPreferences.getString("forecastDate" + i, null)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    icon_id = mSharedPreferences.getInt(mSharedPreferences.getString("forecastIcon" + i, null), R.mipmap.none);
                    ((ForecastViewHolder) holder).forecastIcon[i].setImageResource(icon_id);
                    ((ForecastViewHolder) holder).forecastTemp[i].setText(mSharedPreferences.getString("tmpmin" + i, null) + "° " + mSharedPreferences.getString("tmpmax" + i, null) + "°");
                    ((ForecastViewHolder) holder).forecastTxt[i].setText(
                            mSharedPreferences.getString("condtxt" + i, null) + "。 最高" +
                                    mSharedPreferences.getString("tmpmax" + i, null) + "℃。 " +
                                    mSharedPreferences.getString("windsc" + i, null) + " " +
                                    mSharedPreferences.getString("winddir" + i, null) + " " +
                                    mSharedPreferences.getString("windspd" + i, null) + " km/h。 " +
                                    "降水几率 " + "" + mSharedPreferences.getString("pop" + i, null) + "%。");
                }
//            } else {
//                for (int i = 0; i < mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.size(); i++) {
//                    if (i > 1) {
//                        try {
//                            ((ForecastViewHolder) holder).forecastDate[i].setText(dayForWeek(mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).date));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    icon_id = mSharedPreferences.getInt(mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).cond.txt_d, R.mipmap.none);
//                    ((ForecastViewHolder) holder).forecastIcon[i].setImageResource(icon_id);
//                    ((ForecastViewHolder) holder).forecastTemp[i].setText(
//                            mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).tmp.min + "° " +
//                                    mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).tmp.max + "°");
//                    ((ForecastViewHolder) holder).forecastTxt[i].setText(
//                            mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).cond.txt_d + "。 最高" +
//                                    mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).tmp.max + "℃。 " +
//                                    mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).wind.sc + " " +
//                                    mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).wind.dir + " " +
//                                    mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).wind.spd + " km/h。 " +
//                                    "降水几率 " +
//                                    "" + mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.get(i).pop + "%。");
//                }
//            }

        }
        editor.putBoolean("isCache",true);
        Log.d("test","commit");
        editor.commit();
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        String week = "";
        dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TYPE_ONE) {
            return TYPE_ONE;
        }
        if (position == TYPE_TWO) {
            return TYPE_TWO;
        }
        if (position == TYPE_THREE) {
            return TYPE_THREE;
        }
        if (position == TYPE_FORE) {
            return TYPE_FORE;
        }
        return super.getItemViewType(position);
    }


    /**
     * 当前天气情况
     */
    class NowWeatherViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView weatherIcon;
        private TextView tempFlu;
        private TextView tempMax;
        private TextView tempMin;

        private TextView tempPm;
        private TextView tempQuality;


        public NowWeatherViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            weatherIcon = (ImageView) itemView.findViewById(R.id.weather_icon);
            tempFlu = (TextView) itemView.findViewById(R.id.temp_flu);
            tempMax = (TextView) itemView.findViewById(R.id.temp_max);
            tempMin = (TextView) itemView.findViewById(R.id.temp_min);

            tempPm = (TextView) itemView.findViewById(R.id.temp_pm);
            tempQuality = (TextView) itemView.findViewById(R.id.temp_quality);
        }
    }

    /**
     * 当日小时预告
     */
    class HoursWeatherViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemHourInfoLinearlayout;
        private TextView[] mClock;
        private TextView[] mTemp;
        private TextView[] mHumidity;
        private TextView[] mWind;

        public HoursWeatherViewHolder(View itemView) {
            super(itemView);
//            if (mSharedPreferences.getBoolean("isReadCache", false)==true) {
                int hourlySize = mSharedPreferences.getInt("hourlySize", 0);
                mClock = new TextView[hourlySize];
                mTemp = new TextView[hourlySize];
                mHumidity = new TextView[hourlySize];
                mWind = new TextView[hourlySize];
                itemHourInfoLinearlayout = (LinearLayout) itemView.findViewById(R.id.item_hour_info_linearlayout);
                for (int i = 0; i < hourlySize; i++) {
                    View view = View.inflate(mContext, R.layout.rv_item2_line, null);
                    mClock[i] = (TextView) view.findViewById(R.id.one_clock);
                    mTemp[i] = (TextView) view.findViewById(R.id.one_temp);
                    mHumidity[i] = (TextView) view.findViewById(R.id.one_humidity);
                    mWind[i] = (TextView) view.findViewById(R.id.one_wind);
                    itemHourInfoLinearlayout.addView(view);
                }
//            } else {
//                mClock = new TextView[mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.size()];
//                mTemp = new TextView[mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.size()];
//                mHumidity = new TextView[mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.size()];
//                mWind = new TextView[mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.size()];
//                itemHourInfoLinearlayout = (LinearLayout) itemView.findViewById(R.id.item_hour_info_linearlayout);
//                for (int i = 0; i < mWeatherInfo.HeWeatherDataService3.get(0).hourly_forecast.size(); i++) {
//                    View view = View.inflate(mContext, R.layout.rv_item2_line, null);
//                    mClock[i] = (TextView) view.findViewById(R.id.one_clock);
//                    mTemp[i] = (TextView) view.findViewById(R.id.one_temp);
//                    mHumidity[i] = (TextView) view.findViewById(R.id.one_humidity);
//                    mWind[i] = (TextView) view.findViewById(R.id.one_wind);
//                    itemHourInfoLinearlayout.addView(view);
//                }
//            }
        }
    }

    /**
     * 当日建议
     */
    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView clothBrief;
        private TextView clothTxt;
        private TextView sportBrief;
        private TextView sportTxt;
        private TextView travelBrief;
        private TextView travelTxt;
        private TextView fluBrief;
        private TextView fluTxt;


        public SuggestionViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            clothBrief = (TextView) itemView.findViewById(R.id.cloth_brief);
            clothTxt = (TextView) itemView.findViewById(R.id.cloth_txt);
            sportBrief = (TextView) itemView.findViewById(R.id.sport_brief);
            sportTxt = (TextView) itemView.findViewById(R.id.sport_txt);
            travelBrief = (TextView) itemView.findViewById(R.id.travel_brief);
            travelTxt = (TextView) itemView.findViewById(R.id.travel_txt);
            fluBrief = (TextView) itemView.findViewById(R.id.flu_brief);
            fluTxt = (TextView) itemView.findViewById(R.id.flu_txt);
        }
    }

    /**
     * 未来天气
     */
    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout forecastLinear;
        private TextView[] forecastDate;
        private TextView[] forecastTemp;
        private TextView[] forecastTxt;
        private ImageView[] forecastIcon;

        public ForecastViewHolder(View itemView) {
            super(itemView);
//            if (mSharedPreferences.getBoolean("isReadCache", false)==true) {
                forecastLinear = (LinearLayout) itemView.findViewById(R.id.forecast_linear);
                int dailySize = mSharedPreferences.getInt("dailySize", 0);
                forecastDate = new TextView[dailySize];
                forecastTemp = new TextView[dailySize];
                forecastTxt = new TextView[dailySize];
                forecastIcon = new ImageView[dailySize];
                for (int i = 0; i < dailySize; i++) {
                    View view = View.inflate(mContext, R.layout.rv_item4_line, null);
                    forecastDate[i] = (TextView) view.findViewById(R.id.forecast_date);
                    forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
                    forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
                    forecastIcon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
                    forecastLinear.addView(view);
                }
//            } else {
//                forecastLinear = (LinearLayout) itemView.findViewById(R.id.forecast_linear);
//                forecastDate = new TextView[mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.size()];
//                forecastTemp = new TextView[mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.size()];
//                forecastTxt = new TextView[mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.size()];
//                forecastIcon = new ImageView[mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.size()];
//                for (int i = 0; i < mWeatherInfo.HeWeatherDataService3.get(0).daily_forecast.size(); i++) {
//                    View view = View.inflate(mContext, R.layout.rv_item4_line, null);
//                    forecastDate[i] = (TextView) view.findViewById(R.id.forecast_date);
//                    forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
//                    forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
//                    forecastIcon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
//                    forecastLinear.addView(view);
//                }
//            }
        }
    }
}
