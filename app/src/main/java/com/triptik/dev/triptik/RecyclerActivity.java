package com.triptik.dev.triptik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.listener.RecyclerClickListener;
import com.triptik.dev.triptik.listener.RecyclerTouchListener;

import org.json.JSONObject;
import java.util.List;
import java.util.zip.Inflater;


public class RecyclerActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    AppCompatActivity activity = RecyclerActivity.this;
    List<PostValue> postList;
    ImageButton btnNewTriptik;
    ImageView ivTriptikViewer;
    RelativeLayout triptikContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        getSupportActionBar().hide();

        initViews();

        new JSONAsync().execute();

        btnNewTriptik = (ImageButton) findViewById(R.id.btnNewTriptik);
        btnNewTriptik.setVisibility(View.VISIBLE);

        btnNewTriptik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecyclerActivity.this, AspectSelect.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (postList != null) {
                    Log.i("recyclerActivity", postList.get(position).getTriptikID());

                    triptikContainer = (RelativeLayout) findViewById(R.id.llTriptikContainer);
                    triptikContainer.setVisibility(View.VISIBLE);

                    ivTriptikViewer = (ImageView) findViewById(R.id.ivTriptikViewer);
                    Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_anim);
                    ivTriptikViewer.startAnimation(fadeInAnimation);
                    String photo_url_str = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/"+ postList.get(position).getUserID() +"/gallery/" + postList.get(position).getTriptikID() + "/" + postList.get(position).getTriptikID() + "_panel_4.webp";
                    Picasso.with(getApplicationContext()).load(photo_url_str).into(ivTriptikViewer);

                    //Intent intent = new Intent(Intent.ACTION_VIEW);
                    //intent.setData(Uri.parse(postList.get(position).getTriptikID()));
                    //startActivity(intent);
                }
            }
        }));
    }


    class JSONAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(RecyclerActivity.this, null, "Loading Triptik Gallery ...", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = new JSONHelper().getJSONFromUrl();
            postList = new JSONParser().parse(jsonObject);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            PostAdapter postAdapter = new PostAdapter(activity, postList);
            recyclerView.setAdapter(postAdapter);
            pd.dismiss();
        }
    }
}
