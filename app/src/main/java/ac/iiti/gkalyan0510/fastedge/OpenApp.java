package ac.iiti.gkalyan0510.fastedge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by iitindore on 4/9/17.
 */
public class OpenApp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, intent.getStringExtra("note"), Toast.LENGTH_LONG).show();
    }
}
