
package com.jni.camera;


import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.os.Message;
import android.content.Intent;

import java.io.*;

public class AppActivity extends org.qtproject.qt5.android.bindings.QtActivity
{

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
           ConvertText.ReadDataConvertText(getApplicationContext(),ConvertText.inputFileName);
        }
    }


}

