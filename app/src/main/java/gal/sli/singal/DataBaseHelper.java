package gal.sli.singal;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/gal.sli.singal/databases/";

    private static final String DB_NAME = "singal.db"; //en assets

    private static final int DATABASE_VERSION = 1; //1=0.14_090919

    private SQLiteDatabase myDataBase;

    private final Context myContext;


    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    public void createDataBase_ori() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
        } else {

            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.v("Os meus logs", "Singal existe!!!");
            this.getWritableDatabase();
        } else {
            Log.v("Os meus logs", "Singal non existe!!!");
        }
        dbExist = checkDataBase();
        if (!dbExist) {
            Log.v("Os meus logs", "Creando estrutura do Singal na app...");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
            Log.v("Os meus logs", "Datos de Singal copiados desde assets...");
        }
    }

    public void createDataBase_test() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.d(TAG, "db exists");

            this.getWritableDatabase();
        }

        dbExist = checkDataBase();

        if (!dbExist) {

            this.getReadableDatabase();

            try {
                copyDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }


    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

        }

        if (checkDB != null) {

            if (checkDB.getVersion() != DATABASE_VERSION) {
                Log.v("Os meus logs", "Versións diferentes de Singal!!!");
                String myPath = DB_PATH + DB_NAME;
                checkDB.close();
                SQLiteDatabase.deleteDatabase(new File(myPath));
                checkDB.close();
                return false;
            } else {
                checkDB.close();
                return true;
            }

        } else {
            return false;
        }

    }

    private void copyDataBase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("Os meus logs", "Hai que actualizar Singal!!!");

    }



   public ArrayList<String> getOneItem(String palabra) {

       Cursor c = myDataBase.rawQuery("SELECT lema, cat, acep, def FROM entrada WHERE LOWER(TRIM(lema)) = '"+palabra.trim().toLowerCase()+"'", null);

       String cor_especificacion_semantica ="navy";
       String cor_rexistro ="#009900";
       String cor_entrada_secundaria ="maroon";

      String catbuffer = "";
      String lemabuffer = "";

       ArrayList<String> entrada = new ArrayList<>();

        if (c.moveToFirst()){

            do {
                String lema = c.getString(0);
                String cat = c.getString(1);
                String acep = c.getString(2);
                String def = c.getString(3);

                cat=cat.replaceAll("\\badv conx\\b","adverbio e conxunción");
                cat=cat.replaceAll("\\badv\\b","adverbio");
                cat=cat.replaceAll("\\bconx\\b","conxunción");
                cat=cat.replaceAll("\\bindef\\b","indefinido");
                cat=cat.replaceAll("\\bix\\b","interxección");
                cat=cat.replaceAll("\\bladv\\b","locución adverbial");
                cat=cat.replaceAll("\\bladx\\b","locución adxectiva");
                cat=cat.replaceAll("\\blconx\\b","locución conxuntiva");
                cat=cat.replaceAll("\\blprep\\b","locución preposicional");
                cat=cat.replaceAll("\\blvb\\b","locución verbal");
                cat=cat.replaceAll("\\bnum\\b","numeral");
                cat=cat.replaceAll("\\bprep\\b","preposición");
                cat=cat.replaceAll("\\bpron\\b","pronome");
                cat=cat.replaceAll("\\bvi vp vt\\b","verbo intransitivo, pronominal e transitivo");
                cat=cat.replaceAll("\\bvi vp\\b","verbo intransitivo e pronominal");
                cat=cat.replaceAll("\\bvi vt\\b","verbo intransitivo e transitivo");
                cat=cat.replaceAll("\\bvp vt\\b","verbo pronominal e transitivo");
                cat=cat.replaceAll("\\bvp\\b","verbo pronominal");
                cat=cat.replaceAll("\\bvt\\b","verbo transitivo");
                cat=cat.replaceAll("\\bvi\\b","verbo intransitivo");
                cat=cat.replaceAll("\\badx sf\\b","adxectivo e substantivo feminino");
                cat=cat.replaceAll("\\badx sm\\b","adxectivo e substantivo masculino");
                cat=cat.replaceAll("\\badx s\\b","adxectivo e substantivo");
                cat=cat.replaceAll("\\badxf sf\\b","adxectivo feminino e substantivo feminino");
                cat=cat.replaceAll("\\badxm sm\\b","adxectivo masculino e substantivo masculino");
                cat=cat.replaceAll("\\badxfpl\\b","adxectivo feminino plural");
                cat=cat.replaceAll("\\badxf\\b","adxectivo feminino");
                cat=cat.replaceAll("\\badxm\\b","adxectivo masculino");
                cat=cat.replaceAll("\\badxpl\\b","adxectivo plural");
                cat=cat.replaceAll("\\badx\\b","adxectivo");
                cat=cat.replaceAll("\\bsfpl\\b","substantivo feminino plural");
                cat=cat.replaceAll("\\bsmpl\\b","substantivo masculino plural");
                cat=cat.replaceAll("\\bsm\\b","substantivo masculino");
                cat=cat.replaceAll("\\bsf\\b","substantivo feminino");
                cat=cat.replaceAll("\\bspl\\b","substantivo plural");
                cat=cat.replaceAll("\\bs\\b","substantivo");

                def=def.replace("<m>arc</m>","<m>arcaísmo</m>");
                def=def.replace("<m>col</m>","<m>coloquialismo</m>");
                def=def.replace("<m>cult</m>","<m>cultismo</m>");
                def=def.replace("<m>fam</m>","<m>familia</m>");
                def=def.replace("<m>fig</m>","<m>figurado</m>");
                def=def.replace("<m>lat</m>","<m>latinismo</m>");
                def=def.replace("<m>lit</m>","<m>literario</m>");
                def=def.replace("<m>spp</m>","<m>varias especies</m>");
                def=def.replace("<m>vulg</m>","<m>vulgarismo</m>");
                def=def.replace("<m>xén</m>","<m>xénero</m>");

                String def1 = def.replace("<e>", "<i><font color='"+cor_especificacion_semantica+"'>");
                String def2 = def1.replace("</e>", "</font></i>");
                String def3 = def2.replace("<q>", "<i><font color='grey'>");
                String def4 = def3.replace("</q>", "</font></i>");
                String def5 = def4.replace("<s>", "");
                String def6 = def5.replace("</s>", "");
                String def7 = def6.replace("<l>", "");
                String def8 = def7.replace("</l>", "");

                String def9 = def8.replace("<cit type=\"taxonomy\"><m>xénero</m>", "[<i><font color='"+cor_rexistro+"'>xénero</font><font color='grey'>");
                String def10 = def9.replace("<cit type=\"taxonomy\">", "[<i><font color='grey'>");

                String def11 = def10.replace("<m>varias especies</m></cit>", "</font></i><i><font color='"+cor_rexistro+"'>varias especies</font></i>]<br/>");

                String def12 = def11.replace("</cit>", "</font></i>]<br/>");
                String def13 = def12.replace("<m>", " <i><font color='"+cor_rexistro+"'>");
                String def14 = def13.replace("</m>", "</font></i>");

                if (!lema.equals(lemabuffer))
                    entrada.add("<h1>" + lema +  "</h1><i>" + cat +  "</i><br/><b>" + acep +  "</b>. " + def14);
                else if (!cat.equals(catbuffer))
                    entrada.add("<i>" + cat +  "</i><br/><b>" + acep +  "</b>. " + def14);
                else
                    entrada.add("<b>" + acep +  "</b>. " + def14);

                catbuffer = cat;
                lemabuffer = lema;

            } while(c.moveToNext());

        }

       c.close();

       Cursor m = myDataBase.rawQuery("SELECT lema, cat, acep, def FROM entrada WHERE LOWER(def) LIKE '%<l>"+palabra.trim().toLowerCase()+"</l>%'", null);

       catbuffer = "";
       lemabuffer = "";

       if (m.moveToFirst()){
           do {
               String lema = m.getString(0);
               String cat = m.getString(1);
               String acep = m.getString(2);
               String def = m.getString(3);

               cat=cat.replaceAll("\\badv conx\\b","adverbio e conxunción");
               cat=cat.replaceAll("\\badv\\b","adverbio");
               cat=cat.replaceAll("\\bconx\\b","conxunción");
               cat=cat.replaceAll("\\bindef\\b","indefinido");
               cat=cat.replaceAll("\\bix\\b","interxección");
               cat=cat.replaceAll("\\bladv\\b","locución adverbial");
               cat=cat.replaceAll("\\bladx\\b","locución adxectiva");
               cat=cat.replaceAll("\\blconx\\b","locución conxuntiva");
               cat=cat.replaceAll("\\blprep\\b","locución preposicional");
               cat=cat.replaceAll("\\blvb\\b","locución verbal");
               cat=cat.replaceAll("\\bnum\\b","numeral");
               cat=cat.replaceAll("\\bprep\\b","preposición");
               cat=cat.replaceAll("\\bpron\\b","pronome");
               cat=cat.replaceAll("\\bvi vp vt\\b","verbo intransitivo, pronominal e transitivo");
               cat=cat.replaceAll("\\bvi vp\\b","verbo intransitivo e pronominal");
               cat=cat.replaceAll("\\bvi vt\\b","verbo intransitivo e transitivo");
               cat=cat.replaceAll("\\bvp vt\\b","verbo pronominal e transitivo");
               cat=cat.replaceAll("\\bvp\\b","verbo pronominal");
               cat=cat.replaceAll("\\bvt\\b","verbo transitivo");
               cat=cat.replaceAll("\\bvi\\b","verbo intransitivo");
               cat=cat.replaceAll("\\badx sf\\b","adxectivo e substantivo feminino");
               cat=cat.replaceAll("\\badx sm\\b","adxectivo e substantivo masculino");
               cat=cat.replaceAll("\\badx s\\b","adxectivo e substantivo");
               cat=cat.replaceAll("\\badxf sf\\b","adxectivo feminino e substantivo feminino");
               cat=cat.replaceAll("\\badxm sm\\b","adxectivo masculino e substantivo masculino");
               cat=cat.replaceAll("\\badxfpl\\b","adxectivo feminino plural");
               cat=cat.replaceAll("\\badxf\\b","adxectivo feminino");
               cat=cat.replaceAll("\\badxm\\b","adxectivo masculino");
               cat=cat.replaceAll("\\badxpl\\b","adxectivo plural");
               cat=cat.replaceAll("\\badx\\b","adxectivo");
               cat=cat.replaceAll("\\bsfpl\\b","substantivo feminino plural");
               cat=cat.replaceAll("\\bsmpl\\b","substantivo masculino plural");
               cat=cat.replaceAll("\\bsm\\b","substantivo masculino");
               cat=cat.replaceAll("\\bsf\\b","substantivo feminino");
               cat=cat.replaceAll("\\bspl\\b","substantivo plural");
               cat=cat.replaceAll("\\bs\\b","substantivo");

               def=def.replace("<m>arc</m>","<m>arcaísmo</m>");
               def=def.replace("<m>col</m>","<m>coloquialismo</m>");
               def=def.replace("<m>cult</m>","<m>cultismo</m>");
               def=def.replace("<m>fam</m>","<m>familia</m>");
               def=def.replace("<m>fig</m>","<m>figurado</m>");
               def=def.replace("<m>lat</m>","<m>latinismo</m>");
               def=def.replace("<m>lit</m>","<m>literario</m>");
               def=def.replace("<m>spp</m>","<m>varias especies</m>");
               def=def.replace("<m>vulg</m>","<m>vulgarismo</m>");
               def=def.replace("<m>xén</m>","<m>xénero</m>");

               String def1 = def.replace("<e>", "<i><font color='"+cor_especificacion_semantica+"'>");
               String def2 = def1.replace("</e>", "</font></i>");
               String def3 = def2.replace("<q>", "<i><font color='grey'>");
               String def4 = def3.replace("</q>", "</font></i>");
               String def5 = def4.replace("<s>", "");
               String def6 = def5.replace("</s>", "");
               String def7 = def6.replace("<l>", "");
               String def8 = def7.replace("</l>", "");

               String def9 = def8.replace("<cit type=\"taxonomy\"><m>xénero</m>", "[<i><font color='"+cor_rexistro+"'>xénero</font><font color='grey'>");
               String def10 = def9.replace("<cit type=\"taxonomy\">", "[<i><font color='grey'>");

               String def11 = def10.replace("<m>varias especiesp</m></cit>", "</font></i><i><font color='"+cor_rexistro+"'>varias especies</font></i>]<br/>");

               String def12 = def11.replace("</cit>", "</font></i>]<br/>");
               String def13 = def12.replace("<m>", " <i><font color='"+cor_rexistro+"'>");
               String def14 = def13.replace("</m>", "</font></i>");

               if (!lema.equals(lemabuffer))
                   entrada.add("<big><b><font color='"+cor_entrada_secundaria+"'>" + lema +  "</font></b></big><br/><br/><i>" + cat +  "</i><br/><b>" + acep +  "</b>. " + def14);
               else if (!cat.equals(catbuffer))
                   entrada.add("<i>" + cat +  "</i><br/><b>" + acep +  "</b>. " + def14);
               else
                   entrada.add("<b>" + acep +  "</b>. " + def14);

               catbuffer = cat;
               lemabuffer = lema;

           } while(m.moveToNext());

       }
       m.close();
       myDataBase.close();

       return entrada;

    }

}


