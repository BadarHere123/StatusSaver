package com.infusiblecoder.myvideodownloaderv2.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;

import com.infusiblecoder.myvideodownloaderv2.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

/* renamed from: usama.utech.myvideodownloader.ui.SplashActivity */
public class SplashActivity extends AppCompatActivity {
    private static final int SETTING = 101;

    public static boolean isNetworkAvaliable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(0) != null && connectivityManager.getNetworkInfo(0).getState() == State.CONNECTED) || (connectivityManager.getNetworkInfo(1) != null && connectivityManager.getNetworkInfo(1).getState() == State.CONNECTED);
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.requestStorageGalleryPermission();
            }
        }, 1200);
    }


    public void requestStorageGalleryPermission() {
        Dexter.withActivity(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    SplashActivity.this.gotonext();
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    SplashActivity.this.showPermissionDialog();
                }
            }

            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            public final void onError(DexterError dexterError) {
                Toast.makeText(SplashActivity.this.getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();
    }

    public void showPermissionDialog() {
        Builder builder = new Builder(this);
        builder.setTitle("Permissions Required");
        builder.setMessage("This app required permissions for better use of its features. Allow it from app settings.");
        builder.setPositiveButton("Goto Settings", new OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                SplashActivity.this.goToSetting();
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    public void goToSetting() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivityForResult(intent, 101);
    }


    public void gotonext() {
        if (isNetworkAvaliable(this)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SplashActivity splashActivity = SplashActivity.this;
                    splashActivity.startActivity(new Intent(splashActivity, HomeActivity.class));
                    SplashActivity.this.finish();
                }
            }, 1200);
            return;
        }
        Builder builder = new Builder(this);
        builder.setMessage("Please check your connenction and try again").setCancelable(false).setPositiveButton("Retry", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                SplashActivity.this.gotonext();
            }
        });
        AlertDialog create = builder.create();
        create.setTitle("No Internet");
        create.show();
    }
}
