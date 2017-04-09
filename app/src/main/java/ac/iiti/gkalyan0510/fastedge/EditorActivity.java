
package ac.iiti.gkalyan0510.fastedge;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class EditorActivity extends AppCompatActivity {

    private EditText description;

    private Note note;
    private int position;
    private EditText title;

    private void delete() {
        if (this.note != null) {
            StorageManager.backup(this,"Delete_"+note.getTitle()+"_");
            StorageManager.removeNote(this.getApplicationContext(), this.position);
        }
        this.notifyAndExit("Success Delete");
    }



    private void loadData(Intent intent) {
        String string2 = intent.getStringExtra("note");
        if (string2 != null) {

            this.note = (Note)new Gson().fromJson(string2, (Class)Note.class);
            this.position = this.note.getPosition();
            String string3 = this.note.getTitle();
            String string4 = this.note.getDescription();
            if (string3 != null) {
                this.title.setText((CharSequence)string3);
            }
            if (string4 != null) {
                this.description.setText((CharSequence)string4);
            }
            return;
        }
        this.note = null;
        this.position = StorageManager.getNotes(this.getApplicationContext()).size();
        this.title.setText((CharSequence)"");
        this.description.setText((CharSequence)"");
    }

    private void notifyAndExit(String n) {
        Toast.makeText((Context)this, n, Toast.LENGTH_SHORT).show();
        this.notifyProvider();
        this.finish();
    }

    private void notifyProvider() {
        this.sendBroadcast(new Intent("com.jahertor.fastnotesedge.action.UPDATE_TEXT"));
    }


    private void save() {
        if (!this.description.getText().toString().isEmpty()) {
            StorageManager.addNote(this.getApplicationContext(), new Note(this.position, this.title.getText().toString(), this.description.getText().toString()));
            this.notifyAndExit("Succes saving");
            return;
        }
        Toast.makeText((Context)this,"Empty note" ,Toast.LENGTH_SHORT).show();
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        String string2 = getIntent().getStringExtra("note");

        super.onCreate(bundle);
        this.setContentView(R.layout.activity_editor);
        isStoragePermissionGranted();




        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        this.setTitle("Note");

        this.title = (EditText)this.findViewById(R.id.title);
        this.description = (EditText)this.findViewById(R.id.desc);


        if(string2!=null && string2.length()!=3 || string2==null)
        this.loadData(this.getIntent());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(R.menu.menu_editor, menu2);
        return super.onCreateOptionsMenu(menu2);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.loadData(intent);
    }
    private static final int FILE_SELECT_CODE = 9879;
    private void showFileChooser() {
        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        intent.putExtra("CONTENT_TYPE", "*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                   // Log.d("TAG", "File Uri: " + uri.toString());

                    String path = null;
                    try {
                        path = getPath(this, uri);
                        StorageManager.backup(this,"PreLoadBackup");
                        StorageManager.load(this,getStringFromFile(path));
                        finish();
                    }
                    catch (URISyntaxException e) {
                        e.printStackTrace();
                        Toast.makeText(EditorActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(EditorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                   // Log.d("TAG", "File Path: " + path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int n = menuItem.getItemId();
        if (n == R.id.save) {
            this.save();
            return true;
        }
        else if (n == R.id.delete) {
            this.delete();
            return true;
        }
        else if (n == R.id.backup) {
            StorageManager.backup(this);
            return true;
        }
        else if (n == R.id.choose) {
            Toast.makeText(EditorActivity.this, "goto DeviceStorage/Samsung/FastEdge", Toast.LENGTH_LONG).show();
            showFileChooser();

            return true;
        }
        else {

            this.onBackPressed();
            return true;
        }

    }

}

