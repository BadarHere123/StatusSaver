package com.infusiblecoder.myvideodownloaderv2.fragment;

import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infusiblecoder.myvideodownloaderv2.R;
import com.infusiblecoder.myvideodownloaderv2.adapter.SonglistAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class SongsFragment extends Fragment {
    public ArrayList<File> allsong = null;
    public LinearLayout linear_progress;
    boolean temp = false;
    private ImageView back3;
    private LinearLayout empty_frame;
    private RecyclerView recycle;
    private View view;

    public void setadapterr() {

        try {
            Collections.reverse(this.allsong);
            SonglistAdapter songlistAdapter = new SonglistAdapter(getContext(), this.allsong);
            this.recycle.setLayoutManager(new GridLayoutManager(getContext(), 1));
            this.recycle.setItemAnimator(new DefaultItemAnimator());
            this.recycle.setAdapter(songlistAdapter);
            this.linear_progress.setVisibility(View.GONE);
        }catch (Exception e){

        }

    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_songs, viewGroup, false);
        this.recycle = this.view.findViewById(R.id.recycle);
        this.empty_frame = this.view.findViewById(R.id.empty_frame);
        this.linear_progress = this.view.findViewById(R.id.linear_progress);
        new AsyncTaskRunner().execute("abc");
        return this.view;
    }

    public ArrayList<File> getsong(File file) {
        File[] listFiles;
        ArrayList<File> arrayList = new ArrayList<>();
        for (File file2 : file.listFiles()) {
            if (file2.isDirectory()) {
                arrayList.addAll(getsong(file2));
            } else if (file2.getName().endsWith(".m4a") || file2.getName().endsWith(".wav") || file2.getName().endsWith(".mp3") || file2.getName().endsWith(".MP3")) {
                arrayList.add(file2);
                scanFile(file2.getPath());
            }
        }
        if (arrayList.size() == 0) {
            this.empty_frame.setVisibility(View.VISIBLE);
        } else {
            this.empty_frame.setVisibility(View.GONE);
        }
        return arrayList;
    }

    private void scanFile(String str) {
        MediaScannerConnection.scanFile(getContext(), new String[]{str}, null, new OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
            }
        });
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;

        private AsyncTaskRunner() {
        }

        public String doInBackground(String... strArr) {
            try {
                String sb = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    sb = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                } else {
                    sb = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                            File.separator +
                            SongsFragment.this.getString(R.string.app_name) +
                            File.separator +
                            SongsFragment.this.getString(R.string.song) +
                            File.separator;
                }
                File file = new File(sb);
                if (!file.exists()) {
                    file.mkdir();
                }
                SongsFragment.this.allsong = SongsFragment.this.getsong(file);
            } catch (Exception e) {
                e.printStackTrace();
                this.resp = e.getMessage();
            }
            return "abc";
        }

        public void onPostExecute(String str) {
            SongsFragment.this.setadapterr();
        }

        public void onPreExecute() {
            SongsFragment.this.linear_progress.setVisibility(View.VISIBLE);
        }
    }
}
