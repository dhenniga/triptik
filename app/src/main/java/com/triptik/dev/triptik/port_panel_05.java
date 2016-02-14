package com.triptik.dev.triptik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class port_panel_05 extends Activity {

    ImageButton ib_1, ib_2, ib_3;
    ImageView iv_1, iv_2, iv_3;
    private File triptik_panel_1;
    private File triptik_panel_2;
    private File triptik_panel_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.port_panel_05);

        Typeface RalewayMedium = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

        TextView header_1 = (TextView) findViewById(R.id.t_panel_01_indicator);
        header_1.setTypeface(RalewayMedium);

        TextView header_2 = (TextView) findViewById(R.id.t_panel_02_indicator);
        header_2.setTypeface(RalewayMedium);

        TextView header_3 = (TextView) findViewById(R.id.t_panel_03_indicator);
        header_3.setTypeface(RalewayMedium);

        ib_1 = (ImageButton) findViewById(R.id.imageCamera_button_1);
        ib_2 = (ImageButton) findViewById(R.id.imageCamera_button_2);
        ib_3 = (ImageButton) findViewById(R.id.imageCamera_button_3);

        iv_1 = (ImageView) findViewById(R.id.imageView_1);
        iv_2 = (ImageView) findViewById(R.id.imageView_2);
        iv_3 = (ImageView) findViewById(R.id.imageView_3);

        ib_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                triptik_panel_1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "panel_1.jpg");
                Uri imageFileUri = Uri.fromFile(triptik_panel_1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(intent, 0);
            }
        });

        ib_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                triptik_panel_2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "panel_2.jpg");
                Uri imageFileUri = Uri.fromFile(triptik_panel_2);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(intent, 1);
            }
        });

        ib_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                triptik_panel_3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "panel_3.jpg");
                Uri imageFileUri = Uri.fromFile(triptik_panel_3);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(intent, 2);
            }
        });

        findViewById(R.id.imageView_1).setOnTouchListener(new MultiTouchListener());
        findViewById(R.id.imageView_2).setOnTouchListener(new MultiTouchListener());
        findViewById(R.id.imageView_3).setOnTouchListener(new MultiTouchListener());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(triptik_panel_1.toString(), options);
            iv_1.setImageBitmap(bitmap);
            return;

        } else if (requestCode == 1) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(triptik_panel_2.toString(), options);
            iv_2.setImageBitmap(bitmap);
            return;

        } else if (requestCode == 2) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(triptik_panel_3.toString(), options);
            iv_3.setImageBitmap(bitmap);
            return;

        }
    }

    //  If the user presses the back button in mid-creation, a message will appear.
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Leaving this screen and returning to the template selection screen will delete any progress?  Do you wish to continue?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Save in Drafts and exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}