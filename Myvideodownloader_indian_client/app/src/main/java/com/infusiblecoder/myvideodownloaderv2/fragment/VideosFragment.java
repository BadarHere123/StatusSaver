package com.infusiblecoder.myvideodownloaderv2.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog.Builder;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.infusiblecoder.myvideodownloaderv2.R;
import com.infusiblecoder.myvideodownloaderv2.model.MyFileProvider;
import com.infusiblecoder.myvideodownloaderv2.ui.PlayActivity;
import com.infusiblecoder.myvideodownloaderv2.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class VideosFragment extends Fragment {
    LinearLayout adlayout;


    int i = 0;
    LinearLayout linear_progress;
    ArrayList<File> myvideos = new ArrayList<>();
    LinearLayout noT_found;
    RecyclerView recyclerview;
    boolean temp = false;
    TextView txt_loading;
    View view;
    PreviewAdapter previewAdapter;
    private Handler mHandler;

    public void setadapterr() {
        Collections.reverse(this.myvideos);
        previewAdapter = new PreviewAdapter(this.myvideos);
        this.recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.recyclerview.setItemAnimator(new DefaultItemAnimator());
        this.recyclerview.setAdapter(previewAdapter);
        this.linear_progress.setVisibility(View.GONE);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_videos, viewGroup, false);
        this.recyclerview = this.view.findViewById(R.id.recyclerview);
        this.linear_progress = this.view.findViewById(R.id.linear_progress);
        this.noT_found = this.view.findViewById(R.id.noT_found);
        new AsyncTaskRunner().execute("abc");
        return this.view;
    }

    public ArrayList<File> getlist(File file) {
        File[] listFiles;
        ArrayList<File> arrayList = new ArrayList<>();


        Log.e("bhjhjvjvjvj133310 ", "_wor22_" + file.getAbsolutePath());
        Log.e("bhjhjvjvjvj133310 ", "_len_" + file.listFiles().length);
        for (File file2 : file.listFiles()) {

            Log.e("bhjhjvjvjvj133312 ", "_ iswork_" + file2.getAbsolutePath());

            if (file2.isDirectory()) {
                Log.e("bhjhjvjvjvj133313 ", "_ is dir_ " + file2.getAbsolutePath());
                arrayList.addAll(getlist(file2));
            } else if (file2.getName().endsWith(".mp4") || file2.getName().endsWith(".mkv")) {
                Log.e("bhjhjvjvjvj133314 ", "_ isfile_" + file2.getAbsolutePath());
                arrayList.add(file2);
            }
        }
        Log.e("bhjhjvjvjvj133314 ", "_array  " + arrayList.size());

        if (arrayList.size() == 0) {
            this.noT_found.setVisibility(View.VISIBLE);
        } else {
            this.noT_found.setVisibility(View.GONE);
        }
        return arrayList;
    }

    class AsyncTaskRunner extends AsyncTask<String, String, String> {
        String resp;

        private AsyncTaskRunner() {
        }


        public String doInBackground(String... strArr) {
            Log.e("bhjhjvjvjvj133312 ", "_ 2");


            getActivity().runOnUiThread(new Runnable() {


                @Override
                public void run() {
                    try {
                        String sb = "";

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            sb = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator;
                        } else {
                            sb = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                                    File.separator +
                                    VideosFragment.this.getString(R.string.app_name) +
                                    File.separator +
                                    VideosFragment.this.getString(R.string.video) +
                                    File.separator;
                        }
                        Log.e("bhjhjvjvjvj133312 ", "" + sb);


                        File file = new File(sb);
                        if (!file.exists()) {
                            file.mkdir();
                        }

                        Log.e("bhjhjvjvjvj133312sixgg  ", "" + getlist(file).size());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                            myvideos = new ArrayList<>();
                            ArrayList<File> unsortedlist = getlist(file);

                            for (int i = 0; i < unsortedlist.size(); i++) {


                                Log.e("bhjhjvjvjvj133312 ", "" + unsortedlist.get(i).getAbsolutePath());
                                if (unsortedlist.get(i).getAbsolutePath().contains(Constants.ANDROID_10_IDENTIFER)) {

                                    Log.e("bhjhjvjvjvj133312 ", "" + unsortedlist.get(i).getAbsolutePath());
                                    myvideos.add(unsortedlist.get(i));
                                }
                            }
                        } else {
                        }
                        myvideos = getlist(file);

                        Log.e("bhjhjvjvjvj133312 ", "_ 52" + myvideos.size());

                    } catch (Exception e) {

                        Log.e("bhjhjvjvjvj133312 ", "_ 51111 " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.postDelayed(new Runnable() {
//
//
//                @Override
//                public void run() {
//                    try {
//                        String sb = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
//                                File.separator +
//                                VideosFragment.this.getString(R.string.app_name) +
//                                File.separator +
//                                VideosFragment.this.getString(R.string.video) +
//                                File.separator;
//                        File file = new File(sb);
//                        if (!file.exists()) {
//                            file.mkdir();
//                        }
//                        VideosFragment.this.myvideos = VideosFragment.this.getlist(file);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        resp = e.getMessage();
//                        Log.e("bhjhjvjvjvj133312 ", "_ 5" + e.getMessage());
//
//                    }
//                }
//            },1000);


            return "abc";
        }


        public void onPostExecute(String str) {
            VideosFragment.this.setadapterr();
        }


        public void onPreExecute() {
            VideosFragment.this.linear_progress.setVisibility(View.VISIBLE);
        }
    }

    private class PreviewAdapter extends Adapter<ViewHolder> {
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        ArrayList<File> previewPath;

        public PreviewAdapter(ArrayList<File> arrayList) {
            this.previewPath = arrayList;
        }

        private void showLoadingView(LoadingViewHolder loadingViewHolder, int i) {
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == 0) {
                return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false));
            }
            return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if (viewHolder instanceof ItemViewHolder) {
                populateItemRows((ItemViewHolder) viewHolder, i);
            } else if (viewHolder instanceof LoadingViewHolder) {
                showLoadingView((LoadingViewHolder) viewHolder, i);
            }
        }

        public int getItemCount() {
            ArrayList<File> arrayList = this.previewPath;
            if (arrayList == null) {
                return 0;
            }
            return arrayList.size();
        }

        public int getItemViewType(int i) {
            return this.previewPath.get(i) == null ? 1 : 0;
        }

        private void populateItemRows(ItemViewHolder itemViewHolder, int i) {
            itemViewHolder.setimg.setImageBitmap(ThumbnailUtils.createVideoThumbnail(this.previewPath.get(i).getAbsolutePath(), 1));
            itemViewHolder.clickcard.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(VideosFragment.this.getContext(), PlayActivity.class).putExtra("videourl", PreviewAdapter.this.previewPath.get(i).getAbsolutePath()).putExtra(ConditionalUserProperty.NAME, previewPath.get(i).getName()));
                }
            });
            itemViewHolder.shareimg.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    try {
                        share(previewPath.get(i).getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            itemViewHolder.deleteimg.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    try {

                        Builder builder = new Builder(getActivity());
                        builder.setTitle("Permanently delete this video?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                                Log.e("bhjhjvjvjvj1333 ", i + "_");
                                if (i >= 0) {
                                    delete(i);
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        public void delete(int i) {
            Log.e("bhjhjvjvjvj11 ", i + "_" + previewPath.size());

            File file = new File(previewPath.get(i).getAbsolutePath());

            Log.e("bhjhjvjvjvj ", file.toString());

            if (file.exists()) {
                file.delete();
                this.previewPath.remove(i);
                if (file.exists()) {
                    file.delete();
                }
                String sb2 = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    sb2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                } else {
                    sb2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                            File.separator +
                            VideosFragment.this.getString(R.string.app_name) +
                            File.separator +
                            VideosFragment.this.getString(R.string.video) +
                            File.separator;
                }

                android.util.Log.e("njfsdfjsdjfsdfs", "is exist" + sb2);
                MediaScannerConnection.scanFile(VideosFragment.this.getContext(), new String[]{sb2}, null, new OnScanCompletedListener() {
                    public void onScanCompleted(String str, Uri uri) {
                    }
                });
                notifyDataSetChanged();
                return;
            }
            Toast.makeText(VideosFragment.this.getContext(), VideosFragment.this.getResources().getString(R.string.not_found), Toast.LENGTH_SHORT).show();


        }


        public void share(String str) {
            Uri uri;
            Context context = VideosFragment.this.getContext();
            String sb = VideosFragment.this.getContext().getPackageName() +
                    ".provider";
            uri = MyFileProvider.getUriForFile(context, sb, new File(str));
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", VideosFragment.this.getResources().getString(R.string.download_more_from_link));
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType(MimeTypes.VIDEO_MP4);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                VideosFragment videosFragment = VideosFragment.this;
                String sb2 = VideosFragment.this.getResources().getString(R.string.share_via) +
                        " " +
                        VideosFragment.this.getResources().getString(R.string.app_name);
                videosFragment.startActivity(Intent.createChooser(intent, sb2));
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(VideosFragment.this.getContext(), VideosFragment.this.getResources().getString(R.string.app_not_installed), Toast.LENGTH_SHORT).show();
            }
        }

        public long getItemId(int i) {
            return super.getItemId(i);
        }

        private class ItemViewHolder extends ViewHolder {
            CardView clickcard;
            ImageView deleteimg;
            ImageView setimg;
            ImageView shareimg;

            public ItemViewHolder(View view) {
                super(view);
                this.setimg = view.findViewById(R.id.setimg);
                this.shareimg = view.findViewById(R.id.shareimg);
                this.deleteimg = view.findViewById(R.id.deleteimg);
                this.clickcard = view.findViewById(R.id.clickcard);
            }
        }

        private class LoadingViewHolder extends ViewHolder {
            ProgressBar progressBar;

            public LoadingViewHolder(View view) {
                super(view);
                this.progressBar = view.findViewById(R.id.progressBar);
            }
        }
    }
}
