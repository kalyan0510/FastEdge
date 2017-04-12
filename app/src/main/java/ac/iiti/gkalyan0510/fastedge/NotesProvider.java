/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.widget.RemoteViews
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package ac.iiti.gkalyan0510.fastedge;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailProvider;

public class NotesProvider  extends SlookCocktailProvider {
    public static final String ACTION_UPDATE_TEXT = "com.jahertor.fastnotesedge.action.UPDATE_TEXT";
    String note_data = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals((Object)"com.jahertor.fastnotesedge.action.UPDATE_TEXT")) {
            int[] arrn = SlookCocktailManager.getInstance(context).getCocktailIds(new ComponentName(context, (Class)NotesProvider.class));
            note_data = intent.getStringExtra("note");
            this.onUpdate(context, SlookCocktailManager.getInstance(context), arrn);
        }
    }
    private static RemoteViews mLongClickStateView = null;
    private RemoteViews createStateView(Context context) {
        RemoteViews stateView = new RemoteViews(context.getPackageName(),
                R.layout.edge_description);
        //SlookCocktailManager.getInstance(context).setOnLongClickPendingIntent(stateView, R.id.state_btn1, getLongClickIntent(context, R.id.state_btn1, 0));

        return stateView;
    }
    @Override
    public void onUpdate(Context context, SlookCocktailManager slookCocktailManager, int[] arrn) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.edge_notes);
        remoteViews.setRemoteAdapter( R.id.edgenotell, new Intent(context, (Class)NotesService.class));

        remoteViews.setPendingIntentTemplate( R.id.edgenotell, PendingIntent.getBroadcast((Context)context, (int)0, (Intent)new Intent("com.andro.kid.OPENAPP"), (int)PendingIntent.FLAG_UPDATE_CURRENT));

        if(mLongClickStateView == null) {
            mLongClickStateView = createStateView(context);
        }
        Intent in = new Intent(context,EditorActivity.class);
        if(note_data!=null)
        in.putExtra("note",note_data);
        else
            Log.d("LOG","null note data\n"+note_data);
        mLongClickStateView.setOnClickPendingIntent(R.id.mainEdit,PendingIntent.getActivity(context,0,in, PendingIntent.FLAG_UPDATE_CURRENT));
        Note note =  (Note)new Gson().fromJson(note_data, (Class)Note.class);
        if(note!=null) {
            mLongClickStateView.setTextViewText(R.id.mainText, note.getTitle());
            mLongClickStateView.setTextViewText(R.id.mainNote, note.getDescription());
        }
        else {
            mLongClickStateView.setTextViewText(R.id.mainText, "");
            mLongClickStateView.setTextViewText(R.id.mainNote,"");
            Log.d("LOG","null note\n");
        }
        if (arrn != null) {
            for (int n : arrn) {
                SlookCocktailManager.getInstance(context).notifyCocktailViewDataChanged(n,  R.id.edgenotell);
                slookCocktailManager.updateCocktail(n, remoteViews,mLongClickStateView);
            }
        }
    }
}

