package com.triptik.dev.triptik;

import com.triptik.dev.triptik.gallery.GalleryValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public List<GalleryValue> parse(JSONObject jsonObject) {
        List<GalleryValue> postList = new ArrayList<>();
        GalleryValue galleryValue;
        try {
            JSONArray postsArray = jsonObject.getJSONArray("posts");

            for (int i = 0; i < postsArray.length(); i++) {

                JSONObject posts = postsArray.getJSONObject(i);
                JSONObject post = posts.getJSONObject("post");

                galleryValue = new GalleryValue();

                    String triptikTitle = post.getString("triptikTitle");
                    String triptikID = post.getString("triptikID");
                    String creationDate = post.getString("creation_date");
                    String creationTime = post.getString("creation_time");
                    String userID = post.getString("userID");
                    String username = post.getString("username");
//                    String commentCount = post.getString("commentCount");

                    galleryValue.setTriptikTitle(triptikTitle);
                    galleryValue.setTriptikID(triptikID);
                    galleryValue.setCreationDate(creationDate);
                    galleryValue.setCreationTime(creationTime);
                    galleryValue.setUserID(userID);
                    galleryValue.setUserName(username);
//                    galleryValue.setCommentTotal(commentCount);
                    postList.add(galleryValue);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postList;
    }

}
