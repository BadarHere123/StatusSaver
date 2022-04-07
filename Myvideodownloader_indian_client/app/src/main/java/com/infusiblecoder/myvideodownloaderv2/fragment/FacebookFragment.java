package com.infusiblecoder.myvideodownloaderv2.fragment;

import android.content.ActivityNotFoundException;
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
import android.text.TextUtils;
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
import com.infusiblecoder.myvideodownloaderv2.model.ModelFacebook;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FacebookFragment extends Fragment {
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

    public FacebookFragment() {
        String str = "";
        this.url = str;
        this.download_url = str;
    }

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

    public static List<String> extractUrls(String str) {
        ArrayList arrayList = new ArrayList();
        Matcher matcher = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", Pattern.CASE_INSENSITIVE).matcher(str);
        while (matcher.find()) {
            arrayList.add(str.substring(matcher.start(0), matcher.end(0)));
        }
        return arrayList;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_facebook, viewGroup, false);
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
                FacebookFragment facebookFragment = FacebookFragment.this;
                facebookFragment.url = facebookFragment.txt_url.getText().toString();
                if (FacebookFragment.this.url.equals("")) {
                    Toast.makeText(FacebookFragment.this.getContext(), "Please enter video link from TikTok", Toast.LENGTH_SHORT).show();
                    return;
                }
                String str = "Invalid Link";
                if (FacebookFragment.this.url.length() < 10) {
                    Toast.makeText(FacebookFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    return;
                }
                List extractUrls = FacebookFragment.extractUrls(FacebookFragment.this.url);
                if (extractUrls == null || extractUrls.size() <= 0) {
                    Toast.makeText(FacebookFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    return;
                }
                FacebookFragment.this.url = (String) extractUrls.get(0);
                if (FacebookFragment.this.url.length() > 15) {
                    FacebookFragment.this.position = 1;
                    FacebookFragment facebookFragment2 = FacebookFragment.this;
                    facebookFragment2.getdata(facebookFragment2.url, 1);
                } else {
                    Toast.makeText(FacebookFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                }
                StringBuilder sb = new StringBuilder();
                sb.append(" url length = ");
                sb.append(FacebookFragment.this.url.length());
                Log.e("Checking", sb.toString());
            }
        });
        this.btn_paste.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Item itemAt = ((ClipboardManager) FacebookFragment.this.getContext().getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip().getItemAt(0);
                if (itemAt != null) {
                    String charSequence = itemAt.getText().toString();
                    if (!charSequence.equals("")) {
                        FacebookFragment.this.txt_url.setText(charSequence);
                    }
                }
            }
        });
        this.btn_mydownloads.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FacebookFragment facebookFragment = FacebookFragment.this;
                facebookFragment.startActivity(new Intent(facebookFragment.getContext(), DownloadActivity.class));
            }
        });
        this.progress_main_relative.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
    }

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

            } catch (ActivityNotFoundException unused) {
                Toast.makeText(getActivity(), "App not installed.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }


    }

    public void getdata(String str, int i) {
        this.progress_main_relative.setVisibility(View.VISIBLE);
        this.linear_download.setVisibility(View.GONE);
        this.linear_checking.setVisibility(View.VISIBLE);
        Retrofit client = apiClient.getClient();
        StringBuilder sb = new StringBuilder();
        sb.append("url = ");
        sb.append(str);
        Log.e("Checking", sb.toString());
        apiRest apirest = client.create(apiRest.class);
        String valueOf = String.valueOf(4);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("pass url = ");
        sb2.append(str);
        Log.d("HomeActivity", sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("{\"tiktok_url\":\"");
        sb3.append(str);
        sb3.append("\",\"version\":\"");
        sb3.append(valueOf);
        sb3.append("\"}");
        Java_AES_Cipher_key.encrypt(Data.key, Data.initVector, sb3.toString()).replaceAll("\n", "");
        apirest.getfacebook(str, valueOf).enqueue(new Callback<ModelFacebook>() {
            public void onResponse(Call<ModelFacebook> call, final Response<ModelFacebook> response) {
                String str = "Something Went wrong";
                if (response.body() == null) {
                    FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                    Toast.makeText(FacebookFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                } else if (response.body().getStatus().intValue() != 200) {
                    FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                    try {
                        Toast.makeText(FacebookFragment.this.getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(FacebookFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
                    }
                } else if (!response.body().getOriginalVideo().equals("")) {
                    String str2 = "Checking";
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("response url = ");
                        sb.append(response.body().getOriginalVideo());
                        Log.e(str2, sb.toString());
                        URLDriller uRLDriller = new URLDriller();
                        uRLDriller.drill(response.body().getOriginalVideo());
                        uRLDriller.setListener(new Listener() {
                            public void onURLDrillerRedirect(String str) {
                            }

                            public void onURLDrillerStart(String str) {
                            }

                            public void onURLDrillerFinish(String str) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("drilled url = ");
                                sb.append(response.body().getOriginalVideo());
                                Log.e("Checking", sb.toString());
                                FacebookFragment.this.download_url = str;
                                FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                                FacebookFragment.this.linear_download.setVisibility(View.VISIBLE);
                                String str2 = "Showing Ad";
                                if (!response.body().getMobileAd().equals("reward")) {
                                    if (FacebookFragment.this.alertDialog != null) {
                                        FacebookFragment.this.alertDialog.show();
                                    } else {
                                        Builder builder = new Builder(FacebookFragment.this.getContext());
                                        builder.setMessage(str2).setCancelable(false);
                                        FacebookFragment.this.alertDialog = builder.create();
                                        FacebookFragment.this.alertDialog.show();
                                    }
                                    FacebookFragment.this.showinginterstitialad();
                                } else if (FacebookFragment.this.rewardedVideoAd.isLoaded()) {
                                    FacebookFragment.this.rewardedVideoAd.show();
                                } else {
                                    if (FacebookFragment.this.alertDialog != null) {
                                        FacebookFragment.this.alertDialog.show();
                                    } else {
                                        Builder builder2 = new Builder(FacebookFragment.this.getContext());
                                        builder2.setMessage(str2).setCancelable(false);
                                        FacebookFragment.this.alertDialog = builder2.create();
                                        FacebookFragment.this.alertDialog.show();
                                    }
                                    FacebookFragment.this.showingad();
                                }
                            }

                            public void onURLDrillerFail(String str, Exception exc) {
                                FacebookFragment.this.download_url = response.body().getOriginalVideo();
                                FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                                FacebookFragment.this.linear_download.setVisibility(View.VISIBLE);
                                String str2 = "Showing Ad";
                                if (!response.body().getMobileAd().equals("reward")) {
                                    if (FacebookFragment.this.alertDialog != null) {
                                        FacebookFragment.this.alertDialog.show();
                                    } else {
                                        Builder builder = new Builder(FacebookFragment.this.getContext());
                                        builder.setMessage(str2).setCancelable(false);
                                        FacebookFragment.this.alertDialog = builder.create();
                                        FacebookFragment.this.alertDialog.show();
                                    }
                                    FacebookFragment.this.showinginterstitialad();
                                } else if (FacebookFragment.this.rewardedVideoAd.isLoaded()) {
                                    FacebookFragment.this.rewardedVideoAd.show();
                                } else {
                                    if (FacebookFragment.this.alertDialog != null) {
                                        FacebookFragment.this.alertDialog.show();
                                    } else {
                                        Builder builder2 = new Builder(FacebookFragment.this.getContext());
                                        builder2.setMessage(str2).setCancelable(false);
                                        FacebookFragment.this.alertDialog = builder2.create();
                                        FacebookFragment.this.alertDialog.show();
                                    }
                                    FacebookFragment.this.showingad();
                                }
                            }
                        });
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else {
                    FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                    Toast.makeText(FacebookFragment.this.getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<ModelFacebook> call, Throwable th) {
                FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(FacebookFragment.this.getContext(), "Failed. Please try again", Toast.LENGTH_SHORT).show();
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
                if (!FacebookFragment.this.download_url.equals("")) {
                    FacebookFragment.this.linear_download.setVisibility(View.VISIBLE);
                    FacebookFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, FacebookFragment.this.download_url);
                } else {
                    FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                    Toast.makeText(FacebookFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        FacebookFragment.this.rewardedVideoAd.loadAd(FacebookFragment.this.getString(R.string.ADM_REWARDEDID), new AdRequest.Builder().build());
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
                FacebookFragment.this.alertDialog.dismiss();
                FacebookFragment.this.rewardedVideoAd.show();
            }

            public void onRewardedVideoAdClosed() {
                if (!FacebookFragment.this.download_url.equals("")) {
                    FacebookFragment.this.linear_download.setVisibility(View.VISIBLE);
                    FacebookFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, FacebookFragment.this.download_url);
                    return;
                }
                FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(FacebookFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            public void onRewardedVideoAdFailedToLoad(int i) {
                FacebookFragment.this.alertDialog.dismiss();
                if (!FacebookFragment.this.download_url.equals("")) {
                    FacebookFragment.this.linear_download.setVisibility(View.VISIBLE);
                    FacebookFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, FacebookFragment.this.download_url);
                    return;
                }
                FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(FacebookFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
                if (!FacebookFragment.this.download_url.equals("")) {
                    FacebookFragment.this.linear_download.setVisibility(View.VISIBLE);
                    FacebookFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, FacebookFragment.this.download_url);
                    return;
                }
                FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(FacebookFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                FacebookFragment.this.alertDialog.dismiss();
                if (!FacebookFragment.this.download_url.equals("")) {
                    FacebookFragment.this.linear_download.setVisibility(View.VISIBLE);
                    FacebookFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                    new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, FacebookFragment.this.download_url);
                    return;
                }
                FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
                Toast.makeText(FacebookFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            public void onAdOpened() {
                super.onAdOpened();
            }

            public void onAdLoaded() {
                super.onAdLoaded();
                FacebookFragment.this.alertDialog.dismiss();
                FacebookFragment.this.mInterstitialAd.show();
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

        @Override
        protected String doInBackground(Object... objects) {

            String str = "";
            String str2 = ".";
            try {
                URL url = new URL(objects[0].toString());
                String sb = "Downloading url = " +
                        url.toString();
                Log.e("Checking", sb);
                String str3 = "mp4";
                StringBuilder sb2 = new StringBuilder();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    sb2.append("FacebokVideo_");
                } else {
                    sb2.append(Constants.ANDROID_10_IDENTIFER);
                }
                sb2.append(System.currentTimeMillis());
                URLConnection openConnection = url.openConnection();
                openConnection.setRequestProperty("Accept-Encoding", "identity");
                openConnection.connect();
                int contentLength = openConnection.getContentLength();
                String sb3 = contentLength +
                        str;
                Log.e("lenghtOfFile", sb3);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream(), 8192);

//                String sb5 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
//                        File.separator +
//                        FacebookFragment.this.getString(R.string.app_name) +
//                        File.separator +
//                        FacebookFragment.this.getString(R.string.video) +
//                        File.separator;
                String sb5 = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    sb5 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                } else {
                    sb5 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                            File.separator +
                            FacebookFragment.this.getString(R.string.app_name) +
                            File.separator +
                            FacebookFragment.this.getString(R.string.video) +
                            File.separator;
                }
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
                sb6.append(str2);
                sb6.append(str3);
                if (!new File(sb6.toString()).exists()) {
                    Log.e(str4, "file is exist");
                    String format = new SimpleDateFormat("yymmhh").format(new Date());
                    String sb7 = MimeTypes.BASE_TYPE_VIDEO +
                            format +
                            ".mp4";

                    String sb8 = sb5 +
                            sb2.toString() +
                            str2 +
                            str3;
                    FileOutputStream fileOutputStream = new FileOutputStream(sb8);
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
                        String sb9 = str +
                                (int) ((100 * j2) / ((long) contentLength));
                        publishProgress(sb9);
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
                    Context context = FacebookFragment.this.getContext();
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(sb5);
                    sb10.append(sb2.toString());
                    sb10.append(str2);
                    str3 = str6;
                    sb10.append(str3);
                    MediaScannerConnection.scanFile(context, new String[]{sb10.toString()}, null, new OnScanCompletedListener() {
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                }
                if (VERSION.SDK_INT >= 19) {
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    String sb11 = sb5 +
                            sb2.toString() +
                            str2 +
                            str3;
                    intent.setData(Uri.fromFile(new File(sb11)));
                    FacebookFragment.this.getContext().sendBroadcast(intent);
                } else {
                    String sb12 = "file://" +
                            Environment.getExternalStorageDirectory();
                    FacebookFragment.this.getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(sb12)));
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
                FacebookFragment.this.linear_checking.setVisibility(View.GONE);
                FacebookFragment.this.progress_main_relative.setVisibility(View.VISIBLE);
                FacebookFragment.this.linear_download.setSystemUiVisibility(0);
                return;
            }
            FacebookFragment.this.progress_main_relative.setVisibility(View.GONE);
        }

        public void setProgressValue(int i) {
            FacebookFragment.this.progress_bar_activity_video.setProgress(i);
            TextView access$1200 = FacebookFragment.this.text_view_progress_activity_video;
            String sb = "Downloading " +
                    i +
                    " %";
            access$1200.setText(sb);
        }

        public void onPostExecute(String str) {
            setDownloading(false);
            Toast.makeText(FacebookFragment.this.getContext(), "Video Saved Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
