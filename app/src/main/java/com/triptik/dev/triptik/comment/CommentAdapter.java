package com.triptik.dev.triptik.comment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
        viewHolder.tvCommentReplyUser.setTypeface(RalewayRegular);

        db = new SQLiteHandler(mContext);
        session = new SessionManager(mContext);
        HashMap<String, String> user = db.getUserDetails();
        String userID = user.get("userID");
        Log.d("Logged in userID", userID);

        viewHolder.swipeLayout.setSwipeEnabled(false);

        String commentUser = commentList.get(viewType).getUserID();
        Log.d("Comment User", commentUser);

        if (commentUser.equals(userID)) {

            viewHolder.ibDeleteComment.setVisibility(View.VISIBLE);
            viewHolder.ibEditComment.setVisibility(View.VISIBLE);

            viewHolder.swipeLayout.setSwipeEnabled(true);


            View.OnClickListener onClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {

                        case R.id.ibEditComment:
                            viewHolder.swipeLayout.animateReset();
                            break;

                        case R.id.ibDeleteComment:

                            commentID = commentList.get(viewType).getCommentID();

                            viewHolder.itemView.setAlpha(0.2f);
                            viewHolder.swipeLayout.animateReset();
                            viewHolder.swipeLayout.setSwipeEnabled(false);
                            TriptikViewer triptikViewer = new TriptikViewer();
                            triptikViewer.updateCommentVisibility(commentID, mContext);
                            break;

                        case R.id.ibReplyComment:
                            viewHolder.rlRightContainer.setBackgroundColor(Color.parseColor("#0098ff"));
                            break;

                        default:
                            break;
                    }
                }
            };


            if (viewHolder.ibEditComment != null) {
                viewHolder.ibEditComment.setClickable(true);
                viewHolder.ibEditComment.setOnClickListener(onClick);
            }

            if (viewHolder.ibDeleteComment != null) {
                viewHolder.ibDeleteComment.setClickable(true);
                viewHolder.ibDeleteComment.setOnClickListener(onClick);
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

        } else {


            viewHolder.ibReplyComment.setVisibility(View.VISIBLE);
            viewHolder.ibCommentAuthorGallery.setVisibility(View.VISIBLE);

            viewHolder.rlRightContainer.setBackgroundColor(Color.parseColor("#f8f8f8"));
            viewHolder.rlLeftContainer.setBackgroundColor(Color.parseColor("#f8f8f8"));

            viewHolder.swipeLayout.setSwipeEnabled(true);


            View.OnClickListener onClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {

                        case R.id.ibReplyComment:
                            viewHolder.swipeLayout.animateReset();
                            break;

                        case R.id.ibCommentAuthorGallery:
                            viewHolder.swipeLayout.animateReset();
                            break;

                        default:
                            break;
                    }
                }
            };


            if (viewHolder.ibReplyComment != null) {
                viewHolder.ibReplyComment.setClickable(true);
                viewHolder.ibReplyComment.setOnClickListener(onClick);
            }

            if (viewHolder.ibCommentAuthorGallery != null) {
                viewHolder.ibCommentAuthorGallery.setClickable(true);
                viewHolder.ibCommentAuthorGallery.setOnClickListener(onClick);
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
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentValue currentComment = commentList.get(position);

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
        private View rlRightContainer, rlLeftContainer;

        private TextView tvCommentText, tvCommentUser, tvCommentDateTime, tvCommentReplyUser;
        private ImageView ivCommentThumbnail;
        private ImageButton ibDeleteComment, ibEditComment, ibReplyComment, ibCommentAuthorGallery;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCommentText = (TextView) itemView.findViewById(R.id.tvCommentText);
            tvCommentUser = (TextView) itemView.findViewById(R.id.tvCommentUser);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.slCommentSwipeContainer);
            tvCommentDateTime = (TextView) itemView.findViewById(R.id.tvCommentDateTime);
            ivCommentThumbnail = (ImageView) itemView.findViewById(R.id.ivCommentThumbnail);
            tvCommentReplyUser = (TextView) itemView.findViewById(R.id.tvCommentReplyUser);
            rlRightContainer = itemView.findViewById(R.id.rlRightContainer);
            rlLeftContainer = itemView.findViewById(R.id.rlLeftContainer);
            ibDeleteComment = (ImageButton) itemView.findViewById(R.id.ibDeleteComment);
            ibEditComment = (ImageButton) itemView.findViewById(R.id.ibEditComment);
            ibReplyComment = (ImageButton) itemView.findViewById(R.id.ibReplyComment);
            ibCommentAuthorGallery = (ImageButton) itemView.findViewById(R.id.ibCommentAuthorGallery);
        }
    }
}



