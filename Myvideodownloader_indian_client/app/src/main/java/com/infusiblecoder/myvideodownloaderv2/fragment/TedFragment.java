package com.infusiblecoder.myvideodownloaderv2.fragment;

import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.infusiblecoder.myvideodownloaderv2.R;
import com.infusiblecoder.myvideodownloaderv2.model.ModelTed;
import com.infusiblecoder.myvideodownloaderv2.ui.DownloadActivity;
import com.infusiblecoder.myvideodownloaderv2.utils.Constants;
import com.infusiblecoder.myvideodownloaderv2.utils.Data;
import com.infusiblecoder.myvideodownloaderv2.utils.Java_AES_Cipher_key;
import com.infusiblecoder.myvideodownloaderv2.utils.apiClient;
import com.infusiblecoder.myvideodownloaderv2.utils.apiRest;

import net.pubnative.URLDriller;
import net.pubnative.URLDriller.Listener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TedFragment extends Fragment {
    public AlertDialog alertDialog;
    public String download_url;
    public LinearLayout linear_checking;
    public LinearLayout linear_download;
    public int position = 0;
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

    public TedFragment() {
        String str = "";
        this.url = str;
        this.download_url = str;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_ted, viewGroup, false);
        initview();
        setdialog();
        return this.view;
    }

    private void initview() {
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
                TedFragment tedFragment = TedFragment.this;
                tedFragment.url = tedFragment.txt_url.getText().toString();
                if (TedFragment.this.url.equals("")) {
                    Toast.makeText(TedFragment.this.getContext(), "Please enter video link from TikTok", Toast.LENGTH_SHORT).show();
                } else if (TedFragment.this.url.length() < 10) {
                    Toast.makeText(TedFragment.this.getContext(), "Invalid Link", Toast.LENGTH_SHORT).show();
                } else {
                    TedFragment tedFragment2 = TedFragment.this;
                    tedFragment2.getdata(tedFragment2.url, 1);
                }
            }
        });
        this.btn_paste.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Item itemAt = ((ClipboardManager) TedFragment.this.getContext().getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip().getItemAt(0);
                if (itemAt != null) {
                    String charSequence = itemAt.getText().toString();
                    if (!charSequence.equals("")) {
                        TedFragment.this.txt_url.setText(charSequence);
                    }
                }
            }
        });
        this.btn_mydownloads.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TedFragment tedFragment = TedFragment.this;
                tedFragment.startActivity(new Intent(tedFragment.getContext(), DownloadActivity.class));
            }
        });
        this.progress_main_relative.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
    }

    public void getdata(String str, int i) {
        this.progress_main_relative.setVisibility(View.VISIBLE);
        this.linear_download.setVisibility(View.GONE);
        this.linear_checking.setVisibility(View.VISIBLE);
        Retrofit client = apiClient.getClient();
        String sb = "url = " +
                str;
        Log.e("Checking", sb);
        apiRest apirest = client.create(apiRest.class);
        String valueOf = String.valueOf(4);
        String sb2 = "pass url = " +
                str;
        Log.d("HomeActivity", sb2);
        String sb3 = "{\"tiktok_url\":\"" +
                str +
                "\",\"version\":\"" +
                valueOf +
                "\"}";
        Java_AES_Cipher_key.encrypt(Data.key, Data.initVector, sb3).replaceAll("\n", "");
        apirest.getted(str, valueOf).enqueue(new Callback<ModelTed>() {
            public void onResponse(Call<ModelTed> call, final Response<ModelTed> response) {
                String str = "Something Went wrong";
                if (response.body() == null) {
                    TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                    Toast.makeText(TedFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                } else if (response.body().getStatus().intValue() != 200) {
                    TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                    try {
                        Toast.makeText(TedFragment.this.getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(TedFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    }
                } else if (!response.body().getOriginalVideo().equals("")) {
                    try {
                        if (response.body().getType().equals("mp4")) {
                            TedFragment.this.position = 1;
                        } else if (response.body().getType().equals("jpg")) {
                            TedFragment.this.position = 2;
                        } else {
                            Toast.makeText(TedFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                        }
                        URLDriller uRLDriller = new URLDriller();
                        uRLDriller.drill(response.body().getOriginalVideo());
                        uRLDriller.setListener(new Listener() {
                            public void onURLDrillerRedirect(String str) {
                            }

                            public void onURLDrillerStart(String str) {
                            }

                            public void onURLDrillerFinish(String str) {
                                if (TedFragment.this.position != 0) {
                                    TedFragment.this.download_url = str;
                                    TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                                    TedFragment.this.linear_download.setVisibility(View.VISIBLE);
                                    String str2 = "Showing Ad";
                                    if (!response.body().getMobileAd().equals("reward")) {
                                        if (TedFragment.this.alertDialog != null) {
                                            TedFragment.this.alertDialog.show();
                                        } else {
                                            Builder builder = new Builder(TedFragment.this.getContext());
                                            builder.setMessage(str2).setCancelable(false);
                                            TedFragment.this.alertDialog = builder.create();
                                            TedFragment.this.alertDialog.show();
                                        }
                                        TedFragment.this.showinginterstitialad();
                                    } else if (TedFragment.this.rewardedVideoAd.isLoaded()) {
                                        TedFragment.this.rewardedVideoAd.show();
                                    } else {
                                        if (TedFragment.this.alertDialog != null) {
                                            TedFragment.this.alertDialog.show();
                                        } else {
                                            Builder builder2 = new Builder(TedFragment.this.getContext());
                                            builder2.setMessage(str2).setCancelable(false);
                                            TedFragment.this.alertDialog = builder2.create();
                                            TedFragment.this.alertDialog.show();
                                        }
                                        TedFragment.this.showingad();
                                    }
                                }
                            }

                            public void onURLDrillerFail(String str, Exception exc) {
                                if (TedFragment.this.position != 0) {
                                    TedFragment.this.download_url = response.body().getOriginalVideo();
                                    TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                                    TedFragment.this.linear_download.setVisibility(View.VISIBLE);
                                    String str2 = "Showing Ad";
                                    if (!response.body().getMobileAd().equals("reward")) {
                                        if (TedFragment.this.alertDialog != null) {
                                            TedFragment.this.alertDialog.show();
                                        } else {
                                            Builder builder = new Builder(TedFragment.this.getContext());
                                            builder.setMessage(str2).setCancelable(false);
                                            TedFragment.this.alertDialog = builder.create();
                                            TedFragment.this.alertDialog.show();
                                        }
                                        TedFragment.this.showinginterstitialad();
                                    } else if (TedFragment.this.rewardedVideoAd.isLoaded()) {
                                        TedFragment.this.rewardedVideoAd.show();
                                    } else {
                                        if (TedFragment.this.alertDialog != null) {
                                            TedFragment.this.alertDialog.show();
                                        } else {
                                            Builder builder2 = new Builder(TedFragment.this.getContext());
                                            builder2.setMessage(str2).setCancelable(false);
                                            TedFragment.this.alertDialog = builder2.create();
                                            TedFragment.this.alertDialog.show();
                                        }
                                        TedFragment.this.showingad();
                                    }
                                }
                            }
                        });
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else {
                    TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                    Toast.makeText(TedFragment.this.getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<ModelTed> call, Throwable th) {
                TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TedFragment.this.getContext(), "Failed. Please try again", Toast.LENGTH_SHORT).show();
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
                if (!TedFragment.this.download_url.equals("")) {
                    TedFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TedFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TedFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                    } else {
                        new DownloadImageFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                    }
                } else {
                    TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                    Toast.makeText(TedFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        TedFragment.this.rewardedVideoAd.loadAd(TedFragment.this.getString(R.string.ADM_REWARDEDID), new AdRequest.Builder().build());
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
                TedFragment.this.alertDialog.dismiss();
                TedFragment.this.rewardedVideoAd.show();
            }

            public void onRewardedVideoAdClosed() {
                if (!TedFragment.this.download_url.equals("")) {
                    TedFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TedFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TedFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                        return;
                    }
                    new DownloadImageFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                    return;
                }
                TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TedFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            public void onRewardedVideoAdFailedToLoad(int i) {
                TedFragment.this.alertDialog.dismiss();
                if (!TedFragment.this.download_url.equals("")) {
                    TedFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TedFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TedFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                        return;
                    }
                    new DownloadImageFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                    return;
                }
                TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TedFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
                if (!TedFragment.this.download_url.equals("")) {
                    TedFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TedFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TedFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                        return;
                    }
                    new DownloadImageFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                    return;
                }
                TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TedFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                TedFragment.this.alertDialog.dismiss();
                if (!TedFragment.this.download_url.equals("")) {
                    TedFragment.this.linear_download.setVisibility(View.VISIBLE);
                    TedFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    if (TedFragment.this.position == 1) {
                        new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                        return;
                    }
                    new DownloadImageFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TedFragment.this.download_url);
                    return;
                }
                TedFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(TedFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            public void onAdOpened() {
                super.onAdOpened();
            }

            public void onAdLoaded() {
                super.onAdLoaded();
                TedFragment.this.alertDialog.dismiss();
                TedFragment.this.mInterstitialAd.show();
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
            Log.e("Checking", "download errro = cancelled");
            this.runing = false;
        }

        public String doInBackground(Object... objArr) {
            String str;
            String str2;
            String str3 = "";
            String str4 = "Checking";
            String str5 = ".";
            try {
                URL url = new URL(objArr[0].toString());
                String sb = "Downloading video url = " +
                        url.toString();
                Log.e(str4, sb);
                String str6 = "mp4";
                StringBuilder sb2 = new StringBuilder();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    sb2.append("TedVideo_");
                } else {
                    sb2.append(Constants.ANDROID_10_IDENTIFER);
                }
                sb2.append(System.currentTimeMillis());
                URLConnection openConnection = url.openConnection();
                openConnection.setRequestProperty("Accept-Encoding", "identity");
                openConnection.connect();
                int contentLength = openConnection.getContentLength();
                String sb3 = contentLength +
                        str3;
                Log.e("lenghtOfFile", sb3);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream(), 8192);
                String sb5 = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    sb5 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                } else {
                    sb5 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                            File.separator +
                            TedFragment.this.getString(R.string.app_name) +
                            File.separator +
                            TedFragment.this.getString(R.string.video) +
                            File.separator;
                }
                String str7 = "dir";
                if (!dir_exists(sb5)) {
                    File file = new File(sb5);
                    if (file.mkdirs()) {
                        Log.e(str7, "is created 1");
                    } else {
                        Log.e(str7, "not created 1");
                    }
                    if (file.mkdir()) {
                        Log.e(str7, "is created 2");
                    } else {
                        Log.e(str7, "not created 2");
                    }
                } else {
                    Log.e(str7, "is exist");
                }
                StringBuilder sb6 = new StringBuilder();
                sb6.append(sb5);
                sb6.append(sb2.toString());
                sb6.append(str5);
                sb6.append(str6);
                if (!new File(sb6.toString()).exists()) {
                    Log.e(str7, "file is exist");
                    String format = new SimpleDateFormat("yymmhh").format(new Date());
                    String sb7 = MimeTypes.BASE_TYPE_VIDEO +
                            format +
                            ".mp4";

                    String sb8 = sb5 +
                            sb2.toString() +
                            str5 +
                            str6;
                    FileOutputStream fileOutputStream = new FileOutputStream(sb8);
                    byte[] bArr = new byte[1024];
                    long j = 0;
                    while (true) {
                        int read = bufferedInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        str = str4;
                        String str8 = str5;
                        long j2 = j + ((long) read);
                        try {
                            long j3 = j2;
                            String sb9 = str3 +
                                    (int) ((100 * j2) / ((long) contentLength));
                            publishProgress(sb9);
                            fileOutputStream.write(bArr, 0, read);
                            if (!this.runing) {
                                Log.e("v", "not rurning");
                            }
                            str4 = str;
                            str5 = str8;
                            j = j3;
                        } catch (Exception e) {
                            e = e;
                            e.printStackTrace();
                            String sb10 = "download errro = " +
                                    e.getMessage();
                            Log.e(str, sb10);
                            return null;
                        }
                    }
                    String str9 = str4;
                    String str10 = str5;
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    bufferedInputStream.close();
                    Context context = TedFragment.this.getContext();
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append(sb5);
                    sb11.append(sb2.toString());
                    str2 = str10;
                    sb11.append(str2);
                    sb11.append(str6);
                    MediaScannerConnection.scanFile(context, new String[]{sb11.toString()}, null, new OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                } else {
                    String str11 = str4;
                    str2 = str5;
                }
                if (VERSION.SDK_INT >= 19) {
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    String sb12 = sb5 +
                            sb2.toString() +
                            str2 +
                            str6;
                    intent.setData(Uri.fromFile(new File(sb12)));
                    TedFragment.this.getContext().sendBroadcast(intent);
                } else {
                    String sb13 = "file://" +
                            Environment.getExternalStorageDirectory();
                    TedFragment.this.getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(sb13)));
                }
            } catch (Exception e2) {
                Exception e = e2;
                str = str4;
                e.printStackTrace();
                String sb102 = "download errro = " +
                        e.getMessage();
                Log.e(str, sb102);
                return null;
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
                TedFragment.this.linear_checking.setVisibility(View.GONE);
                TedFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                TedFragment.this.linear_download.setSystemUiVisibility(0);
                return;
            }
            TedFragment.this.progress_main_relative.setVisibility(View.GONE);
        }

        public void setProgressValue(int i) {
            TedFragment.this.progress_bar_activity_video.setProgress(i);
            TextView access$1200 = TedFragment.this.text_view_progress_activity_video;
            String sb = "Downloading " +
                    i +
                    " %";
            access$1200.setText(sb);
        }

        public void onPostExecute(String str) {
            setDownloading(false);
            Toast.makeText(TedFragment.this.getContext(), "Video Saved Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    class DownloadImageFileFromURL extends AsyncTask<Object, String, String> {
        private String old = "-100";
        private boolean runing = true;

        DownloadImageFileFromURL() {
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
            String str;
            String str2 = "";
            try {
                URL url = new URL(objArr[0].toString());
                String sb = "Downloading image url = " +
                        url.toString();
                Log.e("Checking", sb);
                String str3 = ".jpeg";
                StringBuilder sb2 = new StringBuilder();
                sb2.append("InstagramImage_");
                sb2.append(System.currentTimeMillis());
                URLConnection openConnection = url.openConnection();
                openConnection.setRequestProperty("Accept-Encoding", "identity");
                openConnection.connect();
                int contentLength = openConnection.getContentLength();
                String sb3 = contentLength +
                        str2;
                Log.e("lenghtOfFile", sb3);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream(), 8192);
                String sb5 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                        File.separator +
                        TedFragment.this.getString(R.string.app_name) +
                        File.separator +
                        TedFragment.this.getString(R.string.image) +
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
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(sb5);
                    sb7.append(sb2.toString());
                    sb7.append(str3);
                    FileOutputStream fileOutputStream = new FileOutputStream(sb7.toString());
                    byte[] bArr = new byte[1024];
                    long j = 0;
                    while (true) {
                        int read = bufferedInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        String str5 = sb5;
                        j += read;
                        String str6 = str3;
                        String sb8 = str2 +
                                (int) ((100 * j) / ((long) contentLength));
                        publishProgress(sb8);
                        fileOutputStream.write(bArr, 0, read);
                        if (!this.runing) {
                            Log.e("v", "not rurning");
                        }
                        sb5 = str5;
                        str3 = str6;
                    }
                    String str7 = sb5;
                    String str8 = str3;
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    bufferedInputStream.close();
                    Context context = TedFragment.this.getContext();
                    StringBuilder sb9 = new StringBuilder();
                    str = str7;
                    sb9.append(str);
                    sb9.append(sb2.toString());
                    str3 = str8;
                    sb9.append(str3);
                    MediaScannerConnection.scanFile(context, new String[]{sb9.toString()}, null, new OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                } else {
                    str = sb5;
                }
                if (VERSION.SDK_INT >= 19) {
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    String sb10 = str +
                            sb2.toString() +
                            "." +
                            str3;
                    intent.setData(Uri.fromFile(new File(sb10)));
                    TedFragment.this.getContext().sendBroadcast(intent);
                } else {
                    String sb11 = "file://" +
                            Environment.getExternalStorageDirectory();
                    TedFragment.this.getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(sb11)));
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
                TedFragment.this.linear_checking.setVisibility(View.GONE);
                TedFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                TedFragment.this.linear_download.setSystemUiVisibility(0);
                return;
            }
            TedFragment.this.progress_main_relative.setVisibility(View.GONE);
        }

        public void setProgressValue(int i) {
            TedFragment.this.progress_bar_activity_video.setProgress(i);
            TextView access$1200 = TedFragment.this.text_view_progress_activity_video;
            String sb = "Downloading " +
                    i +
                    " %";
            access$1200.setText(sb);
        }

        public void onPostExecute(String str) {
            setDownloading(false);
            Toast.makeText(TedFragment.this.getContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
