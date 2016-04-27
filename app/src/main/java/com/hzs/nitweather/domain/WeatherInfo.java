package com.hzs.nitweather.domain;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hzs on 2016/4/18.
 */
public class WeatherInfo {

    /**
     * aqi : {"city":{"aqi":"54","co":"1","no2":"34","o3":"122","pm10":"36","pm25":"15","qlty":"良","so2":"13"}}
     * basic : {"city":"宁波","cnty":"中国","id":"CN101210401","lat":"29.872000","lon":"121.542000","update":{"loc":"2016-04-18 16:50","utc":"2016-04-18 08:50"}}
     * daily_forecast : [{"astro":{"sr":"05:23","ss":"18:22"},"cond":{"code_d":"101","code_n":"101","txt_d":"多云","txt_n":"多云"},"date":"2016-04-18","hum":"64","pcpn":"9.8","pop":"99","pres":"1019","tmp":{"max":"20","min":"9"},"vis":"10","wind":{"deg":"66","dir":"东南风","sc":"微风","spd":"9"}},{"astro":{"sr":"05:22","ss":"18:23"},"cond":{"code_d":"101","code_n":"101","txt_d":"多云","txt_n":"多云"},"date":"2016-04-19","hum":"52","pcpn":"0.0","pop":"0","pres":"1022","tmp":{"max":"23","min":"14"},"vis":"10","wind":{"deg":"138","dir":"东南风","sc":"微风","spd":"9"}},{"astro":{"sr":"05:21","ss":"18:24"},"cond":{"code_d":"104","code_n":"300","txt_d":"阴","txt_n":"阵雨"},"date":"2016-04-20","hum":"72","pcpn":"2.8","pop":"76","pres":"1012","tmp":{"max":"23","min":"18"},"vis":"5","wind":{"deg":"169","dir":"东南风","sc":"3-4","spd":"15"}},{"astro":{"sr":"05:20","ss":"18:24"},"cond":{"code_d":"300","code_n":"300","txt_d":"阵雨","txt_n":"阵雨"},"date":"2016-04-21","hum":"68","pcpn":"17.5","pop":"75","pres":"1010","tmp":{"max":"23","min":"16"},"vis":"5","wind":{"deg":"339","dir":"西北风","sc":"微风","spd":"10"}},{"astro":{"sr":"05:19","ss":"18:25"},"cond":{"code_d":"101","code_n":"300","txt_d":"多云","txt_n":"阵雨"},"date":"2016-04-22","hum":"50","pcpn":"0.0","pop":"9","pres":"1010","tmp":{"max":"22","min":"16"},"vis":"10","wind":{"deg":"71","dir":"东南风","sc":"微风","spd":"9"}},{"astro":{"sr":"05:18","ss":"18:25"},"cond":{"code_d":"101","code_n":"101","txt_d":"多云","txt_n":"多云"},"date":"2016-04-23","hum":"65","pcpn":"1.1","pop":"53","pres":"1014","tmp":{"max":"23","min":"16"},"vis":"10","wind":{"deg":"144","dir":"东南风","sc":"微风","spd":"6"}},{"astro":{"sr":"05:17","ss":"18:26"},"cond":{"code_d":"101","code_n":"104","txt_d":"多云","txt_n":"阴"},"date":"2016-04-24","hum":"85","pcpn":"8.4","pop":"50","pres":"1015","tmp":{"max":"23","min":"15"},"vis":"2","wind":{"deg":"107","dir":"北风","sc":"3-4","spd":"16"}}]
     * hourly_forecast : [{"date":"2016-04-18 16:00","hum":"68","pop":"0","pres":"1019","tmp":"18","wind":{"deg":"82","dir":"东风","sc":"微风","spd":"12"}},{"date":"2016-04-18 19:00","hum":"80","pop":"0","pres":"1021","tmp":"16","wind":{"deg":"114","dir":"东南风","sc":"微风","spd":"10"}},{"date":"2016-04-18 22:00","hum":"87","pop":"0","pres":"1022","tmp":"13","wind":{"deg":"144","dir":"东南风","sc":"微风","spd":"8"}}]
     * now : {"cond":{"code":"101","txt":"多云"},"fl":"21","hum":"41","pcpn":"0","pres":"1018","tmp":"19","vis":"10","wind":{"deg":"240","dir":"南风","sc":"3-4","spd":"12"}}
     * status : ok
     * suggestion : {"comf":{"brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"},"cw":{"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},"drsg":{"brf":"较舒适","txt":"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"},"flu":{"brf":"较易发","txt":"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。"},"sport":{"brf":"较适宜","txt":"天气较好，但考虑气温较低，推荐您进行室内运动，若户外适当增减衣物并注意防晒。"},"trav":{"brf":"适宜","txt":"天气较好，但丝毫不会影响您出行的心情。温度适宜又有微风相伴，适宜旅游。"},"uv":{"brf":"弱","txt":"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"}}
     */
    @SerializedName("HeWeather data service 3.0")
    public List<HeWeatherDataService3Bean> HeWeatherDataService3;


    public static class HeWeatherDataService3Bean {
        /**
         * city : {"aqi":"54","co":"1","no2":"34","o3":"122","pm10":"36","pm25":"15","qlty":"良","so2":"13"}
         */

        public AqiBean aqi;
        /**
         * city : 宁波
         * cnty : 中国
         * id : CN101210401
         * lat : 29.872000
         * lon : 121.542000
         * update : {"loc":"2016-04-18 16:50","utc":"2016-04-18 08:50"}
         */

