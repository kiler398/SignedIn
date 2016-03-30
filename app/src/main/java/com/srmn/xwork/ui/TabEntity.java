package com.srmn.xwork.ui;

import android.support.v4.app.Fragment;

import com.flyco.tablayout.listener.CustomTabEntity;

public class TabEntity implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;
    public Fragment fragment;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon,Fragment fragment) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
