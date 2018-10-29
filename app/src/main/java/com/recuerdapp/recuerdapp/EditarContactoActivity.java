package com.recuerdapp.recuerdapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.recuerdapp.custom_shape_views.CircularImageView;
import com.recuerdapp.database.Contacto;
import com.recuerdapp.database.DatabaseHandler;

/**
 * Created by 79192 on 21/11/2017.
 */

public class EditarContactoActivity extends AppCompatActivity {
    private DatabaseHandler databaseHandler;

    private Bitmap bmp;

    private EditText[] dataFilterET;
    private TextInputLayout[] dataFilterLayouts;

    private EditText nombreET,numeroET,parentescoET;
    private TextInputLayout nombreETL,numeroETL,parentescoETL;

    private TextView guardarContactoButton;
    private RelativeLayout cancelButton;
    private Contacto contacto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);
        databaseHandler=new DatabaseHandler(this);
        Intent intent=getIntent();
        contacto=databaseHandler.getContacto(intent.getIntExtra("id",-1));
        ImageView addProfilePicture = (ImageView) findViewById(R.id.agregarFotoPerfil);
        CircularImageView profilePicture = (CircularImageView) findViewById(R.id.perfilImage);

        if (contacto.getContactoProfilePicture() == null){
            profilePicture.setVisibility(View.INVISIBLE);
            addProfilePicture.setVisibility(View.VISIBLE);
            addProfilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CapturarFotomemoriaActivity.class);
                    startActivityForResult(intent,1);
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
                    startActivityForResult(intent,1);
                }
            });
        }
        setup();
        populateValues();

    }
    public void setup(){
        TextView textView= (TextView) findViewById(R.id.description_agregar_contacto);
        textView.setText("Editar Contacto");

        nombreET=(EditText)findViewById(R.id.nombreET);
        numeroET=(EditText)findViewById(R.id.numeroET);
        parentescoET=(EditText)findViewById(R.id.parentescoET);

        nombreETL=(TextInputLayout) findViewById(R.id.layoutAgregarContactoNombre);
        numeroETL=(TextInputLayout)findViewById(R.id.layoutAgregarContactoNumero);
        parentescoETL=(TextInputLayout)findViewById(R.id.layoutAgregarContactoParentesco);

        dataFilterET=new EditText[]{nombreET,parentescoET,numeroET};
        dataFilterLayouts=new TextInputLayout[]{nombreETL,parentescoETL,numeroETL};

        guardarContactoButton=(TextView) findViewById(R.id.submitButton);
        cancelButton=(RelativeLayout)findViewById(R.id.backButton);
        guardarContactoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<dataFilterET.length;i++){
                    if(dataFilterET[i].getText().length()==0) {
                        dataFilterLayouts[i].requestFocus();
                        for(int i2=0;i2<dataFilterET.length;i2++) {
                            if(dataFilterET[i2].getText().length()==0) {
                                dataFilterLayouts[i2].setError("Este campo es requerido.");
                                dataFilterLayouts[i2].setErrorEnabled(true);
                            }
                            else{
                                dataFilterLayouts[i2].setErrorEnabled(false);
                            }
                        }
                        return;
                    }
                }
                contacto.setNombre(nombreET.getText().toString());
                contacto.setNumero(numeroET.getText().toString());
                contacto.setParentesco(parentescoET.getText().toString());
                DatabaseHandler databaseHandler=new DatabaseHandler(getApplicationContext());
                databaseHandler.editContacto(contacto);
                if(bmp!=null){
                    contacto.setContactoProfilePicture(bmp);
                }
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
    public void populateValues(){
        nombreET.setText(contacto.getNombre());
        parentescoET.setText(contacto.getParentesco());
        numeroET.setText(contacto.getNumero());
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String pathMain= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM/";
                bmp = BitmapFactory.decodeFile(pathMain+"/"+data.getStringExtra("file_name"));

            }
            if (resultCode == RESULT_CANCELED) {

            }
            ImageView addProfilePicture = (ImageView) findViewById(R.id.agregarFotoPerfil);
            CircularImageView profilePicture = (CircularImageView) findViewById(R.id.perfilImage);
            if (bmp== null){
                profilePicture.setVisibility(View.INVISIBLE);
                addProfilePicture.setVisibility(View.VISIBLE);
                addProfilePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), CapturarFotomemoriaActivity.class);
                        startActivityForResult(intent,1);
                    }
                });
            }
            else{
                profilePicture.setVisibility(View.VISIBLE);
                addProfilePicture.setVisibility(View.INVISIBLE);
                profilePicture.setImageBitmap(bmp);
                profilePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), CapturarFotomemoriaActivity.class);
                        startActivityForResult(intent,1);
                    }
                });
            }
        }

    }
}