        public BasicBean basic;
        /**
         * cond : {"code":"101","txt":"多云"}
         * fl : 21
         * hum : 41
         * pcpn : 0
         * pres : 1018
         * tmp : 19
         * vis : 10
         * wind : {"deg":"240","dir":"南风","sc":"3-4","spd":"12"}
         */

        public NowBean now;
        public String status;
        /**
         * comf : {"brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"}
         * cw : {"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"}
         * drsg : {"brf":"较舒适","txt":"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"}
         * flu : {"brf":"较易发","txt":"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。"}
         * sport : {"brf":"较适宜","txt":"天气较好，但考虑气温较低，推荐您进行室内运动，若户外适当增减衣物并注意防晒。"}
         * trav : {"brf":"适宜","txt":"天气较好，但丝毫不会影响您出行的心情。温度适宜又有微风相伴，适宜旅游。"}
         * uv : {"brf":"弱","txt":"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"}
         */

        public SuggestionBean suggestion;
        /**
         * astro : {"sr":"05:23","ss":"18:22"}
         * cond : {"code_d":"101","code_n":"101","txt_d":"多云","txt_n":"多云"}
         * date : 2016-04-18
         * hum : 64
         * pcpn : 9.8
         * pop : 99
         * pres : 1019
         * tmp : {"max":"20","min":"9"}
         * vis : 10
         * wind : {"deg":"66","dir":"东南风","sc":"微风","spd":"9"}
         */

        public List<DailyForecastBean> daily_forecast;
        /**
         * date : 2016-04-18 16:00
         * hum : 68
         * pop : 0
         * pres : 1019
         * tmp : 18
         * wind : {"deg":"82","dir":"东风","sc":"微风","spd":"12"}
         */

        public List<HourlyForecastBean> hourly_forecast;


        public static class AqiBean {
            /**
             * aqi : 54
             * co : 1
             * no2 : 34
             * o3 : 122
             * pm10 : 36
             * pm25 : 15
             * qlty : 良
             * so2 : 13
             */

            public CityBean city;


            public static class CityBean {
                public String aqi;
                public String co;
                public String no2;
                public String o3;
                public String pm10;
                public String pm25;
                public String qlty;
                public String so2;

            }
        }

        public static class BasicBean {
            public String city;
            public String cnty;
            public String id;
            public String lat;
            public String lon;
            /**
             * loc : 2016-04-18 16:50
             * utc : 2016-04-18 08:50
             */

            public UpdateBean update;


            public static class UpdateBean {
                public String loc;
                public String utc;

            }
        }

        public static class NowBean {
            /**
             * code : 101
             * txt : 多云
             */

            public CondBean cond;
            public String fl;
            public String hum;
            public String pcpn;
            public String pres;
            public String tmp;
            public String vis;
            /**
             * deg : 240
             * dir : 南风
             * sc : 3-4
             * spd : 12
             */

            public WindBean wind;


            public static class CondBean {
                public String code;
                public String txt;

            }

            public static class WindBean {
                public String deg;
                public String dir;
                public String sc;
                public String spd;

            }
        }

        public static class SuggestionBean {
            /**
             * brf : 舒适
             * txt : 白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。
             */

            public ComfBean comf;
            /**
             * brf : 较适宜
             * txt : 较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。
             */

            public CwBean cw;
            /**
             * brf : 较舒适
             * txt : 建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。
             */

            public DrsgBean drsg;
            /**
             * brf : 较易发
             * txt : 昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。
             */

            public FluBean flu;
            /**
             * brf : 较适宜
             * txt : 天气较好，但考虑气温较低，推荐您进行室内运动，若户外适当增减衣物并注意防晒。
             */

            public SportBean sport;
            /**
             * brf : 适宜
             * txt : 天气较好，但丝毫不会影响您出行的心情。温度适宜又有微风相伴，适宜旅游。
             */

            public TravBean trav;
            /**
             * brf : 弱
             * txt : 紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。
             */

            public UvBean uv;


            public static class ComfBean {
                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class CwBean {
                public String brf;
                public String txt;

            }

            public static class DrsgBean {
                public String brf;
                public String txt;

            }

            public static class FluBean {
                public String brf;
                public String txt;

            }

            public static class SportBean {
                public String brf;
                public String txt;

            }

            public static class TravBean {
                public String brf;
                public String txt;

            }

            public static class UvBean {
                public String brf;
                public String txt;

            }
        }

        public static class DailyForecastBean {
            /**
             * sr : 05:23
             * ss : 18:22
             */

            public AstroBean astro;
            /**
             * code_d : 101
             * code_n : 101
             * txt_d : 多云
             * txt_n : 多云
             */

            public CondBean cond;
            public String date;
            public String hum;
            public String pcpn;
            public String pop;
            public String pres;
            /**
             * max : 20
             * min : 9
             */

            public TmpBean tmp;
            public String vis;
            /**
             * deg : 66
             * dir : 东南风
             * sc : 微风
             * spd : 9
             */

            public WindBean wind;


            public static class AstroBean {
                public String sr;
                public String ss;

            }

            public static class CondBean {
                public String code_d;
                public String code_n;
                public String txt_d;
                public String txt_n;

            }

            public static class TmpBean {
                public String max;
                public String min;

            }

            public static class WindBean {
                public String deg;
                public String dir;
                public String sc;
                public String spd;

            }
        }

        public static class HourlyForecastBean {
            public String date;
            public String hum;
            public String pop;
            public String pres;
            public String tmp;
            /**
             * deg : 82
             * dir : 东风
             * sc : 微风
             * spd : 12
             */

            public WindBean wind;


            public static class WindBean {
                public String deg;
                public String dir;
                public String sc;
                public String spd;

            }
        }
    }
}
