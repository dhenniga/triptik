package com.triptik.dev.triptik;

import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bobbyadiprabowo on 3/3/15.
 */
public class ParallaxAdapter extends RecyclerView.Adapter<ParallaxAdapter.ParallaxViewHolder> {

    private List<ParallaxModel> parallaxModelList;

    public ParallaxAdapter(List<ParallaxModel> parallaxModelList) {
        this.parallaxModelList = parallaxModelList;
    }

    @Override
    public ParallaxViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_post, viewGroup, false);
        return new ParallaxViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ParallaxViewHolder parallaxViewHolder, int i) {
        ParallaxModel parallaxModel = parallaxModelList.get(i);

        parallaxViewHolder.parallaxText.setText(parallaxModel.getTitle());

        // for now we will hardcode
        if (i % 2 == 0) {
            parallaxViewHolder.parallaxImage.setImageResource(R.mipmap.background);
        } else if (i % 3 == 0) {
            parallaxViewHolder.parallaxImage.setImageResource(R.mipmap.get_the_look_2);
        } else {
            parallaxViewHolder.parallaxImage.setImageResource(R.mipmap.chat_icon);
        }

        Matrix matrix = parallaxViewHolder.parallaxImage.getImageMatrix();
        matrix.postTranslate(0, -100);
        parallaxViewHolder.parallaxImage.setImageMatrix(matrix);

        parallaxViewHolder.itemView.setTag(parallaxViewHolder);
    }

    @Override
    public int getItemCount() {
        // hardcoded first
        return parallaxModelList.size();
    }

    @Override
    public void onViewRecycled(ParallaxViewHolder parallaxViewHolder) {
        super.onViewRecycled(parallaxViewHolder);
        parallaxViewHolder.parallaxImage.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix = parallaxViewHolder.parallaxImage.getImageMatrix();
        // this is set manually to show to the center
        matrix.reset();
        parallaxViewHolder.parallaxImage.setImageMatrix(matrix);

    }

    public static class ParallaxViewHolder extends RecyclerView.ViewHolder {
        public ImageView parallaxImage;
        public TextView parallaxText;

        public ParallaxViewHolder(View itemView) {
            super(itemView);
            parallaxImage = (ImageView) itemView.findViewById(R.id.fragment_background_image);
            parallaxImage.setScaleType(ImageView.ScaleType.MATRIX);
            parallaxText = (TextView) itemView.findViewById(R.id.fragment_title_header);
        }
    }
}
