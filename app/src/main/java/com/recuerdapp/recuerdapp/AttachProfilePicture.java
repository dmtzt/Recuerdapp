package com.recuerdapp.recuerdapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.recuerdapp.database.MemoriaImgYVid;
import com.recuerdapp.real_path_util.RealPathUtil;

/**
 * Created by 79192 on 31/12/2017.
 */

public class AttachProfilePicture extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            String realPath = "error";
            if (resultCode == RESULT_OK) {

                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                else
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                if (!realPath.equals("error")) {

                }
            }

        }
    }
}
