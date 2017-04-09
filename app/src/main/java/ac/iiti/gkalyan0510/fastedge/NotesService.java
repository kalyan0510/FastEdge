/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.widget.RemoteViewsService
 *  android.widget.RemoteViewsService$RemoteViewsFactory
 */
package ac.iiti.gkalyan0510.fastedge;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class NotesService extends RemoteViewsService {
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NotesFactory(this.getApplicationContext());
    }
}

