package com.infusiblecoder.myvideodownloaderv2.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.infusiblecoder.myvideodownloaderv2.fragment.ImagesFragment;
import com.infusiblecoder.myvideodownloaderv2.fragment.SongsFragment;
import com.infusiblecoder.myvideodownloaderv2.fragment.VideosFragment;

import java.util.ArrayList;
import java.util.List;

public class DownloadAdapter extends FragmentPagerAdapter {
    private List<Fragment> Fragment = new ArrayList();

    public DownloadAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int i) {
        return "";
    }

    public void add(Fragment fragment, String str) {
        this.Fragment.add(fragment);
    }

    public Fragment getItem(int i) {
        if (i == 0) {
            return new VideosFragment();
        }
        if (i == 1) {
            return new ImagesFragment();
        }
        if (i != 2) {
            return null;
        }
        return new SongsFragment();
    }
}
