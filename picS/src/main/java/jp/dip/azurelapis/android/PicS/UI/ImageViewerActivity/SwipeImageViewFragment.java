package jp.dip.azurelapis.android.PicS.UI.ImageViewerActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

import jp.dip.azurelapis.android.PicS.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by kamiyama on 2014/07/21.
 * スワイプして画面を移動できるImageViewr用Fragment
 */
public class SwipeImageViewFragment extends Fragment {


    public static final String URL_BUNDLE_KRY = "URL_BUNDLE_KRY";

    private PhotoViewAttacher photViewAttacher;

    private ImageView imageView;

    private String imageUrl;

    public SwipeImageViewFragment(){

    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        if(args.getString(URL_BUNDLE_KRY) != null) {
            this.imageUrl = args.getString(URL_BUNDLE_KRY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.swipe_image_viewer_fragment, container, false);

        this.imageView = (ImageView) view.findViewById(R.id.image_view_swipe_image_view_fragment);
        //System.out.println("ggg" + this.imageView);
        this.photViewAttacher = new PhotoViewAttacher(imageView);

        //画像URLを準備
        final String imageURL = this.imageUrl;

        //画像をセット
        ImageLoader loader = ImageLoader.getInstance();

        Bitmap cacheBitmap = loader.getMemoryCache().get(imageURL);//MemoryCacheUtil.findCachedBitmapsForImageUri(finalImageUrl, ImageLoader.getInstance().getMemoryCache());

        if (cacheBitmap == null && imageURL != null) {
            File imageFile = loader.getDiskCache().get(imageURL);

            if(imageFile.exists()) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;

                //options.inSampleSize = 4;
                try {
                    cacheBitmap = BitmapFactory.decodeFile(imageFile.toString(), options);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


        final ImageView imageView = this.imageView;
        //imageView.setTag(imageURL);
        if (cacheBitmap != null) {
            //System.out.println(cacheBitmap);
            //System.out.println(this.cacheFargment);
            imageView.setImageBitmap(cacheBitmap);
            //this.cacheFargment.setImage(new BitmapDrawable(cacheBitmap));
        } else {

            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 4;
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


            //final View finalRowView = this.cacheFargment.getImageView();
            //finalRowView.setTag(imageURL);
            loader.loadImage(imageURL, dispConf, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    //ImageView imageView = (ImageView)view;
                    Resources res = getActivity().getResources();
                    Bitmap loadingIcon = BitmapFactory.decodeResource(res, android.R.drawable.stat_notify_sync);
                    photViewAttacher.getImageView().setImageDrawable(new BitmapDrawable(loadingIcon));

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {


                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    /*if (view.getVisibility() == View.INVISIBLE) {
                        //view.setLayoutParams(new ViewGroup.LayoutParams(context,ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,view.getLayoutParams()));
                        //view.setImageDrawable(new BitmapDrawable(bitmap));
                        view.setVisibility(View.VISIBLE);

                    }*/
                    photViewAttacher.cleanup();
                    imageView.setImageBitmap(bitmap);
                    photViewAttacher = new PhotoViewAttacher(imageView);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });


        }


        //ポップアップメニュー用のロングClickListenerの設定
        this.photViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogFragment dialog = new PopupMenu();

                Bundle imageUrlParm = new Bundle();
                imageUrlParm.putString(PopupMenu.IMAGE_URL_ARG, imageUrl);
                dialog.setArguments(imageUrlParm);
                dialog.show(getFragmentManager(), "imagemenu");
                return false;
            }
        });

        return view;
    }

    /**
     * ImageViewに表示する画像のセットをする
     */
    public void setImage(Drawable image) {
        this.photViewAttacher.getImageView().setImageDrawable(image);

    }

    public ImageView getImageView() {
        return this.imageView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.photViewAttacher.cleanup();
        this.imageView.setImageDrawable(null);
        this.imageView = null;

    }
}
