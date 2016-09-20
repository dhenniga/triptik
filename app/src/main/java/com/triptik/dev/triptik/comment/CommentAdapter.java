package com.triptik.dev.triptik.comment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.R;
import java.util.HashMap;
import java.util.List;
import com.triptik.dev.triptik.SwipeLayout;
import com.triptik.dev.triptik.viewer.TriptikViewer;
import com.triptik.dev.triptik.helper.SQLiteHandler;
//import com.triptik.dev.triptik.helper.SessionManager;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<CommentValue> commentList;
    private LayoutInflater inflater;
    private SQLiteHandler db;
//    private SessionManager session;
    public String profile_image;
    public View commentView;

    public CommentAdapter(Context context, List<CommentValue> commentList) {
        this.commentList = commentList;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        commentView = inflater.inflate(R.layout.item_comment, parent, false);
        final ViewHolder viewHolder = new ViewHolder(commentView);

        /* Change the fonts */
        final Typeface RalewayRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Regular.ttf");
        final Typeface RalewayBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Bold.ttf");
        viewHolder.tvCommentText.setTypeface(RalewayRegular);
        viewHolder.tvCommentUser.setTypeface(RalewayBold);
        viewHolder.tvCommentDateTime.setTypeface(RalewayRegular);
        viewHolder.tvCommentReplyUser.setTypeface(RalewayRegular);
        viewHolder.etCommentText.setTypeface(RalewayRegular);

        Log.d("Comment Count", ((String.valueOf(getItemCount()))));

        //  Disable swiping (initial state)
        viewHolder.swipeLayout.setSwipeEnabled(false);

        // Get the logged in userID and set it to a userID string
        db = new SQLiteHandler(mContext);
//        session = new SessionManager(mContext);
        HashMap<String, String> user = db.getUserDetails();
        String userID = user.get("userID");
        Log.d("Logged in userID", userID);


        // Get the userID of the user who made the comment
        String commentUserID = commentList.get(viewType).getUserID();
        Log.d("commentUserID", commentUserID);

        //  If the logged in user and the person who made the comment are the same person
        //  then enable a specific left, right swipe functions.

        if (commentUserID.equals(userID)) {

            return LoggedInUserComment(viewHolder, commentView);

        } else {

            return NotLoggedInUserComment(viewHolder, commentView);

        }

    }



    /**
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }



    /**
     *
     * Set this viewHolder with a comment relating to the person logged into the device.  This changes the
     * swipe menus to relate to this user (edit and delete).
     *
     * @param viewHolder
     * @param view
     * @return
     */
    private ViewHolder LoggedInUserComment (final ViewHolder viewHolder, final View view)  {

        viewHolder.ibDeleteComment.setVisibility(View.VISIBLE);
        viewHolder.ibEditComment.setVisibility(View.VISIBLE);

        viewHolder.swipeLayout.setSwipeEnabled(true);

        return new ViewHolder(view);

    }



    /**
     *
     * @param viewHolder
     */
    public void EditButtonFunction (final ViewHolder viewHolder) {

        Log.d("Button Press", "Edit");



        final ScrollView scComments = (ScrollView) viewHolder.swipeLayout.getRootView().findViewById(R.id.svComments);
        scComments.smoothScrollTo(90, viewHolder.swipeLayout.getBottom() + 1000);
//
//        scrollView.post(new Runnable() {
//
//            @Override
//            public void run() {
//                scrollView.smoothScrollTo(0, viewHolder.llCommentButtonContainer.getBottom());
//            }
//        });


        viewHolder.swipeLayout.animateReset();
        viewHolder.swipeLayout.setBackgroundColor(Color.parseColor("#f8f8f8"));

        final String commentText = viewHolder.tvCommentText.getText().toString();

        final EditText etCommentText = (EditText) viewHolder.swipeLayout.findViewById(R.id.etCommentText);
        final LinearLayout llCommentButtonContainer = (LinearLayout) viewHolder.swipeLayout.findViewById(R.id.llCommentButtonContainer);

        viewHolder.swipeLayout.setSwipeEnabled(false);

        etCommentText.setText(commentText);
        etCommentText.setVisibility(View.VISIBLE);
        viewHolder.tvCommentText.setVisibility(View.GONE);
        llCommentButtonContainer.setVisibility(View.VISIBLE);

        etCommentText.requestFocus();

        //  Show the keyboard
//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(etCommentText, InputMethodManager.SHOW_IMPLICIT);


        //  onClick for Cancel button
        viewHolder.btnCancelEditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("onClick", "Cancel");

                etCommentText.setVisibility(View.GONE);
                viewHolder.tvCommentText.setVisibility(View.VISIBLE);
                llCommentButtonContainer.setVisibility(View.GONE);
                viewHolder.swipeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.swipeLayout.setSwipeEnabled(true);
                notifyItemChanged(viewHolder.getAdapterPosition());

            }
        });


        // onClick for Submit / Update button
        viewHolder.btnSubmitEditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updatedCommentText = etCommentText.getText().toString();

                final int commentID = Integer.parseInt(viewHolder.tvCommentID.getText().toString());

                TriptikViewer triptikViewer = new TriptikViewer();
                triptikViewer.updateCommentText(commentID, updatedCommentText, mContext);

                Log.d("onClick", "Update");

                etCommentText.setVisibility(View.GONE);
                viewHolder.tvCommentText.setText(updatedCommentText);
                viewHolder.tvCommentText.setVisibility(View.VISIBLE);
                llCommentButtonContainer.setVisibility(View.GONE);
                viewHolder.swipeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.swipeLayout.setSwipeEnabled(true);

            }
        });
    }



    /**
     *
     * Set this viewHolder with a comment relating to the person who created the comment.  This changes the
     * swipe menus to relate to this user (gallery and reply).
     *
     * @param viewHolder
     * @param view
     * @return
     */
    private static ViewHolder NotLoggedInUserComment (final ViewHolder viewHolder, View view)
    {

        viewHolder.ibReplyComment.setVisibility(View.VISIBLE);
        viewHolder.ibCommentAuthorGallery.setVisibility(View.VISIBLE);

        viewHolder.rlRightContainer.setBackgroundColor(Color.parseColor("#f8f8f8"));
        viewHolder.rlLeftContainer.setBackgroundColor(Color.parseColor("#f8f8f8"));

        viewHolder.swipeLayout.setSwipeEnabled(true);

        return new ViewHolder(view);

    }



    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)  {

        CommentValue currentComment = commentList.get(position);

        holder.tvCommentID.setText(((String.valueOf(currentComment.getCommentID()))));
        holder.tvCommentUserID.setText(currentComment.getUserID());
        holder.tvCommentText.setText(currentComment.getCommentText());
        holder.tvCommentUser.setText(currentComment.getUsername());
        holder.tvCommentDateTime.setText(currentComment.getCreation_date() + "  |  " + currentComment.getCreation_time().substring(0, 5));

        profile_image = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + currentComment.getUserID() + "/pic.webp";
        Picasso.with(mContext).load(profile_image).resize(130, 130).centerCrop().into(holder.ivCommentThumbnail);



        /**
         *
         */
        holder.ibReplyComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("onClick", "Reply Comment");
                holder.swipeLayout.animateReset();

            }
        });



        /**
         *
         */
        holder.ibCommentAuthorGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("onClick", "Comment Gallery");
                holder.swipeLayout.animateReset();

            }
        });



        /**
         *
         */
        holder.ibEditComment.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("onClick", "Edit Comment");

                EditButtonFunction(holder);

            }
        });



        /**
         *
         */
        holder.ibDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Log.d("onClick", "Delete Comment");
                final int commentID = Integer.parseInt(holder.tvCommentID.getText().toString());


                Animation fadeout = new AlphaAnimation(1, 0);
                fadeout.setDuration(500);
                holder.swipeLayout.startAnimation(fadeout);
                holder.swipeLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        TriptikViewer triptikViewer = new TriptikViewer();
                        triptikViewer.updateCommentVisibility(commentID, mContext);
                        
                        // Update the commentTotal
                        TextView tvCommentTotal = (TextView) commentView.getRootView().findViewById(R.id.tvCommentTotal);
                        int updatedCommentTotal = Integer.parseInt(tvCommentTotal.getText().toString()) - 1;
                        tvCommentTotal.setText(((String.valueOf(updatedCommentTotal))));

                        Log.d("updatedCommentTotal", ((String.valueOf(updatedCommentTotal))));
                        Log.d("CommentID Visibility", ((String.valueOf(commentID))));
                        Log.d("Comment Author", holder.tvCommentUser.getText().toString());
                        Log.d("Comment Content", holder.tvCommentText.getText().toString());
                    }
                }, 500);

                fadeout.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        holder.swipeLayout.removeAllViews();
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });
                }
            });
    }



    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {

        return commentList == null ? 0 : commentList.size();

    }



    /**
     *
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        SwipeLayout swipeLayout;
        View rlRightContainer, rlLeftContainer;
        TextView tvCommentText, tvCommentUser, tvCommentDateTime, tvCommentReplyUser, tvCommentID, tvCommentUserID;
        EditText etCommentText;
        ImageView ivCommentThumbnail;
        ImageButton ibDeleteComment, ibEditComment, ibReplyComment, ibCommentAuthorGallery;
        Button btnCancelEditComment, btnSubmitEditComment;
        LinearLayout llCommentButtonContainer;

        public ViewHolder(View itemView) {

            super(itemView);

            btnSubmitEditComment = (Button) itemView.findViewById(R.id.btnSubmitEditComment);
            btnCancelEditComment = (Button) itemView.findViewById(R.id.btnCancelEditComment);
            llCommentButtonContainer = (LinearLayout) itemView.findViewById(R.id.llCommentButtonContainer);
            tvCommentID = (TextView) itemView.findViewById(R.id.tvCommentID);
            tvCommentUserID = (TextView) itemView.findViewById(R.id.tvCommentUserID);
            tvCommentText = (TextView) itemView.findViewById(R.id.tvCommentText);
            etCommentText = (EditText) itemView.findViewById(R.id.etCommentText);
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



