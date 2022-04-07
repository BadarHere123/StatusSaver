package com.infusiblecoder.myvideodownloaderv2.utils;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.infusiblecoder.myvideodownloaderv2.R;
import com.infusiblecoder.myvideodownloaderv2.model.TikTokNoWaterMarkApi;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.WINDOW_SERVICE;
import static com.infusiblecoder.myvideodownloaderv2.utils.Constants.TikTokApi;

public class downloadVideo {


    public static Context Mcontext;
    public static ProgressDialog pd;
    public static Dialog dialog;
    public static SharedPreferences prefs;
    public static Boolean fromService;


    static WindowManager windowManager2;
    static WindowManager.LayoutParams params;


    public static void Start(final Context context, String url, Boolean service) {

        Mcontext = context;
        fromService = service;
        Log.i("LOGClipboard111111 clip", "work 2");
//SessionID=title;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        if (!fromService) {
            pd = new ProgressDialog(context);
            pd.setMessage("genarating download link");
            pd.setCancelable(false);
            pd.show();
        }
        if (url.contains("tiktok.com")) {


            String myurlis = url;
            final Dialog dialog = new Dialog(Mcontext);


            windowManager2 = (WindowManager) Mcontext.getSystemService(WINDOW_SERVICE);

            int size = 0;

            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) Mcontext).getWindowManager()
                        .getDefaultDisplay()
                        .getMetrics(displayMetrics);

                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                size = width / 2;

            } catch (Exception e) {
                size = WindowManager.LayoutParams.WRAP_CONTENT;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(
                        size,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                params.x = 0;
                params.y = 100;
            } else {
                params = new WindowManager.LayoutParams(
                        size,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                params.x = 0;
                params.y = 100;
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
            }
            dialog.getWindow().setAttributes(params);


            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.mp3dialog);
            dialog.findViewById(R.id.txt_with).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    getAllData(myurlis, "true");
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.txt_without).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    getAllData(myurlis, "false");
                    dialog.dismiss();
                }
            });
            dialog.show();

        }

    }


    public static void getAllData(String url, String watermark) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, TikTokApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("rescccccccc " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getString("status").equals("success")) {

                        Gson gson = new Gson();
                        TikTokNoWaterMarkApi tikTokNoWaterMarkApidata = gson.fromJson(jsonObject.toString(), TikTokNoWaterMarkApi.class);

                        System.out.println("resccccccccdataFull_Vide " + tikTokNoWaterMarkApidata.video_full_title);
                        System.out.println("resccccccccdataORG " + tikTokNoWaterMarkApidata.ogvideourl);

                        if (watermark.equals("true")) {
                            new downloadFile().Downloading(Mcontext, tikTokNoWaterMarkApidata.ogvideourl, tikTokNoWaterMarkApidata.video_full_title + "_" + tikTokNoWaterMarkApidata.username, ".mp4");

                            if (!fromService) {
                                pd.dismiss();

                                Handler mHandler = new Handler(Looper.getMainLooper()) {
                                    @Override
                                    public void handleMessage(Message message) {
                                        Toast.makeText(Mcontext, "Starting Download", Toast.LENGTH_SHORT).show();

                                    }
                                };
                            }

                        } else if (watermark.equals("false") && tikTokNoWaterMarkApidata.watermark_removed.equals("yes")) {


                            new downloadFile().Downloading(Mcontext, tikTokNoWaterMarkApidata.videourl, tikTokNoWaterMarkApidata.video_full_title + "_" + tikTokNoWaterMarkApidata.username, ".mp4");
                            //   new DownloadFileFromURL(Mcontext,tikTokNoWaterMarkApidata.name).execute(tikTokNoWaterMarkApidata.videourl);


                            if (!fromService) {
                                pd.dismiss();

                                Handler mHandler = new Handler(Looper.getMainLooper()) {
                                    @Override
                                    public void handleMessage(Message message) {
                                        Toast.makeText(Mcontext, "Starting Download", Toast.LENGTH_SHORT).show();

                                    }
                                };
                            }

                        }


                    } else {
                        if (!fromService) {
                            pd.dismiss();

                            Handler mHandler = new Handler(Looper.getMainLooper()) {
                                @Override
                                public void handleMessage(Message message) {
                                    Toast.makeText(Mcontext, "Some Thing Went Wrong", Toast.LENGTH_SHORT).show();


                                }
                            };
                        }
                    }


                } catch (Exception e) {
                    if (!fromService) {
                        pd.dismiss();

                        Handler mHandler = new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message message) {
                                Toast.makeText(Mcontext, "Some Thing Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        };
                    }
                    System.out.println("i ah error " + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("i ah error " + error.getMessage());

                if (!fromService) {
                    pd.dismiss();

                    Handler mHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            Toast.makeText(Mcontext, "Some Thing Went Wrong", Toast.LENGTH_SHORT).show();

                        }
                    };
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();
                params.put("tikurl", url);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VollySingltonClass.getmInstance(Mcontext).addToRequsetque(stringRequest);


    }


}
