package com.infusiblecoder.myvideodownloaderv2.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.infusiblecoder.myvideodownloaderv2.R;
import com.infusiblecoder.myvideodownloaderv2.adapter.DownloadAdapter;

public class DownloadActivity extends AppCompatActivity {
    ImageView img_back;
    InterstitialAd mInterstitialAd;
    BottomNavigationView navigation;
    MenuItem prevMenuItem;
    Toolbar toolbar;
    ViewPager viewPager;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_download);
        initview();
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        loadinginterstitialad();
    }

    private void initview() {
        this.img_back = findViewById(R.id.img_back);
        this.navigation = findViewById(R.id.navigation);
        this.viewPager = findViewById(R.id.viewpager);
        this.viewPager.setAdapter(new DownloadAdapter(getSupportFragmentManager()));
        this.viewPager.setCurrentItem(0);
        this.viewPager.setOffscreenPageLimit(3);
        this.viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                if (DownloadActivity.this.prevMenuItem != null) {
                    DownloadActivity.this.prevMenuItem.setChecked(false);
                } else {
                    DownloadActivity.this.navigation.getMenu().getItem(0).setChecked(false);
                }
                String sb = "onPageSelected: " +
                        i;
                Log.d("page", sb);
                DownloadActivity.this.navigation.getMenu().getItem(i).setChecked(true);
                DownloadActivity downloadActivity = DownloadActivity.this;
                downloadActivity.prevMenuItem = downloadActivity.navigation.getMenu().getItem(i);
            }
        });
        this.navigation.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.navigation_image) {
                    DownloadActivity.this.viewPager.setCurrentItem(1);
                } else if (itemId == R.id.navigation_song) {
                    DownloadActivity.this.viewPager.setCurrentItem(2);
                } else if (itemId == R.id.navigation_video) {
                    DownloadActivity.this.viewPager.setCurrentItem(0);
                }
                return false;
            }
        });
        this.img_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DownloadActivity.this.onBackPressed();
            }
        });
    }

    public void onBackPressed() {
        InterstitialAd interstitialAd = this.mInterstitialAd;
        if (interstitialAd == null || !interstitialAd.isLoaded()) {
            super.onBackPressed();
        } else {
            this.mInterstitialAd.show();
        }
    }

    private void loadinginterstitialad() {
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(getResources().getString(R.string.ADM_INTERSTITIALID));
        this.mInterstitialAd.loadAd(new Builder().build());
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                super.onAdClosed();
                DownloadActivity.super.onBackPressed();
            }

            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            public void onAdOpened() {
                super.onAdOpened();
            }

            public void onAdLoaded() {
                super.onAdLoaded();
            }

            public void onAdClicked() {
                super.onAdClicked();
            }

            public void onAdImpression() {
                super.onAdImpression();
            }
        });
    }
}
