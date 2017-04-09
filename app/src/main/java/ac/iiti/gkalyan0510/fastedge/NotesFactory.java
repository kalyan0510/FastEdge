/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.widget.RemoteViews
 *  android.widget.RemoteViewsService
 *  android.widget.RemoteViewsService$RemoteViewsFactory
 *  com.google.gson.Gson
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package ac.iiti.gkalyan0510.fastedge;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.google.gson.Gson;

class NotesFactory
implements RemoteViewsService.RemoteViewsFactory {
    private Context context = null;

    NotesFactory(Context context) {
        this.context = context;
    }

    public int getCount() {
        return 2 + StorageManager.getNotes(this.context).size();
    }

    public long getItemId(int n) {
        return n;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public RemoteViews getViewAt(int n) {
        Log.d("PPPPP",2 + StorageManager.getNotes(this.context).size()+"");
        RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(), R.layout.edge_item);
        if (n+1 ==   this.getCount()) {
            remoteViews.setViewVisibility(R.id.lltit,View.GONE);
            remoteViews.setViewVisibility(R.id.idid, View.GONE);
            remoteViews.setViewVisibility(R.id.lldesc, View.GONE);
            remoteViews.setViewVisibility(R.id.lladd, View.VISIBLE);
           // Log.d("LOGG","CLICKED BUDDY");
            remoteViews.setOnClickFillInIntent(R.id.linearlo, new Intent(context,EditorActivity.class));

            return remoteViews;
        }else if(n==0){
            remoteViews.setViewVisibility(R.id.lltit,View.GONE);

            remoteViews.setViewVisibility(R.id.lldesc, View.GONE);
            remoteViews.setViewVisibility(R.id.lladd, View.GONE);
            remoteViews.setViewVisibility(R.id.idid, View.VISIBLE);


            PackageManager pm = context.getPackageManager();

            Intent intent = new Intent();
            intent.putExtra("note", "cal");
            remoteViews.setOnClickFillInIntent(R.id.calc, intent);
            intent = new Intent();
            intent.putExtra("note", "clo");
            remoteViews.setOnClickFillInIntent(R.id.clock, intent);
            intent = new Intent();
            intent.putExtra("note", "myf");
            remoteViews.setOnClickFillInIntent(R.id.myfiles, intent);

            intent = new Intent();
            intent.putExtra("note", "spl");
            remoteViews.setOnClickFillInIntent(R.id.splanner, intent);
            intent = new Intent();
            intent.putExtra("note", "gal");
            remoteViews.setOnClickFillInIntent(R.id.gallery, intent);
            intent = new Intent();
            intent.putExtra("note", "chr");
            remoteViews.setOnClickFillInIntent(R.id.chrome, intent);

            //remoteViews.setOnClickFillInIntent(R.id.calc,i.addCategory(Intent.CATEGORY_LAUNCHER));
            // remoteViews.setOnClickFillInIntent(R.id.calc, pm.getLaunchIntentForPackage("com.sec.android.app.popupcalculator"));
            //remoteViews.setOnClickFillInIntent(R.id.clock, pm.getLaunchIntentForPackage("com.sec.android.app.clockpackage"));
            return remoteViews;
        }
        remoteViews.setViewVisibility(R.id.lltit,View.VISIBLE);
        remoteViews.setViewVisibility(R.id.idid, View.GONE);
        remoteViews.setViewVisibility(R.id.lldesc, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.lladd, View.GONE);
        Note note = StorageManager.getNote(this.context, n-1);
        String string2 = note.getTitle();
        if (string2 != null && !string2.isEmpty()) {
            remoteViews.setTextViewText(R.id.lltit, string2);
            remoteViews.setViewVisibility(R.id.lltit, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.lltit, View.GONE);
        }
        remoteViews.setTextViewText(R.id.lldesc,note.getDescription());
        Intent intent = new Intent();
        intent.putExtra("note", new Gson().toJson(note));
        remoteViews.setOnClickFillInIntent(R.id.linearlo, intent);
        return remoteViews;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onCreate() {
    }

    public void onDataSetChanged() {
    }

    public void onDestroy() {
    }
}

