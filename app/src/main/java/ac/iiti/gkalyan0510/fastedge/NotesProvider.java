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
import android.widget.RemoteViews;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailProvider;

public class NotesProvider  extends SlookCocktailProvider {
    public static final String ACTION_UPDATE_TEXT = "com.jahertor.fastnotesedge.action.UPDATE_TEXT";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals((Object)"com.jahertor.fastnotesedge.action.UPDATE_TEXT")) {
            int[] arrn = SlookCocktailManager.getInstance(context).getCocktailIds(new ComponentName(context, (Class)NotesProvider.class));
            this.onUpdate(context, SlookCocktailManager.getInstance(context), arrn);
        }
    }

    @Override
    public void onUpdate(Context context, SlookCocktailManager slookCocktailManager, int[] arrn) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.edge_notes);
        remoteViews.setRemoteAdapter( R.id.edgenotell, new Intent(context, (Class)NotesService.class));

        remoteViews.setPendingIntentTemplate( R.id.edgenotell, PendingIntent.getActivity((Context)context, (int)0, (Intent)new Intent(context, (Class)EditorActivity.class), (int)PendingIntent.FLAG_UPDATE_CURRENT));
        remoteViews.setPendingIntentTemplate( R.id.idid, PendingIntent.getActivity((Context)context, (int)0, (Intent)new Intent("com.andro.kid.OPENAPP"), (int)PendingIntent.FLAG_UPDATE_CURRENT));


        if (arrn != null) {
            for (int n : arrn) {
                SlookCocktailManager.getInstance(context).notifyCocktailViewDataChanged(n,  R.id.edgenotell);
                slookCocktailManager.updateCocktail(n, remoteViews);
            }
        }
    }
}

