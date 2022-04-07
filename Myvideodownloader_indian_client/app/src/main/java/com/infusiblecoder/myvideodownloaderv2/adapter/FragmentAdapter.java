package com.infusiblecoder.myvideodownloaderv2.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.infusiblecoder.myvideodownloaderv2.fragment.FacebookFragment;
import com.infusiblecoder.myvideodownloaderv2.fragment.InstagramFragment;
import com.infusiblecoder.myvideodownloaderv2.fragment.TedFragment;
import com.infusiblecoder.myvideodownloaderv2.fragment.TiktokFragment;
import com.infusiblecoder.myvideodownloaderv2.fragment.TwitterFragment;
import com.infusiblecoder.myvideodownloaderv2.fragment.VimeoFragment;
import com.infusiblecoder.myvideodownloaderv2.fragment.YoutubeFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> Fragment = new ArrayList();
    private String[] tabTitles = {"Tiktok", "Instagram", "Facebook", "LiveLeak", "Youtube", "TED", "Viemo"};

    public FragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public int getCount() {
        return 7;
    }

    public void add(Fragment fragment, String str) {
        this.Fragment.add(fragment);
    }

    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new TiktokFragment();
            case 1:
                return new InstagramFragment();
            case 2:
                return new FacebookFragment();
            case 3:
                return new TwitterFragment();
            case 4:
                return new YoutubeFragment();
            case 5:
                return new TedFragment();
            case 6:
                return new VimeoFragment();
            default:
                return null;
        }
    }

    public CharSequence getPageTitle(int i) {
        return this.tabTitles[i];
    }
}
