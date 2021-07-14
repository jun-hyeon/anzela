package com.example.angela;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Server extends Thread {

    URL url;

    public String get(String method, String uri) {

        String result = null;

        try {
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdW5oeWVvbiIsImlhdCI6MTYyNTA0MTY4MiwiZXhwIjoxNjU2NTc3NjgyfQ.NFnGiqMVgMQMUgO0GdNODedEwp44pl4VjsDr99ilacA";
            String line = "Bearer " + token;
            url = new URL("http://15.165.35.47" + uri);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod(method);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestProperty("content-type", "application/json");
            urlConnection.setRequestProperty("Authorization", line);


            int responseCode = urlConnection.getResponseCode();

            Log.d("posts","주소는 " + url);
            Log.d("code","응답코드" + responseCode);
            Log.d("message","응답메시지" + urlConnection.getResponseMessage());

            urlConnection.connect();
            StringBuffer response = new StringBuffer();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }

            result = response.toString();
            urlConnection.disconnect();

          //  Log.d("TAG",result);
        } catch (ConnectException e) {
            Log.e("TAG", "ConnectException ");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Post> getPosts() throws JSONException {
        String page = "?page="+1;

        String result = get("GET","/api/v1/posts"+page);

        JSONObject main = new JSONObject(result);

        JSONArray bodyContent = main.getJSONObject("data").getJSONArray("content");
        ArrayList<Post> PostList = new ArrayList<>();


        for(int i = 0; i < bodyContent.length(); i++){
            Post post = new Post();

            JSONObject item = bodyContent.getJSONObject(i);
            post.setId(item.getInt("id"));
            post.setTitle(item.getString("title"));
            post.setContent(item.getString("content"));
            post.setCurCnt(item.getInt("cruCnt"));
            post.setStartDate(item.getString("startDate"));
            post.setStartPoint(item.getString("startPoint"));
            post.setStartLat(item.getDouble("startLat"));
            post.setStartLng(item.getDouble("startLng"));
            post.setCmtCnt(item.getInt("cmtCnt"));
            post.setRegDate(item.getString("regDate"));

            if(item.has("endPoint")) {
                post.setEndPoint(item.getString("endPoint"));
            }
            if(item.has("endLat")){
                post.setEndLat(item.getDouble("endLat"));
            }
            if(item.has("endLng")){
                post.setEndLng(item.getDouble("endLng"));
            }
            PostList.add(post);
        }

        for (int i = 0; i < PostList.size(); i++) {
         Log.e("postList",""+PostList.get(i));     //size = 2
        }
//        Log.d("TAG",""+postList.size());

        return PostList;
    }



    public ArrayList<Post> getAround() throws JSONException {
        double lat = 37.123;
        double lng = 126.12634;
        String req = "?page="+ 1 + "&lat=" + lat + "&lng=" + lng;

        String result = get("GET","/api/v1/posts/around"+req);

        JSONObject main = new JSONObject(result);
//        Log.e("getAround",""+main);

        JSONArray bodyContent = main.getJSONObject("data").getJSONArray("content");
      //  Log.d("bodyContent",""+bodyContent);

        ArrayList<Post> AroundList = new ArrayList<>();

        for (int i = 0; i < bodyContent.length(); i++) {
            Post around = new Post();
            JSONObject item = bodyContent.getJSONObject(i);
            around.setId(item.getInt("id"));
            around.setTitle(item.getString("title"));
            around.setContent(item.getString("content"));
            around.setCurCnt(item.getInt("cruCnt"));
            around.setStartDate(item.getString("startDate"));
            around.setStartPoint(item.getString("startPoint"));
            around.setStartLat(item.getDouble("startLat"));
            around.setStartLng(item.getDouble("startLng"));
            around.setCmtCnt(item.getInt("cmtCnt"));
            around.setRegDate(item.getString("regDate"));

            if(item.has("endPoint")) {
                around.setEndPoint(item.getString("endPoint"));
            }
            if(item.has("endLat")){
                around.setEndLat(item.getDouble("endLat"));
            }
            if(item.has("endLng")){
                around.setEndLng(item.getDouble("endLng"));
            }
            AroundList.add(around);
        }

        for (int i = 0; i < AroundList.size(); i++) {
            Log.e("AroundList",""+AroundList.get(i));

        }
        return AroundList;
    }

    public ArrayList<Post> getSoon() throws JSONException{
        String date = "20210131";
        String result = get("GET","/api/v1/posts/soon"+"?date="+date);

        JSONObject main = new JSONObject(result);
        //Log.e("SOON",""+main);

        JSONArray bodyContent = main.getJSONObject("data").getJSONArray("content");

        ArrayList<Post> SoonList = new ArrayList<>();

        for (int i = 0; i < bodyContent.length(); i++) {
            Post soon = new Post();
            JSONObject item = bodyContent.getJSONObject(i);
            soon.setId(item.getInt("id"));
            soon.setTitle(item.getString("title"));
            soon.setContent(item.getString("content"));
            soon.setCurCnt(item.getInt("cruCnt"));
            soon.setStartDate(item.getString("startDate"));
            soon.setStartPoint(item.getString("startPoint"));
            soon.setStartLat(item.getDouble("startLat"));
            soon.setStartLng(item.getDouble("startLng"));
            soon.setCmtCnt(item.getInt("cmtCnt"));
            soon.setRegDate(item.getString("regDate"));

            if(item.has("endPoint")) {
                soon.setEndPoint(item.getString("endPoint"));
            }
            if(item.has("endLat")){
                soon.setEndLat(item.getDouble("endLat"));
            }
            if(item.has("endLng")){
                soon.setEndLng(item.getDouble("endLng"));
            }

            SoonList.add(soon);

        }

        for (int i = 0; i < SoonList.size(); i++) {
            Log.e("SOONLIST",""+SoonList.get(i));
        }
        return SoonList;
    }

    public void getDetail(String postId) throws JSONException {
        String id = postId;
        String result = get("GET", "/api/v1/posts/" + id);

        JSONObject main = new JSONObject(result);
//        Log.e("detial",""+main);
        JSONObject bodyContent = main.getJSONObject("data");
        Log.e("DETAILDATA",""+bodyContent);

        ArrayList<Post> DetailList = new ArrayList<>();


            Post detail = new Post();
            detail.setId(bodyContent.getInt("id"));
            detail.setTitle(bodyContent.getString("title"));
            detail.setContent(bodyContent.getString("content"));
            detail.setCurCnt(bodyContent.getInt("cruCnt"));
            detail.setStartDate(bodyContent.getString("startDate"));
            detail.setStartPoint(bodyContent.getString("startDate"));
            detail.setStartLat(bodyContent.getDouble("startLat"));
            detail.setStartLng(bodyContent.getDouble("startLng"));
            detail.setCmtCnt(bodyContent.getInt("cmtCnt"));
            detail.setRegDate(bodyContent.getString("regDate"));

            if(bodyContent.has("endPoint")) {
                detail.setEndPoint(bodyContent.getString("endPoint"));
            }
            if(bodyContent.has("endLat")){
                detail.setEndLat(bodyContent.getDouble("endLat"));
            }
            if(bodyContent.has("endLng")){
                detail.setEndLng(bodyContent.getDouble("endLng"));


            DetailList.add(detail);
//            Log.e("DETAIL", "" + main);
        }

            Log.e("DETAILlist",""+DetailList);
    }
}
