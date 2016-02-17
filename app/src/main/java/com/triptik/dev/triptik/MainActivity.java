package com.triptik.dev.triptik;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triptik.dev.triptik.helper.SQLiteHandler;
import com.triptik.dev.triptik.helper.SessionManager;

import java.net.URL;
import java.util.HashMap;

public class MainActivity extends Activity {

    private TextView txtName, txtEmail, txtUserID,menubar_triptik_name;
    private Button btnLogout;
    private ImageButton btnNewTriptik, btnGallery;
    private Button btnGetStarted;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        menubar_triptik_name = (TextView) findViewById(R.id.menubar_triptik_name);
        menubar_triptik_name.setText("Welcome to Triptik");

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtUserID = (TextView) findViewById(R.id.userID);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnGetStarted = (Button) findViewById(R.id.btnGetStarted);

        Typeface RalewayMedium = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        Typeface RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");


        txtName.setTypeface(RalewayLight);
        txtEmail.setTypeface(RalewayLight);
        txtUserID.setTypeface(RalewayLight);
        btnLogout.setTypeface(RalewayMedium);

        btnNewTriptik = (ImageButton) findViewById(R.id.btnNewTriptik);
        btnNewTriptik.setVisibility(View.VISIBLE);

        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        btnGallery.setVisibility(View.VISIBLE);


        btnNewTriptik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AspectSelect.class);
                startActivity(intent);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });




        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // go to login screen is session is not logged on
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String userID = user.get("userID");
        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtUserID.setText((userID));
        txtName.setText(name);
        txtEmail.setText(email);

        String photo_url_str = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/users/" + userID + "/pic.webp";
        ImageView imageView = (ImageView) findViewById(R.id.profile_image_welcome);
        Picasso.with(this).load(photo_url_str).into(imageView);

        // Get Started button click event
        btnGetStarted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
            }
        });

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
