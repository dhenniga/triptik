package com.triptik.dev.triptik.gallery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.AspectSelect;
import com.triptik.dev.triptik.JSONHelper;
import com.triptik.dev.triptik.JSONParser;
import com.triptik.dev.triptik.R;
import com.triptik.dev.triptik.ServiceHandler;
import com.triptik.dev.triptik.app.AppConfig;
import com.triptik.dev.triptik.comment.JSONCommentParser;
import com.triptik.dev.triptik.viewer.TriptikViewer;
import com.triptik.dev.triptik.listener.RecyclerClickListener;
import com.triptik.dev.triptik.listener.RecyclerTouchListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GalleryActivity extends Activity {

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final String EXTRA_TRIPTIK_ID = "EXTRA_TRIPTIK_ID";
    public static final String EXTRA_COMMENT_COUNT = "EXTRA_COMMENT_COUNT";
    public static final String EXTRA_LIKES_COUNT = "EXTRA_LIKES_COUNT";

    private RecyclerView recyclerView;
    private List<GalleryValue> postList;
    private ImageButton btnNewTriptik, btnUpdate;
    private TextView tvCommentTotal, tvLikesTotal, galleryLoadingProgressMessage, galleryLoadingProgressMessageSubText;
    private TextView menubar_triptik_name;
    private RelativeLayout gallery_progressloader_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recycler);

        initViews();

        new JSONAsync().execute();



        // Functions for menubar buttons
        btnNewTriptik = (ImageButton) findViewById(R.id.btnNewTriptik);
        btnNewTriptik.setVisibility(View.VISIBLE);
        btnNewTriptik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, AspectSelect.class);
                startActivity(intent);
            }
        });

        btnUpdate = (ImageButton) findViewById(R.id.btnUpdate);
        btnUpdate.setVisibility(View.VISIBLE);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, GalleryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Typeface RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-ExtraLight.ttf");

        menubar_triptik_name = (TextView) findViewById(R.id.menubar_triptik_name);
        menubar_triptik_name.setText("Triptik World Gallery");
        menubar_triptik_name.setTypeface(RalewayLight);

        tvCommentTotal = (TextView) findViewById(R.id.tvCommentTotal);
        tvLikesTotal = (TextView) findViewById(R.id.tvLikesTotal);

//
//        galleryLoadingProgressMessageSubText = (TextView) findViewById(R.id.galleryLoadingProgressMessageSubText);
//        galleryLoadingProgressMessageSubText.setTypeface(RalewayLight);
//
//        galleryLoadingProgressMessage = (TextView) findViewById(R.id.galleryLoadingProgressMessage);
//        galleryLoadingProgressMessage.setTypeface(RalewayLight);


    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GalleryActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(GalleryActivity.this, recyclerView, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (postList != null) {
                    Log.i("recyclerActivity", postList.get(position).getTriptikID());

                    Intent intent = new Intent(getApplicationContext(), TriptikViewer.class);
                    GalleryValue galleryValue = postList.get(position);

                    intent.putExtra(EXTRA_USER_ID, galleryValue.getUserID());
                    intent.putExtra(EXTRA_TRIPTIK_ID, galleryValue.getTriptikID());
                    intent.putExtra(EXTRA_COMMENT_COUNT, galleryValue.getCommentTotal());
                    intent.putExtra(EXTRA_LIKES_COUNT, galleryValue.getLikesTotal());

                    startActivity(intent);
                }
            }
        }));
    }


    class JSONAsync extends AsyncTask<String, Void, Void> {
        ProgressDialog pd;
//        private String URL_MAIN = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/populateHomeTriptikRecycler.php";

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(GalleryActivity.this, null, "Loading Triptik Gallery ...", true, false);
        }



        @Override
        protected Void doInBackground(String... args) {

            Bundle extras = getIntent().getExtras();

            if (extras != null) {

                final String userID = extras.getString(TriptikViewer.EXTRA_USER_ID);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userID", userID));

                ServiceHandler serviceClient = new ServiceHandler();
                String json = serviceClient.makeServiceCall(AppConfig.URL_GET_USERID_GALLERY, ServiceHandler.POST, params);

                if (json.length() > 4) {

                    JSONObject jsonObject = new JSONHelper().getJSONFromString(json);
                    Log.d("SERVER Request: ", "> " + json);
                    postList = new JSONParser().parse(jsonObject);
                    return null;

                } else {

                    Log.d("SERVER Request: ", "> " + json);
                }

                return null;


            } else {

                JSONObject jsonObject = new JSONHelper().getJSONFromUrl(AppConfig.URL_POPULATE_STANDARD_GALLERY);
                postList = new JSONParser().parse(jsonObject);
                return null;

            }
        }


        @Override
        protected void onPostExecute(Void result) {
            GalleryAdapter galleryAdapter = new GalleryAdapter(GalleryActivity.this, postList);
            recyclerView.setAdapter(galleryAdapter);
            pd.dismiss();
        }
    }
}
