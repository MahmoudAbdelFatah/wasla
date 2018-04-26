package android.wasla.support.util;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mahmoudabdelfatahabd on 09-Apr-18.
 */

public class CheckOfflineFirebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //present Data Offline
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
