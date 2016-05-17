package com.hzs.nitweather.domain;

/**
 * Created by hzs on 2016/5/11.
 */
public class VersionInfo {

    /**
     * name : NITWeather
     * version : 1
     * changelog : First release
     * updated_at : 1462940559
     * versionShort : 1.0
     * build : 1
     * installUrl : http://download.fir.im/v2/app/install/5732b2f900fc74170d00000e?download_token=60ac0b326e3d4dfccfe0a212ed3ac9e6
     * install_url : http://download.fir.im/v2/app/install/5732b2f900fc74170d00000e?download_token=60ac0b326e3d4dfccfe0a212ed3ac9e6
     * direct_install_url : http://download.fir.im/v2/app/install/5732b2f900fc74170d00000e?download_token=60ac0b326e3d4dfccfe0a212ed3ac9e6
     * update_url : http://fir.im/nitweather
     * binary : {"fsize":2000663}
     */

    public String name;
    public String version;
    public String changelog;
    public int updated_at;
    public String versionShort;
    public String build;
    public String installUrl;
    public String install_url;
    public String direct_install_url;
    public String update_url;
    /**
     * fsize : 2000663
     */

    public BinaryBean binary;


    public static class BinaryBean {
        public int fsize;

    }
}
