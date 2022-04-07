package com.infusiblecoder.myvideodownloaderv2.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.infusiblecoder.myvideodownloaderv2.R;

import java.io.File;
import java.util.ResourceBundle;

import static com.infusiblecoder.myvideodownloaderv2.utils.Constants.DOWNLOAD_DIRECTORY;


public class downloadFile {
    public static DownloadManager downloadManager;
    public static long downloadID;
    private static String mBaseFolderPath;
    private static ResourceBundle preferences;


    public static void Downloading(final Context context, String url, String title, String ext) {
        String cutTitle = "";
        Context context1 = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            cutTitle = Constants.ANDROID_10_IDENTIFER + title;
        } else {

            cutTitle = title;

        }
        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        cutTitle = cutTitle.replaceAll(characterFilter, "");
        cutTitle = cutTitle.replaceAll("['+.^:,#\"]", "");
        cutTitle = cutTitle.replace(" ", "-").replace("!", "").replace(":", "") + ext;
        if (cutTitle.length() > 100)
            cutTitle = cutTitle.substring(0, 100) + ext;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //  Uri mainUri = FileProvider.get(context, context.getApplicationContext().getPackageName() + ".provider", url);


        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription("Downloading");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String folderName = DOWNLOAD_DIRECTORY;


        // mBaseFolderPath = context.getExternalFilesDir(null).getAbsolutePath() + File.separator + folderName;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mBaseFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator;

        } else {
            mBaseFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                    File.separator +
                    context.getString(R.string.app_name) +
                    File.separator +
                    context.getString(R.string.video) +
                    File.separator;
        }


        System.out.println("myerroris5555555555 " + mBaseFolderPath);
        // mBaseFolderPath = android.os.Environment.getDataDirectory() + File.separator + folderName;

        File mBaseFolderPathfile = new File(mBaseFolderPath);

        if (!mBaseFolderPathfile.exists()) {
            mBaseFolderPathfile.mkdir();
        }
        String[] bits = mBaseFolderPath.split("/");
        String Dir = bits[bits.length - 1];
        //  request.setDestinationUri(new File(mBaseFolderPath).);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, cutTitle);

        } else {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS +
                    File.separator +
                    context.getString(R.string.app_name) +
                    File.separator +
                    context.getString(R.string.video) +
                    File.separator, cutTitle);

        }


        request.allowScanningByMediaScanner();
        downloadID = downloadManager.enqueue(request);
        Log.e("downloadFileName", cutTitle);

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Toast.makeText(context, "done started1122", Toast.LENGTH_SHORT).show();

            }
        };

    }
}
