package jp.dip.azurelapis.android.PicS;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ListView;
import jp.dip.azurelapis.android.PicS.UI.BrowserFragment.OnLoadFinishWebPage;
import jp.dip.azurelapis.android.PicS.UI.CommonUi.IconAndTextListViewAdapter;
import jp.dip.azurelapis.android.PicS.UI.MainOnPageChangeListnere;
import jp.dip.azurelapis.android.PicS.UI.MainViewPagerAdapter;

public class MainActivity extends FragmentActivity {

    //サイドメニュー
    private LinearLayout leftDrawableLinearLayout;
    private ListView leftDrawarMenuListView;
    private IconAndTextListViewAdapter leftDrowarListViewAdapter;

    //メインコンテンツ部分
    private ViewPager mainViewPager;
    private MainViewPagerAdapter viewPagerAdapter;

    public static Context context;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getWindow().requestFeature(android.view.Window.FEATURE_ACTION_MODE_OVERLAY);

        setContentView(R.layout.main);

        //サイドメニューの初期化
        this.leftDrawableLinearLayout = (LinearLayout)findViewById(R.id.left_drawer);
        this.leftDrawarMenuListView = (ListView)findViewById(R.id.left_drawer_menu_listview);
        this.leftDrowarListViewAdapter = new IconAndTextListViewAdapter(this);
        this.leftDrawarMenuListView.setAdapter(this.leftDrowarListViewAdapter);



        //ViewPagerの設定
        this.mainViewPager = (ViewPager)findViewById(R.id.main_view_pager);
        this.viewPagerAdapter = new MainViewPagerAdapter(new OnWebPageLoaded(), getSupportFragmentManager());
        this.mainViewPager.setAdapter(this.viewPagerAdapter);
        this.mainViewPager.setOffscreenPageLimit(3);

        //Action Barの設定
        ActionBar actionBar = getActionBar();
        // NvigationModeをNAVIGATION_MODE_TABSに設定
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionTabListnere actionTabListnere = new ActionTabListnere(this.mainViewPager);
        actionBar.addTab(actionBar.newTab().setText("ブラウザ").setTabListener(actionTabListnere));
        actionBar.addTab(actionBar.newTab().setText("画像").setTabListener(actionTabListnere));
        actionBar.addTab(actionBar.newTab().setText("テキスト").setTabListener(actionTabListnere));


        //actionbarの背景色
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#283593")));

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.browser_navigation);

        this.mainViewPager.setOnPageChangeListener(new MainOnPageChangeListnere(actionBar));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Browserナビゲーション関係
        //戻る
        MenuItem actionBackItem = menu.add("← 戻る");
        actionBackItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        actionBackItem.setIcon(android.R.drawable.ic_media_previous);
        actionBackItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                viewPagerAdapter.getBrowserFragment().goBack();
                return false;
            }
        });


        //進む
        MenuItem actionNextItem = menu.add("進む →");
        actionNextItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        actionNextItem.setIcon(android.R.drawable.ic_media_next);
        actionNextItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                viewPagerAdapter.getBrowserFragment().goForward();
                return false;
            }
        });


        return true;
    }



    private class ActionTabListnere implements ActionBar.TabListener{

        private ViewPager viewPager;

        public ActionTabListnere(ViewPager viewPager){
            this.viewPager = viewPager;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            this.viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    }


    private class OnWebPageLoaded implements OnLoadFinishWebPage{

        @Override
        public void onLoadFinishWebPage(String url,String html) {
            //Toast.makeText(getApplicationContext(), "laoded !! " + html, Toast.LENGTH_LONG).show();
            viewPagerAdapter.refreshImageFragmnet(url,html);
            viewPagerAdapter.refreshTextFragmnet(url,html);
            viewPagerAdapter.refreshBrowserFragmnet(url, html);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && this.viewPagerAdapter.getBrowserFragment().canGoBack()) {
            this.viewPagerAdapter.getBrowserFragment().goBack();
            return true;
        }else{
            return false;
        }

    }
}



