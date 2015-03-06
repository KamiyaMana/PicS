package jp.dip.azurelapis.android.PicS.UI.MainActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebIconDatabase;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import jp.dip.azurelapis.android.PicS.Datas.BookMark.BookMark;
import jp.dip.azurelapis.android.PicS.Datas.DatabaseUtils.BookMarkDataBaseUtils;
import jp.dip.azurelapis.android.PicS.R;
import jp.dip.azurelapis.android.PicS.UI.BrowserFragment.OnLoadFinishWebPage;
import jp.dip.azurelapis.android.PicS.UI.CommonUi.IconAndTextData;
import jp.dip.azurelapis.android.PicS.UI.CommonUi.IconAndTextListViewAdapter;

public class MainActivity extends FragmentActivity {

    //サイドメニュー
    private DrawerLayout lefitSideDrawarLayoyt;
    private ActionBarDrawerToggle actionbatTogle;

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
        this.lefitSideDrawarLayoyt = (DrawerLayout) findViewById(R.id.left_side_menu_drawer_layout);
        this.leftDrawableLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        this.leftDrawarMenuListView = (ListView) findViewById(R.id.left_drawer_menu_listview);

        IconAndTextListViewAdapter iconAndTextListViewAdapter = new IconAndTextListViewAdapter(this);
        this.leftDrowarListViewAdapter = iconAndTextListViewAdapter;
        iconAndTextListViewAdapter.setDefaultIconImage(getResources().getDrawable(android.R.drawable.star_big_off));

        this.initSideMenuList(this.leftDrowarListViewAdapter);
        this.initSideMenuListView(this.leftDrawarMenuListView);
        this.leftDrawarMenuListView.setAdapter(this.leftDrowarListViewAdapter);


        //ViewPagerの設定
        this.mainViewPager = (ViewPager) findViewById(R.id.main_view_pager);
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





        //サイドメニューのアプリアイコンによる開閉設定
        this.actionbatTogle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                lefitSideDrawarLayoyt,         /* DrawerLayout object */
                R.drawable.pics_icon,  /* nav drawer icon to replace 'Up' caret */
                R.string.side_menu_toggle_open,  /* "open drawer" description */
               R.string.side_menu_toggle_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.app_name);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.app_name);
            }
        };

        // Set the drawer toggle as the DrawerListener
        this.lefitSideDrawarLayoyt.setDrawerListener(actionbatTogle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);




    }




    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            // アプリアイコンがタップされたときの処理

            if (this.lefitSideDrawarLayoyt.isDrawerOpen(Gravity.LEFT)) {

                //ドロワーが開いてる時は閉じる
                this.lefitSideDrawarLayoyt.closeDrawer(Gravity.LEFT);
            } else {

                //ドロワーが閉じている時は開く
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


    private class ActionTabListnere implements ActionBar.TabListener {

        private ViewPager viewPager;

        public ActionTabListnere(ViewPager viewPager) {
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


    private class OnWebPageLoaded implements OnLoadFinishWebPage {

        @Override
        public void onLoadFinishWebPage(String url, String html) {
            //Toast.makeText(getApplicationContext(), "laoded !! " + html, Toast.LENGTH_LONG).show();
            viewPagerAdapter.refreshImageFragmnet(url, html);
            viewPagerAdapter.refreshTextFragmnet(url, html);
            viewPagerAdapter.refreshBrowserFragmnet(url, html);

            //webview fragmentの進捗バーの管理
            viewPagerAdapter.getBrowserFragment().invisibleLoadingBar();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && this.viewPagerAdapter.getBrowserFragment().canGoBack()) {
            this.viewPagerAdapter.getBrowserFragment().goBack();
            return true;
        } else {
            return false;
        }

    }


    /**
     * サイドメニューの初期化処理をする
     */
    private void initSideMenuList(final IconAndTextListViewAdapter menuListAdapter) {

        //サイドメニュー生成
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                menuListAdapter.clear();

                BookMarkDataBaseUtils bookmarkDB = new BookMarkDataBaseUtils(context);
                bookmarks = bookmarkDB.selectAllBookMark();

                for (BookMark bookMark : bookmarks) {

                    String menuText = bookMark.getTitile();
                    String menuUrl = bookMark.getUrl();
                    Bitmap iconBitmap = bookMark.getIcon();

                    Drawable icon = context.getResources().getDrawable(android.R.drawable.btn_star_big_off);

                    if (iconBitmap != null) {
                        icon = new BitmapDrawable(iconBitmap);
                    }

                    IconAndTextData iconAndTextData = new IconAndTextData(icon, menuText, menuUrl);
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
     *
     * @param menuListview
     */
    private void initSideMenuListView(ListView menuListview) {
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

                final BookMark bookMark = bookmarks.get(i);
                final String message = "ブックマークを削除しますか？" +
                        "\n" +
                        "\n" +
                        "\n" +
                        bookMark.getTitile() +
                        "\n" +
                        "\n" +
                        bookMark.getUrl();

                DialogFragment dialogFragment = new DialogFragment() {

                    @NonNull
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("確認")
                                .setMessage(message)
                                .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //ブックマークを削除する
                                        BookMarkDataBaseUtils bookMarkDataBaseUtils = new BookMarkDataBaseUtils(context);
                                        bookMarkDataBaseUtils.deletBookMark(bookMark.getId());
                                        initSideMenuList(leftDrowarListViewAdapter);
                                    }
                                })

                                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //Dialogを閉じる
                                        dialog.dismiss();
                                    }
                                });

                        return builder.create();
                    }
                };

                dialogFragment.show(getSupportFragmentManager(),"BookMarkDelDialogFragment");

                return true;
            }
        });

    }



}



