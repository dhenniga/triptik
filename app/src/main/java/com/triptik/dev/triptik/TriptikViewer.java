package com.triptik.dev.triptik;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.app.AppConfig;
import com.triptik.dev.triptik.comment.CommentAdapter;
import com.triptik.dev.triptik.comment.CommentValue;
import com.triptik.dev.triptik.comment.JSONCommentParser;
import com.triptik.dev.triptik.helper.SQLiteHandler;
import com.triptik.dev.triptik.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriptikViewer extends Activity {

    private static final String TAG = TriptikViewer.class.getSimpleName();

    private ImageView ivTriptikViewer, ivCommentThumbnail;
    private ImageButton btnNewTriptik, btnGallery, btnDownload;
    private LinearLayout menubar_icon_container;
    private ProgressDialog progressDialog;
    private RecyclerView rvCommentRecycler;
    private ScrollView svComments;
    private ToggleButton tbtnAddComment;
    private SQLiteHandler db;
    private SessionManager session;

    private Typeface RalewayLight;

    private TextView tvCommentUser, tvCommentDateTime;

    private List<CommentValue> commentList;
    private Activity activity = TriptikViewer.this;
    private RelativeLayout rlTriptikBaseline;

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static String EXTRA_USER_LOGGED_IN = "EXTRA_USER_LOGGED_IN";
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

        RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");

        ivTriptikViewer = (ImageView) findViewById(R.id.ivTriptikViewer);
        btnNewTriptik = (ImageButton) findViewById(R.id.btnNewTriptik);
        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        btnDownload = (ImageButton) findViewById(R.id.btnDownload);
        menubar_icon_container = (LinearLayout) findViewById(R.id.menubar_icon_container);

        rlTriptikBaseline = (RelativeLayout) findViewById(R.id.rlTriptikBaseline);

        tbtnAddComment = (ToggleButton) findViewById(R.id.tbtnAddComment);
        tbtnAddComment.setTypeface(RalewayLight);

        svComments = (ScrollView) findViewById(R.id.svComments);

        rvCommentRecycler = (RecyclerView) findViewById(R.id.rvCommentRecycler);
        ivCommentThumbnail = (ImageView) findViewById(R.id.ivCommentThumbnail);

        tvCommentUser = (TextView) findViewById(R.id.tvCommentUser);

        tvCommentDateTime = (TextView) findViewById(R.id.tvCommentDateTime);

        btnNewTriptik.setVisibility(View.VISIBLE);
        btnGallery.setVisibility(View.VISIBLE);
        btnDownload.setVisibility(View.VISIBLE);

        final ViewGroup parent = (ViewGroup) findViewById(R.id.rlCommentsContainer);

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        menubar_icon_container.setLayoutParams(rlp);

        initViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userId = extras.getString(TriptikViewer.EXTRA_USER_ID);
            String triptikId = extras.getString(TriptikViewer.EXTRA_TRIPTIK_ID);

            String photo_url_str = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + userId + "/gallery/" + triptikId + "/" + triptikId + "_panel_4.webp";

            Picasso.with(getApplicationContext())
                    .load(photo_url_str)
                    .resize((int) dpWidth, (int) dpHeight)
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
                    v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_add_comment, parent, false);
                    parent.addView(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    Button btnSendComment = (Button) v.findViewById(R.id.btnSendComment);
                    final EditText etCommentText = (EditText) v.findViewById(R.id.etCommentText);
                    focusOnView();

                    btnSendComment.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View view) {

                            String commentText = etCommentText.getText().toString().trim();

                            if (!commentText.matches("")) {

                                Bundle extras = getIntent().getExtras();
                                final String triptikID = extras.getString(TriptikViewer.EXTRA_TRIPTIK_ID);

                                db = new SQLiteHandler(getApplicationContext());
                                session = new SessionManager(getApplicationContext());
                                HashMap<String, String> user = db.getUserDetails();
                                EXTRA_USER_LOGGED_IN = user.get("userID");

                                uploadComment(triptikID, EXTRA_USER_LOGGED_IN, commentText);
                                Log.d("uploadComment", "Event occured");
                                ToastView("Comment Uploaded");

                                etCommentText.setText("");
                                focusOnView();
                                parent.removeAllViews();

                                initViews();

                            } else {

                                ToastView("Please enter a comment before posting");

                            }
                        }
                    });

                } else {

                    tbtnAddComment.setText("Add comment");
                    parent.removeAllViews();

                }

            }
        });

        new JSONAsync().execute();

    }


    /**
     * Upload a comment to the database
     *
     * @param triptikID
     * @param userID
     * @param commentText
     */
    private void uploadComment(final String triptikID, final String userID, final String commentText) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.URL_POST_COMMENTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
            }
        }
                ,new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("commentText", commentText);
                params.put("userID", userID);
                params.put("triptikID", triptikID);
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);

    }



    /**
     *
     */
    private void initViews() {
        rvCommentRecycler = (RecyclerView) findViewById(R.id.rvCommentRecycler);

        // smooth scrolling fix for the Comment recyclerView
        rvCommentRecycler.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        rvCommentRecycler.setLayoutManager(layoutManager);

    }


    /**
     *
     */
    private final void focusOnView() {

        svComments.post(new Runnable() {

            @Override
            public void run() {

                if (!tbtnAddComment.isChecked()) {

                    svComments.smoothScrollTo(0, (rvCommentRecycler.getBottom()));

                } else {

                    svComments.smoothScrollTo(0, (rlTriptikBaseline.getTop()));

                }
            }
        });
    }



    /**
     *
     */
    class JSONAsync extends AsyncTask<String, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(TriptikViewer.this, null, "Loading Comments...", true, false);
        }

        @Override
        protected Void doInBackground(String... args) {

            Bundle extras = getIntent().getExtras();
            final String triptikID = extras.getString(TriptikViewer.EXTRA_TRIPTIK_ID);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("triptikID", triptikID));

            ServiceHandler serviceClient = new ServiceHandler();
            String json = serviceClient.makeServiceCall(AppConfig.URL_GET_COMMENTS, ServiceHandler.POST, params);

            if (json.length() > 4) {

                JSONObject jsonObject = new JSONHelper().getJSONFromString(json);
                Log.d("SERVER Request: ", "> " + json);
                commentList = new JSONCommentParser().parse(jsonObject);

            } else {

                Log.d("SERVER Request: ", "> " + json);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            CommentAdapter commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
            rvCommentRecycler.setAdapter(commentAdapter);

            pd.dismiss();
        }
    }

    /**
     * @param toastTextString
     */
    public void ToastView(String toastTextString) {
        final Typeface RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView toastText = (TextView) layout.findViewById(R.id.tvCustomToast);
        toastText.setTypeface(RalewayLight);
        toastText.setText(toastTextString);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 130);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
