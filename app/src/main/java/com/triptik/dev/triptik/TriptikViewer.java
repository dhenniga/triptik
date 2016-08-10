package com.triptik.dev.triptik;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.comment.CommentAdapter;
import com.triptik.dev.triptik.comment.CommentValue;
import com.triptik.dev.triptik.comment.JSONCommentParser;
import com.triptik.dev.triptik.gallery.GalleryActivity;
import com.triptik.dev.triptik.gallery.GalleryValue;
import com.triptik.dev.triptik.listener.RecyclerClickListener;
import com.triptik.dev.triptik.listener.RecyclerTouchListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

public class TriptikViewer extends Activity {

    private ImageView ivTriptikViewer, ivCommentThumbnail;
    private ImageButton btnNewTriptik, btnGallery, btnDownload;
    private LinearLayout menubar_icon_container;
    private ProgressDialog progressDialog;
    private RecyclerView rvCommentRecycler;
    private Button btnSendComment;
    private ToggleButton tbtnAddComment;

    private TextView tvCommentUser, tvCommentDateTime, tvCommentText;

    private List<CommentValue> commentList;
    private Activity activity = TriptikViewer.this;

    private View view;

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final String EXTRA_TRIPTIK_ID = "EXTRA_TRIPTIK_ID";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.triptik_viewer);


        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels;

        Typeface RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");

        ivTriptikViewer = (ImageView) findViewById(R.id.ivTriptikViewer);
        btnNewTriptik = (ImageButton) findViewById(R.id.btnNewTriptik);
        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        btnDownload = (ImageButton) findViewById(R.id.btnDownload);
        menubar_icon_container = (LinearLayout) findViewById(R.id.menubar_icon_container);

        tbtnAddComment = (ToggleButton) findViewById(R.id.tbtnAddComment);
        tbtnAddComment.setTypeface(RalewayLight);

        btnSendComment = (Button) findViewById(R.id.btnSendComment);



        rvCommentRecycler = (RecyclerView) findViewById(R.id.rvCommentRecycler);
        ivCommentThumbnail = (ImageView) findViewById(R.id.ivCommentThumbnail);

        tvCommentUser = (TextView) findViewById(R.id.tvCommentUser);

        tvCommentDateTime = (TextView) findViewById(R.id.tvCommentDateTime);
        tvCommentText = (TextView) findViewById(R.id.tvCommentText);

        btnNewTriptik.setVisibility(View.VISIBLE);
        btnGallery.setVisibility(View.VISIBLE);
        btnDownload.setVisibility(View.VISIBLE);

        final ViewGroup parent = (ViewGroup) findViewById(R.id.rlCommentsContainer);

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        menubar_icon_container.setLayoutParams(rlp);

        initViews();

        new JSONAsync().execute();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userId = extras.getString(TriptikViewer.EXTRA_USER_ID);
            String triptikId = extras.getString(TriptikViewer.EXTRA_TRIPTIK_ID);

            String photo_url_str = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + userId + "/gallery/" + triptikId + "/" + triptikId + "_panel_4.webp";

            Picasso.with(getApplicationContext())
                    .load(photo_url_str)
                    .resize((int)dpWidth, (int)dpHeight)
                    .into(ivTriptikViewer);

        }


        TextView tvTriptikBaseLine = (TextView) findViewById(R.id.tvTriptikBaseLine);
        tvTriptikBaseLine.setTypeface(RalewayLight);


        btnNewTriptik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TriptikViewer.this, AspectSelect.class);
                startActivity(intent);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        tbtnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tbtnAddComment.isChecked()) {

                    tbtnAddComment.setText("Cancel comment");
                    v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_add_comment, null);
                    parent.addView(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }

                else {

                    tbtnAddComment.setText("Add comment");
                    parent.removeAllViews();

                }

            }
        });


//        btnSendComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                btnSendComment.setBackgroundColor(Color.parseColor("#ff6600"));
//
//            }
//        });

    }


    private void initViews() {
        rvCommentRecycler = (RecyclerView) findViewById(R.id.rvCommentRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        rvCommentRecycler.setLayoutManager(layoutManager);
    }


    class JSONAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        private String URL_MAIN = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/getCommentsForTriptik.php";

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(TriptikViewer.this, null, "Loading Comments...", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = new JSONHelper().getJSONFromUrl(URL_MAIN);
            commentList = new JSONCommentParser().parse(jsonObject);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            CommentAdapter commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
            rvCommentRecycler.setAdapter(commentAdapter);
            pd.dismiss();
        }
    }
}
