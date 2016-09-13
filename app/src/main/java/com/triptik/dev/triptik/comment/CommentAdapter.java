package com.triptik.dev.triptik.comment;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.R;
import java.util.HashMap;
import java.util.List;
import com.triptik.dev.triptik.SwipeLayout;
import com.triptik.dev.triptik.ToastView;
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
    public View commentView;
    private RecyclerView rvCommentRecycler;

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

        commentView = inflater.inflate(R.layout.item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(commentView);

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

            return LoggedInUserComment(viewHolder, commentView, parent, viewType, current_profile_image);

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
     * @param parent
     * @param viewType
     * @param current_profile_image
     * @return
     */
    private ViewHolder LoggedInUserComment (final ViewHolder viewHolder, final View view, final ViewGroup parent, final int viewType, final String current_profile_image)  {

        viewHolder.ibDeleteComment.setVisibility(View.VISIBLE);
        viewHolder.ibEditComment.setVisibility(View.VISIBLE);

        viewHolder.swipeLayout.setSwipeEnabled(true);

        final View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                switch (v.getId()) {

                    case R.id.ibEditComment:

                        EditButtonFunction(viewHolder, parent, current_profile_image);


//                        btnSubmitEditComment.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                Log.d("Button Press", "Update");
//
//                            }
//                        });
//
//                        btnCancelEditComment.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                Log.d("Button Press", "Cancel");
//
//                                String tvCommentUserString = viewHolder.tvCommentUser.getText().toString();
//                                String tvCommentTextString = viewHolder.tvCommentText.getText().toString();
//                                String tvCommentDateTimeString = viewHolder.tvCommentDateTime.getText().toString();
//                                Log.d("CommentUser", tvCommentUserString);
//                                Log.d("CommentDateTime", tvCommentDateTimeString);
//                                Log.d("CommentText", tvCommentTextString);
//
//                                View originalComment = inflater.inflate(R.layout.item_comment, parent, false);
//                                viewHolder.swipeLayout.removeAllViewsInLayout();
//                                viewHolder.swipeLayout.addView(originalComment);
//                                notifyItemChanged(viewType);
//
//                                TextView tvCommentUser = (TextView) originalComment.findViewById(R.id.tvCommentUser);
//                                TextView tvCommentDateTime = (TextView) originalComment.findViewById(R.id.tvCommentDateTime);
//                                TextView tvCommentText = (TextView) originalComment.findViewById(R.id.tvCommentText);
//                                ImageView ivCommentThumbnail = (ImageView) originalComment.findViewById(R.id.ivCommentThumbnail);
//                                ImageButton ibDeleteComment = (ImageButton) originalComment.findViewById(R.id.ibDeleteComment);
//                                ImageButton ibEditComment = (ImageButton) originalComment.findViewById(R.id.ibEditComment);
//
//                                tvCommentUser.setText(tvCommentUserString);
//                                tvCommentDateTime.setText(tvCommentDateTimeString);
//                                tvCommentText.setText(tvCommentTextString);
//
//                                tvCommentText.setTypeface(RalewayRegular);
//                                tvCommentUser.setTypeface(RalewayBold);
//                                tvCommentDateTime.setTypeface(RalewayRegular);
//
//                                //  Set the thumbnail to the current logged in users profile picture
//                                Picasso.with(mContext).load(current_profile_image).resize(130, 130).centerCrop().into(ivCommentThumbnail);
//
//                                if (ibEditComment != null) {
//                                    ibEditComment.setVisibility(View.VISIBLE);
//                                    EditButtonFunction(viewHolder, parent, current_profile_image);
//                                    ibEditComment.setOnClickListener(this);
//                                }
//
//                            }
//                        });


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
     * @param viewHolder
     * @param parent
     * @param current_profile_image
     */
    public void EditButtonFunction (ViewHolder viewHolder, ViewGroup parent, String current_profile_image) {

        Log.d("Button Press", "Edit");

        final View editCommentLayout = inflater.inflate(R.layout.item_reply_comment, parent, false);

        final Typeface RalewayRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Regular.ttf");
        final Typeface RalewayBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Bold.ttf");

        String commentText = viewHolder.tvCommentText.getText().toString();
        String commentCreator = viewHolder.tvCommentUser.getText().toString();
        String commentDateTime = viewHolder.tvCommentDateTime.getText().toString();

        viewHolder.swipeLayout.removeAllViewsInLayout();
        viewHolder.swipeLayout.setSwipeEnabled(false);
        viewHolder.swipeLayout.addView(editCommentLayout);


        //  Get everything created
        EditText etCommentText = (EditText) editCommentLayout.findViewById(R.id.etCommentText);
        TextView tvCommentUser = (TextView) editCommentLayout.findViewById(R.id.tvCommentUser);
        TextView tvCommentDateTime = (TextView) editCommentLayout.findViewById(R.id.tvCommentDateTime);
        final ImageView ivCommentThumbnail = (ImageView) editCommentLayout.findViewById(R.id.ivCommentThumbnail);
        Button btnSubmitEditComment = (Button) editCommentLayout.findViewById(R.id.btnSubmitEditComment);
        Button btnCancelEditComment = (Button) editCommentLayout.findViewById(R.id.btnCancelEditComment);


        //  Setup the fonts
        etCommentText.setTypeface(RalewayRegular);
        tvCommentUser.setTypeface(RalewayBold);
        tvCommentDateTime.setTypeface(RalewayRegular);
        btnSubmitEditComment.setTypeface(RalewayBold);
        btnCancelEditComment.setTypeface(RalewayBold);

        //  Set the thumbnail to the current logged in users profile picture
        Picasso.with(mContext).load(current_profile_image).resize(130, 130).centerCrop().into(ivCommentThumbnail);


        //  Set all the TextView's to the correct text
        etCommentText.setText(commentText);
        tvCommentUser.setText(commentCreator);
        tvCommentDateTime.setText(commentDateTime);


        //  Set the focus to the editText box
        etCommentText.requestFocus();

        //  Show the keyboard
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etCommentText, InputMethodManager.SHOW_IMPLICIT);
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

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

//            itemView.setOnClickListener(this);

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

//
//        public void onClick(View v) {
//
//
//        }
    }
}



