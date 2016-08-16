package com.triptik.dev.triptik;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.triptik.dev.triptik.app.AppConfig;

import net.gotev.uploadservice.UploadService;

import org.apache.http.client.methods.HttpPost;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class Upload extends AppCompatActivity {
//
//
//    static RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//    public static int uploadComment(final String commentText, final String userID, final String triptikID) {
//
//        int serverResponseCode = 0;
//
//
//
//                StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_COMMENTS, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        System.out.println(response.toString());
//
//                    }
//                }) {
//
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String,String> parameters  = new HashMap<>();
//                        parameters.put("commentText",commentText);
//                        parameters.put("triptikID",triptikID);
//                        parameters.put("userID", userID);
//                        return parameters;
//                    }
//                };
//                requestQueue.add(request);
//            }


    public static int uploadFile(String uploadThis, String uploadWith, String uploadTo, String uploadWho, UploadCallback callback) {

        int serverResponseCode = 0;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(uploadThis);
            if (!sourceFile.isFile()) {
                Log.e("uploadFile", "Source File not exist :" + uploadThis);
                return 0;
            } else {


                try {
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);


                    URL url = new URL(uploadWith);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", uploadThis);
                    conn.setRequestProperty("upload_to", uploadTo);
                    conn.setRequestProperty("userID", uploadWho);

                    dos = new DataOutputStream(conn.getOutputStream());


                    //first parameter - upload_to
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"upload_to\"" + lineEnd + lineEnd + uploadTo + lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"userID\"" + lineEnd + lineEnd + uploadWho + lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name='uploaded_file'; filename='" + uploadThis + "'" + lineEnd);
                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();
                    Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                    //callback
                    callback.onResponse(serverResponseMessage, serverResponseCode);

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {

                    ex.printStackTrace();
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

                } catch (Exception e) {

                    e.printStackTrace();
                    Log.e("Upload Exception", "Exception : " + e.getMessage(), e);
                }
                return serverResponseCode;
            }
        }

    /**
     *
     */
    public interface UploadCallback {
        /**
         *
         * @param content
         * @param httpStatus - {@see org.apache.http.HttpStatus}
         */
        void onResponse(String content, int httpStatus);
    }
}
