package com.triptik.dev.triptik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.triptik.dev.triptik.helper.SQLiteHandler;
import com.triptik.dev.triptik.helper.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class port_panel_01 extends Activity implements UploadTriptik.UploadCallback {

    //Constant!
    private static final int MAX_PANELS = 3;
    private static final int REQUEST_PHOTO = 6;
    private static int PANEL_PENDING_PHOTO = 0;

    private static String uniqueTriptikID = (String.valueOf(UUID.randomUUID()));  //  generate triptikID number

    ImageButton ib_1, ib_2, ib_3;
    ImageView iv_1, iv_2, iv_3;
    Button btnTriptikSave;
    ImageButton save, imageCamera_button_1, imageCamera_button_2, imageCamera_button_3;
    EditText etTriptikName;
    File mTempSavedPhoto, triptik_panel_2, triptik_panel_3;
    TextView t_panel_01_indicator, t_panel_02_indicator, t_panel_03_indicator;
    LinearLayout menubar;



    private SQLiteHandler db;
    private SessionManager session;

    // data structure, hold files to be uploaded...
    private List<File> mToUpload;

    private Button.OnClickListener mPanelsClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageCamera_button_1:
                    PANEL_PENDING_PHOTO = 1;
                    break;
                case R.id.imageCamera_button_2:
                    PANEL_PENDING_PHOTO = 2;
                    break;
                case R.id.imageCamera_button_3:
                    PANEL_PENDING_PHOTO = 3;
                    break;
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mTempSavedPhoto = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "panel_" + PANEL_PENDING_PHOTO + ".webp");
            Uri imageFileUri = Uri.fromFile(mTempSavedPhoto);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            startActivityForResult(intent, REQUEST_PHOTO);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.port_panel_01);

        menubar = (LinearLayout) findViewById(R.id.menubar);
        t_panel_01_indicator = (TextView) findViewById(R.id.t_panel_01_indicator);
        t_panel_02_indicator = (TextView) findViewById(R.id.t_panel_02_indicator);
        t_panel_03_indicator = (TextView) findViewById(R.id.t_panel_03_indicator);
        imageCamera_button_1 = (ImageButton) findViewById(R.id.imageCamera_button_1);
        imageCamera_button_2 = (ImageButton) findViewById(R.id.imageCamera_button_2);
        imageCamera_button_3 = (ImageButton) findViewById(R.id.imageCamera_button_3);

        // list files to upload
        mToUpload = new ArrayList<>();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        final HashMap<String, String> user = db.getUserDetails();
        final String userID = user.get("userID");

        final Typeface RalewayMedium = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        final Typeface RalewayLight = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");

        TextView saveTriptikHeaderText = (TextView) findViewById(R.id.tvTriptikSaveHeader);
        saveTriptikHeaderText.setTypeface(RalewayLight);

        TextView saveTriptikHeaderTextSubText = (TextView) findViewById(R.id.tvTriptikSaveHeaderSubText);
        saveTriptikHeaderTextSubText.setTypeface(RalewayLight);

        TextView progressMessageSubText = (TextView) findViewById(R.id.progressMessageSubText);
        progressMessageSubText.setTypeface(RalewayLight);

        TextView progressMessage = (TextView) findViewById(R.id.progressMessage);
        progressMessage.setTypeface(RalewayLight);


        ib_1 = (ImageButton) findViewById(R.id.imageCamera_button_1);
        ib_2 = (ImageButton) findViewById(R.id.imageCamera_button_2);
        ib_3 = (ImageButton) findViewById(R.id.imageCamera_button_3);

        save = (ImageButton) findViewById(R.id.btnSave);
        checkPanalStatus();

        btnTriptikSave = (Button) findViewById(R.id.btnTriptikSave);
        btnTriptikSave.setTypeface(RalewayMedium);

        etTriptikName = (EditText) findViewById(R.id.etTriptikName);

        iv_1 = (ImageView) findViewById(R.id.imageView_1);
        iv_2 = (ImageView) findViewById(R.id.imageView_2);
        iv_3 = (ImageView) findViewById(R.id.imageView_3);

        ib_1.setOnClickListener(mPanelsClickListener);
        ib_2.setOnClickListener(mPanelsClickListener);
        ib_3.setOnClickListener(mPanelsClickListener);

        findViewById(R.id.imageView_1).setOnTouchListener(new MultiTouchListener());
        findViewById(R.id.imageView_2).setOnTouchListener(new MultiTouchListener());
        findViewById(R.id.imageView_3).setOnTouchListener(new MultiTouchListener());

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mToUpload.clear(); //make sure no leftover files

                File panel_1_imageFile = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/panel_1.webp");
                File panel_2_imageFile = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/panel_2.webp");
                File panel_3_imageFile = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/panel_3.webp");
                File gallery_image = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/" + uniqueTriptikID + ".webp");

                // list of files to upload
                mToUpload.add(panel_1_imageFile);
                mToUpload.add(panel_2_imageFile);
                mToUpload.add(panel_3_imageFile);
                mToUpload.add(gallery_image);

                boolean hasAllPanels = panel_1_imageFile.exists() && panel_2_imageFile.exists() && panel_3_imageFile.exists();

                if (hasAllPanels) {
                    getTriptikSaveName(userID);
                    return;
                } else {
                    reportMissingPanels();
                }
            }
        });
    }


    /**
     * @param userID
     */
    private void getTriptikSaveName(final String userID) {

        final RelativeLayout saveTriptikFileName = (RelativeLayout) findViewById(R.id.triptik_save_name_dialog_layout);
        saveTriptikFileName.setVisibility(View.VISIBLE);

        menubar.setVisibility(View.GONE);
        t_panel_01_indicator.setVisibility(View.GONE);
        t_panel_02_indicator.setVisibility(View.GONE);
        t_panel_03_indicator.setVisibility(View.GONE);
        imageCamera_button_1.setVisibility(View.GONE);
        imageCamera_button_2.setVisibility(View.GONE);
        imageCamera_button_3.setVisibility(View.GONE);

        btnTriptikSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String triptikName = etTriptikName.getText().toString().trim();
                if (!triptikName.isEmpty()) {
                    saveTriptikFileName.setVisibility(View.GONE);
                    etTriptikName.getText();
                    uploadFiles(userID, triptikName);
                } else {
                    ToastView("Please enter a Triptik save name to continue...");
                }
            }
        });
    }

    /**
     * @param userID
     * @param saveName
     */
    private void uploadFiles(String userID, String saveName) {
        RelativeLayout progressSaving = (RelativeLayout) findViewById(R.id.progressloader_container);
        progressSaving.setVisibility(View.VISIBLE);
        takeScreenshot(uniqueTriptikID);

        for (File fileToUpload : mToUpload) {
            saveTriptik(fileToUpload, userID, saveName, uniqueTriptikID);
        }
    }


    /**
     * @param uniqueTriptikID
     */
    private void takeScreenshot(String uniqueTriptikID) {
        // image naming and path  to include sd card  appending name you choose for file
        String mPath = "storage/emulated/0/Android/data/com.triptik.dev.triptik/files/" + uniqueTriptikID + ".webp";

        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        File imageFile = new File(mPath);
        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 93;
            bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        t_panel_01_indicator.setVisibility(View.VISIBLE);
        t_panel_02_indicator.setVisibility(View.VISIBLE);
        t_panel_03_indicator.setVisibility(View.VISIBLE);
        imageCamera_button_1.setVisibility(View.VISIBLE);
        imageCamera_button_2.setVisibility(View.VISIBLE);
        imageCamera_button_3.setVisibility(View.VISIBLE);
        menubar.setVisibility(View.VISIBLE);


    }



    /**
     * @return
     */
    private List<Integer> findMissingPanels() {
        List<Integer> missingPanels = new ArrayList<>();

        for (int i = 0; i < mToUpload.size(); i++) {
            File file = mToUpload.get(i);
            if (!file.exists()) {
                missingPanels.add(i + 1); // + 1 because we are 0 index
            }
        }

        return missingPanels;
    }

    /**
     * All reporting goes here. Make it pretty !
     */
    private void reportMissingPanels() {
        String errorMsg = null; //display msg does first!

        List<Integer> missingPanels = findMissingPanels();
        String noPanels = "You have to take 3 Pictures to save to Triptik";
        String baseErrorStr = "No pictures taken for Panel : %d";
        String extraPanelStr = ", Panel : %d";

        if (missingPanels.size() == MAX_PANELS) {
            ToastView(noPanels);
            return; // finish
        }

        // missing panels to display ...
        for (Integer panel : missingPanels) {
            if (errorMsg == null) { // first case?
                errorMsg = String.format(baseErrorStr, panel);
            } else {
                errorMsg += String.format(extraPanelStr, panel);
            }
        }

        //display
        ToastView(errorMsg);
    }

    /**
     * saveTriptik Method
     * <p/>
     * This is the main method for uploading the images and
     * all metadata required by the database and PHP files.
     *
     * @param panelNumber
     * @param userID
     */
    void saveTriptik(File panelNumber, String userID, String saveName, String uniqueTriptikID) {

        final String uploadThis = panelNumber.toString();
        final String uploadWith = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/triptikSave.php";
        final String triptikID = uniqueTriptikID;
        final String uploadWho = userID;
        final String uploadTo = "users/" + userID + "/gallery/" + triptikID + "/";
        final String panelInstance = panelNumber.getPath().substring(68, 69);
        //final String panelInstance = panelNumber.getPath();
        final String triptikTitle = saveName;
        final UploadTriptik uploadTriptik = new UploadTriptik();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                uploadTriptik.uploadTriptikFile(uploadThis, uploadWith, uploadTo, uploadWho, panelInstance, triptikTitle, triptikID, port_panel_01.this);
            }
        });

        Log.i(triptikID.toString() + " - uploadThis", uploadThis.toString());
        Log.i(triptikID.toString() + " - uploadWith", uploadWith.toString());
        Log.i(triptikID.toString() + " - uploadTo", uploadTo.toString());
        Log.i(triptikID.toString() + " - uploadWho", uploadWho.toString());
        Log.i(triptikID.toString() + " - panelInstance", panelInstance.toString());
        Log.i(triptikID.toString() + " - triptikTitle", triptikTitle.toString());
    }


    private void checkPanalStatus(){
        File panel_1_imageFile = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/panel_1.webp");
        File panel_2_imageFile = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/panel_2.webp");
        File panel_3_imageFile = new File("storage/emulated/0/Android/data/com.triptik.dev.triptik/files/panel_3.webp");
        boolean hasAllPanels = panel_1_imageFile.exists() && panel_2_imageFile.exists() && panel_3_imageFile.exists();
        if (hasAllPanels) {
            save.setVisibility(View.VISIBLE);
            return;
        }
    };
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        checkPanalStatus();

        if (requestCode == REQUEST_PHOTO) {
            if (resultCode == RESULT_OK) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(mTempSavedPhoto.toString(), options);

                ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.WEBP, 93, bytesOut);

                saveBitmapToFile(bytesOut, "storage/emulated/0/Android/data/com.triptik.dev.triptik/files/panel_" + PANEL_PENDING_PHOTO + ".webp");
                saveBitmapToPanel(bitmap, PANEL_PENDING_PHOTO);
            }

            File tempPanel_1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "panel_1.webp");
            File tempPanel_2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "panel_2.webp");
            File tempPanel_3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "panel_3.webp");
            tempPanel_1.delete();
            Log.i("Removing TempContent", "Panel_1");
            tempPanel_2.delete();
            Log.i("Removing TempContent", "Panel_2");
            tempPanel_3.delete();
            Log.i("Removing TempContent", "Panel_3");
        }
    }

    /**
     * @param bytesOut
     * @param path
     */
    private void saveBitmapToFile(ByteArrayOutputStream bytesOut, String path) {
        byte[] bitmapdata = bytesOut.toByteArray();

        //write the bytes in file
        File newFile = new File(path);

        try {
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBitmapToPanel(Bitmap bitmap, int panel) {
        switch (panel) {
            case 1:
                iv_1.setImageBitmap(bitmap);
                break;

            case 2:
                iv_2.setImageBitmap(bitmap);
                break;
            case 3:
                iv_3.setImageBitmap(bitmap);
                break;
            default:
                // throw exception, no panel nr!
                break;
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

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(port_panel_01.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void ToastView(String toastTextString) {
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
     * @param content
     * @param httpStatus - {@see org.apache.http.HttpStatus}
     */
    @Override
    public void onResponse(String content, int httpStatus) {
        if (!mToUpload.isEmpty()) {
            mToUpload.remove(mToUpload.get(0)); //remove first, at index 0
            Log.i("New Thread Count", String.valueOf(Thread.activeCount()));
            Log.i("mUpload", mToUpload.toString().trim());
        }

        if (mToUpload.isEmpty()) { //no more files to send ?
            // request bulk finish!
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout progressSaving = (RelativeLayout) findViewById(R.id.progressloader_container);
                    progressSaving.setVisibility(View.GONE);
                    //redisplayControOverlay();
                    ToastView("Triptik Saved\n" + etTriptikName.getText().toString().trim());
                }
            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPanalStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
        checkPanalStatus();
    }
}