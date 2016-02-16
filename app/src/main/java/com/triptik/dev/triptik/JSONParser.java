package com.triptik.dev.triptik;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public List<PostValue> parse(JSONObject jsonObject) {
        List<PostValue> postList = new ArrayList<>();
        PostValue postValue;
        try {
            JSONArray postsArray = jsonObject.getJSONArray("posts");

            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject posts = postsArray.getJSONObject(i);
                JSONObject post = posts.getJSONObject("post");

                postValue = new PostValue();

                String triptikTitle = post.getString("triptikTitle");
                String triptikID = post.getString("triptikID");
                String creationDate = post.getString("creation_date");
                String creationTime = post.getString("creation_time");
                String userID = post.getString("userID");
                String username = post.getString("username");

                postValue.setTriptikTitle(triptikTitle);
                postValue.setTriptikID(triptikID);
                postValue.setCreationDate(creationDate);
                postValue.setCreationTime(creationTime);
                postValue.setUserID(userID);
                postValue.setUserName(username);
                postList.add(postValue);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postList;
    }

}
