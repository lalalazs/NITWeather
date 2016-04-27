package com.hzs.nitweather.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hzs on 2016/4/13.
 */
public class ReadUtils {
    public static StringBuilder getResponseFromeHttp(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while((line = reader.readLine())!=null){
            response.append(line);
        }
        return response;
    }


}
