package com.infusiblecoder.myvideodownloaderv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.infusiblecoder.myvideodownloaderv2.BuildConfig;
import com.infusiblecoder.myvideodownloaderv2.R;
import com.infusiblecoder.myvideodownloaderv2.model.MyFileProvider;
import com.infusiblecoder.myvideodownloaderv2.ui.SongPlayerActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SonglistAdapter extends Adapter<SonglistAdapter.MyViewHolder> {
    ArrayList<File> allsong;
    Context context;

    public SonglistAdapter(Context context2, ArrayList<File> arrayList) {
        this.context = context2;
        this.allsong = arrayList;
    }

    public static String getSize(long j) {
        if (j <= 0) {
            return "0";
        }
        String[] strArr = {"Byte", "KB", "MB", "GB", "TB"};
        double d = (double) j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        StringBuilder sb = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double pow = Math.pow(1024.0d, log10);
        Double.isNaN(d);
        sb.append(decimalFormat.format(d / pow));
        sb.append(" ");
        sb.append(strArr[log10]);
        return sb.toString();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_item, null));
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        String str = "";
        myViewHolder.txt_name.setText(this.allsong.get(i).getName().replace(".mp3", str).replace(".m4a", str).replace(".wav", str));
        myViewHolder.txt_size.setText(getSize(this.allsong.get(i).length()));
        myViewHolder.play.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SonglistAdapter.this.context, SongPlayerActivity.class);
                intent.putExtra("allsong", SonglistAdapter.this.allsong);
                intent.putExtra("position", myViewHolder.getAdapterPosition());
                SonglistAdapter.this.context.startActivity(intent);
            }
        });
        myViewHolder.share.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                String sbq = context.getPackageName() +
                        ".provider";
                Uri uriForFile = MyFileProvider.getUriForFile(SonglistAdapter.this.context, sbq, SonglistAdapter.this.allsong.get(myViewHolder.getAdapterPosition()));
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("audio/mp3");
                String sb = "All in one Video, Image Downloader : Tiktok, Youtubr, Facebook, Instagram, LiveLeak, etc.\n\nApp Link : \n" +
                        "https://play.google.com/store/apps/details?id=" +
                        BuildConfig.APPLICATION_ID +
                        "\n";
                intent.putExtra("android.intent.extra.TEXT", sb);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                SonglistAdapter.this.context.startActivity(Intent.createChooser(intent, "Share Music"));
            }
        });
    }

    public int getItemCount() {
        return this.allsong.size();
    }

    public class MyViewHolder extends ViewHolder {
        LinearLayout play;
        ImageView share;
        TextView txt_name;
        TextView txt_size;

        public MyViewHolder(View view) {
            super(view);
            this.txt_name = view.findViewById(R.id.txt_name);
            this.txt_size = view.findViewById(R.id.txt_size);
            this.play = view.findViewById(R.id.play);
            this.share = view.findViewById(R.id.share);
        }
    }
}
