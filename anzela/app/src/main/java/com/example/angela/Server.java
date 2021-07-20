package com.example.angela;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Server extends Thread{



    public String get(String method,boolean output ,String uri,String content ) {

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


            urlConnection.setDoOutput(output);


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

        String result = get("GET",false,"/api/v1/posts"+page,null);

        JSONObject main = new JSONObject(result);

        JSONArray bodyContent = main.getJSONObject("data").getJSONArray("content");

        ArrayList<Post> PostList = new ArrayList<>();

        Log.e("GETPOSTS",""+bodyContent);
        for(int i = 0; i < bodyContent.length(); i++){

            Post post = new Post();
            User user = new User();

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
            user.setUid(User.getString("uid"));
            user.setProfileUrl(User.getString("profileUrl"));



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



    public ArrayList<Post> getAround(double lat, double lng) throws JSONException {

        String req = "?page="+ 1 + "&lat=" + lat + "&lng=" + lng;

        String result = get("GET",false,"/api/v1/posts/around"+req,null);

        JSONObject main = new JSONObject(result);
//        Log.e("getAround",""+main);

        JSONArray bodyContent = main.getJSONObject("data").getJSONArray("content");

        Log.e("GETAround",""+bodyContent);

        ArrayList<Post> AroundList = new ArrayList<>();

        for (int i = 0; i < bodyContent.length(); i++) {
            Post around = new Post();
            User user = new User();
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
            user.setUid(User.getString("uid"));
            user.setProfileUrl(User.getString("profileUrl"));

            AroundList.add(around);
        }

        for (int i = 0; i < AroundList.size(); i++) {
            Log.e("AroundList",""+AroundList.get(i));

        }
        return AroundList;
    }

    public ArrayList<Post> getSoon() throws JSONException{
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        String strdate = dateFormat.format(date);
        String result = get("GET",false,"/api/v1/posts/soon?date="+strdate,null);

        JSONObject main = new JSONObject(result);
        //Log.e("SOON",""+main);

        JSONArray bodyContent = main.getJSONObject("data").getJSONArray("content");
        Log.e("GETSoon",""+bodyContent);
        ArrayList<Post> SoonList = new ArrayList<>();

        for (int i = 0; i < bodyContent.length(); i++) {

            Post soon = new Post();
            User user = new User();
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

           user.setUid(User.getString("uid"));
            user.setProfileUrl(User.getString("profileUrl"));


            SoonList.add(soon);

        }

        for (int i = 0; i < SoonList.size(); i++) {
            Log.e("soonList",""+SoonList.get(i));
        }
        return SoonList;
    }

    public Post getDetail(int postId) throws JSONException {
        int id = postId;
        String result = get("GET", false,"/api/v1/posts/" + id,null);

        JSONObject main = new JSONObject(result);

        JSONObject bodyContent = main.getJSONObject("data");
        Log.e("DetailBodyContent",""+bodyContent);

        Post post = new Post();
        User user = new User();

        post.setId(bodyContent.getInt("id"));
        post.setTitle(bodyContent.getString("title"));
        post.setContent(bodyContent.getString("content"));
        post.setCurCnt(bodyContent.getInt("cruCnt"));
        post.setStartDate(bodyContent.getString("startDate"));
        post.setStartPoint(bodyContent.getString("startPoint"));
        post.setStartLat(bodyContent.getDouble("startLat"));
        post.setStartLng(bodyContent.getDouble("startLng"));

        if(bodyContent.has("endPoint")){
            post.setEndPoint(bodyContent.getString("endPoint"));
        }

        post.setEndLat(bodyContent.getDouble("endLat"));
        post.setEndLng(bodyContent.getDouble("endLng"));
        post.setCmtCnt(bodyContent.getInt("cmtCnt"));
        post.setRegDate(bodyContent.getString("regDate"));

        //User
        JSONObject User = bodyContent.getJSONObject("user");
        user.setUid(User.getString("uid"));
        user.setProfileUrl(User.getString("profileUrl"));

        post.setUser(user);

        //Comments

        JSONArray Comments = bodyContent.getJSONArray("comments");
        ArrayList<Comment> comments = new ArrayList<>();

        for (int i = 0; i < Comments.length(); i++) {

            JSONObject CommentsItem = Comments.getJSONObject(i);
            Comment comment = new Comment();
            comment.setId(CommentsItem.getInt("id"));
            comment.setContent(CommentsItem.getString("content"));
            comment.setDepth(CommentsItem.getInt("depth"));
            comment.setRegDate(CommentsItem.getString("regDate"));




//            detailPost.setCmId();
//            detailPost.setCmContent(CommentsItem.getString("content"));
//            detailPost.setCmDepth(CommentsItem.getInt("depth"));
//            detailPost.setCmregDate(CommentsItem.getString("regDate"));

            JSONObject cmUser =  CommentsItem.getJSONObject("user");
            User cUser = new User();
            cUser.setUid(cmUser.getString("uid"));
            cUser.setProfileUrl(cmUser.getString("profileUrl"));

            comment.setUser(cUser);
            comments.add(comment);

//            detailPost.setCmUid(cmUser.getString("id"));
//            detailPost.setCmprofilUrl(cmUser.getString("profileUrl"));

        }
        post.setComments(comments);
        Log.e("DETAILPOST",""+post);
        for (int i = 0; i < comments.size(); i++) {
            Log.e("COMMENTS",""+comments.get(i));
        }

        return post;

        }


    public void postWrite(Post post) throws JSONException {


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


        get("POST",true,"/api/v1/posts",writeResult);

    }

    public void postModify(Post post,int id) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("title",post.getTitle());
        obj.put("content",post.getContent());
        obj.put("cruCnt",post.getCurCnt());
        obj.put("startDate",post.getStartDate());
        obj.put("startPoint",post.getStartPoint());
        obj.put("endPoint",post.getEndPoint());

        String content = obj.toString();

        Log.e("JSONOBJMODIFY",""+obj);

        Log.e("MODIFY",""+content);

        get("POST",true,"/api/v1/posts/"+id,content);

    }

    public void postComment(int id, String content) throws JSONException {

            JSONObject obj = new JSONObject();
            obj.put("content",content);
            String result = obj.toString();

            get("POST",true,"/api/v1/posts/"+id+"/comment",result);
            Log.e("RESULT",""+result);
    }

    public void postDelete(int deleteId){

        get("DELETE",false,"/api/v1/posts/"+deleteId,null);

    }
}

