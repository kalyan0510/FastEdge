/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.util.Base64
 *  com.google.gson.Gson
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Iterator
 */
package ac.iiti.gkalyan0510.fastedge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class StorageManager {

    public static void addNote(Context context, Note note) {
        ArrayList<Note> arrayList = StorageManager.getNotes(context);
        if (arrayList.contains(note)) {
            arrayList.set(arrayList.indexOf(note), note);
        } else {
            arrayList.add(note);
        }
        StorageManager.setNotes(context, arrayList);
    }



    private static SharedPreferences.Editor getEditor(Context context) {
        return StorageManager.getPreferences(context).edit();
    }

    public static Note getNote(Context context, int n) {
        return (Note)StorageManager.getNotes(context).get(n);
    }

    public static ArrayList<Note> getNotes(Context context) {

        try {
            String string2 = StorageManager.getPreferences(context).getString("list", null);
            if (string2 != null) {
                return ((NoteList)new Gson().fromJson(string2, (Class)NoteList.class)).getList();
            }
            return new ArrayList();
        } catch (Exception e) {
            System.out.println("invalid json format");
            return null;
        }
    }
    public static boolean isvalid(Context con,String s){
        try {

            if (s != null) {
                 ((NoteList)new Gson().fromJson(s, (Class)NoteList.class)).getList();
                return true;
            }else
                return false;


        } catch (Exception e) {
            System.out.println("invalid json format");
            return false;
        }
    }
    public static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }
    public static void backup(Context context){
        backup(context,"");
    }
    public static void backup(Context context,String xx) {

        String data = StorageManager.getPreferences(context).getString("list", null);
        if (data != null) {


            File folder = new File(Environment.getExternalStorageDirectory()+"/Samsung/FastEdge/");
            Log.d("LOG",folder.toString());

            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            Log.d("LOG XX MK",""+folder.mkdirs());
            File file = new File(folder, xx+"data_"+dateFormat.format(date).trim()+".html");

            Log.d("LOG XX",""+file.toString());

                try
                {
                    boolean x = file.createNewFile();

                    FileOutputStream fOut = new FileOutputStream(file);

                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                    myOutWriter.append(data);

                    myOutWriter.close();

                    fOut.flush();
                    fOut.close();

                   if(x)
                    Toast.makeText(context,"Stored at Samsung/FastEdge\n" , Toast.LENGTH_LONG).show();
                    else
                       Toast.makeText(context, "Allow Storage Permission", Toast.LENGTH_LONG).show();
                }
                catch (FileNotFoundException c){
                    c.printStackTrace();
                }
                catch (IOException e)
                {
                    Log.e("Exception", "File write failed: " + e.toString());
                }

        }
    }
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName().concat(".datagk"), 0);
    }



    public static void removeNote(Context context, int n) {
        ArrayList<Note> arrayList = StorageManager.getNotes(context);
        arrayList.remove(n);
        if (!arrayList.isEmpty()) {
            int n2 = 0;
            Iterator iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                ((Note)iterator.next()).setPosition(n2);
                ++n2;
            }
        }
        StorageManager.setNotes(context, arrayList);
    }

    public static void removeNote(Context context, Note note) {
        StorageManager.getNotes(context).remove(note);
    }
    public static void load(Context context, String x) throws Exception{
        if(isvalid(context,x)) {
            StorageManager.getEditor(context).putString("list", x).commit();
        }else {
            throw new Exception("INVALID FILE");
        }
        context.sendBroadcast(new Intent("com.jahertor.fastnotesedge.action.UPDATE_TEXT"));

    }
    private static void setNotes(Context context, ArrayList<Note> arrayList) {
        StorageManager.getEditor(context).putString("list", new Gson().toJson(new NoteList(arrayList))).commit();
    }
}

