
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
import com.googlecode.tesseract.android.TessBaseAPI;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.os.Environment;



public class ConvertText {

    private static Activity m_ActivityInstance;

    public static void Init(Activity ActivityInstance)
    {
        m_ActivityInstance = ActivityInstance;

    }
    public static String ReadDataConvertText(Context context,InputStream fileStream)
    {
           Dexter.withActivity(m_ActivityInstance).withPermissions(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.INTERNET,
                                    Manifest.permission.ACCESS_WIFI_STATE,
                                    Manifest.permission.ACCESS_NETWORK_STATE
                            ).withListener(new MultiplePermissionsListener() {
                        @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                        }
                        @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
                    }).check();


            try {
                     BitmapFactory.Options options = new BitmapFactory.Options();
                     options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                     Bitmap bitmap = BitmapFactory.decodeStream(fileStream,null, options);

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
                     tessBaseApi.end();

                     return extractedText;
                 }
             catch (IOException e) {
                            e.printStackTrace();
                 }

             return "No Read Image";

    }

}
