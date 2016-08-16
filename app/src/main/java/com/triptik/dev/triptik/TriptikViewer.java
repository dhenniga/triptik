package com.triptik.dev.triptik;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.app.AppConfig;
import com.triptik.dev.triptik.app.AppController;
import com.triptik.dev.triptik.comment.CommentAdapter;
import com.triptik.dev.triptik.comment.CommentValue;
import com.triptik.dev.triptik.comment.JSONCommentParser;
import com.triptik.dev.triptik.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    private Button btnSendComment;
    private ScrollView svComments;
    private ToggleButton tbtnAddComment;
    private ObjectAnimator objectAnimator;
    private SQLiteHandler db;
//    private EditText etCommentText;

    private TextView tvCommentUser, tvCommentDateTime, tvCommentText;

    private List<CommentValue> commentList;
    private Activity activity = TriptikViewer.this;
    private Animation animFadein;
    private RelativeLayout rlTriptikBaseline;

    private LinearLayout llCommentButtonContainer;

    RequestQueue requestQueue;

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

        rlTriptikBaseline = (RelativeLayout) findViewById(R.id.rlTriptikBaseline);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        tbtnAddComment = (ToggleButton) findViewById(R.id.tbtnAddComment);
        tbtnAddComment.setTypeface(RalewayLight);

        llCommentButtonContainer = (LinearLayout) findViewById(R.id.llCommentButtonContainer);

        svComments = (ScrollView) findViewById(R.id.svComments);

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
                                final String userID = extras.getString(TriptikViewer.EXTRA_USER_ID);
                                final String triptikID = extras.getString(TriptikViewer.EXTRA_TRIPTIK_ID);

                                uploadComment(triptikID, userID, commentText);
                                Log.d("uploadComment","Event occured");
                                ToastView("Comment Uploaded");

                                etCommentText.setText("");

                            } else {

                                ToastView("Please enter a comment before posting");

                            }
                        }
                    });
                }

                else {

                    tbtnAddComment.setText("Add comment");
                    focusOnView();
                    parent.removeAllViews();

                }

            }
        });
    }



    private void uploadComment (final String triptikID, final String userID, final String commentText) {

        // Tag used to cancel the request
        String tag_string_req = "upload_comment";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_COMMENTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Comment Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        try {

                                new Thread(new Runnable() {
                                    public void run() {

                                        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_COMMENTS, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                System.out.println(response.toString());
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }) {

                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String,String> parameters  = new HashMap<String, String>();
                                                parameters.put("commentText",commentText);
                                                parameters.put("triptikID",triptikID);
                                                parameters.put("userID",userID);
                                                return parameters;
                                            }
                                        };

                                        requestQueue.add(request);
                                    }
                                }).start();

                        } catch (Exception e) {
                            Log.e("AndroidUploadService", e.getMessage(), e);

                        }


//                        JSONObject comment = jObj.getJSONObject("comment");
//                        String commentText = comment.getString("commentText");
//                        String triptikID = comment.getString("triptikID");
//                        String userUD = comment.getString("userID");

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Comment Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("commentText", commentText);
                params.put("userID", userID);
                params.put("triptikID", triptikID);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
























    private void initViews() {
        rvCommentRecycler = (RecyclerView) findViewById(R.id.rvCommentRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        rvCommentRecycler.setLayoutManager(layoutManager);
    }


    private final void focusOnView(){
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



    class JSONAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(TriptikViewer.this, null, "Loading Comments...", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = new JSONHelper().getJSONFromUrl(AppConfig.URL_GET_COMMENTS);
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

    public void ToastView (String toastTextString) {
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
