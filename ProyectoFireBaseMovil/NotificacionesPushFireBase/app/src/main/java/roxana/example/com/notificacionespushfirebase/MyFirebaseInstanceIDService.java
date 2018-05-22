package roxana.example.com.notificacionespushfirebase;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

public static final String TAG="Mensajes";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token=FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Token: "+token);
    }
}