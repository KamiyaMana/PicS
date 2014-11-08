package jp.dip.azurelapis.android.PicS;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.webkit.WebIconDatabase;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import jp.dip.azurelapis.android.PicS.Datas.BookMark.BookMark;
import jp.dip.azurelapis.android.PicS.Datas.DatabaseUtils.BookMarkDataBaseUtils;
import jp.dip.azurelapis.android.PicS.UI.BrowserFragment.OnLoadFinishWebPage;
import jp.dip.azurelapis.android.PicS.UI.CommonUi.IconAndTextData;
import jp.dip.azurelapis.android.PicS.UI.CommonUi.IconAndTextListViewAdapter;
import jp.dip.azurelapis.android.PicS.UI.MainOnPageChangeListnere;
import jp.dip.azurelapis.android.PicS.UI.MainViewPagerAdapter;

import java.util.List;

public class MainActivity extends FragmentActivity {

    //サイドメニュー
    private DrawerLayout lefitSideDrawarLayoyt;
    private LinearLayout leftDrawableLinearLayout;
    private ListView leftDrawarMenuListView;
    private IconAndTextListViewAdapter leftDrowarListViewAdapter;

    private List<BookMark> bookmarks;//現在サイドメニューにあるBokMark

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

        //WebViewでアイコン取得可能とする
        WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());

        //サイドメニューの初期化
        this.lefitSideDrawarLayoyt = (DrawerLayout)findViewById(R.id.left_side_menu_drawer_layout);
        this.leftDrawableLinearLayout = (LinearLayout)findViewById(R.id.left_drawer);
        this.leftDrawarMenuListView = (ListView)findViewById(R.id.left_drawer_menu_listview);
        this.leftDrowarListViewAdapter = new IconAndTextListViewAdapter(this);
        this.initSideMenuList(this.leftDrowarListViewAdapter);
        this.initSideMenuListView(this.leftDrawarMenuListView);
        this.leftDrawarMenuListView.setAdapter(this.leftDrowarListViewAdapter);



        //ViewPagerの設定
        this.mainViewPager = (ViewPager)findViewById(R.id.main_view_pager);
        this.viewPagerAdapter = new MainViewPagerAdapter(new OnWebPageLoaded(), getSupportFragmentManager());
        this.mainViewPager.setAdapter(this.viewPagerAdapter);
        this.mainViewPager.setOffscreenPageLimit(3);

        //Action Barの設定
        this.getActionBar().setHomeButtonEnabled(true);
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
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            // アプリアイコンがタップされたときの処理

            if (this.lefitSideDrawarLayoyt.isDrawerOpen(Gravity.LEFT)) {
                this.lefitSideDrawarLayoyt.closeDrawer(Gravity.LEFT);
            }else{
                this.lefitSideDrawarLayoyt.openDrawer(Gravity.LEFT);

            }
        }

        return super.onMenuItemSelected(featureId, item);

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


        //ブックマーク追加
        MenuItem actionAddBookMarkItem = menu.add("ブックマーク追加");
        actionAddBookMarkItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        actionAddBookMarkItem.setIcon(android.R.drawable.star_big_on);
        actionAddBookMarkItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                BookMarkDataBaseUtils bookmarkUtil = new BookMarkDataBaseUtils(context);

                BookMark bookMark = new BookMark();
                String pagetTitile = viewPagerAdapter.getBrowserFragment().getPageTitle();
                String pagetUrl = viewPagerAdapter.getBrowserFragment().getUrl();
                Bitmap favcon = viewPagerAdapter.getBrowserFragment().getFavcon();

                bookMark.setTitile(pagetTitile);
                bookMark.setUrl(pagetUrl);
                bookMark.setIcon(favcon);

                bookmarkUtil.insertBookMark(bookMark);

                //描画の更新
                //leftDrowarListViewAdapter.notifyDataSetChanged();
                initSideMenuList(leftDrowarListViewAdapter);
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


    /**
     * サイドメニューの初期化処理をする
     */
    private void initSideMenuList(final IconAndTextListViewAdapter menuListAdapter){

        AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                menuListAdapter.clear();

                BookMarkDataBaseUtils bookmarkDB = new BookMarkDataBaseUtils(context);
                bookmarks = bookmarkDB.selectAllBookMark();

                for(BookMark bookMark : bookmarks){

                    String menuText = bookMark.getTitile();
                    Bitmap iconBitmap = bookMark.getIcon();

                    Drawable icon = context.getResources().getDrawable(android.R.drawable.star_big_on);

                    if(iconBitmap != null){
                        icon = new BitmapDrawable(iconBitmap);
                    }

                    IconAndTextData iconAndTextData = new IconAndTextData(icon, menuText);
                    menuListAdapter.addMenuItem(iconAndTextData);
                }

                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                menuListAdapter.notifyDataSetChanged();
            }
        };

        asyncTask.execute();
    }


    /**
     * ListViewの初期化をする
     * @param menuListview
     */
    private void initSideMenuListView(ListView menuListview){
        menuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookMark bookMark = bookmarks.get(i);
                viewPagerAdapter.getBrowserFragment().loadPage(bookMark.getUrl());
            }
        });

        //ロングタップされた時の動作
        menuListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookMark bookMark = bookmarks.get(i);
                BookMarkDataBaseUtils bookMarkDataBaseUtils = new BookMarkDataBaseUtils(context);
                bookMarkDataBaseUtils.deletBookMark(bookMark.getId());
                initSideMenuList(leftDrowarListViewAdapter);
                return false;
            }
        });

    }
}



