package com.recuerdapp.database;
        import android.content.ContentValues;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        //import android.icu.text.LocaleDisplayNames;


        import java.sql.Date;
        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;
        import java.util.Locale;

/**
 * Created by Erick and Xaviar from 15/11/2017 thru 26/12/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{
    public static final int VERSION = 18;
    public static final String BASE_DE_DATOS = "recuerdapp";


    private static final String TABLA_PERFIL = "perfil";
    private static final String TABLA_MEMORIAS = "memorias";
    private static final String TABLA_CONTACTOS = "contactos";
    private static final String TABLA_IMG_Y_VID = "img_y_video";

    private static final String CAMPO_ID = "id";
    private static final String CAMPO_NOMBRE = "nombre";
    private static final String CAMPO_RUTA = "ruta";


    private static final String CAMPO_FECHA_DE_NACIMIENTO = "fd_nacimento";
    private static final String CAMPO_GENERO = "genero";
    private static final String CAMPO_TIPO_SAN = "tipo_san";
    private static final String CAMPO_DIR_CALLE = "dir_calle";
    private static final String CAMPO_DIR_NUM = "dir_numero";
    private static final String CAMPO_DIR_COL = "dir_colonia";
    private static final String CAMPO_DIR_ESTADO = "dir_estado";
    private static final String CAMPO_DIR_CIUDAD = "dir_ciudad";
    private static final String CAMPO_CONTACTO_DIR_T = "contacto_dir_t";
    private static final String CAMPO_CONTACTO_DIR_N = "contacto_dir_N";
    private static final String CAMPO_ALERGIAS = "alergias";
    private static final String CAMPO_MEDS_Y_H = "meds_y_h";
    private static final String CAMPO_ANTECEDENTES_PATO = "antecedentes_pato";
    private static final String CAMPO_ANTECEDENTES_HF = "antecedentes_hf";


    private static final String CAMPO_NUMERO = "numero_celular";
    private static final String CAMPO_PARENTESCO = "parentesco";
    private static final String CAMPO_FECHA_HORA = "fecha_hora";
    private static final String CAMPO_FORMATO = "formato";
    private static final String CAMPO_DESCRIPCION = "descripcion";


    private static final String CAMPO_MEMORIA = "memoria";
    private static final String CAMPO_TIPO = "tipo";
    private static final String CAMPO_DIR_URI = "dir_uri";


    private static final String CREAR_TABLA_PERFIL = "CREATE TABLE" + " " + TABLA_PERFIL +
            "("+ CAMPO_ID + " INTEGER PRIMARY KEY DEFAULT 1, "+ CAMPO_NOMBRE + " TEXT, "+ CAMPO_CONTACTO_DIR_T + " TEXT, "+
            CAMPO_CONTACTO_DIR_N + " TEXT, " + CAMPO_FECHA_DE_NACIMIENTO +" DATETIME, "+ CAMPO_GENERO + " TEXT, "+
            CAMPO_TIPO_SAN + " TEXT, "+ CAMPO_DIR_CALLE + " TEXT, "+ CAMPO_DIR_NUM + " TEXT, "+ CAMPO_DIR_COL + " TEXT, "+
            CAMPO_DIR_ESTADO + " TEXT, "+ CAMPO_DIR_CIUDAD + " TEXT, "+CAMPO_ALERGIAS + " TEXT, "+CAMPO_MEDS_Y_H + " TEXT, "+
            CAMPO_ANTECEDENTES_PATO + " TEXT, "+ CAMPO_ANTECEDENTES_HF + " TEXT, "+ CAMPO_RUTA+" TEXT)";

    private static final String CREAR_TABLA_MEMORIA = "CREATE TABLE" + " " + TABLA_MEMORIAS +
            "("+ CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+" "+" "+CAMPO_FECHA_HORA +
            " DATETIME DEFAULT CURRENT_TIMESTAMP, "+" "+ CAMPO_NOMBRE + " TEXT, "+" " + CAMPO_FORMATO +
            " INTEGER, "+" " + CAMPO_DESCRIPCION + " TEXT, "+" " + CAMPO_RUTA + " TEXT)";
// + CAMPO_TIPO + " INTEGER)"
    private static final String CREAR_TABLA_CONTACTOS = "CREATE TABLE" + " " + TABLA_CONTACTOS +
            "("+ CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+" "+" "+CAMPO_NOMBRE +
            " TEXT, "+" "+ CAMPO_NUMERO + " TEXT, "+" " + CAMPO_PARENTESCO +
            " TEXT, "+" " + CAMPO_RUTA + " TEXT)";
    private static final String CREAR_TABLA_IMG_Y_VID = "CREATE TABLE" + " " + TABLA_IMG_Y_VID +
            "("+ CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+" "+" "+CAMPO_MEMORIA +
            " INTEGER, "+" "+ CAMPO_TIPO + " INTEGER, "+" " + CAMPO_DIR_URI + " TEXT)";

    /*
    * private Integer id;
    private Date fecha_hora;
    private String nombre;
    private Integer formato;
    //private Boolean urgencia;
    private String descripcion;
    private String ruta;
    *
    * */
    public DatabaseHandler(Context context) {
        super(context, BASE_DE_DATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREAR_TABLA_MEMORIA);
        db.execSQL(CREAR_TABLA_PERFIL);
        db.execSQL(CREAR_TABLA_CONTACTOS);
        db.execSQL(CREAR_TABLA_IMG_Y_VID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_PERFIL);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_MEMORIAS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_CONTACTOS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_IMG_Y_VID);
        onCreate(db);
    }

    public void insertContacto(Contacto contacto){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CAMPO_NOMBRE, contacto.getNombre());
        values.put(CAMPO_PARENTESCO, contacto.getParentesco());
        values.put(CAMPO_NUMERO, contacto.getNumero());
        values.put(CAMPO_RUTA, contacto.getRuta());

        db.insert(TABLA_CONTACTOS, null, values);
        db.close();
    }

    public void insertMemoria(Memoria memoria){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CAMPO_NOMBRE, memoria.getNombre());
        values.put(CAMPO_DESCRIPCION, memoria.getDescripcion());
        values.put(CAMPO_FORMATO, memoria.getFormato().toString());
        values.put(CAMPO_RUTA, memoria.getRuta());
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(CAMPO_FECHA_HORA,dateFormat.format(calendar.getTime()));
        db.insert(TABLA_MEMORIAS, null, values);
        db.close();
    }

    public void insertImgVid(MemoriaImgYVid imgVid){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CAMPO_MEMORIA, imgVid.getMemoria());
        values.put(CAMPO_TIPO, imgVid.getTipo());
        values.put(CAMPO_DIR_URI, imgVid.getDirURI());

        db.insert(TABLA_IMG_Y_VID, null, values);
        db.close();
    }

    public Memoria getMemoria(Integer id){
        Memoria memoria=new Memoria();
        String selectQuery = "SELECT  * FROM " + TABLA_MEMORIAS+" WHERE "+CAMPO_ID+"="+String.valueOf(id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            memoria.setId(Integer.parseInt(cursor.getString(0)));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Calendar c=Calendar.getInstance();
                c.setTime(dateFormat.parse(cursor.getString(1)));
                memoria.setFecha_hora(c);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            memoria.setNombre(cursor.getString(2));
            memoria.setFormato(Integer.parseInt(cursor.getString(3)));
            memoria.setDescripcion(cursor.getString(4));
            memoria.setRuta(cursor.getString(5));

        }
        cursor.close();

        return memoria;
    }

    public ArrayList<MemoriaImgYVid> getImgVid(Integer memory){
        ArrayList<MemoriaImgYVid> imgYVids=new ArrayList<MemoriaImgYVid>();
        String selectQuery = "SELECT  * FROM " + TABLA_IMG_Y_VID+" WHERE "+CAMPO_MEMORIA+"="+String.valueOf(memory);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MemoriaImgYVid memoriaImgYVid = new MemoriaImgYVid();
                memoriaImgYVid.setId(Integer.parseInt(cursor.getString(0)));
                memoriaImgYVid.setMemoria(Integer.parseInt(cursor.getString(1)));
                memoriaImgYVid.setTipo(Integer.parseInt(cursor.getString(2)));
                memoriaImgYVid.setDirURI(cursor.getString(3));
                imgYVids.add(memoriaImgYVid);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return imgYVids;
    }

    public void editContacto(Contacto contacto){
        String where = "id=?";
        String[] whereArgs = new String[] {String.valueOf(contacto.getId())};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPO_NOMBRE, contacto.getNombre());
        values.put(CAMPO_PARENTESCO, contacto.getParentesco());
        values.put(CAMPO_NUMERO, contacto.getNumero());
        db.update(TABLA_CONTACTOS, values, where, whereArgs);
    }

    public boolean perfilIsEdited(){
        String selectQuery = "SELECT  * FROM " + TABLA_PERFIL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()>0)
            return true;
        cursor.close();
        return false;
    }
    public void editPerfil(Perfil perfil){
        if(perfilIsEdited()){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CAMPO_NOMBRE, perfil.getNombre());
            values.put(CAMPO_CONTACTO_DIR_T, perfil.getContactoDirectoT());
            values.put(CAMPO_CONTACTO_DIR_N, perfil.getContactoDirectoN());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            values.put(CAMPO_FECHA_DE_NACIMIENTO, dateFormat.format(perfil.getFechaNacimiento().getTime()));

            values.put(CAMPO_GENERO, perfil.getGenero().toString());
            values.put(CAMPO_TIPO_SAN, perfil.getTipoSanguineo());

            values.put(CAMPO_DIR_CALLE, perfil.getDirCalle());
            values.put(CAMPO_DIR_NUM, perfil.getDirNum());
            values.put(CAMPO_DIR_COL, perfil.getDirCol());
            values.put(CAMPO_DIR_ESTADO, perfil.getDirEstado());
            values.put(CAMPO_DIR_CIUDAD, perfil.getDirCiudad());

            values.put(CAMPO_ALERGIAS, perfil.getAlergias());
            values.put(CAMPO_MEDS_Y_H, perfil.getMedicamentosYHorario());
            values.put(CAMPO_ANTECEDENTES_PATO, perfil.getAntecedentesPatologicos());
            values.put(CAMPO_ANTECEDENTES_HF, perfil.getAntecedentesHeredofamiliares());
            values.put(CAMPO_RUTA, perfil.getRuta());
            db.update(TABLA_PERFIL,values,CAMPO_ID+"=?",new String[]{"1"});
            db.close();
        }
        else{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(CAMPO_NOMBRE, perfil.getNombre());
            values.put(CAMPO_CONTACTO_DIR_T, perfil.getContactoDirectoT());
            values.put(CAMPO_CONTACTO_DIR_N, perfil.getContactoDirectoN());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            values.put(CAMPO_FECHA_DE_NACIMIENTO, dateFormat.format(perfil.getFechaNacimiento().getTime()));

            values.put(CAMPO_GENERO, perfil.getGenero().toString());
            values.put(CAMPO_TIPO_SAN, perfil.getTipoSanguineo());

            values.put(CAMPO_DIR_CALLE, perfil.getDirCalle());
            values.put(CAMPO_DIR_NUM, perfil.getDirNum());
            values.put(CAMPO_DIR_COL, perfil.getDirCol());
            values.put(CAMPO_DIR_ESTADO, perfil.getDirEstado());
            values.put(CAMPO_DIR_CIUDAD, perfil.getDirCiudad());


            values.put(CAMPO_ALERGIAS, perfil.getAlergias());
            values.put(CAMPO_MEDS_Y_H, perfil.getMedicamentosYHorario());
            values.put(CAMPO_ANTECEDENTES_PATO, perfil.getAntecedentesPatologicos());
            values.put(CAMPO_ANTECEDENTES_HF, perfil.getAntecedentesHeredofamiliares());
            values.put(CAMPO_RUTA, perfil.getRuta());
            db.insert(TABLA_PERFIL, null, values);
            db.close();
        }
    }
    public Perfil getPerfil() {
        Perfil perfil=new Perfil();
        String selectQuery = "SELECT  * FROM " + TABLA_PERFIL+" WHERE "+CAMPO_ID+"=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(!perfilIsEdited())
            return null;
/*            values.put(CAMPO_RUTA, perfil.getRuta());*/
        if (cursor.moveToFirst()) {
            perfil.setNombre(cursor.getString(1));
            perfil.setContactoDirectoT(cursor.getString(2));
            perfil.setContactoDirectoN(cursor.getString(3));
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c=Calendar.getInstance();
                c.setTime(dateFormat.parse(cursor.getString(4)));
                perfil.setFechaNacimiento(c);
                System.out.println(cursor.getString(4));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            perfil.setGenero(cursor.getString(5));
            perfil.setTipoSanguineo(cursor.getString(6));

            perfil.setDirCalle(cursor.getString(7));
            perfil.setDirNum(cursor.getString(8));
            perfil.setDirCol(cursor.getString(9));
            perfil.setDirEstado(cursor.getString(10));
            perfil.setDirCiudad(cursor.getString(11));


            perfil.setAlergias(cursor.getString(12));
            perfil.setMedicamentosYHorario(cursor.getString(13));
            perfil.setAntecedentesPatologicos(cursor.getString(14));
            perfil.setAntecedentesHeredofamiliares(cursor.getString(15));
        }
        cursor.close();
        // return contact list
        return perfil;
    }

    public List<Contacto> getContactos() {
        List<Contacto> contactoList = new ArrayList<Contacto>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLA_CONTACTOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping throt
        if (cursor.moveToFirst()) {
            do {
                Contacto contacto = new Contacto();
                contacto.setId(Integer.parseInt(cursor.getString(0)));
                contacto.setNombre(cursor.getString(1));
                contacto.setNumero(cursor.getString(2));
                contacto.setParentesco(cursor.getString(3));
                contacto.setRuta(cursor.getString(4));
                contactoList.add(contacto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactoList;
    }
    public Contacto getContacto(Integer id) {

        Contacto contacto=null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLA_CONTACTOS + " WHERE "+CAMPO_ID+"="+id.toString();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping throt
        if (cursor.moveToFirst()) {
            contacto = new Contacto();
            contacto.setId(Integer.parseInt(cursor.getString(0)));
            contacto.setNombre(cursor.getString(1));
            contacto.setNumero(cursor.getString(2));
            contacto.setParentesco(cursor.getString(3));
            contacto.setRuta(cursor.getString(4));
        }
        cursor.close();
        // return contact list
        return contacto;
    }
    public int getContactosCount(){
        String countQuery = "SELECT * FROM " + TABLA_CONTACTOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int ct=cursor.getCount();
        cursor.close();

        // return count
        return ct;

    }
    // Returns all memories
    public List<Memoria> getMemorias() {
        List<Memoria> memoriaList = new ArrayList<Memoria>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLA_MEMORIAS + " ORDER BY " + CAMPO_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping throt
        if (cursor.moveToFirst()) {
            do {
                Memoria memoria = new Memoria();
                memoria.setId(Integer.parseInt(cursor.getString(0)));
                memoria.setNombre(cursor.getString(2));
                memoria.setFormato(Integer.parseInt(cursor.getString(3)));
                memoria.setDescripcion(cursor.getString(4));
                memoria.setRuta(cursor.getString(5));
                memoriaList.add(memoria);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return memory list
        return memoriaList;
    }

    public void editMemoria(Memoria memoria){
        String where = "id=?";
        String[] whereArgs = new String[] {String.valueOf(memoria.getId())};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPO_DESCRIPCION, memoria.getDescripcion());
        db.update(TABLA_MEMORIAS, values, where, whereArgs);
    }

    public int getMemoriasCount(){
        String countQuery = "SELECT * FROM " + TABLA_MEMORIAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int ct=cursor.getCount();
        cursor.close();

        // return count
        return ct;

    }
    public void removeImgYVids(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLA_IMG_Y_VID, CAMPO_ID + "=" + id.toString(), null);
    }
    public void removeContacto(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLA_CONTACTOS, CAMPO_ID + "=" + id.toString(), null);
    }
    public void removeMemoria(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLA_MEMORIAS, CAMPO_ID + "=" + id.toString(), null);
    }

}

