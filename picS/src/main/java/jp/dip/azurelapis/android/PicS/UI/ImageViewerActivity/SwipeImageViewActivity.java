package jp.dip.azurelapis.android.PicS.UI.ImageViewerActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import jp.dip.azurelapis.android.PicS.R;

/**
 * Created by kamiyama on 2014/07/21.
 * 画像ビューワー機能やら共有機能やらもろもろ表示するActivity
 */
public class SwipeImageViewActivity extends FragmentActivity {

    public static final String ACTIVE_IMAGE_URL = "ACTIVE_IMAGE_URL";//最初に表示されるべき画像のURL
    public static final String IMAGE_URLS = "IMAGE_URLS";//画像のURLのリスト

    public static final String ALL_DOWNLOAD_BUTTON_TITILE = "まとめてダウンロード";

    private ViewPager imageViewPager;
    private SwipeImageViewViewPagerAdapter viewpagerAdapter;

    private List<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //アクションバーを非表示に
        //getActionBar().hide();
        //getWindow().requestFeature(android.view.Window.FEATURE_ACTION_BAR_OVERLAY);


        setContentView(R.layout.image_view_activity);

        this.imageViewPager = (ViewPager) findViewById(R.id.image_View_viewpager_swipe_image_viewr_activity);
        this.imageViewPager.setOffscreenPageLimit(3);

        this.viewpagerAdapter = new SwipeImageViewViewPagerAdapter(getSupportFragmentManager());


        this.intentGetParameter(getIntent());
        //this.viewpagerAdapter.addImages(this.imageUrls);
        this.imageViewPager.setAdapter(this.viewpagerAdapter);

        //表示画像を決定する
        String activeImageUrl = getIntent().getStringExtra(ACTIVE_IMAGE_URL);
        String tempImageUrl;
        for (int i = 0; i < imageUrls.size(); i++) {

            tempImageUrl = imageUrls.get(i);
            if (activeImageUrl.equals(tempImageUrl)) {
                this.imageViewPager.setCurrentItem(i);
                break;
            }
        }


    }

    /**
     * アクションばーのメニュー追加
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        MenuItem actionItem = menu.add(ALL_DOWNLOAD_BUTTON_TITILE);
        actionItem.setIcon(android.R.drawable.ic_menu_save);
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    /**
     * アクションバーが押された時の動作
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //まとめてダウンロードボタンが押された場合
        if (item.getTitle().equals(ALL_DOWNLOAD_BUTTON_TITILE)) {
            Toast.makeText(this, "まとめてダウンロードを開始しました", Toast.LENGTH_LONG).show();


            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DownloadManager downLoadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                    for(String url : imageUrls) {
                        //Toast.makeText(this, "!"+ url, Toast.LENGTH_LONG).show();

                        Uri uri = Uri.parse(url);
                        DownloadManager.Request request = new DownloadManager.Request(uri);

                        //保存ファイル名
                        String fileName = url.substring(url.lastIndexOf("/")+1,url.length());
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
                        request.setTitle(fileName);

                        //DLに使う回線種別
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                                | DownloadManager.Request.NETWORK_WIFI);

                        request.setShowRunningNotification(true);
                        request.setVisibleInDownloadsUi(true);
                        downLoadManager.enqueue(request);
                    }
                }
            });
            thread.start();

        }

        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        this.intentGetParameter(intent);
    }

    /**
     * IntentからURLなど各種パラメータを取り出す
     *
     * @param intent
     */
    private void intentGetParameter(Intent intent) {

        this.viewpagerAdapter.clearImageUrls();

        this.imageUrls = (List<String>) intent.getSerializableExtra(IMAGE_URLS);
        this.viewpagerAdapter.addImages(imageUrls);

    }



}
