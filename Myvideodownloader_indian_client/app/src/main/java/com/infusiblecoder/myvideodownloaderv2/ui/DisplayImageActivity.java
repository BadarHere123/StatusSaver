package com.infusiblecoder.myvideodownloaderv2.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.infusiblecoder.myvideodownloaderv2.R;


public class DisplayImageActivity extends AppCompatActivity {



    private ImageView img_back;
    private String name;
    private ImageView video_view;
    private String videourl;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_display_image);
        this.name = getIntent().getStringExtra(ConditionalUserProperty.NAME);
        this.videourl = getIntent().getStringExtra("videourl");
        this.img_back = findViewById(R.id.back);
        this.video_view = findViewById(R.id.video_view);
        try {
            if (this.videourl.equals("")) {
                this.video_view.setImageDrawable(getResources().getDrawable(R.drawable.ic_do_not_disturb_black_24dp));
            } else {
                this.video_view.setImageURI(Uri.parse(this.videourl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.img_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DisplayImageActivity.this.onBackPressed();
            }
        });
    }
}
