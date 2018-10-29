package com.recuerdapp.contact_list;

/**
 * Created by 79192 on 25/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.recuerdapp.custom_shape_views.CircularImageView;
import com.recuerdapp.database.Contacto;
import com.recuerdapp.recuerdapp.CapturarFotomemoriaActivity;
import com.recuerdapp.recuerdapp.MostrarContactoActivity;
import com.recuerdapp.recuerdapp.R;

/**
 * Created by DavidAlejandro on 19/11/2017.
 */

public class ContactoListAdapter extends ArrayAdapter<Contacto> {
    TextView nombreTextView;
    TextView parentescoTextView;
    CircularImageView imagenImageView;
    RelativeLayout llamarButton;
    RelativeLayout layout;
    private Context contactoContext;


    public ContactoListAdapter(Context context, List<Contacto> listaContactos) {
        super(context, 0, listaContactos);
        contactoContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Contacto contacto = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contacto_row, parent, false);
        }

        layout = (RelativeLayout) convertView.findViewById(R.id.contacto_row_layout);
        nombreTextView = (TextView) convertView.findViewById(R.id.nombreTextView);
        parentescoTextView = (TextView) convertView.findViewById(R.id.parentescoTextView);
        imagenImageView=(CircularImageView) convertView.findViewById(R.id.imagenImageView);
        llamarButton = (RelativeLayout) convertView.findViewById(R.id.llamar_contacto_button);

        nombreTextView.setText(contacto.getNombre());
        parentescoTextView.setText(contacto.getParentesco());
        Bitmap profilePicture=contacto.getContactoProfilePicture();

        layout.setTag(position);
        llamarButton.setTag(position);
        imagenImageView.setTag(position);
        if(profilePicture==null) {
            imagenImageView.setImageDrawable(ContextCompat.getDrawable(contactoContext, R.mipmap.ic_add_picture));
            imagenImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    Contacto contacto = getItem(position);
                    Intent intent = new Intent(getContext(), CapturarFotomemoriaActivity.class);
                    intent.putExtra("contact",true);
                    intent.putExtra("contact_id",contacto.getId());
                    contactoContext.startActivity(intent);
                }
            });

        }
        else{
            imagenImageView.setImageBitmap(profilePicture);
        }





        llamarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                Contacto contacto = getItem(position);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + contacto.getNumero()));
                contactoContext.startActivity(intent);
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                Contacto contacto = getItem(position);
                Intent intent = new Intent(getContext(), MostrarContactoActivity.class);
                intent.putExtra("id",contacto.getId());
                contactoContext.startActivity(intent);

            }
        });
        return convertView;
    }

    private void loadImageFromStorage(String path)
    {
        try {
            File file = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
            imagenImageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}