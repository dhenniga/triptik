package com.triptik.dev.triptik;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class AspectSelect extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aspect_select);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
        Typeface RalewayMedium = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

        TextView square_button_text = (TextView) findViewById(R.id.square_button_text);
        TextView portrait_button_text = (TextView) findViewById(R.id.portrait_button_text);
        TextView landscape_button_text = (TextView) findViewById(R.id.landscape_button_text);
        TextView section_description = (TextView) findViewById(R.id.section_description);
        TextView aspect_header_text = (TextView) findViewById(R.id.aspect_header_text);
        TextView select_a_template_text = (TextView) findViewById(R.id.select_a_template_text);

        square_button_text.setTypeface(face);
        portrait_button_text.setTypeface(face);
        landscape_button_text.setTypeface(face);
        section_description.setTypeface(RalewayMedium);
        aspect_header_text.setTypeface(face);
        select_a_template_text.setTypeface(face);
    }

    public void portrait_button_OnClick(View v) {
        Intent intent = new Intent(v.getContext(), port_select_layout.class);
        startActivityForResult(intent, 0);
    }
}
