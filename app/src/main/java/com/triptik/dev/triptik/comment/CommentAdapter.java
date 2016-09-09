package com.triptik.dev.triptik.comment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.R;
import java.util.HashMap;
import java.util.List;
import com.triptik.dev.triptik.SwipeLayout;
import com.triptik.dev.triptik.viewer.TriptikViewer;
import com.triptik.dev.triptik.helper.SQLiteHandler;
import com.triptik.dev.triptik.helper.SessionManager;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<CommentValue> commentList;
    private LayoutInflater inflater;
    private SQLiteHandler db;
    private SessionManager session;
    public String profile_image;

    public CommentAdapter(Context context, List<CommentValue> commentList) {
        this.commentList = commentList;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }


    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final View view = inflater.inflate(R.layout.item_comment, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        // Change the fonts
        final Typeface RalewayRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Regular.ttf");
        final Typeface RalewayBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Bold.ttf");
        viewHolder.tvCommentText.setTypeface(RalewayRegular);
        viewHolder.tvCommentUser.setTypeface(RalewayBold);
        viewHolder.tvCommentDateTime.setTypeface(RalewayRegular);
        viewHolder.tvCommentReplyUser.setTypeface(RalewayRegular);

        Log.d("Comment Count", ((String.valueOf(getItemCount()))));

        //  Disable swiping (initial state)
        viewHolder.swipeLayout.setSwipeEnabled(false);

        // Get the logged in userID and set it to a userID string
        db = new SQLiteHandler(mContext);
        session = new SessionManager(mContext);
        HashMap<String, String> user = db.getUserDetails();
        String userID = user.get("userID");
        Log.d("Logged in userID", userID);
        final String current_profile_image = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + userID + "/pic.webp";

//          Get the userID of the user who made the comment
        String commentUserID = commentList.get(viewType).getUserID();
        Log.d("commentUserID", commentUserID);

        //  If the logged in user and the person who made the comment are the same person
        //  then enable a specific left, right swipe functions.
        if (commentUserID.equals(userID)) {

            return LoggedInUserComment(viewHolder, view, parent, viewType, current_profile_image);

        } else {

            return NotLoggedInUserComment(viewHolder, view);

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
     * @param parent
     * @param viewType
     * @param current_profile_image
     * @return
     */
    public ViewHolder LoggedInUserComment (final ViewHolder viewHolder, View view, final ViewGroup parent, final int viewType, final String current_profile_image) {

        viewHolder.ibDeleteComment.setVisibility(View.VISIBLE);
        viewHolder.ibEditComment.setVisibility(View.VISIBLE);

        viewHolder.swipeLayout.setSwipeEnabled(true);

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                switch (v.getId()) {

                    case R.id.ibEditComment:

                        Log.d("Button Press", "Edit");

                        View comment = inflater.inflate(R.layout.item_reply_comment, parent, false);

                        final Typeface RalewayRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Regular.ttf");
                        final Typeface RalewayBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Bold.ttf");

                        String commentText = viewHolder.tvCommentText.getText().toString();
                        String commentCreator = viewHolder.tvCommentUser.getText().toString();
                        String commentDateTime = viewHolder.tvCommentDateTime.getText().toString();

                        viewHolder.swipeLayout.removeAllViewsInLayout();
                        viewHolder.swipeLayout.setSwipeEnabled(false);
                        viewHolder.swipeLayout.addView(comment);

                        EditText etCommentText = (EditText) viewHolder.swipeLayout.findViewById(R.id.etCommentText);
                        TextView tvCommentUser = (TextView) viewHolder.swipeLayout.findViewById(R.id.tvCommentUser);
                        TextView tvCommentDateTime = (TextView) viewHolder.swipeLayout.findViewById(R.id.tvCommentDateTime);
                        ImageView ivCommentThumbnail = (ImageView) viewHolder.swipeLayout.findViewById(R.id.ivCommentThumbnail);
                        Button btnSubmitEditComment = (Button) viewHolder.swipeLayout.findViewById(R.id.btnSubmitEditComment);
                        Button btnCancelEditComment = (Button) viewHolder.swipeLayout.findViewById(R.id.btnCancelEditComment);

                        etCommentText.setTypeface(RalewayRegular);
                        tvCommentUser.setTypeface(RalewayBold);
                        tvCommentDateTime.setTypeface(RalewayRegular);
                        btnSubmitEditComment.setTypeface(RalewayBold);
                        btnCancelEditComment.setTypeface(RalewayBold);

                        Picasso.with(mContext).load(current_profile_image).resize(130, 130).centerCrop().into(ivCommentThumbnail);

                        etCommentText.setText(commentText);
                        tvCommentUser.setText(commentCreator);
                        tvCommentDateTime.setText(commentDateTime);

                        etCommentText.requestFocus();

                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etCommentText, InputMethodManager.SHOW_IMPLICIT);

                        break;


                    case R.id.ibDeleteComment:

                        Log.d("Button Press", "Delete");
                        final int commentID = Integer.parseInt(viewHolder.tvCommentID.getText().toString());


                        Animation fadeout = new AlphaAnimation(1, 0);
                        fadeout.setDuration(500);
                        viewHolder.swipeLayout.startAnimation(fadeout);
                        viewHolder.swipeLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TriptikViewer triptikViewer = new TriptikViewer();
                                triptikViewer.updateCommentVisibility(commentID, mContext);
                                Log.d("CommentID Visibility", ((String.valueOf(commentID))));
                                Log.d("Comment Author", viewHolder.tvCommentUser.getText().toString());
                                Log.d("Comment Content", viewHolder.tvCommentText.getText().toString());
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
                                viewHolder.swipeLayout.removeAllViews();
                                notifyItemChanged(viewType);
                            }
                        });

                        break;

                    default:
                        break;
                }
            }
        };

        if (viewHolder.ibEditComment != null) {
            viewHolder.ibEditComment.setOnClickListener(onClick);
        }

        if (viewHolder.ibDeleteComment != null) {
            viewHolder.ibDeleteComment.setOnClickListener(onClick);
        }

        return new ViewHolder(view);

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
    public ViewHolder NotLoggedInUserComment (final ViewHolder viewHolder, View view)
    {

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
                        Log.d("Button Press", "Reply");
                        break;

                    case R.id.ibCommentAuthorGallery:
                        viewHolder.swipeLayout.animateReset();
                        Log.d("Button Press", "Gallery");
                        break;

                    default:
                        break;
                }
            }
        };

        if (viewHolder.ibReplyComment != null) {
            viewHolder.ibReplyComment.setOnClickListener(onClick);
        }

        if (viewHolder.ibCommentAuthorGallery != null) {
            viewHolder.ibCommentAuthorGallery.setOnClickListener(onClick);
        }

        return new ViewHolder(view);

    }



    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CommentValue currentComment = commentList.get(position);

        holder.tvCommentID.setText(((String.valueOf(currentComment.getCommentID()))));
        holder.tvCommentUserID.setText(currentComment.getUserID());
        holder.tvCommentText.setText(currentComment.getCommentText());
        holder.tvCommentUser.setText(currentComment.getUsername());
        holder.tvCommentDateTime.setText(currentComment.getCreation_date() + "  |  " + currentComment.getCreation_time().substring(0, 5));

        profile_image = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + currentComment.getUserID() + "/pic.webp";
        Picasso.with(mContext).load(profile_image).resize(130, 130).centerCrop().into(holder.ivCommentThumbnail);

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

        private SwipeLayout swipeLayout;
        private View rlRightContainer, rlLeftContainer;
        private TextView tvCommentText, tvCommentUser, tvCommentDateTime, tvCommentReplyUser, tvCommentID, tvCommentUserID;
        private ImageView ivCommentThumbnail;
        private ImageButton ibDeleteComment, ibEditComment, ibReplyComment, ibCommentAuthorGallery;
        private Button btnCancelEditComment, btnSubmitEditComment;

        public ViewHolder(View itemView) {

            super(itemView);

            btnSubmitEditComment = (Button) itemView.findViewById(R.id.btnSubmitEditComment);
            btnCancelEditComment = (Button) itemView.findViewById(R.id.btnCancelEditComment);
            tvCommentID = (TextView) itemView.findViewById(R.id.tvCommentID);
            tvCommentUserID = (TextView) itemView.findViewById(R.id.tvCommentUserID);
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



