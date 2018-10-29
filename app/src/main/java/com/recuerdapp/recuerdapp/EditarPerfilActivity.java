package com.recuerdapp.recuerdapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.recuerdapp.custom_shape_views.CircularImageView;
import com.recuerdapp.database.DatabaseHandler;
import com.recuerdapp.database.Perfil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by 79192 on 21/11/2017.
 */

public class EditarPerfilActivity extends AppCompatActivity{
    private boolean initial;

    private RelativeLayout cancelButton;

    private TextView submitButton;

    private TextView descriptionView;

    private EditText nombreET,contactoNumeroET,calleET,numeroET,coloniaET,ciudadET,alergiasET,medicamentosET,antecedentesPatET,antecedentesHerET;
    private TextInputLayout nombreETL,contactoNumeroETL,calleETL,numeroETL,coloniaETL,ciudadETL,alergiasETL,medicamentosETL,antecedentesPatETL,
            antecedentesHerETL;

    private Spinner contactoTypePicker,sangreTipoPicker,generoPicker,estadoPicker;


    private DatePickerDialog dtFdNac;


    private  EditText fdNacET;
    private TextInputLayout fdNacETL;

    private final String[] contactoDirectoV={"Hospital","Médico"};
    private final String[] estadoDomicilioV = {"Aguascalientes", "Baja California", "Baja California Sur",
            "Campeche", "Chiapas", "Chihuahua", "Ciudad de México", "Coahuila", "Colima",
            "Durango", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Estado de México",
            "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro",
            "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas",
            "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas"};
    private final String[] generoV = {"Femenino", "Masculino"};
    private final String[] tipoDeSangreV = {"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    private AppCompatActivity mainView;

    private EditText[] dataFilterET;
    private TextInputLayout[] dataFilterLayouts;

    private SimpleDateFormat dateFormatter;

    private int dtY;
    private int dtM;
    private int dtD;

    private ArrayAdapter<String> adapterCTT;
    private ArrayAdapter<String> adapterG;
    private ArrayAdapter<String> adapterTS;
    private ArrayAdapter<String> adapterED;

    Bitmap bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        Intent intent=getIntent();
        initial=intent.getBooleanExtra("initial",false);
        mainView=this;
        ImageView addProfilePicture = (ImageView) findViewById(R.id.agregarFotoPerfil);
        CircularImageView profilePicture = (CircularImageView) findViewById(R.id.perfilImage);
        bmp=Perfil.getProfilePicture();
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
        setupWidgets();
        populateValues();
    }
    private void populateValues(){
        DatabaseHandler databaseHandler=new DatabaseHandler(mainView.getApplicationContext());
        Perfil perfil=databaseHandler.getPerfil();
        if(perfil!=null) {
            nombreET.setText(perfil.getNombre());
            contactoTypePicker.setSelection(adapterCTT.getPosition(perfil.getContactoDirectoT()));
            contactoNumeroET.setText(perfil.getContactoDirectoN());
            sangreTipoPicker.setSelection(adapterTS.getPosition(perfil.getTipoSanguineo()));
            generoPicker.setSelection(adapterG.getPosition(perfil.getGenero()));
            calleET.setText(perfil.getDirCalle());
            numeroET.setText(perfil.getDirNum());
            coloniaET.setText(perfil.getDirCol());
            ciudadET.setText(perfil.getDirCiudad());
            estadoPicker.setSelection(adapterED.getPosition(perfil.getDirEstado()));
            alergiasET.setText(perfil.getAlergias());
            medicamentosET.setText(perfil.getMedicamentosYHorario());
            antecedentesPatET.setText(perfil.getAntecedentesPatologicos());
            antecedentesHerET.setText(perfil.getAntecedentesHeredofamiliares());

            dtY=perfil.getFechaNacimiento().get(Calendar.YEAR);
            dtM=perfil.getFechaNacimiento().get(Calendar.MONTH);
            dtD=perfil.getFechaNacimiento().get(Calendar.DATE);

            Calendar newDate = Calendar.getInstance();
            newDate.set(dtY, dtM, dtD);
            fdNacET.setText(dateFormatter.format(newDate.getTime()));
            dtFdNac.getDatePicker().updateDate(dtY,dtM,dtD);

        }
    }
    private void setupWidgets(){

        descriptionView=(TextView)findViewById(R.id.description_editar_perfil);
        descriptionView.setText("Editar Perfil");
        cancelButton=(RelativeLayout) findViewById(R.id.backButton);

        if(initial) {
            descriptionView.setText("Crear Perfil");
            cancelButton.setVisibility(View.INVISIBLE);
        }
        else{
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainView.finish();
                }
            });
        }


        nombreET=(EditText)findViewById(R.id.nombreET);
        contactoTypePicker=(Spinner)findViewById(R.id.contactoTypePicker);
        contactoNumeroET=(EditText)findViewById(R.id.contactoNumeroET);
        sangreTipoPicker=(Spinner)findViewById(R.id.sangreTipoPicker);
        generoPicker=(Spinner)findViewById(R.id.generoPicker);
        calleET=(EditText)findViewById(R.id.calleET);
        numeroET=(EditText)findViewById(R.id.numeroET);
        coloniaET=(EditText)findViewById(R.id.coloniaET);
        ciudadET=(EditText)findViewById(R.id.ciudadET);
        estadoPicker=(Spinner)findViewById(R.id.estadoPicker);
        alergiasET=(EditText)findViewById(R.id.alergiasET);
        medicamentosET=(EditText)findViewById(R.id.medicamentosYHorarioET);
        antecedentesPatET=(EditText)findViewById(R.id.antecedentesPatET);
        antecedentesHerET=(EditText)findViewById(R.id.antecedentesHerET);

        nombreETL=(TextInputLayout) findViewById(R.id.editarPerfilLayoutNombre);
        contactoNumeroETL=(TextInputLayout)findViewById(R.id.editarPerfilLayoutCtNumero);
        calleETL=(TextInputLayout)findViewById(R.id.editarPerfilLayoutDirCalle);
        numeroETL=(TextInputLayout)findViewById(R.id.editarPerfilLayoutDirNumero);
        coloniaETL=(TextInputLayout)findViewById(R.id.editarPerfilLayoutDirCol);
        ciudadETL=(TextInputLayout)findViewById(R.id.editarPerfilLayoutDirCiudad);
        alergiasETL=(TextInputLayout)findViewById(R.id.editarPerfilLayoutAlergias);
        medicamentosETL=(TextInputLayout)findViewById(R.id.editarPerfilLayoutMedYH);
        antecedentesPatETL=(TextInputLayout)findViewById(R.id.editarPerfilLayoutAntePat);
        antecedentesHerETL=(TextInputLayout)findViewById(R.id.editarPerfilLayoutAnteHer);

        final List<String> ctTipoList = new ArrayList<>(Arrays.asList(contactoDirectoV));
        adapterCTT = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,ctTipoList);
        adapterCTT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactoTypePicker.setAdapter(adapterCTT);

        final List<String> generoList = new ArrayList<>(Arrays.asList(generoV));
        adapterG = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,generoList);
        adapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generoPicker.setAdapter(adapterG);

        final List<String> tipoSList = new ArrayList<>(Arrays.asList(tipoDeSangreV));
        adapterTS = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,tipoSList);
        adapterTS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sangreTipoPicker.setAdapter(adapterTS);

        final List<String> estadoList = new ArrayList<>(Arrays.asList(estadoDomicilioV));
        adapterED = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,estadoList);
        adapterED.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoPicker.setAdapter(adapterED);



        fdNacET=(EditText)findViewById(R.id.fdNacET);
        fdNacETL=(TextInputLayout) findViewById(R.id.editarPerfilLayoutFdNac);
        fdNacET.setInputType(InputType.TYPE_NULL);
        dateFormatter=new SimpleDateFormat("MM/dd/yyyy", Locale.ROOT);
        Calendar newCalendar = Calendar.getInstance();
        dtFdNac = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fdNacET.setText(dateFormatter.format(newDate.getTime()));
                dtY=year;
                dtM=monthOfYear;
                dtD=dayOfMonth;
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dtFdNac.setMessage("Fecha de Nacimiento");

        Calendar newDate = Calendar.getInstance();
        dtFdNac.getDatePicker().setMaxDate(newDate.getTimeInMillis());

        fdNacET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dtFdNac.show();
            }
        });
        fdNacET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dtFdNac.show();
                }
            }
        });




        dataFilterET=new EditText[]{nombreET,contactoNumeroET,fdNacET,calleET,
                numeroET,coloniaET,ciudadET};
        dataFilterLayouts=new TextInputLayout[]{nombreETL,contactoNumeroETL,fdNacETL,calleETL,
                numeroETL,coloniaETL,ciudadETL};
        submitButton=(TextView)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
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
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.YEAR,dtY);
                calendar.set(Calendar.MONTH,dtM);
                calendar.set(Calendar.DATE,dtD);
                //,dtM,dtD


                Perfil perfil=new Perfil();
                perfil.setNombre(String.valueOf(nombreET.getText()));

                perfil.setFechaNacimiento(calendar);

                perfil.setContactoDirectoT(contactoTypePicker.getSelectedItem().toString());
                perfil.setContactoDirectoN(String.valueOf(contactoNumeroET.getText()));
                perfil.setDirCalle(calleET.getText().toString());
                perfil.setDirNum(numeroET.getText().toString());
                perfil.setDirCol(coloniaET.getText().toString());
                perfil.setDirCiudad(ciudadET.getText().toString());
                perfil.setDirEstado(estadoPicker.getSelectedItem().toString());
                perfil.setTipoSanguineo(sangreTipoPicker.getSelectedItem().toString());
                perfil.setGenero(generoPicker.getSelectedItem().toString());

                perfil.setAlergias(alergiasET.getText().toString());
                perfil.setMedicamentosYHorario(medicamentosET.getText().toString());
                perfil.setAntecedentesHeredofamiliares(antecedentesHerET.getText().toString());
                perfil.setAntecedentesPatologicos(antecedentesPatET.getText().toString());

                DatabaseHandler databaseHandler=new DatabaseHandler(mainView.getApplicationContext());
                databaseHandler.editPerfil(perfil);

                if(bmp!=null){
                    Perfil.setProfile(bmp);
                }

                if(!initial) {
                    mainView.finish();
                }
                else{
                    mainView.startActivity(new Intent(mainView.getApplicationContext(),MainActivity.class));
                    mainView.finish();
                }
            }
        });
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
