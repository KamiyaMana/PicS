package jp.dip.azurelapis.android.PicS.UI.ImageFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.dip.azurelapis.android.PicS.ImageFilter.ImageFilter;
import jp.dip.azurelapis.android.PicS.R;
import jp.dip.azurelapis.android.PicS.Util.UrlUtiles;

/**
 * Created by kamiyama on 2014/07/12.
 */
public class ImageCardListViewAdapter extends BaseAdapter implements ImageFilterble{


    private int samplingScale = 1;//画像の圧縮読み込みの割合

    private List<URL> imageURLs = new ArrayList<URL>();
    private List<String> imageUrlStrings = new ArrayList<String>();

    private Context context;

    private int lastIndex = -1;

    //イメージフィルタ判定モジュール
    private List<ImageFilter> imageFilters = new ArrayList<ImageFilter>();


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
        }

        this.imageURLs.add(Url);
        this.imageUrlStrings.add(Url.toString());
        notifyDataSetChanged();
    }

    public List<URL> getImageUrls() {
        return this.imageURLs;
    }

    /**
     * 複数の画像をまとめてセットする
     * すべてデータを追加してから再描画するので描画処理が軽くなる
     */
    public synchronized void setImageList(List<URL> imageURLs) {
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

    private List<String> converInageUrl(List<URL> imageURL) {
        List<String> urlStrings = new ArrayList<String>();
        for (URL url : imageURL) {
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
    public int getCount() {
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

        Log.d("fffffffffff","getview");
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

        try {
            if (imageURL == null) {
                view.setVisibility(View.INVISIBLE);

                return view;
            }


            ImageCardCache.imageFileURLTextView.setText(imageURL.toString());

            ImageCardCache.imageFileNameTextView.setText(UrlUtiles.getFileNameFromUrl(imageURL.getFile()));


            //画像をセット
            ImageLoader loader = ImageLoader.getInstance();


            final TextView finalImageFIleSizeTextView = ImageCardCache.imageFileSizeTextView;
            Bitmap cacheBitmap = loader.getMemoryCache().get(imageURL.toString());//MemoryCacheUtil.findCachedBitmapsForImageUri(finalImageUrl, ImageLoader.getInstance().getMemoryCache());

            final ImageView imageView = (ImageView) view.findViewById(R.id.image_card_row_imageView);
          /*
            if (cacheBitmap == null) {
                File imageFile = loader.getDiskCache().get(imageURL.toString());
                if (imageFile.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;

                    options.inSampleSize = this.samplingScale;

                    cacheBitmap = BitmapFactory.decodeFile(imageFile.toString(), options);
                }
                //BitmapDrawable drawableBitmap = new BitmapDrawable();
            }
            */

            imageView.setTag(imageURL.toString());
            if (cacheBitmap != null) {

                imageView.setImageBitmap(cacheBitmap);
                if(throwImageFiter(cacheBitmap)){
                    imageView.setVisibility(View.VISIBLE);
                }else {
                    imageView.setVisibility(View.GONE);
                }

                Log.d("fffffffffff", "gggg");

                finalImageFIleSizeTextView.setText((cacheBitmap.getByteCount() / 1000f) + " KB");

            } else {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = this.samplingScale;
                options.inScaled = false;//true;
                options.inPurgeable = false;//true;
                options.inPreferredConfig = Bitmap.Config.RGB_565;

                DisplayImageOptions dispConf = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .showImageOnFail(android.R.drawable.stat_notify_error)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .resetViewBeforeLoading(true)
                        //.decodingOptions(options)
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
                        /*
                        if (view.getTag().equals(imageURL.toString())) {

                            if (finalRowView.getVisibility() == View.VISIBLE
                                    && finalRowView.getTag().equals(imageURL.toString())) {
                                finalRowView.setVisibility(View.INVISIBLE);
                            } else {
                            }
                        }
                        */
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        if (view.getTag().equals(imageURL.toString())) {
                            if (finalRowView.getVisibility() == View.INVISIBLE
                                    && finalRowView.getTag().equals(imageURL.toString())) {

                                if(throwImageFiter(bitmap)){
                                    finalRowView.setVisibility(View.VISIBLE);
                                }else {
                                    finalRowView.setVisibility(View.GONE);
                                }

                            }

                        }

                        Log.d("fffffffffff", "RRRRRR "+ finalRowView.getVisibility() +"x : " + bitmap.getWidth() + "  y:" + bitmap.getHeight());

                        if(throwImageFiter(bitmap)){
                            finalRowView.setVisibility(View.VISIBLE);
                        }else {
                            //finalRowView.setVisibility(View.GONE);
                        }

                        finalImageFIleSizeTextView.setText("x " + bitmap.getWidth() + "y " + bitmap.getHeight() +" "+ ( bitmap.getByteCount() / 1000f) + " byte");


                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });


                // ImageCardCache.imageView.setVisibility(View.VISIBLE);
                //ImageCardCache.imageView.refreshDrawableState();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //アニメーション
/*
        if (i > this.lastIndex) {
            this.lastIndex = i;
            Animation animation = AnimationUtils.loadAnimation(this.context, R.anim.card_fade_in);
            view.startAnimation(animation);
        }

*/
        //タッチListenerの設定

        ImageCardTouchListnere onClickListnere = new ImageCardTouchListnere(this.context, imageURL.toString(), this.imageUrlStrings);
        view.setOnClickListener(onClickListnere);
        return view;
    }




    /**
     * 画像のサンプリングレートをセットする
     *
     * @param samplingScale
     */
    public void setSamplingScale(int samplingScale) {
        this.samplingScale = samplingScale;
    }


    /**
     * イメージフィルタを通す
     * @param bitmap　フィルタリング対象の画像
     * @return
     */
    @Override
    public boolean throwImageFiter(Bitmap bitmap) {

        boolean ret = true;

        //ImageFilterを取り出しすべてのFilterを通し、OKのならTrueを返す
        for(ImageFilter imageFilter : this.imageFilters){

            ret = imageFilter.isAllow(bitmap);

            //System.out.println("image size filter : x " + bitmap.getWidth() + " y : " + bitmap.getHeight());

            //Filter判定Falseの場合返す
            if(!ret){
                return false;
            }
        }

        return true;
    }

    /**
     * イメージFilterを追加する
     * @param imageFilter
     */
    public void addImageFilter(ImageFilter imageFilter){

        if(imageFilter != null) {
            this.imageFilters.add(imageFilter);
        }
    }

    /**
     * 参照へのCache
     */
    private static class ImageCardCache {

        //メインのImageView
        public static ImageView imageView;

        public static TextView imageFileNameTextView;

        public static TextView imageFileSizeTextView;

        public static TextView imageFileURLTextView;

    }
}
