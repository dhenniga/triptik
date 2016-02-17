package com.triptik.dev.triptik;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import java.lang.Override;
import java.lang.String;
import java.util.List;

/**
 * Created by Krrishnaaaa on Jun 24, 2015
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final Context mContext;
    private List<PostValue> postList;
    private LayoutInflater inflater;

//    private Picasso mPicasso;

    public PostAdapter(Context context, List<PostValue> postList) {
        this.postList = postList;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;

//        Picasso.Builder picassoBuilder = new Picasso.Builder(mContext);
//        picassoBuilder.indicatorsEnabled(true);
//        picassoBuilder.memoryCache(new LruCache(1024 * 4)); // 4mb ?
//        mPicasso = picassoBuilder.build();
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

        Picasso.with(mContext).load(photo_url_str).into(holder.ivTriptikPreview);
        Picasso.with(mContext).load(profile_image).into(holder.ivProfileImage);
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
