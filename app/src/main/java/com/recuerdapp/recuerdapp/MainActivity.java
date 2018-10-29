package com.recuerdapp.recuerdapp;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.recuerdapp.contact_list.ContactoListAdapter;
import com.recuerdapp.custom_shape_views.CircularImageView;
import com.recuerdapp.database.Contacto;
import com.recuerdapp.database.DatabaseHandler;
import com.recuerdapp.database.Memoria;
import com.recuerdapp.database.MemoriaImgYVid;
import com.recuerdapp.database.Perfil;
import com.recuerdapp.image_grid.GridViewAdapter;
import com.recuerdapp.image_grid.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    static DatabaseHandler databaseHandler;
    private TabLayout tabLayout;

    private final static String[] mes={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre",
            "Noviembre","Diciembre"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHandler=new DatabaseHandler(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bitmap b=BitmapFactory.decodeFile(data.getStringExtra("file_name"));
                Perfil.setProfile(b);
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void openCapturarFotomemoria(){
        startActivity(new Intent(this, CapturarFotomemoriaActivity.class));
    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static ContentResolver resolver;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber,ContentResolver res) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            resolver=res;
            return fragment;
        }

        private ArrayList<Image> getMemoryThumbnails(ContentResolver resolver) {
            ArrayList<Image> imageItems = new ArrayList<Image>();
            List<Memoria> memorias=databaseHandler.getMemorias();
            for (int i = 0; i <memorias.size(); i++) {
               // Bitmap bitmap=BitmapFactory.
                List<MemoriaImgYVid> imgsYVids=databaseHandler.getImgVid(memorias.get(i).getId());
                Bitmap bitmap=null;
                bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.message);
                bitmap.setDensity(5);
                if(imgsYVids.size()>0) {
                    if (imgsYVids.get(0).getTipo() == MemoriaImgYVid.TYPE_IMG_DIR) {

                        String pathMain = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.recuerdapp/DCIM/";

                        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(pathMain + imgsYVids.get(0).getDirURI()), 100, 100, false);

                    }
                    if (imgsYVids.get(0).getTipo() == MemoriaImgYVid.TYPE_IMG_URI) {
                        try {
                            bitmap = BitmapFactory.decodeFile(imgsYVids.get(0).getDirURI());
                        } catch (Exception e) {

                        }
                    }
                    /*if (imgsYVids.get(0).getTipo() == MemoriaImgYVid.TYPE_VID_DIR) {
                        try {
                            String pathMain= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM/";
                            bitmap = ThumbnailUtils.createVideoThumbnail(pathMain+imgsYVids.get(0).getDirURI(), MediaStore.Video.Thumbnails.MICRO_KIND);
                        } catch (Exception e) {

                        }
                    }*/
                }
                imageItems.add(new Image(bitmap, memorias.get(i).getId()));
            }
            return imageItems;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView;
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.memory_fragment, container, false);
                /*FloatingActionButton addMemory=(FloatingActionButton) rootView.findViewById(R.id.addMemoryButton);
                addMemory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        view.getContext().startActivity(new Intent(view.getContext(), GuardarMemoriaActivity.class));

                    }
                });

                GridView memoryView = (GridView) rootView.findViewById(R.id.gridView);
                GridViewAdapter gridAdapter = new GridViewAdapter(rootView.getContext(), R.layout.memory_preview_layout, getMemoryThumbnails(resolver));
                memoryView.setAdapter(gridAdapter);

                memoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Image item = (Image) parent.getItemAtPosition(position);
                        Intent intent = new Intent(rootView.getContext(), MostrarMemoriaActivity.class);
                        intent.putExtra("id",item.getIdx());
                        System.out.println("pressed");
                        startActivity(intent);
                    }
                });*/
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                DatabaseHandler databaseHandler;
                List<Contacto> contactosList;
                ListView listView;
                ContactoListAdapter contactoAdapter;
                rootView = inflater.inflate(R.layout.contact_fragment, container, false);
                databaseHandler = new DatabaseHandler(rootView.getContext());
                listView = (ListView)rootView.findViewById(R.id.contactosListView);
                contactosList = databaseHandler.getContactos();
                contactoAdapter = new ContactoListAdapter(rootView.getContext(), contactosList);
                listView.setAdapter(contactoAdapter);


                Button addContactoButton=(Button)rootView.findViewById(R.id.agregarContacto);
                addContactoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootView.getContext(), AgregarContactoActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else{
                final Perfil perfil=databaseHandler.getPerfil();
                rootView = inflater.inflate(R.layout.profile_fragment, container, false);
                if(perfil!=null) {
                    TextView nombreView=(TextView) rootView.findViewById(R.id.nombreView);
                    nombreView.setText(perfil.getNombre());

                    TextView ctDirView=(TextView) rootView.findViewById(R.id.contactoDirectoView);
                    ctDirView.setText(perfil.getContactoDirectoT());

                    TextView ctDirNumView=(TextView) rootView.findViewById(R.id.numeroView);
                    ctDirNumView.setText(perfil.getContactoDirectoN());




                    TextView edadView=(TextView) rootView.findViewById(R.id.edadView);
                    edadView.setText(String.valueOf(perfil.getEdad()));
                    TextView fdNacView=(TextView) rootView.findViewById(R.id.fechaDeNacimientoView);
                    Calendar fdN=perfil.getFechaNacimiento();
                    fdNacView.setText(String.valueOf(fdN.get(Calendar.DATE))+" de "+mes[fdN.get(Calendar.MONTH)]+
                            ", "+String.valueOf(fdN.get(Calendar.YEAR)));

                    TextView generoView=(TextView)rootView.findViewById(R.id.generoView);
                    generoView.setText(perfil.getGenero());


                    TextView tipoDSView=(TextView)rootView.findViewById(R.id.tipoDeSangreView);
                    tipoDSView.setText(perfil.getTipoSanguineo());

                    TextView domicilioView=(TextView)rootView.findViewById(R.id.domicilioView);
                    domicilioView.setText(perfil.getDomicilio()+".");

                    TextView alergiasView=(TextView)rootView.findViewById(R.id.alergiasView);
                    if(perfil.getAlergias().length()==0)
                        alergiasView.setText("Ninguna");
                    else
                        alergiasView.setText(perfil.getAlergias());

                    TextView mYHView=(TextView)rootView.findViewById(R.id.mYHView);
                    if(perfil.getMedicamentosYHorario().length()==0)
                        mYHView.setText("Ninguno");
                    else
                        mYHView.setText(perfil.getMedicamentosYHorario());

                    TextView antePatView=(TextView)rootView.findViewById(R.id.antPatView);
                    if(perfil.getAntecedentesPatologicos().length()==0)
                        antePatView.setText("Ninguno");
                    else
                        antePatView.setText(perfil.getAntecedentesPatologicos());

                    TextView anteHerView=(TextView)rootView.findViewById(R.id.antHerView);
                    if(perfil.getAntecedentesHeredofamiliares().length()==0)
                        anteHerView.setText("Ninguno");
                    else
                        anteHerView.setText(perfil.getAntecedentesHeredofamiliares());


                    ImageView callNum=(ImageView)rootView.findViewById(R.id.llamarNumeroButton);
                    callNum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + perfil.getContactoDirectoN()));
                            startActivity(intent);
                        }
                    });


                }
                ImageView edPerfView=(ImageView) rootView.findViewById(R.id.editPerfilButton);
                edPerfView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(view.getContext(), EditarPerfilActivity.class);
                        intent.putExtra("initial",false);
                        startActivity(intent);
                    }
                });
            }
            return rootView;
        }


        @Override
        public void onResume() {
            super.onResume();
            final View rootView=this.getView();
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {


                FloatingActionButton addMemory=(FloatingActionButton) rootView.findViewById(R.id.addMemoryButton);
                addMemory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        view.getContext().startActivity(new Intent(view.getContext(), GuardarMemoriaActivity.class));

                    }
                });

                GridView memoryView = (GridView) rootView.findViewById(R.id.gridView);
                GridViewAdapter gridAdapter = new GridViewAdapter(rootView.getContext(), R.layout.memory_preview_layout, getMemoryThumbnails(rootView.getContext().getContentResolver()));
                memoryView.setAdapter(gridAdapter);

                memoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Image item = (Image) parent.getItemAtPosition(position);
                        Intent intent = new Intent(rootView.getContext(), MostrarMemoriaActivity.class);
                        intent.putExtra("id",item.getIdx());
                        System.out.println("pressed");
                        startActivity(intent);

                    }
                });

            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                DatabaseHandler databaseHandler;
                List<Contacto> contactosList;
                ListView listView;
                ContactoListAdapter contactoAdapter;
                databaseHandler = new DatabaseHandler(rootView.getContext());
                listView = (ListView)rootView.findViewById(R.id.contactosListView);
                contactosList = databaseHandler.getContactos();
                contactoAdapter = new ContactoListAdapter(rootView.getContext(), contactosList);
                listView.setAdapter(contactoAdapter);
            }
            else{
                final Perfil perfil=databaseHandler.getPerfil();
                if(perfil!=null) {
                    ImageView addProfilePicture = (ImageView) rootView.findViewById(R.id.agregarFotoPerfil);
                    CircularImageView profilePicture = (CircularImageView) rootView.findViewById(R.id.perfilImage);

                    if (Perfil.getProfilePicture() == null){
                        profilePicture.setVisibility(View.INVISIBLE);
                        addProfilePicture.setVisibility(View.VISIBLE);
                        addProfilePicture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(rootView.getContext(), CapturarFotomemoriaActivity.class);
                                intent.putExtra("profile",true);
                                startActivity(intent);
                            }
                        });
                    }
                    else{
                        profilePicture.setVisibility(View.VISIBLE);
                        addProfilePicture.setVisibility(View.INVISIBLE);
                        profilePicture.setImageBitmap(Perfil.getProfilePicture());

                    }

                    TextView nombreView=(TextView) rootView.findViewById(R.id.nombreView);
                    nombreView.setText(perfil.getNombre());

                    TextView ctDirView=(TextView) rootView.findViewById(R.id.contactoDirectoView);
                    ctDirView.setText(perfil.getContactoDirectoT());

                    TextView ctDirNumView=(TextView) rootView.findViewById(R.id.numeroView);
                    ctDirNumView.setText(perfil.getContactoDirectoN());


                    TextView edadView=(TextView) rootView.findViewById(R.id.edadView);
                    edadView.setText(String.valueOf(perfil.getEdad()));
                    TextView fdNacView=(TextView) rootView.findViewById(R.id.fechaDeNacimientoView);
                    Calendar fdN=perfil.getFechaNacimiento();
                    fdNacView.setText(String.valueOf(fdN.get(Calendar.DATE))+" de "+mes[fdN.get(Calendar.MONTH)]+
                            ", "+String.valueOf(fdN.get(Calendar.YEAR)));

                    TextView generoView=(TextView)rootView.findViewById(R.id.generoView);
                    generoView.setText(perfil.getGenero());


                    TextView tipoDSView=(TextView)rootView.findViewById(R.id.tipoDeSangreView);
                    tipoDSView.setText(perfil.getTipoSanguineo());

                    TextView domicilioView=(TextView)rootView.findViewById(R.id.domicilioView);
                    domicilioView.setText(perfil.getDomicilio()+".");

                    TextView alergiasView=(TextView)rootView.findViewById(R.id.alergiasView);
                    if(perfil.getAlergias().length()==0)
                        alergiasView.setText("Ninguna");
                    else
                        alergiasView.setText(perfil.getAlergias());

                    TextView mYHView=(TextView)rootView.findViewById(R.id.mYHView);
                    if(perfil.getMedicamentosYHorario().length()==0)
                        mYHView.setText("Ninguno");
                    else
                        mYHView.setText(perfil.getMedicamentosYHorario());

                    TextView antePatView=(TextView)rootView.findViewById(R.id.antPatView);
                    if(perfil.getAntecedentesPatologicos().length()==0)
                        antePatView.setText("Ninguno");
                    else
                        antePatView.setText(perfil.getAntecedentesPatologicos());

                    TextView anteHerView=(TextView)rootView.findViewById(R.id.antHerView);
                    if(perfil.getAntecedentesHeredofamiliares().length()==0)
                        anteHerView.setText("Ninguno");
                    else
                        anteHerView.setText(perfil.getAntecedentesHeredofamiliares());



                    ImageView callNum=(ImageView)rootView.findViewById(R.id.llamarNumeroButton);
                    callNum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + perfil.getContactoDirectoN()));
                            startActivity(intent);
                        }
                    });

                }
            }
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1,getContentResolver());
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MEMORIAS";
                case 1:
                    return "CONTACTOS";
                case 2:
                    return "PERFIL";
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_informacion){
            startActivity(new Intent(this,InformacionActivity.class));
        }
        if(id==R.id.action_emergencia){
            Perfil perfil=databaseHandler.getPerfil();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + perfil.getContactoDirectoN()));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}