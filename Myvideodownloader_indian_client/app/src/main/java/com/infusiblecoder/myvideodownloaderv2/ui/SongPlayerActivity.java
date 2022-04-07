package com.infusiblecoder.myvideodownloaderv2.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.infusiblecoder.myvideodownloaderv2.R;

import java.io.File;
import java.util.ArrayList;


public class SongPlayerActivity extends AppCompatActivity implements OnClickListener {

    boolean ad = false;
    ArrayList<? extends File> allsong;
    boolean back = false;
    boolean check_aero = false;
    ImageView img_aero;
    ImageView img_back;
    ImageView img_next;
    ImageView img_play;
    ImageView img_previous;
    ImageView img_share;
    MediaPlayer mediaPlayer;
    int position;
    SeekBar seekBar;
    String sname;
    String str;
    String str2;
    int total;
    int totalduration;
    TextView txt_current_time;
    TextView txt_song_name;
    TextView txt_total_time;

    public Handler handler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            SongPlayerActivity.this.seekBar.setProgress(i);
            String access0 = SongPlayerActivity.this.createTime(i);
            SongPlayerActivity.this.txt_current_time.setText(access0);
            if (access0.equals(SongPlayerActivity.this.str)) {
                SongPlayerActivity.this.nextsong();
            }
        }
    };
    Thread update_seekbar;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_song_player);
        this.img_play = findViewById(R.id.img_play);
        this.img_next = findViewById(R.id.img_next);
        this.img_previous = findViewById(R.id.img_previous);
        this.img_back = findViewById(R.id.img_back);
        this.seekBar = findViewById(R.id.seekbar);
        this.txt_song_name = findViewById(R.id.txt_song_name);
        this.txt_total_time = findViewById(R.id.txt_total_time);
        this.txt_current_time = findViewById(R.id.txt_current_time);
        this.img_back.setOnClickListener(this);
        this.img_play.setOnClickListener(this);
        this.img_next.setOnClickListener(this);
        this.img_previous.setOnClickListener(this);
        Intent intent = getIntent();
        allsong = intent.getExtras().getParcelableArrayList("allsong");
        this.position = intent.getIntExtra("position", 0);
        this.txt_song_name.setText(this.allsong.get(this.position).getName());
        this.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(this.allsong.get(this.position).toString()));
        this.mediaPlayer.start();
        this.seekBar.setMax(this.mediaPlayer.getDuration());
        this.update_seekbar = new Thread() {
            public void run() {
                if (!Thread.interrupted()) {
                    SongPlayerActivity songPlayerActivity = SongPlayerActivity.this;
                    songPlayerActivity.totalduration = songPlayerActivity.mediaPlayer.getDuration();
                    int i = 0;
                    while (i / 100 <= SongPlayerActivity.this.totalduration / 100) {
                        try {
                            Message message = new Message();
                            message.what = SongPlayerActivity.this.mediaPlayer.getCurrentPosition();
                            SongPlayerActivity.this.handler.sendMessage(message);
                            sleep(250);
                            i = SongPlayerActivity.this.mediaPlayer.getCurrentPosition();
                            SongPlayerActivity.this.seekBar.setProgress(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        totaltime();
        this.update_seekbar.start();
        this.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                SongPlayerActivity.this.mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back :
                onBackPressed();
                return;
            case R.id.img_next:
                nextsong();
                return;
            case R.id.img_play :
                this.seekBar.setMax(this.mediaPlayer.getDuration());
                if (this.mediaPlayer.isPlaying()) {
                    this.img_play.setImageResource(R.drawable.play_icon);
                    this.mediaPlayer.pause();
                    return;
                }
                this.img_play.setImageResource(R.drawable.pause_icon);
                this.mediaPlayer.start();
                return;
            case R.id.img_previous :
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
                int i = this.position;
                if (i - 1 < 0) {
                    i = this.allsong.size();
                }
                this.position = i - 1;
                this.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(this.allsong.get(this.position).toString()));
                this.sname = this.allsong.get(this.position).getName();
                this.txt_song_name.setText(this.sname);
                this.mediaPlayer.start();
                this.img_play.setImageResource(R.drawable.pause_icon);
                this.totalduration = this.mediaPlayer.getDuration();
                this.seekBar.setMax(this.mediaPlayer.getDuration() - 500);
                totaltime();
                return;
            default:
                return;
        }
    }


    public void nextsong() {
        this.mediaPlayer.stop();
        this.mediaPlayer.release();
        this.position = (this.position + 1) % this.allsong.size();
        this.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(this.allsong.get(this.position).toString()));
        this.sname = this.allsong.get(this.position).getName();
        this.txt_song_name.setText(this.sname);
        totaltime();
        this.mediaPlayer.start();
        this.img_play.setImageResource(R.drawable.pause_icon);
        this.totalduration = this.mediaPlayer.getDuration();
        this.seekBar.setMax(this.mediaPlayer.getDuration());
        totaltime();
    }


    public String createTime(int i) {
        int i2 = i / 1000;
        int i3 = i2 / 60;
        int i4 = i2 % 60;
        String sb2 = i3 +
                ":";
        if (i4 < 10) {
            String sb3 = sb2 +
                    "0";
            sb2 = sb3;
        }
        String sb4 = sb2 +
                i4;
        return sb4;
    }

    public void totaltime() {
        this.total = this.mediaPlayer.getDuration();
        int i = this.total;
        int i2 = (i / 1000) / 60;
        int i3 = (i / 1000) % 60;
        String sb = i2 +
                ":";
        this.str = sb;
        if (i3 < 10) {
            String sb2 = this.str +
                    "0";
            this.str = sb2;
        }
        String sb3 = this.str +
                i3;
        this.str = sb3;
        if (i3 - 1 >= 0) {
            this.txt_total_time.setText(this.str);
        } else {
            this.txt_total_time.setText("00:00");
        }
    }

    public void onBackPressed() {
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
        }
        this.update_seekbar.interrupt();
        super.onBackPressed();
    }


    public void onResume() {
        if (this.ad) {
            onBackPressed();
        }
        super.onResume();
        this.img_play.setImageResource(R.drawable.pause_icon);
    }


    public void onDestroy() {
        this.update_seekbar.interrupt();
        super.onDestroy();
    }


    public void onPause() {
        this.update_seekbar.interrupt();
        this.ad = true;
        super.onPause();
    }
}
