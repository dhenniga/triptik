package com.triptik.dev.triptik;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;

public class port_select_layout extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.port_select_layout);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Laffayette_Comic_Pro.ttf");

        TextView header_1 = (TextView) findViewById(R.id.section_description);
        header_1.setTypeface(face);

        TextView header_3 = (TextView) findViewById(R.id.aspect_header_text);
        header_3.setTypeface(face);

        TextView header_4 = (TextView) findViewById(R.id.select_a_template_text);
        header_4.setTypeface(face);

        File dir = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }

    }


    public void port_panel_01_OnClick(View v) {
        Intent intent = new Intent(v.getContext(), port_panel_01.class);
        startActivityForResult(intent, 0);
    }

    public void port_panel_02_OnClick(View v) {
        Intent intent = new Intent(v.getContext(), port_panel_02.class);
        startActivityForResult(intent, 0);
    }

    public void port_panel_03_OnClick(View v) {
        Intent intent = new Intent(v.getContext(), port_panel_03.class);
        startActivityForResult(intent, 0);
    }

    public void port_panel_05_OnClick(View v) {
        Intent intent = new Intent(v.getContext(), port_panel_05.class);
        startActivityForResult(intent, 0);
    }

    public void port_panel_07_OnClick(View v) {
        Intent intent = new Intent(v.getContext(), port_panel_07.class);
        startActivityForResult(intent, 0);
    }

    public void port_panel_08_OnClick(View v) {
        Intent intent = new Intent(v.getContext(), port_panel_08.class);
        startActivityForResult(intent, 0);
    }


}
