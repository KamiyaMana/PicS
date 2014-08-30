package jp.dip.azurelapis.android.PicS.UI.ImageFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import jp.dip.azurelapis.android.PicS.R;
import jp.dip.azurelapis.android.PicS.Util.UrlUtiles;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamiyama on 2014/07/12.
 */
public class ImageCardListViewAdapter extends BaseAdapter {

    private List<URL> imageURLs = new ArrayList<URL>();
    private List<String> imageUrlStrings = new ArrayList<String>();

    private Context context;

    private int lastIndex = -1;


    /**
     * コンストラクタ
     */
    public ImageCardListViewAdapter(Context context) {
        this.context = context;
    }

    /**
     * 画像を追加する
     *
     * @param Url
     */
    public void addImage(URL Url) {
        if (Url == null) {
            return;
        } else {

        }

        this.imageURLs.add(Url);
        this.imageUrlStrings.add(Url.toString());
        notifyDataSetChanged();
    }

    public List<URL> getImageUrls(){
        return this.imageURLs;
    }

    /**
     * 複数の画像をまとめてセットする
     * すべてデータを追加してから再描画するので描画処理が軽くなる
     */
    public void setImageList(List<URL> imageURLs) {
        clearImages();
        this.lastIndex = -1;
        this.imageURLs = imageURLs;
        this.imageUrlStrings = this.converInageUrl(imageURLs);
        notifyDataSetChanged();
    }

    /**
     * まとめて現在のImageListに追加する
     *
     * @param imageURLs
     */
    public void addImageList(List<URL> imageURLs) {
        this.imageURLs.addAll(imageURLs);

        List<String> stirngUrl = this.converInageUrl(imageURLs);
        this.imageUrlStrings.addAll(stirngUrl);

        notifyDataSetChanged();
    }

    private List<String> converInageUrl(List<URL> imageURL){
        List<String> urlStrings = new ArrayList<String>();
        for(URL url : imageURL){
            urlStrings.add(url.toString());
        }

        return urlStrings;
    }

    /**
     * すべてのイメージのURLの削除
     */
    public void clearImages() {
        this.imageURLs.clear();
        this.imageUrlStrings.clear();
        notifyDataSetChanged();
        this.lastIndex = -1;
    }


    /**
     * 引数で与えれたIndexのImageカードを消す
     *
     * @param index
     * @return
     */
    public URL removeImage(int index) {
        URL ret = null;
        if (this.imageURLs.size() > index && index >= 0) {
            ret = this.imageURLs.remove(index);
        }
        notifyDataSetChanged();
        return ret;
    }


    @Override
    public synchronized int getCount() {
        return this.imageURLs.size();
    }

    @Override
    public Object getItem(int i) {
        return this.imageURLs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(this.context, R.layout.image_card_row, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_card_row_imageView);
            ImageCardCache.imageView = imageView;

            TextView imageFileNameTextView = (TextView) view.findViewById(R.id.image_card_row_image_filename_textview);
            ImageCardCache.imageFileNameTextView = imageFileNameTextView;

            TextView imageFileSizeTextView = (TextView) view.findViewById(R.id.image_card_row_image_filesize_textview);
            ImageCardCache.imageFileSizeTextView = imageFileSizeTextView;

            TextView imageFileURLTextView = (TextView) view.findViewById(R.id.image_card_row_image_uri_textview);
            ImageCardCache.imageFileURLTextView = imageFileURLTextView;
        }

        //画像のURL
        final URL imageURL = this.imageURLs.get(i);
        if (imageURL == null) {
            view.setVisibility(View.INVISIBLE);

            return view;
        } else {

        }


        ImageCardCache.imageFileURLTextView.setText(imageURL.toString());
        //TextView textView = (TextView)view.findViewById(R.id.image_card_row_image_uri_textview);
        ///textView.setText(imageURL.toString());

        ImageCardCache.imageFileNameTextView.setText(UrlUtiles.getFileNameFromUrl(imageURL.getFile()));


        //画像をセット
        ImageLoader loader = ImageLoader.getInstance();


        final TextView finalImageFIleSizeTextView = (TextView) view.findViewById(R.id.image_card_row_image_filesize_textview);
        Bitmap cacheBitmap = loader.getMemoryCache().get(imageURL.toString());//MemoryCacheUtil.findCachedBitmapsForImageUri(finalImageUrl, ImageLoader.getInstance().getMemoryCache());

        final ImageView imageView = (ImageView) view.findViewById(R.id.image_card_row_imageView);
        if (cacheBitmap == null) {
            File imageFile = loader.getDiskCache().get(imageURL.toString());
            cacheBitmap = BitmapFactory.decodeFile(imageFile.toString());
            //BitmapDrawable drawableBitmap = new BitmapDrawable();
        }

        imageView.setTag(imageURL.toString());
        if (cacheBitmap != null) {
            imageView.setImageBitmap(cacheBitmap);
            finalImageFIleSizeTextView.setText((cacheBitmap.getByteCount()/1000f) + " KB");

        } else {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1024;
            options.inScaled = true;
            options.inPurgeable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            DisplayImageOptions dispConf = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .showImageOnFail(android.R.drawable.stat_notify_error)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .resetViewBeforeLoading(true)
                    .decodingOptions(options)
                            //.imageScaleType(ImageScaleType.EXACTLY)
                            //.displayer(new FadeInBitmapDisplayer(300))
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .build();


            final View finalRowView = view;
            finalRowView.setTag(imageURL.toString());
            loader.displayImage(imageURL.toString(), imageView, dispConf, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    if (view.getTag().equals(imageURL.toString())) {
                        if (finalRowView.getVisibility() == View.VISIBLE
                                && finalRowView.getTag().equals(imageURL.toString())) {
                            finalRowView.setVisibility(View.INVISIBLE);
                        } else {
                        }
                    }

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (view.getTag().equals(imageURL.toString())) {
                        if (finalRowView.getVisibility() == View.INVISIBLE
                                && finalRowView.getTag().equals(imageURL.toString())) {
                            //view.setLayoutParams(new ViewGroup.LayoutParams(context,ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,view.getLayoutParams()));
                            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                            view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            finalRowView.setVisibility(View.VISIBLE);
                        }

                    }
                    finalImageFIleSizeTextView.setText((bitmap.getByteCount()/1000f) + " byte");


                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });

            ImageCardCache.imageView.setVisibility(View.VISIBLE);
            ImageCardCache.imageView.refreshDrawableState();
            //Toast.makeText(this.context, ">>> " + imageURL.toString(), Toast.LENGTH_LONG).show();

        }

        //アニメーション
        if (i > this.lastIndex) {
            this.lastIndex = i;
            Animation animation = AnimationUtils.loadAnimation(this.context, R.anim.card_fade_in);
            view.startAnimation(animation);
        }


        //タッチListenerの設定

        ImageCardTouchListnere onClickListnere = new ImageCardTouchListnere(this.context, imageURL.toString(), this.imageUrlStrings);
        view.setOnClickListener(onClickListnere);
        return view;
    }


    /**
     * 参照へのCache
     */
    private static class ImageCardCache {

        private static ImageCardCache instance;

        //メインのImageView
        public static ImageView imageView;

        public static TextView imageFileNameTextView;

        public static TextView imageFileSizeTextView;

        public static TextView imageFileURLTextView;

        /**
         * コンストラクタ
         */
        private ImageCardCache() {

        }


    }
}
