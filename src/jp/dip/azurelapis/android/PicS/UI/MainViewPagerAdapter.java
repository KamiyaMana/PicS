package jp.dip.azurelapis.android.PicS.UI;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import jp.dip.azurelapis.android.PicS.UI.BrowserFragment.BrowserFragment;
import jp.dip.azurelapis.android.PicS.UI.BrowserFragment.OnLoadFinishWebPage;
import jp.dip.azurelapis.android.PicS.UI.ImageFragment.ImageFragment;
import jp.dip.azurelapis.android.PicS.UI.TextFragment.TextCardData;
import jp.dip.azurelapis.android.PicS.UI.TextFragment.TextFragment;
import jp.dip.azurelapis.android.PicS.Util.HtmlParse;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamiyama on 2014/07/09.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    //このアダプターが持つFragment
    private List<Fragment> fragments = new ArrayList<Fragment>();

    private BrowserFragment browserFragment;
    private ImageFragment imageFragment;
    private TextFragment textFragment;

    private OnLoadFinishWebPage onLoadFinishWebPage;

    public MainViewPagerAdapter(OnLoadFinishWebPage onLoadFinishWebPage, FragmentManager fragmentManager) {
        super(fragmentManager);

        this.onLoadFinishWebPage = onLoadFinishWebPage;
        this.browserFragment = new BrowserFragment();
        this.browserFragment.setOnFinishLoadWebPage(this.onLoadFinishWebPage);
        this.imageFragment = new ImageFragment();
        this.textFragment = new TextFragment();

        fragments.add(this.browserFragment);
        fragments.add(this.imageFragment);
        fragments.add(this.textFragment);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void refreshImageFragmnet(final String url, final String html) {


        AsyncTask<Void, Void, List<URL>> asyncTask = new AsyncTask<Void, Void, List<URL>>() {
            @Override
            protected List<URL> doInBackground(Void... voids) {
                return HtmlParse.getImatgeUrl(url);

            }

            @Override
            protected void onPostExecute(List<URL> urls) {
                super.onPostExecute(urls);
                if(urls == null) {
                    return;
                }
                imageFragment.setImages(urls);

            }
        };

        asyncTask.execute();

    }


    public void refreshTextFragmnet(final String url, final String html) {

        AsyncTask<Void, Void, List<TextCardData>> asyncTask = new AsyncTask<Void, Void, List<TextCardData>>() {
            @Override
            protected List<TextCardData> doInBackground(Void... voids) {
                return HtmlParse.getTextCardDatas(url);

            }

            @Override
            protected void onPostExecute(List<TextCardData> textCardDatas) {
                super.onPostExecute(textCardDatas);
                if(textCardDatas == null) {
                    return;
                }
                textFragment.setTextCards(textCardDatas);
            }
        };

        asyncTask.execute();

    }

    /**
     * 表示中のBrowserフラグメントを返す
     * @return
     */
    public BrowserFragment getBrowserFragment(){
        return this.browserFragment;
    }
}
