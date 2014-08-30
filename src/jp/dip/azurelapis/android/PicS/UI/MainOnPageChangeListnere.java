package jp.dip.azurelapis.android.PicS.UI;

import android.app.ActionBar;
import android.support.v4.view.ViewPager;

/**
 * Created by kamiyama on 2014/07/09.
 */
public class MainOnPageChangeListnere implements ViewPager.OnPageChangeListener{


    private ActionBar actionBar;

    public MainOnPageChangeListnere(ActionBar actionBar){
        this.actionBar = actionBar;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        actionBar.setSelectedNavigationItem(i);

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
