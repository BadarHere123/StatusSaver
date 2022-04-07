package com.infusiblecoder.myvideodownloaderv2.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.infusiblecoder.myvideodownloaderv2.BuildConfig;
import com.infusiblecoder.myvideodownloaderv2.R;
import com.infusiblecoder.myvideodownloaderv2.model.MyFileProvider;
import com.infusiblecoder.myvideodownloaderv2.ui.DisplayImageActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ImagesFragment extends Fragment {
    LinearLayout adlayout;

    int f145i = 0;
    LinearLayout linear_progress;
    ArrayList<File> myvideos = new ArrayList<>();
    LinearLayout noT_found;
    RecyclerView recyclerview;
    boolean temp = false;
    TextView txt_loading;
    View view;

    public void setadapterr() {
        Collections.reverse(this.myvideos);
        PreviewAdapter previewAdapter = new PreviewAdapter(this.myvideos);
        this.recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.recyclerview.setItemAnimator(new DefaultItemAnimator());
        this.recyclerview.setAdapter(previewAdapter);
        this.linear_progress.setVisibility(View.GONE);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_images, viewGroup, false);
        this.recyclerview = this.view.findViewById(R.id.recyclerview);
        this.linear_progress = this.view.findViewById(R.id.linear_progress);
        this.noT_found = this.view.findViewById(R.id.noT_found);
        new AsyncTaskRunner().execute("abc");
        return this.view;
    }

    public ArrayList<File> getlist(File file) {
        File[] listFiles;
        ArrayList<File> arrayList = new ArrayList<>();
        for (File file2 : file.listFiles()) {
            if (file2.isDirectory()) {
                arrayList.addAll(getlist(file2));
            } else if (file2.getName().endsWith(".jpg") || file2.getName().endsWith(".png") || file2.getName().endsWith(".jpeg")) {
                arrayList.add(file2);
            }
        }
        if (arrayList.size() == 0) {
            this.noT_found.setVisibility(View.VISIBLE);
        } else {
            this.noT_found.setVisibility(View.GONE);
        }
        return arrayList;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;

        private AsyncTaskRunner() {
        }

        public String doInBackground(String... strArr) {
            try {
                String sb = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    sb = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                } else {
                    sb = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                            File.separator +
                            ImagesFragment.this.getString(R.string.app_name) +
                            File.separator +
                            ImagesFragment.this.getString(R.string.image) +
                            File.separator;
                }
                File file = new File(sb);
                if (!file.exists()) {
                    file.mkdir();
                }
                ImagesFragment.this.myvideos = ImagesFragment.this.getlist(file);
            } catch (Exception e) {
                e.printStackTrace();
                this.resp = e.getMessage();
            }
            return "abc";
        }

        public void onPostExecute(String str) {
            ImagesFragment.this.setadapterr();
        }

        public void onPreExecute() {
            ImagesFragment.this.linear_progress.setVisibility(View.VISIBLE);
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

        private void populateItemRows(ItemViewHolder itemViewHolder, final int i) {
            itemViewHolder.setimg.setImageURI(Uri.parse(this.previewPath.get(i).getAbsolutePath()));
            itemViewHolder.clickcard.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ImagesFragment.this.startActivity(new Intent(ImagesFragment.this.getContext(), DisplayImageActivity.class).putExtra("videourl", PreviewAdapter.this.previewPath.get(i).getAbsolutePath()).putExtra(ConditionalUserProperty.NAME, PreviewAdapter.this.previewPath.get(i).getName()));
                }
            });
            itemViewHolder.shareimg.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    try {
                        PreviewAdapter.this.share(PreviewAdapter.this.previewPath.get(i).getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            itemViewHolder.deleteimg.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    try {
                        PreviewAdapter.this.showdialog(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void showdialog(final int i) {
            Builder builder = new Builder(ImagesFragment.this.getContext());
            builder.setTitle("Permanently delete this image?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    PreviewAdapter.this.delete(i);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }

        public void delete(int i) {
            File file = new File(this.previewPath.get(i).getAbsolutePath());
            if (file.exists()) {
                file.delete();
                this.previewPath.remove(i);
                if (file.exists()) {
                    file.delete();
                }
                String sb2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                        File.separator +
                        ImagesFragment.this.getString(R.string.app_name) +
                        File.separator;
                MediaScannerConnection.scanFile(ImagesFragment.this.getContext(), new String[]{sb2}, null, new OnScanCompletedListener() {
                    public void onScanCompleted(String str, Uri uri) {
                    }
                });
                notifyDataSetChanged();
                return;
            }
            Toast.makeText(ImagesFragment.this.getContext(), ImagesFragment.this.getResources().getString(R.string.not_found), Toast.LENGTH_SHORT).show();
        }
        public void share(String str) {
            Uri uri;
            if (VERSION.SDK_INT >= 24) {
                Context context = ImagesFragment.this.getContext();
                String sb = ImagesFragment.this.getContext().getPackageName() +
                        ".provider";
                uri = MyFileProvider.getUriForFile(context, sb, new File(str));
            } else {
                uri = Uri.fromFile(new File(str));
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            String sb2 = ImagesFragment.this.getResources().getString(R.string.download_more_from_link) +
                    "\n\n" +
                    "App Link : \n" +
                    "https://play.google.com/store/apps/details?id=" +
                    BuildConfig.APPLICATION_ID;
            intent.putExtra("android.intent.extra.TEXT", sb2);
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                ImagesFragment imagesFragment = ImagesFragment.this;
                String sb3 = ImagesFragment.this.getResources().getString(R.string.share_via) +
                        " " +
                        ImagesFragment.this.getResources().getString(R.string.app_name);
                imagesFragment.startActivity(Intent.createChooser(intent, sb3));
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(ImagesFragment.this.getContext(), ImagesFragment.this.getResources().getString(R.string.app_not_installed), Toast.LENGTH_SHORT).show();
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
