package com.triptik.dev.triptik.gallery;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.R;
import com.triptik.dev.triptik.comment.CommentAdapter;

import java.lang.Override;
import java.lang.String;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {


    private Context mContext;
    private List<GalleryValue> galleryList;
    private LayoutInflater inflater;


    public GalleryAdapter(Context context, List<GalleryValue> galleryList) {
        this.galleryList = galleryList;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_post, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        Typeface RalewayRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Regular.ttf");
        Typeface RalewayBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Bold.ttf");
        viewHolder.tvTitle.setTypeface(RalewayRegular);
        viewHolder.tvUserName.setTypeface(RalewayRegular);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GalleryValue currentPost = galleryList.get(position);
        holder.tvTitle.setText(currentPost.getTriptikTitle());
        holder.tvCreationDate.setText(currentPost.getCreationDate().substring(2, 10));
        holder.tvCreationTime.setText(currentPost.getCreationTime().substring(0,5));
        holder.tvUserName.setText(currentPost.getUserName());

        if (currentPost.getCommentTotal().toString() == "0") {

            holder.tvCommentTotal.setVisibility(View.GONE);
            holder.fragment_comment_button.setAlpha(0.2f);

        } else {

            holder.tvCommentTotal.setText(currentPost.getCommentTotal());
            holder.tvCommentTotal.setVisibility(View.VISIBLE);
            holder.fragment_comment_button.setAlpha(1f);
        }


        if (currentPost.getLikesTotal().toString() == "0"){

            holder.tvLikesTotal.setVisibility(View.GONE);
            holder.fragment_likes_button.setAlpha(0.2f);
        } else {

            holder.tvLikesTotal.setText(currentPost.getLikesTotal());
            holder.tvLikesTotal.setVisibility(View.VISIBLE);
            holder.fragment_likes_button.setAlpha(1f);
        }


        String photo_url_str = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + currentPost.getUserID() + "/gallery/" + currentPost.getTriptikID()+ "/" + currentPost.getTriptikID() + "_panel_1.webp";
        String profile_image = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + currentPost.getUserID() + "/pic.webp";

        Picasso.with(mContext)
                .load(photo_url_str)
                .centerCrop()
                .resize(658, 400)
                .into(holder.ivTriptikPreview);

        Picasso.with(mContext).load(profile_image).resize(150,150).centerCrop().into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return galleryList == null ? 0 : galleryList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCreationDate, tvCreationTime, tvUserName, tvCommentTotal, tvLikesTotal;
        ImageView ivTriptikPreview, ivProfileImage;
        RelativeLayout fragment_comment_button, fragment_likes_button;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.fragment_title_header);
            tvCreationDate = (TextView) itemView.findViewById(R.id.triptik_date_created);
            tvCreationTime = (TextView) itemView.findViewById(R.id.triptik_time_created);
            tvUserName = (TextView) itemView.findViewById(R.id.triptik_username_creator);
            ivTriptikPreview = (ImageView) itemView.findViewById(R.id.fragment_background_image);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            tvCommentTotal = (TextView) itemView.findViewById(R.id.tvCommentTotal);
            tvLikesTotal = (TextView) itemView.findViewById(R.id.tvLikesTotal);

            fragment_comment_button = (RelativeLayout) itemView.findViewById(R.id.fragment_comment_button);
            fragment_likes_button = (RelativeLayout) itemView.findViewById(R.id.fragment_likes_button);
        }
    }
}
