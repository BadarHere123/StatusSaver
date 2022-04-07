package com.infusiblecoder.myvideodownloaderv2.fragment;

import android.app.Dialog;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.infusiblecoder.myvideodownloaderv2.R;
import com.infusiblecoder.myvideodownloaderv2.model.DataModel;
import com.infusiblecoder.myvideodownloaderv2.ui.DownloadActivity;
import com.infusiblecoder.myvideodownloaderv2.utils.Data;
import com.infusiblecoder.myvideodownloaderv2.utils.Java_AES_Cipher_key;
import com.infusiblecoder.myvideodownloaderv2.utils.apiClient;
import com.infusiblecoder.myvideodownloaderv2.utils.apiRest;
import com.infusiblecoder.myvideodownloaderv2.utils.downloadVideo;

import net.pubnative.URLDriller;
import net.pubnative.URLDriller.Listener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiktokFragment extends Fragment {

    public AlertDialog alertDialog;

    public String download_url;

    public LinearLayout linear_checking;

    public LinearLayout linear_download;

    public int position = 2;

    public ProgressBar progress_bar_activity_video;

    public RelativeLayout progress_main_relative;

    public RewardedVideoAd rewardedVideoAd;

    public TextView text_view_progress_activity_video;

    public TextView txt_url;

    public String url;
    InterstitialAd mInterstitialAd;
    View view;
    private TextView btn_download;
    private TextView btn_mp3;
    private TextView btn_mydownloads;
    private TextView btn_paste;

    public TiktokFragment() {
        String str = "";
        this.url = str;
        this.download_url = str;
    }

    public static List<String> extractUrls(String str) {
        ArrayList arrayList = new ArrayList();
        Matcher matcher = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", Pattern.CASE_INSENSITIVE).matcher(str);
        while (matcher.find()) {
            arrayList.add(str.substring(matcher.start(0), matcher.end(0)));
        }
        return arrayList;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_tiktok, viewGroup, false);
        initview();
        setdialog();
        return this.view;
    }

    private void initview() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getActivity())) {
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getActivity().getPackageName())
                );
                startActivityForResult(intent, 1231);
            }
        }

        this.txt_url = this.view.findViewById(R.id.etx_url);
        this.btn_paste = this.view.findViewById(R.id.btn_paste);
        this.btn_download = this.view.findViewById(R.id.btn_download);
        this.btn_mp3 = this.view.findViewById(R.id.btn_mp3);
        this.btn_mydownloads = this.view.findViewById(R.id.btn_downloads);
        this.linear_checking = this.view.findViewById(R.id.linear_checking);
        this.linear_download = this.view.findViewById(R.id.linear_download);
        this.progress_main_relative = this.view.findViewById(R.id.progress_main_relative);
        this.progress_bar_activity_video = this.view.findViewById(R.id.progress_bar_activity_video);
        this.text_view_progress_activity_video = this.view.findViewById(R.id.text_view_progress_activity_video);
        this.btn_download.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TiktokFragment tiktokFragment = TiktokFragment.this;
                tiktokFragment.url = tiktokFragment.txt_url.getText().toString();
                if (TiktokFragment.this.url.equals("")) {
                    Toast.makeText(TiktokFragment.this.getContext(), "Please enter video link from TikTok", Toast.LENGTH_SHORT).show();
                    return;
                }
                String str = "Invalid Link";
                if (TiktokFragment.this.url.length() < 10) {
                    Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    return;
                }
                List extractUrls = TiktokFragment.extractUrls(TiktokFragment.this.url);
                if (extractUrls == null || extractUrls.size() <= 0) {
                    Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    return;
                }
                TiktokFragment.this.url = (String) extractUrls.get(0);
                if (TiktokFragment.this.url.length() > 15) {
                    TiktokFragment.this.position = 1;
                    // TiktokFragment.this.updatedialog();

                    downloadVideo.Start(getActivity(), url, false);

                } else {
                    Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                }
                String sb = " url length = " +
                        TiktokFragment.this.url.length();
                Log.e("Checking", sb);
            }
        });
        this.btn_mp3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TiktokFragment tiktokFragment = TiktokFragment.this;
                tiktokFragment.url = tiktokFragment.txt_url.getText().toString();
                if (TiktokFragment.this.url.equals("")) {
                    Toast.makeText(TiktokFragment.this.getContext(), "Please enter video link from TikTok", Toast.LENGTH_SHORT).show();
                    return;
                }
                String str = "Invalid Link";
                if (TiktokFragment.this.url.length() < 10) {
                    Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    return;
                }
                List extractUrls = TiktokFragment.extractUrls(TiktokFragment.this.url);
                if (extractUrls == null || extractUrls.size() <= 0) {
                    Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    return;
                }
                TiktokFragment.this.url = (String) extractUrls.get(0);
                if (TiktokFragment.this.url.length() > 15) {
                    TiktokFragment.this.position = 2;
                    TiktokFragment tiktokFragment2 = TiktokFragment.this;
                    tiktokFragment2.downloadmp3(tiktokFragment2.url);
                } else {
                    Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                }
                String sb = " url length = " +
                        TiktokFragment.this.url.length();
                Log.e("Checking", sb);
            }
        });
        this.btn_paste.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Item itemAt = ((ClipboardManager) TiktokFragment.this.getContext().getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip().getItemAt(0);
                if (itemAt != null) {
                    String charSequence = itemAt.getText().toString();
                    if (!charSequence.equals("")) {
                        TiktokFragment.this.txt_url.setText(charSequence);
                    }
                }
            }
        });
        this.btn_mydownloads.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TiktokFragment tiktokFragment = TiktokFragment.this;
                tiktokFragment.startActivity(new Intent(tiktokFragment.getContext(), DownloadActivity.class));
            }
        });
        this.progress_main_relative.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
    }

    public void updatedialog() {


        final Dialog dialog = new Dialog(getContext(), R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.watermark_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.findViewById(R.id.txt_with).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                TiktokFragment tiktokFragment = TiktokFragment.this;
                tiktokFragment.getdata(tiktokFragment.url, 1);
            }
        });
        dialog.findViewById(R.id.txt_without).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                TiktokFragment tiktokFragment = TiktokFragment.this;
                tiktokFragment.getdata(tiktokFragment.url, 2);
            }
        });
        dialog.findViewById(R.id.txt_mp3).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                TiktokFragment.this.position = 2;
                TiktokFragment tiktokFragment = TiktokFragment.this;
                tiktokFragment.downloadmp3(tiktokFragment.url);
            }
        });
        dialog.show();
    }

    public void getdata(String str, final int i) {
        this.progress_main_relative.setVisibility(View.VISIBLE);
        this.linear_download.setVisibility(View.GONE);
        this.linear_checking.setVisibility(View.VISIBLE);
        apiRest apirest = apiClient.getClient().create(apiRest.class);
        String valueOf = String.valueOf(4);
        String sb = "pass url = " +
                str;
        Log.d("HomeActivity", sb);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("{\"tiktok_url\":\"");
        sb2.append(str);
        sb2.append("\",\"version\":\"");
        sb2.append(valueOf);
        sb2.append("\"}");

        Log.e("myrfsdjfsjdnfs ",sb2.toString());
        apirest.getdata(Java_AES_Cipher_key.encrypt(Data.key, Data.initVector, sb2.toString()).replaceAll("\n", "")).enqueue(new Callback<DataModel>() {
            public void onResponse(Call<DataModel> call, final Response<DataModel> response) {
                String str = "Something Went wrong";
                if (response.body() == null) {
                    TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                    Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                } else if (response.body().getStatus().intValue() == 200) {
                    String str2 = "";
                    if (i == 1) {
                        if (!response.body().getWatermarkVideo().equals(str2)) {
                            try {
                                URLDriller uRLDriller = new URLDriller();
                                uRLDriller.drill(response.body().getWatermarkVideo());
                                uRLDriller.setListener(new Listener() {
                                    public void onURLDrillerFail(String str, Exception exc) {
                                    }

                                    public void onURLDrillerRedirect(String str) {
                                    }

                                    public void onURLDrillerStart(String str) {
                                    }

                                    public void onURLDrillerFinish(String str) {
                                        TiktokFragment.this.download_url = str;
                                        TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                                        TiktokFragment.this.linear_download.setVisibility(View.VISIBLE);
                                        String str2 = "Showing Ad";
                                        if (!response.body().getMobile_ad().equals("reward")) {
                                            if (TiktokFragment.this.alertDialog != null) {
                                                TiktokFragment.this.alertDialog.show();
                                            } else {
                                                Builder builder = new Builder(TiktokFragment.this.getContext());
                                                builder.setMessage(str2).setCancelable(false);
                                                TiktokFragment.this.alertDialog = builder.create();
                                                TiktokFragment.this.alertDialog.show();
                                            }
                                            TiktokFragment.this.showinginterstitialad();
                                        } else if (TiktokFragment.this.rewardedVideoAd.isLoaded()) {
                                            TiktokFragment.this.rewardedVideoAd.show();
                                        } else {
                                            if (TiktokFragment.this.alertDialog != null) {
                                                TiktokFragment.this.alertDialog.show();
                                            } else {
                                                Builder builder2 = new Builder(TiktokFragment.this.getContext());
                                                builder2.setMessage(str2).setCancelable(false);
                                                TiktokFragment.this.alertDialog = builder2.create();
                                                TiktokFragment.this.alertDialog.show();
                                            }
                                            TiktokFragment.this.showingad();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                            Toast.makeText(TiktokFragment.this.getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (!response.body().getOriginalVideo().equals(str2)) {
                        try {
                            URLDriller uRLDriller2 = new URLDriller();
                            uRLDriller2.drill(response.body().getOriginalVideo());
                            uRLDriller2.setListener(new Listener() {
                                public void onURLDrillerFail(String str, Exception exc) {
                                }

                                public void onURLDrillerRedirect(String str) {
                                }

                                public void onURLDrillerStart(String str) {
                                }

                                public void onURLDrillerFinish(String str) {
                                    TiktokFragment.this.download_url = str;
                                    TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                                    TiktokFragment.this.linear_download.setVisibility(View.VISIBLE);
                                    String str2 = "Showing Ad";
                                    if (!response.body().getMobile_ad().equals("reward")) {
                                        if (TiktokFragment.this.alertDialog != null) {
                                            TiktokFragment.this.alertDialog.show();
                                        } else {
                                            Builder builder = new Builder(TiktokFragment.this.getContext());
                                            builder.setMessage(str2).setCancelable(false);
                                            TiktokFragment.this.alertDialog = builder.create();
                                            TiktokFragment.this.alertDialog.show();
                                        }
                                        TiktokFragment.this.showinginterstitialad();
                                    } else if (TiktokFragment.this.rewardedVideoAd.isLoaded()) {
                                        TiktokFragment.this.rewardedVideoAd.show();
                                    } else {
                                        if (TiktokFragment.this.alertDialog != null) {
                                            TiktokFragment.this.alertDialog.show();
                                        } else {
                                            Builder builder2 = new Builder(TiktokFragment.this.getContext());
                                            builder2.setMessage(str2).setCancelable(false);
                                            TiktokFragment.this.alertDialog = builder2.create();
                                            TiktokFragment.this.alertDialog.show();
                                        }
                                        TiktokFragment.this.showingad();
                                    }
                                }
                            });
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                        Toast.makeText(TiktokFragment.this.getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                    try {
                        Toast.makeText(TiktokFragment.this.getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            public void onFailure(Call<DataModel> call, Throwable th) {
                TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TiktokFragment.this.getContext(), "Failed. Please try again "+call+"___"+th.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void downloadmp3(String str) {
        this.progress_main_relative.setVisibility(View.VISIBLE);
        this.linear_download.setVisibility(View.GONE);
        this.linear_checking.setVisibility(View.VISIBLE);
        apiRest apirest = apiClient.getClient().create(apiRest.class);
        String valueOf = String.valueOf(4);
        String sb = "{\"tiktok_url\":\"" +
                str +
                "\",\"version\":\"" +
                valueOf +
                "\"}";
        apirest.getdata(Java_AES_Cipher_key.encrypt(Data.key, Data.initVector, sb).replaceAll("\n", "")).enqueue(new Callback<DataModel>() {
            public void onResponse(Call<DataModel> call, final Response<DataModel> response) {

                String str = "Something Went wrong";
                if (response.body() == null) {


                    Log.e("bhjhjvjvjvj133314 ", "_array  " + "wor3" + response);

                    TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                    Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                } else if (response.body().getStatus().intValue() != 200) {
                    TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);

                    Toast.makeText(TiktokFragment.this.getContext(), "wor2", Toast.LENGTH_SHORT).show();
                    Log.e("bhjhjvjvjvj133314 ", "_array  " + "wor2");
                    try {
                        Toast.makeText(TiktokFragment.this.getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(TiktokFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getmp3() != null && !response.body().getmp3().equals("")) {
                    Log.e("bhjhjvjvjvj133314 ", "_array  " + "wor1");

                    try {
                        URLDriller uRLDriller = new URLDriller();
                        uRLDriller.drill(response.body().getmp3());
                        uRLDriller.setListener(new Listener() {
                            public void onURLDrillerFail(String str, Exception exc) {
                            }

                            public void onURLDrillerRedirect(String str) {
                            }

                            public void onURLDrillerStart(String str) {
                            }

                            public void onURLDrillerFinish(String str) {
                                TiktokFragment.this.download_url = str;
                                TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                                TiktokFragment.this.linear_download.setVisibility(View.VISIBLE);
                                String str2 = "Showing Ad";
                                if (!response.body().getMobile_ad().equals("reward")) {
                                    if (TiktokFragment.this.alertDialog != null) {
                                        TiktokFragment.this.alertDialog.show();
                                    } else {
                                        Builder builder = new Builder(TiktokFragment.this.getContext());
                                        builder.setMessage(str2).setCancelable(false);
                                        TiktokFragment.this.alertDialog = builder.create();
                                        TiktokFragment.this.alertDialog.show();
                                    }
                                    TiktokFragment.this.showinginterstitialad();
                                } else if (TiktokFragment.this.rewardedVideoAd.isLoaded()) {
                                    TiktokFragment.this.rewardedVideoAd.show();
                                } else {
                                    if (TiktokFragment.this.alertDialog != null) {
                                        TiktokFragment.this.alertDialog.show();
                                    } else {
                                        Builder builder2 = new Builder(TiktokFragment.this.getContext());
                                        builder2.setMessage(str2).setCancelable(false);
                                        TiktokFragment.this.alertDialog = builder2.create();
                                        TiktokFragment.this.alertDialog.show();
                                    }
                                    TiktokFragment.this.showingad();
                                }
                            }
                        });
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else {
                    TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);

                    Log.e("bhjhjvjvjvj133314 ", "_array  " + "wor7" + response.body().getMsg());
                    Toast.makeText(TiktokFragment.this.getContext(), "No Mp3 File Found", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<DataModel> call, Throwable th) {
                TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TiktokFragment.this.getContext(), "Failed. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadVideoAd() {
        MobileAds.initialize(getContext(), getString(R.string.ADM_APPID));
        this.rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        this.rewardedVideoAd.loadAd(getString(R.string.ADM_REWARDEDID), new AdRequest.Builder().build());
        this.rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            public void onRewarded(RewardItem rewardItem) {
            }

            public void onRewardedVideoAdFailedToLoad(int i) {
            }

            public void onRewardedVideoAdLeftApplication() {
            }

            public void onRewardedVideoAdLoaded() {
            }

            public void onRewardedVideoAdOpened() {
            }

            public void onRewardedVideoCompleted() {
            }

            public void onRewardedVideoStarted() {
            }

            public void onRewardedVideoAdClosed() {
                if (!TiktokFragment.this.download_url.equals("")) {
                    TiktokFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TiktokFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TiktokFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);
                    } else {
                        new DownloadMp3FileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);
                    }
                } else {
                    TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                    Toast.makeText(TiktokFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        TiktokFragment.this.rewardedVideoAd.loadAd(TiktokFragment.this.getString(R.string.ADM_REWARDEDID), new AdRequest.Builder().build());
                    }
                }, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            }
        });
    }


    public void showingad() {
        this.rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        this.rewardedVideoAd.loadAd(getString(R.string.ADM_REWARDEDID), new AdRequest.Builder().build());
        this.rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            public void onRewarded(RewardItem rewardItem) {
            }

            public void onRewardedVideoAdLeftApplication() {
            }

            public void onRewardedVideoAdOpened() {
            }

            public void onRewardedVideoCompleted() {
            }

            public void onRewardedVideoStarted() {
            }

            public void onRewardedVideoAdLoaded() {
                TiktokFragment.this.alertDialog.dismiss();
                TiktokFragment.this.rewardedVideoAd.show();
            }

            public void onRewardedVideoAdClosed() {
                if (!TiktokFragment.this.download_url.equals("")) {
                    TiktokFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TiktokFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TiktokFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);


                        return;
                    }
                    new DownloadMp3FileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);
                    return;
                }
                TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TiktokFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            public void onRewardedVideoAdFailedToLoad(int i) {
                TiktokFragment.this.alertDialog.dismiss();
                if (!TiktokFragment.this.download_url.equals("")) {
                    TiktokFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TiktokFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TiktokFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);
                        return;
                    }
                    new DownloadMp3FileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);
                    return;
                }
                TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TiktokFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showinginterstitialad() {
        this.mInterstitialAd = new InterstitialAd(getContext());
        this.mInterstitialAd.setAdUnitId(getResources().getString(R.string.ADM_INTERSTITIALID));
        this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                super.onAdClosed();
                if (!TiktokFragment.this.download_url.equals("")) {
                    TiktokFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TiktokFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TiktokFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);
                        return;
                    }
                    new DownloadMp3FileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);
                    return;
                }
                TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TiktokFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                TiktokFragment.this.alertDialog.dismiss();
                if (!TiktokFragment.this.download_url.equals("")) {
                    TiktokFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TiktokFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TiktokFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);
                        return;
                    }
                    new DownloadMp3FileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TiktokFragment.this.download_url);
                    return;
                }
                TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TiktokFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            public void onAdOpened() {
                super.onAdOpened();
            }

            public void onAdLoaded() {
                super.onAdLoaded();
                TiktokFragment.this.alertDialog.dismiss();
                TiktokFragment.this.mInterstitialAd.show();
            }

            public void onAdClicked() {
                super.onAdClicked();
            }

            public void onAdImpression() {
                super.onAdImpression();
            }
        });
    }

    private void setdialog() {
        Builder builder = new Builder(getContext());
        builder.setMessage("Showing Ad").setCancelable(false);
        this.alertDialog = builder.create();
    }

    class DownloadFileFromURL extends AsyncTask<Object, String, String> {
        private String old = "-100";
        private boolean runing = true;

        DownloadFileFromURL() {
        }


        public void onPreExecute() {
            super.onPreExecute();
            setDownloading(true);
            setProgressValue(0);
            Log.v("prepost", "ok");
        }

        public boolean dir_exists(String str) {
            File file = new File(str);
            return file.exists() && file.isDirectory();
        }


        public void onCancelled() {
            super.onCancelled();
            this.runing = false;
        }


        public String doInBackground(Object... objArr) {
            String str = "";
            String str2 = ".";

            Log.v("prepost112232323", "ok " + objArr[0].toString());

            // downloadFile.Downloading(getActivity(), objArr[0].toString(), System.currentTimeMillis() + "tiktok", ".mp4");


//            try {
//                URL url = new URL(objArr[0].toString());
//                String str3 = "mp4";
//                StringBuilder sb = new StringBuilder();
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    sb.append("TikTokDownload_");
//                }else {
//                    sb.append(Constants.ANDROID_10_IDENTIFER);
//                }
//                sb.append(System.currentTimeMillis());
//                URLConnection openConnection = url.openConnection();
//                openConnection.setRequestProperty("Accept-Encoding", "identity");
//                openConnection.connect();
//                int contentLength = openConnection.getContentLength();
//                String sb2 = contentLength +
//                        str;
//                Log.e("lenghtOfFile", sb2);
//                BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream(), 8192);
//                String sb4 ="";
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    sb4 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+File.separator;;
//                }else {
//                    sb4 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
//                            File.separator +
//                            TiktokFragment.this.getString(R.string.app_name) +
//                            File.separator +
//                            TiktokFragment.this.getString(R.string.video) +
//                            File.separator;
//                }
//
//                String str4 = "dir";
//                if (!dir_exists(sb4)) {
//                    File file = new File(sb4);
//                    if (file.mkdirs()) {
//                        Log.e(str4, "is created 1");
//                    } else {
//                        Log.e(str4, "not created 1");
//                    }
//                    if (file.mkdir()) {
//                        Log.e(str4, "is created 2");
//                    } else {
//                        Log.e(str4, "not created 2");
//                    }
//                } else {
//                    Log.e(str4, "is exist");
//                }
//                StringBuilder sb5 = new StringBuilder();
//                sb5.append(sb4);
//                sb5.append(sb.toString());
//                sb5.append(str2);
//                sb5.append(str3);
//
//                Log.e("dfjnsdjkfhjkhfjs1122", "is exist "+sb5.toString());
//
//                if (!new File(sb5.toString()).exists()) {
//                    Log.e(str4, "file is exist");
//                    String format = new SimpleDateFormat("yymmhh").format(new Date());
//                    String sb6 = MimeTypes.BASE_TYPE_VIDEO +
//                            format +
//                            ".mp4";
//
//                    String sb7 = sb4 +
//                            sb.toString() +
//                            str2 +
//                            str3;
//                    FileOutputStream fileOutputStream = new FileOutputStream(sb7);
//                    byte[] bArr = new byte[1024];
//                    long j = 0;
//                    while (true) {
//                        int read = bufferedInputStream.read(bArr);
//                        if (read == -1) {
//                            break;
//                        }
//                        String str5 = str3;
//                        long j2 = j + ((long) read);
//                        long j3 = j2;
//                        String sb8 = str +
//                                (int) ((100 * j2) / ((long) contentLength));
//                        publishProgress(sb8);
//                        fileOutputStream.write(bArr, 0, read);
//                        if (!this.runing) {
//                            Log.e("v", "not rurning");
//                        }
//                        str3 = str5;
//                        j = j3;
//                    }
//                    String str6 = str3;
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                    bufferedInputStream.close();
//                    Context context = TiktokFragment.this.getContext();
//                    StringBuilder sb9 = new StringBuilder();
//                    sb9.append(sb4);
//                    sb9.append(sb.toString());
//                    sb9.append(str2);
//                    str3 = str6;
//                    sb9.append(str3);
//                    MediaScannerConnection.scanFile(context, new String[]{sb9.toString()}, null, new OnScanCompletedListener() {
//                        public void onScanCompleted(String str, Uri uri) {
//                        }
//                    });
//                }
//                if (VERSION.SDK_INT >= 19) {
//                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//                    String sb10 = sb4 +
//                            sb.toString() +
//                            str2 +
//                            str3;
//                    intent.setData(Uri.fromFile(new File(sb10)));
//                    TiktokFragment.this.getContext().sendBroadcast(intent);
//                } else {
//                    String sb11 = "file://" +
//                            Environment.getExternalStorageDirectory();
//                    TiktokFragment.this.getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(sb11)));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return null;
        }


        public void onProgressUpdate(String... strArr) {
            try {
                if (!strArr[0].equals(this.old)) {
                    this.old = strArr[0];
                    String sb = strArr[0] +
                            "%";
                    Log.v("download", sb);
                    setDownloading(true);
                    setProgressValue(Integer.parseInt(strArr[0]));
                }
            } catch (Exception unused) {
            }
        }

        private void setDownloading(boolean z) {
            if (z) {
                TiktokFragment.this.linear_checking.setVisibility(View.GONE);
                TiktokFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                TiktokFragment.this.linear_download.setSystemUiVisibility(0);
                return;
            }
            TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
        }

        public void setProgressValue(int i) {
            TiktokFragment.this.progress_bar_activity_video.setProgress(i);
            TextView access$1200 = TiktokFragment.this.text_view_progress_activity_video;
            String sb = "Downloading " +
                    i +
                    " %";
            access$1200.setText(sb);
        }


        public void onPostExecute(String str) {
            setDownloading(false);
            Toast.makeText(TiktokFragment.this.getContext(), "Video Saved Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    class DownloadMp3FileFromURL extends AsyncTask<Object, String, String> {
        private String old = "-100";
        private boolean runing = true;

        DownloadMp3FileFromURL() {
        }


        public void onPreExecute() {
            super.onPreExecute();
            setDownloading(true);
            setProgressValue(0);
            Log.v("prepost", "ok");
        }

        public boolean dir_exists(String str) {
            File file = new File(str);
            return file.exists() && file.isDirectory();
        }


        public void onCancelled() {
            super.onCancelled();
            this.runing = false;
        }


        public String doInBackground(Object... objArr) {
            String str = "";
            String str2 = ".";
            try {
                URL url = new URL(objArr[0].toString());
                String sb = "Downloading url = " +
                        url.toString();
                Log.e("Checking", sb);
                String str3 = "mp3";
                if (url.toString().contains(str2)) {
                    str3 = url.toString().substring(url.toString().lastIndexOf(str2));
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("TikTokDownload_");
                sb2.append(System.currentTimeMillis());
                URLConnection openConnection = url.openConnection();
                openConnection.setRequestProperty("Accept-Encoding", "identity");
                openConnection.connect();
                int contentLength = openConnection.getContentLength();
                String sb3 = contentLength +
                        str;
                Log.e("lenghtOfFile", sb3);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream(), 8192);
                String sb5 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                        File.separator +
                        TiktokFragment.this.getString(R.string.app_name) +
                        File.separator +
                        TiktokFragment.this.getString(R.string.song) +
                        File.separator;
                String str4 = "dir";
                if (!dir_exists(sb5)) {
                    File file = new File(sb5);
                    if (file.mkdirs()) {
                        Log.e(str4, "is created 1");
                    } else {
                        Log.e(str4, "not created 1");
                    }
                    if (file.mkdir()) {
                        Log.e(str4, "is created 2");
                    } else {
                        Log.e(str4, "not created 2");
                    }
                } else {
                    Log.e(str4, "is exist");
                }
                StringBuilder sb6 = new StringBuilder();
                sb6.append(sb5);
                sb6.append(sb2.toString());
                sb6.append(str3);
                if (!new File(sb6.toString()).exists()) {
                    Log.e(str4, "file is exist");
                    String sb7 = sb5 +
                            sb2.toString() +
                            str3;
                    FileOutputStream fileOutputStream = new FileOutputStream(sb7);
                    byte[] bArr = new byte[1024];
                    long j = 0;
                    while (true) {
                        int read = bufferedInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        String str5 = str3;
                        long j2 = j + ((long) read);
                        long j3 = j2;
                        String sb8 = str +
                                (int) ((100 * j2) / ((long) contentLength));
                        publishProgress(sb8);
                        fileOutputStream.write(bArr, 0, read);
                        if (!this.runing) {
                            Log.e("v", "not rurning");
                        }
                        str3 = str5;
                        j = j3;
                    }
                    String str6 = str3;
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    bufferedInputStream.close();
                    Context context = TiktokFragment.this.getContext();
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(sb5);
                    sb9.append(sb2.toString());
                    str3 = str6;
                    sb9.append(str3);
                    MediaScannerConnection.scanFile(context, new String[]{sb9.toString()}, null, new OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                }
                if (VERSION.SDK_INT >= 19) {
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    String sb10 = sb5 +
                            sb2.toString() +
                            str2 +
                            str3;
                    intent.setData(Uri.fromFile(new File(sb10)));
                    TiktokFragment.this.getContext().sendBroadcast(intent);
                } else {
                    String sb11 = "file://" +
                            Environment.getExternalStorageDirectory();
                    TiktokFragment.this.getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(sb11)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        public void onProgressUpdate(String... strArr) {
            try {
                if (!strArr[0].equals(this.old)) {
                    this.old = strArr[0];
                    String sb = strArr[0] +
                            "%";
                    Log.v("download", sb);
                    setDownloading(true);
                    setProgressValue(Integer.parseInt(strArr[0]));
                }
            } catch (Exception unused) {
            }
        }

        private void setDownloading(boolean z) {
            if (z) {
                TiktokFragment.this.linear_checking.setVisibility(View.GONE);
                TiktokFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                TiktokFragment.this.linear_download.setSystemUiVisibility(0);
                return;
            }
            TiktokFragment.this.progress_main_relative.setVisibility(View.GONE);
        }

        public void setProgressValue(int i) {
            TiktokFragment.this.progress_bar_activity_video.setProgress(i);
            TextView access$1200 = TiktokFragment.this.text_view_progress_activity_video;
            String sb = "Downloading " +
                    i +
                    " %";
            access$1200.setText(sb);
        }


        public void onPostExecute(String str) {
            setDownloading(false);
            Toast.makeText(TiktokFragment.this.getContext(), "Video Saved Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
