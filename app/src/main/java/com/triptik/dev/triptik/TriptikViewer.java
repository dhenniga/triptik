package com.triptik.dev.triptik;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class TriptikViewer extends Activity {

    ImageView ivTriptikViewer;
    ImageButton btnNewTriptik, btnGallery;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.triptik_viewer);

        ivTriptikViewer = (ImageView) findViewById(R.id.ivTriptikViewer);
        btnNewTriptik = (ImageButton) findViewById(R.id.btnNewTriptik);
        btnGallery = (ImageButton) findViewById(R.id.btnGallery);

        btnNewTriptik.setVisibility(View.VISIBLE);
        btnGallery.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userId = extras.getString(RecyclerActivity.EXTRA_USER_ID);
            String triptikId = extras.getString(RecyclerActivity.EXTRA_TRIPTIK_ID);

            String photo_url_str = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + userId + "/gallery/" + triptikId + "/" + triptikId + "_panel_4.webp";
            Picasso.with(getApplicationContext()).load(photo_url_str).into(ivTriptikViewer);

        }


        Typeface RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");

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
    }
}
