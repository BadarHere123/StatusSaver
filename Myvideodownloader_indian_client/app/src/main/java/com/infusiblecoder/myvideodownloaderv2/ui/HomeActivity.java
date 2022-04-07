package com.infusiblecoder.myvideodownloaderv2.ui;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;
import com.infusiblecoder.myvideodownloaderv2.BuildConfig;
import com.infusiblecoder.myvideodownloaderv2.R;
import com.infusiblecoder.myvideodownloaderv2.adapter.FragmentAdapter;

public class HomeActivity extends AppCompatActivity {

    public AlertDialog alertDialog;
    InterstitialAd mInterstitialAd;
    MenuItem prevMenuItem;
    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    private int[] tabTitles = {R.drawable.tiktok_icon, R.drawable.instagram_icon, R.drawable.facebook_icon, R.drawable.liveleak_icon, R.drawable.youtube_icon, R.drawable.ted_icon, R.drawable.vimeo_icon};

    public static String getDeviceName() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        if (str2.startsWith(str)) {
            return capitalize(str2);
        }
        String sb = capitalize(str) +
                " " +
                str2;
        return sb;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (char c : charArray) {
            if (!z || !Character.isLetter(c)) {
                if (Character.isWhitespace(c)) {
                    z = true;
                }
                sb.append(c);
            } else {
                sb.append(Character.toUpperCase(c));
                z = false;
            }
        }
        return sb.toString();
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        initview();
        this.toolbar = findViewById(R.id.toolbar);
        this.toolbar.setTitle("Tiktok");
        setSupportActionBar(this.toolbar);
        loadinginterstitialad();
    }

    private void initview() {
        this.viewPager = findViewById(R.id.viewpager);
        this.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        this.viewPager.setCurrentItem(0);
        this.viewPager.setOffscreenPageLimit(6);
        this.tabLayout = findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(this.viewPager);
        for (int i = 0; i < this.tabLayout.getTabCount(); i++) {
            this.tabLayout.getTabAt(i).setIcon(this.tabTitles[i]);
        }
        this.viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                if (HomeActivity.this.prevMenuItem != null) {
                    HomeActivity.this.prevMenuItem.setChecked(false);
                }
                String sb = "onPageSelected: " +
                        i;
                Log.d("page", sb);
                HomeActivity.this.settiyle(i);
            }
        });
    }


    public void settiyle(int i) {
        String str = "Data";
        SharedPreferences sharedPreferences = getSharedPreferences(str, 0);
        Editor edit = sharedPreferences.edit();
        String str2 = "counter";
        edit.putInt(str2, sharedPreferences.getInt(str2, 0) + 1);
        edit.apply();
        if (sharedPreferences.getInt(str2, 0) >= 3) {
            Editor edit2 = getSharedPreferences(str, 0).edit();
            edit2.putInt(str2, 0);
            edit2.apply();
            InterstitialAd interstitialAd = this.mInterstitialAd;
            if (interstitialAd == null || !interstitialAd.isLoaded()) {
                showinginterstitialad();
            } else {
                this.mInterstitialAd.show();
            }
        }
        if (i == 0) {
            this.toolbar.setTitle("Tiktok");
        } else if (i == 1) {
            this.toolbar.setTitle("Instagram");
        } else if (i == 2) {
            this.toolbar.setTitle("Facebook");
        } else if (i == 3) {
            this.toolbar.setTitle("LiveLeak");
        } else if (i == 4) {
            this.toolbar.setTitle("Youtube");
        } else if (i == 5) {
            this.toolbar.setTitle("TED");
        } else if (i == 6) {
            this.toolbar.setTitle("Viemo");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        String str = "android.intent.action.VIEW";
        switch (menuItem.getItemId()) {
            case R.id.nav_feedback:
                sendEmailFeedback();
                return true;
            case R.id.nav_how:
                try {
                    Builder builder = new Builder(this);
                    builder.setTitle(getString(R.string.how_to_use_title));
                    builder.setMessage(getString(R.string.how_to_use));
                    builder.setPositiveButton("OK", new OnClickListener() {
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.setCancelable(true);
                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.nav_notworking:
                sendEmail();
                return true;
            case R.id.nav_privacy:
                startActivity(new Intent(this, com.infusiblecoder.myvideodownloaderv2.ui.PolicyActivity.class));
                return true;
            case R.id.nav_rate:
                try {
                    startActivity(new Intent(str, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException unused) {
                    startActivity(new Intent(str, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                return true;
            case R.id.nav_share:
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", "Video Downloader");
                    String string = getString(R.string.share_msg);
                    String sb = string +
                            " \n\nApp link : \nhttps://play.google.com/store/apps/details?id=" +
                            BuildConfig.APPLICATION_ID +
                            "\n\n";
                    intent.putExtra("android.intent.extra.TEXT", sb);
                    startActivity(Intent.createChooser(intent, "choose one"));
                } catch (Exception unused2) {
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    //TODO
    public void sendEmailFeedback() {
        try {
            String[] strArr = {"infusiblecoder@gmail.com"};
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setData(Uri.parse("mailto:"));
            intent.setType("text/plain");
            int i = VERSION.SDK_INT;
            getDeviceName();
            intent.putExtra("android.intent.extra.EMAIL", strArr);
            intent.putExtra("android.intent.extra.SUBJECT", "Video Downloader Feedback");
            intent.putExtra("android.intent.extra.TEXT", "\n\n type here...");
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
                finish();
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this, "App not installed.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmail() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
            i.putExtra(Intent.EXTRA_TEXT, "body of email");

            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(HomeActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadinginterstitialad() {
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(getResources().getString(R.string.ADM_INTERSTITIALID));
        this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                super.onAdClosed();
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

    private void showinginterstitialad() {
        Builder builder = new Builder(this);
        builder.setMessage("Showing Ad").setCancelable(false);
        this.alertDialog = builder.create();
        this.alertDialog.show();
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(getResources().getString(R.string.ADM_INTERSTITIALID));
        this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                super.onAdClosed();
            }

            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                HomeActivity.this.alertDialog.dismiss();
            }

            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            public void onAdOpened() {
                super.onAdOpened();
            }

            public void onAdLoaded() {
                super.onAdLoaded();
                HomeActivity.this.alertDialog.dismiss();
                HomeActivity.this.mInterstitialAd.show();
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
