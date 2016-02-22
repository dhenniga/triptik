package com.triptik.dev.triptik;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.lang.Override;
import java.lang.String;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    private Context mContext;
    private List<PostValue> postList;
    private LayoutInflater inflater;


    public PostAdapter(Context context, List<PostValue> postList) {
        this.postList = postList;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public PostAdapter() {

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostValue currentPost = postList.get(position);
        holder.tvTitle.setText(currentPost.getTriptikTitle());
        holder.tvCreationDate.setText(currentPost.getCreationDate());
        holder.tvCreationTime.setText(currentPost.getCreationTime());
        holder.tvUserName.setText(currentPost.getUserName());

        String photo_url_str = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + currentPost.getUserID() + "/gallery/" + currentPost.getTriptikID()+ "/" + currentPost.getTriptikID() + "_panel_1.webp";
        String profile_image = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + currentPost.getUserID() + "/pic.webp";

        Picasso.with(mContext)
                .load(photo_url_str)
                .centerCrop()
                .resize(658, 400)
                .into(holder.ivTriptikPreview);


        Picasso.with(mContext).load(profile_image).resize(120,120).centerCrop().into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCreationDate, tvCreationTime, tvUserName;
        ImageView ivTriptikPreview, ivProfileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.fragment_title_header);
            tvCreationDate = (TextView) itemView.findViewById(R.id.triptik_date_created);
            tvCreationTime = (TextView) itemView.findViewById(R.id.triptik_time_created);
            tvUserName = (TextView) itemView.findViewById(R.id.triptik_username_creator);
            ivTriptikPreview = (ImageView) itemView.findViewById(R.id.fragment_background_image);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.profile_image);
        }
    }
}
