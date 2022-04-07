package com.infusiblecoder.myvideodownloaderv2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {
    private static App instance;

    public static boolean hasNetwork() {
        return instance.checkIfHasNetwork();
    }

    public static App getInstance() {
        return instance;
    }

    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
    }

    public boolean checkIfHasNetwork() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
