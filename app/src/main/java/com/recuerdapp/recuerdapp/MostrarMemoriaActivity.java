package com.recuerdapp.recuerdapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.recuerdapp.database.DatabaseHandler;
import com.recuerdapp.database.Memoria;
import com.recuerdapp.database.MemoriaImgYVid;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by 79192 on 30/12/2017.
 */

public class MostrarMemoriaActivity extends AppCompatActivity {
    private SectionsMemoriaPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout editButton;

    private TextView descripcionView;
    private TextView fechaView;

    private ImageView returnButton;
    private final String[] monthAbreviation=new String[]{"Ene","Feb","Mar","Abr","Mayo","Jun","Jul","Ago","Sep",
            "Oct","Nov","Dic"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_memoria);


/*
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();


        final DatabaseHandler handler=new DatabaseHandler(this.getApplicationContext());


        mSectionsPagerAdapter = new SectionsMemoriaPagerAdapter(getSupportFragmentManager(),intent.getIntExtra("id",-1),handler.getImgVid(intent.getIntExtra("id",-1)).size());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        final Memoria memoria=handler.getMemoria(intent.getIntExtra("id",-1));
        descripcionView=(TextView) findViewById(R.id.descripcionView);
        descripcionView.setText(memoria.getDescripcion());

        fechaView=(TextView)findViewById(R.id.fechaView);
        Calendar date=memoria.getFecha_hora();
        fechaView.setText(String.valueOf(date.get(Calendar.DATE))+" "+monthAbreviation[date.get(Calendar.MONTH)]+" "+String.valueOf(date.get(Calendar.YEAR)));

        returnButton=(ImageView)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editButton=(RelativeLayout)findViewById(R.id.backButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
                CharSequence[] arr={"Editar memoria","Eliminar memoria"};
                adb.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            Intent intent=new Intent(getApplicationContext(),EditarMemoriaActivity.class);
                            intent.putExtra("id",memoria.getId());
                            startActivity(intent);
                        }
                        else{
                            handler.removeMemoria(memoria.getId());
                            finish();
                        }
                    }
                });
                //adb.setTitle("Editar Memoria");
                adb.show();



            }
        });
    }

    public static class PlaceholderMemoriaFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_ID = "id";

        public PlaceholderMemoriaFragment() {
        }

        public static PlaceholderMemoriaFragment newInstance(int sectionNumber,int id) {
            PlaceholderMemoriaFragment fragment = new PlaceholderMemoriaFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_SECTION_ID, id);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView;
            rootView = inflater.inflate(R.layout.vista_memoria_fragment, container, false);
            final ImageView imageView=(ImageView) rootView.findViewById(R.id.imageView);
            final ImageView playButton=(ImageView) rootView.findViewById(R.id.playButton);
            final VideoView videoView=(VideoView)rootView.findViewById(R.id.videoView);

            /*final Handler seekBarHandler=new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    return false;
                }
            });*/


            DatabaseHandler handler = new DatabaseHandler(rootView.getContext());
            ArrayList<MemoriaImgYVid> memoriaImgsYVids=handler.getImgVid(getArguments().getInt(ARG_SECTION_ID));
            Integer g=getArguments().getInt(ARG_SECTION_NUMBER)-1;
            if(g<memoriaImgsYVids.size()) {
                Bitmap bitmap;
                if(memoriaImgsYVids.get(g).getTipo()==MemoriaImgYVid.TYPE_IMG_DIR){
                    String pathMain= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM/";
                    bitmap = BitmapFactory.decodeFile(pathMain+memoriaImgsYVids.get(g).getDirURI());
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.INVISIBLE);
                    playButton.setVisibility(View.INVISIBLE);

                }
                else if(memoriaImgsYVids.get(g).getTipo()==MemoriaImgYVid.TYPE_VID_DIR){
                    String pathMain= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM/";
                    videoView.setVideoPath(pathMain+memoriaImgsYVids.get(g).getDirURI());
                    imageView.setVisibility(View.VISIBLE);
                    bitmap = ThumbnailUtils.createVideoThumbnail(pathMain+memoriaImgsYVids.get(g).getDirURI(), MediaStore.Video.Thumbnails.MINI_KIND);
                    imageView.setImageBitmap(bitmap);
                    videoView.setVisibility(View.VISIBLE);

                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            videoView.seekTo(0);
                            videoView.stopPlayback();
                            imageView.setVisibility(View.VISIBLE);
                            playButton.setVisibility(View.VISIBLE);
                        }
                    });
                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            playButton.setVisibility(View.INVISIBLE);
                            imageView.setVisibility(View.INVISIBLE);
                            videoView.start();
                            videoView.resume();
                        }
                    });
                }
                else if(memoriaImgsYVids.get(g).getTipo()==MemoriaImgYVid.TYPE_IMG_URI) {
                    //String pathMain= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM/";
                    imageView.setImageURI(Uri.parse(memoriaImgsYVids.get(g).getDirURI()));
                    imageView.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.INVISIBLE);
                    playButton.setVisibility(View.INVISIBLE);
                }

            }
            return rootView;
        }
    }
    public class SectionsMemoriaPagerAdapter extends FragmentPagerAdapter {
        int id;
        int size;
        public SectionsMemoriaPagerAdapter(FragmentManager fm,int id,int size) {
            super(fm);
            this.id=id;
            this.size=size;

        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderMemoriaFragment.newInstance(position + 1,id);
        }

        @Override
        public int getCount() {
            return size;
        }
    }
}
