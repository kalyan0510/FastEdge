package ac.iiti.gkalyan0510.fastedge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * Created by iitindore on 4/9/17.
 */
public class OpenApp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        String string2=intent.getStringExtra("note");
        if(string2!=null ){
            // Toast.makeText(EditorActivity.this, "CaLcALCAlcaCLalclcla", Toast.LENGTH_SHORT).show();
            PackageManager pm  = context.getPackageManager();
            Intent i;
            switch (string2){
                case "cal":
                    i = pm.getLaunchIntentForPackage(Constants.CALCULATOR_PACKAGE);
                    break;
                case "clo":
                    i = pm.getLaunchIntentForPackage(Constants.CLOCK_PACKAGE_NAME);
                    break;
                case "myf":
                    i = pm.getLaunchIntentForPackage("com.sec.android.app.myfiles");
                    break;
                case "spl":
                    i = pm.getLaunchIntentForPackage(Constants.CALENDAR_PACKAGE_NAME);
                    break;
                case "gal":
                    i = pm.getLaunchIntentForPackage("com.sec.android.gallery3d");
                    break;
                case "chr":
                    i = pm.getLaunchIntentForPackage("com.android.chrome");
                    break;
                default:
                    /*i = new Intent(context,EditorActivity.class);
                    i.putExtra("note",string2);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                    i = new Intent("com.jahertor.fastnotesedge.action.UPDATE_TEXT");
                    i.putExtra("note",string2);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.sendBroadcast(i);
                    return;

            }





            if(i!=null){
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                context.startActivity(i);
                }
            return;
        }
        else
        {

            Intent i = new Intent(context,EditorActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
