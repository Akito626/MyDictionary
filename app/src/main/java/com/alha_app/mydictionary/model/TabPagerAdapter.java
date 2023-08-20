package com.alha_app.mydictionary.model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alha_app.mydictionary.DictionaryActivity;
import com.alha_app.mydictionary.EnglishIndexFragment;
import com.alha_app.mydictionary.IndexFragment;
import com.alha_app.mydictionary.TagListFragment;
import com.alha_app.mydictionary.WordListFragment;

import java.util.ArrayList;
import java.util.List;

public class TabPagerAdapter extends FragmentStateAdapter {
    private DictionaryActivity activity;
    private List<Fragment> fragmentList = new ArrayList<>();
    public TabPagerAdapter(DictionaryActivity activity){
        super(activity);

        this.activity = activity;

        fragmentList.add(new WordListFragment());
        fragmentList.add(new IndexFragment());
        fragmentList.add(new EnglishIndexFragment());
        fragmentList.add(new TagListFragment());

        activity.setFragmentList(fragmentList);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        Fragment fragment = fragmentList.get(position);
        return fragment;
    }

    @Override
    public int getItemCount(){
        return 4;
    }
}
