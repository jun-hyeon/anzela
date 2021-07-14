package com.example.angela;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather extends Thread{

    String sky_value;
    String temp_value;
    Date date = new Date();

    SimpleDateFormat sdfNowYear = new SimpleDateFormat("yyyyMMdd");
    String strnowYear = sdfNowYear.format(date);
    SimpleDateFormat sdNowTime = new SimpleDateFormat("kk00");
    String time = sdNowTime.format(date);


    public void func() throws IOException, JSONException {

        if(time.equals("0300")||time.equals("0400"))
            time = "0200";
        if(time.equals("0600")||time.equals("0700"))
            time = "0500";
        if(time.equals("0900")||time.equals("1000"))
            time = "0800";
        if(time.equals("1200")||time.equals("1300"))
            time = "1100";
        if(time.equals("1500")||time.equals("1600"))
            time ="1400";
        if(time.equals("1800")||time.equals("1900"))
            time = "1700";
        if(time.equals("2100")||time.equals("2200"))
            time = "2000";
        if(time.equals("2400")||time.equals("0100"))
            time = "2300";


        String endPoint =  "http://apis.data.go.kr/1360000/VilageFcstInfoService/";
        String serviceKey = "yhbA1HQUzkkLYQNVlvBuBP9OREDCYGGgaq5kWTnR9hfnA566qw26PX2fcI8%2BHRfxj8KsE503qHEScWiglW4rMA%3D%3D";
        String pageNo = "1";
        String numOfRows = "10";
        String baseDate = strnowYear; //원하는 날짜
        String baseTime = time; //원하는 시간
        String nx = "58"; //위경도임.
        String ny = "125"; //위경도 정보는 api문서 볼 것

        System.out.println("날짜는 "+strnowYear+" 시간은 "+time);

        String s = endPoint+"getVilageFcst?serviceKey="+serviceKey
                +"&pageNo=" + pageNo
                +"&numOfRows=" + numOfRows
                +"+&dataType=JSON"
                + "&base_date=" + baseDate
                +"&base_time="+baseTime
                +"&nx="+nx
                +"&ny="+ny;

        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader bufferedReader = null;
        if(conn.getResponseCode() == 200) {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }else{
            //connection error :(
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        String result= stringBuilder.toString();
//        System.out.println("날씨  " + result);
        conn.disconnect();

        JSONObject mainObject = new JSONObject(result);
        JSONArray itemArray = mainObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
        for(int i=0; i<itemArray.length(); i++){
            JSONObject item = itemArray.getJSONObject(i);
            String category = item.getString("category");
            String value = item.getString("fcstValue");

            if(category.equals("SKY")){
                sky_value = value;
            }
            if(category.equals("T3H")){
                temp_value = value;
            }

         //   System.out.println(category+"  "+value);
        }
    }

    public String getTemp_value(){
        return temp_value;
    }

    public String getSky_value(){
        if(sky_value.equals("1"))
            return "맑음";

        if(sky_value.equals("3"))
            return "구름많음";

        if(sky_value.equals("4"))
            return "흐림";

        return sky_value;
    }


}
