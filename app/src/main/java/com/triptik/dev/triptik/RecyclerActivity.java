package com.triptik.dev.triptik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;
import java.util.List;


public class RecyclerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AppCompatActivity activity = RecyclerActivity.this;
    List<PostValue> postList;
    TextView fragment_title_header;
    ImageButton btnNewTriptik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recycler);


        getSupportActionBar().hide();


        initViews();
        new JSONAsync().execute();

        //Typeface RalewayBold = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        //Typeface RalewayMedium = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        //Typeface RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");

        //fragment_title_header = (TextView) findViewById(R.id.fragment_title_header);
        //fragment_title_header.setTypeface(RalewayLight);

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
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new RecyclerClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                if (postList != null) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(postList.get(position).getTriptikID()));
//                    startActivity(intent);
//                }
//            }
//        }));
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
