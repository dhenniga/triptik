package com.triptik.dev.triptik;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;

public class PortSelectLayout extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.port_select_layout);


        //  Delete all the files in the working directory
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

    /**
     *
     * @param panel
     */
    public void selectPanel(View panel) {
        Intent intent = new Intent(panel.getContext(), PanelsActivity.class);

        int layoutId = R.layout.port_panel_01;
        switch (panel.getId()) {
            case R.id.port_panel_01:
                layoutId = R.layout.port_panel_01;
                break;
            case R.id.port_panel_02:
                layoutId = R.layout.port_panel_02;
                break;
            case R.id.port_panel_03:
                layoutId = R.layout.port_panel_03;
                break;
            case R.id.port_panel_05:
                layoutId = R.layout.port_panel_05;
                break;
            case R.id.port_panel_07:
                layoutId = R.layout.port_panel_07;
                break;
            case R.id.port_panel_08:
                layoutId = R.layout.port_panel_08;
                break;
        }
        //add to arguments to be read on the resulting Activity
        intent.putExtra(PanelsActivity.PANEL_ID_KEY, layoutId);

        startActivityForResult(intent, 0);
    }
}
