
package com.jni.camera;


import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.os.Message;
import android.content.Intent;
import android.provider.MediaStore;
import android.media.MediaScannerConnection;
import android.net.Uri;
import java.io.*;
import android.os.Environment;
import android.support.v4.content.FileProvider;

public class AppActivity extends org.qtproject.qt5.android.bindings.QtActivity
{

    public static final int PIC_CROP = 2;
    public AppActivity()
    {        

        ConvertText.Init(this);

    }
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ConvertText.REQUEST_CAPTURE_IMAGE){

           File picture = new File(ConvertText.inputFileName);
           if(!picture.exists()) return;
           performCrop(picture);
         }
       else if (requestCode == PIC_CROP){
           String readData = ConvertText.ReadDataConvertText(getApplicationContext(),ConvertText.inputFileName);
           if(!readData.contains("Key")){ ConvertText.sendKey("Error-1 No found Key,Please try again."); return;}

           int start = readData.indexOf("Key")+5 < readData.length() ? readData.indexOf("Key")+5 : -1;
           if(start<0){ ConvertText.sendKey("Error-2 No found Key,Please try again."); return;}

           int end = readData.indexOf("Key")+13 <= readData.length() ? readData.indexOf("Key")+13 : -1;
           if(end<0){ ConvertText.sendKey("Error-3 No found Key,Please try again."); return;}

           String key = readData.subSequence(start,end).toString();
           ConvertText.sendKey(key.replaceAll("\\s+",""));

        }
    }
    private void performCrop(File img){

        try {

            Uri pUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", img);
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
           // cropIntent.setAction(android.content.Intent.ACTION_VIEW);
            cropIntent.setDataAndType(pUri, "image/*");
//            Log.i("TAG", "startPhotoZoom"+ ConvertText.outputImgUri +" uri");
            cropIntent.putExtra("crop", "true");
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, pUri);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch (Exception e) {
                   e.printStackTrace();
        }


    }

}

