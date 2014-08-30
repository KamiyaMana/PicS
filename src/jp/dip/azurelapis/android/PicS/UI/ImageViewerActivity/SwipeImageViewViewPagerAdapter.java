package jp.dip.azurelapis.android.PicS.UI.ImageViewerActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamiyama on 2014/08/03.
 */
public class SwipeImageViewViewPagerAdapter extends FragmentStatePagerAdapter {

    //ViewPager内に表示する画像の情報
    private List<String> imageUrls = new ArrayList<String>();
    private SwipeImageViewFragment cacheFargment;


    public SwipeImageViewViewPagerAdapter(FragmentManager fm) {
        super(fm);
        
    }

    /**
     * 一枚ずつ画像のURLを追加する
     *
     * @param url
     */
    public void addImage(String url) {
        if (url == null) {
            return;
        }

        this.imageUrls.add(url);
    }

    /**
     * 複数の画像のURLを追加する
     */
    public void addImages(List<String> urls) {
        if (urls == null) {
            return;
        }

        this.imageUrls.addAll(urls);
        this.notifyDataSetChanged();
    }

    /**
     * すべてのがぞうのURLを消す
     */
    public void clearImageUrls() {
        this.imageUrls.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.imageUrls.size();//this.imageUrls.size();
    }


    @Override
    public Fragment getItem(int i) {
        //if (this.cacheFargment == null) {
            //キャッシュの作成
           // this.cacheFargment = new SwipeImageViewFragment();
        //}

        SwipeImageViewFragment fragment = new SwipeImageViewFragment(this.imageUrls.get(i));

        return fragment;//this.cacheFargment;
    }


}
