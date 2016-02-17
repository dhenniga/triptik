package com.triptik.dev.triptik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.triptik.dev.triptik.listener.RecyclerClickListener;
import com.triptik.dev.triptik.listener.RecyclerTouchListener;

import org.json.JSONObject;
import java.util.List;


public class RecyclerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AppCompatActivity activity = RecyclerActivity.this;
    List<PostValue> postList;
    ImageButton btnNewTriptik;

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
