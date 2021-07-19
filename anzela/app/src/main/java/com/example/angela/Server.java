package com.example.angela;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Server extends Thread {



    public String get(String method, String uri,String content) {

        String result = null;
        URL url;
        int responseCode = 0;

        try {
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdW5oeWVvbiIsImlhdCI6MTYyNTA0MTY4MiwiZXhwIjoxNjU2NTc3NjgyfQ.NFnGiqMVgMQMUgO0GdNODedEwp44pl4VjsDr99ilacA";
            String line = "Bearer " + token;
            url = new URL("http://3.35.241.60" + uri);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);

            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod(method);

            if(method.equals("POST")){
                urlConnection.setDoOutput(true);
            }

            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestProperty("content-type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Authorization", line);

            if(urlConnection.getDoOutput()){
                String obj = content;
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(content);
                wr.flush();
                wr.close();
            }


             responseCode = urlConnection.getResponseCode();

            Log.d("posts","주소는 " + url);
            Log.d("code","응답코드" + responseCode);
            Log.d("message","응답메시지" + urlConnection.getResponseMessage());

            urlConnection.connect();
            StringBuffer response = new StringBuffer();

            if  (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
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
            Log.e("TAG", "ConnectException "+ responseCode);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Post> getPosts() throws JSONException {
        String page = "?page="+1;

        String result = get("GET","/api/v1/posts"+page,null);

        JSONObject main = new JSONObject(result);

        JSONArray bodyContent = main.getJSONObject("data").getJSONArray("content");

        ArrayList<Post> PostList = new ArrayList<>();

        Log.e("GETPOSTS",""+bodyContent);
        for(int i = 0; i < bodyContent.length(); i++){

            Post post = new Post();

            JSONObject item = bodyContent.getJSONObject(i);
            JSONObject User = item.getJSONObject("user");

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
            post.setUid(User.getString("uid"));
            post.setProfileUrl(User.getString("profileUrl"));



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
        Log.d("TAG",""+PostList.size());

        return PostList;
    }



    public ArrayList<Post> getAround() throws JSONException {
        double lat = 37.123;
        double lng = 126.12634;
        String req = "?page="+ 1 + "&lat=" + lat + "&lng=" + lng;

        String result = get("GET","/api/v1/posts/around"+req,null);

        JSONObject main = new JSONObject(result);
//        Log.e("getAround",""+main);

        JSONArray bodyContent = main.getJSONObject("data").getJSONArray("content");

        Log.e("GETAround",""+bodyContent);

        ArrayList<Post> AroundList = new ArrayList<>();

        for (int i = 0; i < bodyContent.length(); i++) {
            Post around = new Post();

            JSONObject item = bodyContent.getJSONObject(i);
            JSONObject User = item.getJSONObject("user");

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
            around.setUid(User.getString("uid"));
            around.setProfileUrl(User.getString("profileUrl"));

            AroundList.add(around);
        }

        for (int i = 0; i < AroundList.size(); i++) {
            Log.e("AroundList",""+AroundList.get(i));

        }
        return AroundList;
    }

    public ArrayList<Post> getSoon() throws JSONException{
        String date = "20210131";
        String result = get("GET","/api/v1/posts/soon?date="+date,null);

        JSONObject main = new JSONObject(result);
        //Log.e("SOON",""+main);

        JSONArray bodyContent = main.getJSONObject("data").getJSONArray("content");
        Log.e("GETSoon",""+bodyContent);
        ArrayList<Post> SoonList = new ArrayList<>();

        for (int i = 0; i < bodyContent.length(); i++) {

            Post soon = new Post();

            JSONObject item = bodyContent.getJSONObject(i);
            JSONObject User = item.getJSONObject("user");

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

            soon.setUid(User.getString("uid"));
            soon.setProfileUrl(User.getString("profileUrl"));

            SoonList.add(soon);

        }

        for (int i = 0; i < SoonList.size(); i++) {
            Log.e("soonList",""+SoonList.get(i));
        }
        return SoonList;
    }

    public DetailPost getDetail(int postId) throws JSONException {
        int id = postId;
        String result = get("GET", "/api/v1/posts/" + id,null);

        JSONObject main = new JSONObject(result);

        JSONObject bodyContent = main.getJSONObject("data");
        Log.e("DetailBodyContent",""+bodyContent);

        DetailPost detailPost = new DetailPost();

        detailPost.setId(bodyContent.getInt("id"));
        detailPost.setTitle(bodyContent.getString("title"));
        detailPost.setContent(bodyContent.getString("content"));
        detailPost.setCruCnt(bodyContent.getInt("cruCnt"));
        detailPost.setStartDate(bodyContent.getString("startDate"));
        detailPost.setStartPoint(bodyContent.getString("startPoint"));
        detailPost.setStartLat(bodyContent.getDouble("startLat"));
        detailPost.setStartLng(bodyContent.getDouble("startLng"));

        if(bodyContent.has("endPoint")){
            detailPost.setEndPoint(bodyContent.getString("endPoint"));
        }
        detailPost.setEndLat(bodyContent.getDouble("endLat"));
        detailPost.setEndLng(bodyContent.getDouble("endLng"));
        detailPost.setCmtCnt(bodyContent.getInt("cmtCnt"));
        detailPost.setRegDate(bodyContent.getString("regDate"));

        //User
        JSONObject User = bodyContent.getJSONObject("user");
        detailPost.setUid(User.getString("uid"));
        detailPost.setProfileUrl(User.getString("profileUrl"));




        //Comments

        JSONArray Comments = bodyContent.getJSONArray("comments");
        for (int i = 0; i < Comments.length(); i++) {

            JSONObject CommentsItem = Comments.getJSONObject(i);
            detailPost.setCmId(CommentsItem.getInt("id"));
            detailPost.setCmContent(CommentsItem.getString("content"));
            detailPost.setCmDepth(CommentsItem.getInt("depth"));
            detailPost.setCmregDate(CommentsItem.getString("regDate"));

            JSONObject cmUser =  CommentsItem.getJSONObject("user");

            detailPost.setCmUid(cmUser.getString("id"));
            detailPost.setCmprofilUrl(cmUser.getString("profileUrl"));
        }



        Log.e("DETAILPOST",""+detailPost);

        return detailPost;
        }


    public String postWrite(Post post) throws JSONException {


        JSONObject obj = new JSONObject();
        obj.put("title",post.getTitle());
        obj.put("content",post.getContent());
        obj.put("cruCnt",post.getCurCnt());
        obj.put("startDate",post.getStartDate());
        obj.put("startPoint",post.getStartPoint());
        obj.put("endPoint",post.getEndPoint());

        Log.e("OBJ",""+ obj);

        String writeResult = obj.toString();
        Log.e("Write",""+writeResult);

        get("POST","/api/v1/posts",writeResult);

        return writeResult;
    }
}

