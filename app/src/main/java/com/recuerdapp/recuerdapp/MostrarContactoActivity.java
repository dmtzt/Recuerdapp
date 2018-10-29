package com.recuerdapp.recuerdapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.recuerdapp.custom_shape_views.CircularImageView;
import com.recuerdapp.database.Contacto;
import com.recuerdapp.database.DatabaseHandler;

/**
 * Created by 79192 on 01/01/2018.
 */

public class MostrarContactoActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_contacto);
        databaseHandler=new DatabaseHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        final Contacto contacto=databaseHandler.getContacto(intent.getIntExtra("id",-1));

        TextView nombreView=(TextView) findViewById(R.id.nombreView);
        nombreView.setText(contacto.getNombre());

        TextView parentescoView=(TextView) findViewById(R.id.parentescoView);
        parentescoView.setText(contacto.getParentesco());

        TextView ctDirNumView=(TextView) findViewById(R.id.numeroView);
        ctDirNumView.setText(contacto.getNumero());
        ImageView callNum=(ImageView)findViewById(R.id.llamarNumeroButton);
        callNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + contacto.getNumero()));
                startActivity(intent);
            }
        });
        ImageView edPerfView=(ImageView) findViewById(R.id.editContactoButton);
        edPerfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), EditarContactoActivity.class);
                intent.putExtra("id",contacto.getId());
                startActivity(intent);
            }
        });
        RelativeLayout cancelButton=(RelativeLayout)findViewById(R.id.backButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        TextView eliminarContactoButton=(TextView)findViewById(R.id.eliminarContactoButton);
        eliminarContactoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                databaseHandler.removeContacto(intent.getIntExtra("id",-1));
                finish();
            }
        });

        ImageView addProfilePicture = (ImageView) findViewById(R.id.agregarFotoPerfil);
        CircularImageView profilePicture = (CircularImageView) findViewById(R.id.perfilImage);

        if (contacto.getContactoProfilePicture()== null){
            profilePicture.setVisibility(View.INVISIBLE);
            addProfilePicture.setVisibility(View.VISIBLE);
            addProfilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CapturarFotomemoriaActivity.class);
                    intent.putExtra("contact",true);
                    intent.putExtra("contact_id",contacto.getId());
                    startActivity(intent);
                }
            });
        }
        else{
            profilePicture.setVisibility(View.VISIBLE);
            addProfilePicture.setVisibility(View.INVISIBLE);
            profilePicture.setImageBitmap(contacto.getContactoProfilePicture());
            profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CapturarFotomemoriaActivity.class);
                    intent.putExtra("contact",true);
                    intent.putExtra("contact_id",contacto.getId());
                    startActivity(intent);
                }
            });
        }

    }

}
