package com.infusiblecoder.myvideodownloaderv2.ui;

import android.app.Dialog;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.infusiblecoder.myvideodownloaderv2.R;
public class PlayActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 999;
    private static final String SHARE_ID = "com.android.all";

    public ImageView exo_play;

    public Boolean playing;
    LinearLayout empty_vieew;
    ProgressBar loader;
    SimpleExoPlayerView simpleExoPlayerView;
    TextView txt_loading;
    private Dialog dialog;
    private Boolean downloading;
    private ImageView exo_pause;
    private String extension;
    private boolean firstcall = true;

    private ImageView img_back;
    private String name;
    private SimpleExoPlayer player;
    private ProgressBar progress_bar_activity_video;
    private TextView text_view_progress_activity_video;
    private String title;
    private DefaultTrackSelector trackSelector;
    private String urlToDownload;
    private TextView video_name;
    private String videourl;
    private View view;
    private boolean visibleuser;

    public PlayActivity() {
        Boolean valueOf = Boolean.valueOf(false);
        this.downloading = valueOf;
        this.visibleuser = false;
        this.playing = valueOf;
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_play);
        this.name = getIntent().getStringExtra(ConditionalUserProperty.NAME);
        this.videourl = getIntent().getStringExtra("videourl");
        this.img_back = findViewById(R.id.back);
        this.video_name = findViewById(R.id.video_name);
        this.loader = findViewById(R.id.loader);
        this.exo_pause = findViewById(R.id.exo_pause);
        this.exo_play = findViewById(R.id.exo_play);
        this.progress_bar_activity_video = findViewById(R.id.progress_bar_activity_video);
        this.text_view_progress_activity_video = findViewById(R.id.text_view_progress_activity_video);
        String str = this.videourl;
        this.urlToDownload = str;
        this.title = this.name;
        this.extension = str.substring(str.lastIndexOf("."));
        this.video_name.setText(this.name);
        ThumbnailUtils.createVideoThumbnail(this.videourl, 1);
        initplayer();
        this.img_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PlayActivity.this.onBackPressed();
            }
        });
    }

    private void initplayer() {
        this.firstcall = false;
        Uri parse = Uri.parse(this.videourl);
        this.player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
        this.simpleExoPlayerView = findViewById(R.id.video_view);
        this.simpleExoPlayerView.setPlayer(this.player);
        ExtractorMediaSource extractorMediaSource = new ExtractorMediaSource(parse, new DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer")), new DefaultExtractorsFactory(), null, null);
        this.player.prepare(new LoopingMediaSource(extractorMediaSource));
        this.player.setPlayWhenReady(true);
        this.exo_play.setVisibility(View.GONE);
        this.simpleExoPlayerView.setControllerShowTimeoutMs(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
        this.player.addListener(new EventListener() {
            public void onLoadingChanged(boolean z) {
            }

            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            public void onPlayerError(ExoPlaybackException exoPlaybackException) {
            }

            public void onPositionDiscontinuity(int i) {
            }

            public void onRepeatModeChanged(int i) {
            }


            public void onShuffleModeEnabledChanged(boolean z) {
            }

            public void onTimelineChanged(Timeline timeline, Object obj, int i) {
            }

            public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
            }

            public void onPlayerStateChanged(boolean z, int i) {
                if (i == 3) {
                    try {
                        PlayActivity.this.loader.setVisibility(View.GONE);
                        PlayActivity.this.simpleExoPlayerView.setVisibility(View.VISIBLE);
                        PlayActivity.this.exo_play.setVisibility(View.GONE);
                        PlayActivity.this.playing = Boolean.valueOf(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (i == 2) {
                    PlayActivity.this.loader.setVisibility(View.VISIBLE);
                    PlayActivity.this.simpleExoPlayerView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        if (this.player == null && this.playing.booleanValue()) {
            initplayer();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        releaseplayer();
    }

    public void onPause() {
        super.onPause();
        releaseplayer();
    }

    private void releaseplayer() {
        SimpleExoPlayer simpleExoPlayer = this.player;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            this.player = null;
            this.trackSelector = null;
        }
    }

    public void onStop() {
        super.onStop();
        releaseplayer();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
