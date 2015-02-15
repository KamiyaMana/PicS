package jp.dip.azurelapis.android.PicS.AppBase;

import android.app.Application;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;

import java.net.URI;

/**
 * Created by kamiyama on 2014/07/12.
 * アプリ全体で共有したい情報はこっちに持たせる
 */
public class PicSApplicationBase extends Application {

    //現在参照してるページのURI　ActivityやFragmentが破棄されても再開できるようにするため
    private static URI browsingUri;

    public PicSApplicationBase() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 画像キャッシュのグローバル設定の生成と初期化を行う
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(8)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LRULimitedMemoryCache(5 * 1024 * 1024))//new UsingFreqLimitedMemoryCache(5 * 1024 * 1024))//5MBキャッシュ
                        //.discCacheExtraOptions(720, 480, Bitmap.CompressFormat.JPEG, 75)
                .memoryCacheSize(5 * 1024 * 1024)
                .diskCache(new LimitedAgeDiscCache(getCacheDir(), 10 * 1024 * 1024))//秒単位で寿命指定する
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .diskCacheFileCount(5000)
                .diskCacheSize(50 * 1024 * 1024)
                        //.imageDownloader(new (new BaseImageDownloader(this, 120 * 1000, 120 * 1000)))
                .imageDecoder(new BaseImageDecoder(true))
                .memoryCacheSizePercentage(5)
                .build();
        ImageLoader.getInstance().init(config);
    }

    //getter setter
    public static URI getBrowsingUri() {
        return browsingUri;

    }


    public static void setBrowsingUri(URI browsingUri) {
        PicSApplicationBase.browsingUri = browsingUri;
    }
}
