package com.triptik.dev.triptik.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GalleryValue currentPost = galleryList.get(position);
        holder.tvTitle.setText(currentPost.getTriptikTitle());
        holder.tvCreationDate.setText(currentPost.getCreationDate().substring(2,10));
        holder.tvCreationTime.setText(currentPost.getCreationTime().substring(0,5));
        holder.tvUserName.setText(currentPost.getUserName());
        holder.tvCommentTotal.setText(((String.valueOf(position + 1))));

        String photo_url_str = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + currentPost.getUserID() + "/gallery/" + currentPost.getTriptikID()+ "/" + currentPost.getTriptikID() + "_panel_1.webp";
        String profile_image = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + currentPost.getUserID() + "/pic.webp";

        Picasso.with(mContext)
                .load(photo_url_str)
                .centerCrop()
                .resize(658, 400)
                .into(holder.ivTriptikPreview);


        Picasso.with(mContext).load(profile_image).resize(300,300).centerCrop().into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return galleryList == null ? 0 : galleryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCreationDate, tvCreationTime, tvUserName, tvCommentTotal;
        ImageView ivTriptikPreview, ivProfileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.fragment_title_header);
            tvCreationDate = (TextView) itemView.findViewById(R.id.triptik_date_created);
            tvCreationTime = (TextView) itemView.findViewById(R.id.triptik_time_created);
            tvUserName = (TextView) itemView.findViewById(R.id.triptik_username_creator);
            ivTriptikPreview = (ImageView) itemView.findViewById(R.id.fragment_background_image);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            tvCommentTotal = (TextView) itemView.findViewById(R.id.tvCommentTotal);
        }
    }
}
