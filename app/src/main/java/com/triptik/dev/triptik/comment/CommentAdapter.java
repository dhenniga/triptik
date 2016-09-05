package com.triptik.dev.triptik.comment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.R;

import java.util.HashMap;
import java.util.List;

import com.triptik.dev.triptik.SwipeLayout;
import com.triptik.dev.triptik.TriptikViewer;
import com.triptik.dev.triptik.helper.SQLiteHandler;
import com.triptik.dev.triptik.helper.SessionManager;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<CommentValue> commentList;
    private LayoutInflater inflater;
    int commentID;
    private SQLiteHandler db;
    private SessionManager session;

    public CommentAdapter(Context context, List<CommentValue> commentList) {
        this.commentList = commentList;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final View view = inflater.inflate(R.layout.item_comment, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        Typeface RalewayRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Regular.ttf");
        Typeface RalewayBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Bold.ttf");
        viewHolder.tvCommentText.setTypeface(RalewayRegular);
        viewHolder.tvCommentUser.setTypeface(RalewayBold);
        viewHolder.tvCommentDateTime.setTypeface(RalewayRegular);

        db = new SQLiteHandler(mContext);
        session = new SessionManager(mContext);
        HashMap<String, String> user = db.getUserDetails();
        String userID = user.get("userID");

        viewHolder.swipeLayout.setSwipeEnabled(false);

        if (userID == userID) {

            viewHolder.swipeLayout.setSwipeEnabled(true);


            View.OnClickListener onClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {

                        case R.id.list_item_left:
                            viewHolder.swipeLayout.animateReset();
                            break;

                        case R.id.list_item_right:

                            commentID = commentList.get(viewType).getCommentID();

                            viewHolder.itemView.setAlpha(0.2f);
                            viewHolder.swipeLayout.animateReset();
                            viewHolder.swipeLayout.setSwipeEnabled(false);
                            TriptikViewer triptikViewer = new TriptikViewer();
                            triptikViewer.updateCommentVisibility(commentID, mContext);
                            break;
                        default:
                            break;
                    }
                }
            };


            if (viewHolder.leftView != null) {
                viewHolder.leftView.setClickable(true);
                viewHolder.leftView.setOnClickListener(onClick);
            }

            if (viewHolder.rightView != null) {
                viewHolder.rightView.setClickable(true);
                viewHolder.rightView.setOnClickListener(onClick);
            }
        }

        viewHolder.swipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
            }

            @Override
            public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
//                Toast.makeText(swipeLayout.getContext(),
//                        (moveToRight ? "Left" : "Right") + " clamp reached",
//                        Toast.LENGTH_SHORT)
//                        .show();
            }

            @Override
            public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
            }

            @Override
            public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
            }
        });


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentValue currentComment = commentList.get(position);

        int commentID2 = commentList.get(position).getCommentID();

        holder.tvCommentText.setText(currentComment.getCommentText());
        holder.tvCommentUser.setText(currentComment.getUsername());
        holder.tvCommentDateTime.setText(currentComment.getCreation_date() + "  |  " + currentComment.getCreation_time().substring(0, 5));

        String profile_image = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + currentComment.getUserID() + "/pic.webp";
        Picasso.with(mContext).load(profile_image).resize(130, 130).centerCrop().into(holder.ivCommentThumbnail);

    }

    public void updateData(List<CommentValue> items) {
        commentList.clear();
        commentList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private SwipeLayout swipeLayout;
        private View rightView;
        private View leftView;

        private TextView tvCommentText, tvCommentUser, tvCommentDateTime;
        private ImageView ivCommentThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCommentText = (TextView) itemView.findViewById(R.id.tvCommentText);
            tvCommentUser = (TextView) itemView.findViewById(R.id.tvCommentUser);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.slCommentSwipeContainer);
            tvCommentDateTime = (TextView) itemView.findViewById(R.id.tvCommentDateTime);
            ivCommentThumbnail = (ImageView) itemView.findViewById(R.id.ivCommentThumbnail);
            rightView = itemView.findViewById(R.id.list_item_right);
            leftView = itemView.findViewById(R.id.list_item_left);
        }
    }
}



