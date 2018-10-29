package com.recuerdapp.recuerdapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.recuerdapp.database.DatabaseHandler;

/**
 * Created by 79192 on 16/11/2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        DatabaseHandler handler=new DatabaseHandler(this.getApplicationContext());
        if(handler.getPerfil()!=null) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        else{
            Intent intent=new Intent(SplashActivity.this, EditarPerfilActivity.class);
            intent.putExtra("initial",true);
            startActivity(intent);
        }
        // close splash activity
        finish();
    }
}
