package com.triptik.dev.triptik.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONCommentParser {

    public List<CommentValue> parse(JSONObject jsonObject) {
        List<CommentValue> commentList = new ArrayList<>();
        CommentValue commentValue;
        try {
            JSONArray commentsArray = jsonObject.getJSONArray("comments");

            for (int i = 0; i < commentsArray.length(); i++) {

                JSONObject comments = commentsArray.getJSONObject(i);
                JSONObject comment = comments.getJSONObject("comment");

                commentValue = new CommentValue();

                int commentID = comment.getInt("commentID");
                String commentText = comment.getString("commentText");
                String triptikID = comment.getString("triptikID");
                String creation_date = comment.getString("creation_date");
                String creation_time = comment.getString("creation_time");
                String userID = comment.getString("userID");
                String username = comment.getString("username");
                int isVisible = comment.getInt("isVisible");


                commentValue.setCommentID(commentID);
                commentValue.setCommentText(commentText);
                commentValue.setTriptikID(triptikID);
                commentValue.setCreation_date(creation_date);
                commentValue.setCreation_time(creation_time);
                commentValue.setUserID(userID);
                commentValue.setUsername(username);
                commentValue.setIsVisible(isVisible);

                commentList.add(commentValue);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commentList;
    }

}
