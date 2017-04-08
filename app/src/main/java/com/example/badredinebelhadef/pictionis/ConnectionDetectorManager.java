package com.example.badredinebelhadef.pictionis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by badredine belhadef on 06/04/2017
 */

public class ConnectionDetectorManager {

    public static boolean isConnectingToInternet(Context context) {

        Boolean hasActiveInternet = false;

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivity != null) {

            NetworkInfo activeNetWork = connectivity.getActiveNetworkInfo();

            if(activeNetWork != null) {

                switch (activeNetWork.getType()) {

                    case ConnectivityManager.TYPE_WIFI :
                        hasActiveInternet = true;
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        hasActiveInternet = true;
                        break;
                    default:
                        hasActiveInternet = false;
                        break;
                }
            }
        }
        return  hasActiveInternet;
    }
}
