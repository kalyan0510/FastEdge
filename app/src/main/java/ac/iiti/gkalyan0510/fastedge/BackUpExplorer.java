package ac.iiti.gkalyan0510.fastedge;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackUpExplorer extends ListActivity {

    private String path;
    boolean isOnDeleteMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isOnDeleteMode = false;
        setContentView(R.layout.activity_back_up_explorer);
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // Use the current directory as title
        path = "/";
        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        setTitle(path);

        // Read all files sorted into the values-array
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".")) {
                    values.add(file);
                }
            }
        }
        Collections.sort(values);

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(R.menu.menu_editor, menu2);
        return super.onCreateOptionsMenu(menu2);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int n = menuItem.getItemId();
        if (n == R.id.bckdel) {
            LinearLayout l = (LinearLayout)findViewById(R.id.Line);
            if(isOnDeleteMode){
                l.setBackgroundColor(Color.WHITE);
                isOnDeleteMode = false;
            }else {
                isOnDeleteMode = true;
                l.setBackgroundColor(Color.RED);
            }
            return true;
        }
        else {
            //Toast.makeText(EditorActivity.this, "Bckpressed", Toast.LENGTH_SHORT).show();
            this.onBackPressed();
            return true;
        }
        //return super.onOptionsItemSelected(menuItem);
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
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String filename = (String) getListAdapter().getItem(position);



        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }
        if (new File(filename).isDirectory()) {
            Intent intent = new Intent(this, BackUpExplorer.class);
            intent.putExtra("path", filename);
            startActivity(intent);
        } else {

            Log.d("TAG", "File Uri: " + filename);
            // Get the path
            String path = null;
            try {

                //Toast.makeText(EditorActivity.this, path+"-", Toast.LENGTH_SHORT).show();
                //  Toast.makeText(EditorActivity.this, getStringFromFile(path), Toast.LENGTH_SHORT).show();
                if(isOnDeleteMode){
                    File file = new File(filename);
                    Toast.makeText(BackUpExplorer.this, (file.delete()?"deleted":"detele failed"), Toast.LENGTH_SHORT).show();
                }else {
                    StorageManager.backup(this, "PreLoadBackup");
                    StorageManager.load(this, getStringFromFile(filename));
                    finish();
                }

            }
            catch (URISyntaxException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Log.d("TAG", "File Path: " + path);


        }
    }


}
  //  am start -n ac.iiti.gkalyan0510.fastedge/ac.iiti.gkalyan0510.fastedge.BackUpExplorer