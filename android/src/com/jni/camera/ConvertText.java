
package com.jni.camera;

import android.app.Activity;
import android.Manifest;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.*;


import com.googlecode.leptonica.android.Pixa;
import com.googlecode.tesseract.android.TessBaseAPI;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.os.Environment;
import android.net.Uri;
import android.widget.Toast;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
//import android.content.ActivityNotFoundException;

import java.util.Timer;
import java.util.TimerTask;

public class ConvertText {

    private static Activity m_ActivityInstance;

    public static final int REQUEST_CAPTURE_IMAGE  = 1;
    public static String inputFileName;
    public static Uri outputImgUri;

    public static native void KeyInformation(String keyBody);

    public static Timer timer;
    public static int seconds = 0;
    public static TimerTask task;
    public static Intent cameraIntent;
    public static File outputImageFile;

    public static void Init(Activity ActivityInstance)
    {

        m_ActivityInstance = ActivityInstance;
        timer = new Timer();
        MyTimer();

    }

    public static String ReadDataConvertText(Context context,String filename)
    {
           try {
                     timer.schedule(task, 0, 1000);
                     BitmapFactory.Options options = new BitmapFactory.Options();
                     options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                     Bitmap bitmap = BitmapFactory.decodeFile(filename, options);//context.getAssets().open("capture.jpg")
                     File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/tesseract/tessdata");
                         if(!f.exists()){
                               f.mkdirs();
                       }
                     File f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/tesseract/tessdata/eng.traineddata");
                           if(!f1.exists()){
                                   InputStream in = context.getAssets().open("tessdata/eng.traineddata");
                                   FileOutputStream fout = new FileOutputStream(f1);
                           byte[] buf = new byte[1024];
                           int len;
                           while ((len = in.read(buf)) > 0) {
                                   fout.write(buf, 0, len);
                           }
                           in.close();
                           fout.close();
                      }

                     TessBaseAPI tessBaseApi = new TessBaseAPI();
                     tessBaseApi.init(Environment.getExternalStorageDirectory().getAbsolutePath()+"/tesseract", "eng");
                     tessBaseApi.setImage(bitmap);
                     String extractedText = tessBaseApi.getUTF8Text();

                     seconds=0;
                     timer.cancel();
                     timer.purge();
                     tessBaseApi.end();
                     return extractedText;

                 }
             catch (Exception e) {
                            e.printStackTrace();
                 }

             return "Error-1 No Read Image";

    }
    public static void SetPermission()
    {

        Dexter.withActivity(m_ActivityInstance).withPermissions(
        Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CAMERA
                         ).withListener(new MultiplePermissionsListener() {
                     @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                     }
                     @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
                    }).check();
    }
    public static void OpenCamera(Context context)
    {
        try {

            String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/"+m_ActivityInstance.getPackageName()+"/imageData";
            File pictureSaveFolderPath = new File(filepath);
            if(!pictureSaveFolderPath.exists()){
                   pictureSaveFolderPath.mkdirs();
            }

            String imageFileName = "outputImage.jpg";   //System.currentTimeMillis()
            outputImageFile = new File(filepath, imageFileName);

            if (outputImageFile.exists()) {
                outputImageFile.delete();
            }
            outputImgUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", outputImageFile);
            inputFileName = filepath+"/"+imageFileName;
            cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputImgUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            m_ActivityInstance.startActivityForResult(cameraIntent, REQUEST_CAPTURE_IMAGE );


        }
        catch (Exception  e) {
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(m_ActivityInstance, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }

    }
   public static void sendKey(String sendKeyData)
   {
       KeyInformation(sendKeyData);
   }
   public static void MyTimer() {

        task = new TimerTask() {
            @Override
            public void run() {
                if (seconds > 2) {
                    seconds=0;
                    timer.cancel();
                    timer.purge();
                    //sendKey("Error-2 No Read Image");
                    System.exit(0);

                }
                seconds++;
            }
        };

    }

}
