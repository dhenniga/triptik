package com.triptik.dev.triptik;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.triptik.dev.triptik.app.AppConfig;
import com.triptik.dev.triptik.app.AppController;
import com.triptik.dev.triptik.helper.SQLiteHandler;
import com.triptik.dev.triptik.helper.SessionManager;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class RegisterActivity extends Activity implements Upload.UploadCallback {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private File mFile, profilePic;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);


        Typeface RalewayBold = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        Typeface RalewayMedium = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        Typeface RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");

        TextView nameInputFont = (EditText) findViewById(R.id.name);
        nameInputFont.setTypeface(RalewayBold);

        TextView emailInputFont = (EditText) findViewById(R.id.email);
        emailInputFont.setTypeface(RalewayBold);

        TextView passwordInputFont = (EditText) findViewById(R.id.password);
        passwordInputFont.setTypeface(RalewayBold);

        TextView linkToLoginScreenFont = (Button) findViewById(R.id.btnLinkToLoginScreen);
        linkToLoginScreenFont.setTypeface(RalewayMedium);

        TextView registerButtonFont = (Button) findViewById(R.id.btnRegister);
        registerButtonFont.setTypeface(RalewayMedium);

        TextView testButtonFont = (TextView) findViewById(R.id.section_description);
        testButtonFont.setTypeface(RalewayLight);

        /**
         *
         * Delete Profile Image file
         */
        mFile = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/pic.jpg");
        profilePic = new File ("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/pic.webp");
        mFile.delete();
        profilePic.delete();


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                final EditText emailValidate = (EditText) findViewById(R.id.email);
                String emailVal = emailValidate.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (emailVal.matches(emailPattern)) {
                    if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        registerUser(name, email, password);
                    } else {
                        ToastView("Please Enter your Details!");
                    }
                } else {
                    ToastView("Invalid Email Address");
                }
            }
        });


        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String name, final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        mFile = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/pic.jpg");

                        Bitmap photo = BitmapFactory.decodeFile(mFile.toString());
                        photo = Bitmap.createScaledBitmap(photo, 500, 667, false);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.WEBP, 60, bytes);

                        profilePic = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/pic.webp");
                        try {
                            profilePic.createNewFile();
                            FileOutputStream fo = new FileOutputStream(profilePic);
                            fo.write(bytes.toByteArray());
                            fo.close();
                        } catch (Exception e) {

                        }



                        final Upload upload = new Upload();
                        try {

                            if (profilePic.exists()) {

                                final String uploadThis = profilePic.toString();
                                final String uploadWith = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/uploadProfilePicture.php";
                                final String uploadTo = "users/" + uid + "/";
                                final String uploadWho = uid;


                                new Thread(new Runnable() {
                                    public void run() {
                                        upload.uploadFile(uploadThis, uploadWith, uploadTo, uploadWho, RegisterActivity.this);
                                    }
                                }).start();

                                ToastView("UserID: " + uploadTo);
                            } else {
                                ToastView("No Profile Image Detected");
                            }
                        } catch (Exception e) {
                            Log.e("AndroidUploadService", e.getMessage(), e);

                        }


                        JSONObject user = jObj.getJSONObject("user");
                        String userID = user.getString("userID");
                        String name = user.getString("name");
                        String email = user.getString("email");

                        // Inserting row in users table
                        db.addUser(userID, name, email);

                        ToastView("User successfully registered. Try login now!");


//                        File dir = getFilesDir();
//                        File file = new File(dir, "pic.jpg");
//                        boolean deleted = file.delete();

                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

    /**
     *
     * @param content
     * @param httpStatus - Check this for valid http status codes {@see org.apache.http.HttpStatus}
     */
    @Override
    public void onResponse(String content, int httpStatus) {
        if (httpStatus == HttpStatus.SC_OK) {
            // all good ... do stuff
        }
    }
}