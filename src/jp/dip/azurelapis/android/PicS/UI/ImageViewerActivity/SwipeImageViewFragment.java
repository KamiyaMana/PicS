package jp.dip.azurelapis.android.PicS.UI.ImageViewerActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import jp.dip.azurelapis.android.PicS.R;
import jp.dip.azurelapis.android.PicS.UI.CommonUi.IconAndTextData;
import jp.dip.azurelapis.android.PicS.UI.CommonUi.IconAndTextListViewAdapter;
import uk.co.senab.photoview.PhotoViewAttacher;

import java.io.File;

/**
 * Created by kamiyama on 2014/07/21.
 * スワイプして画面を移動できるImageViewr用Fragment
 */
public class SwipeImageViewFragment extends Fragment {


    private PhotoViewAttacher photViewAttacher;

    private ImageView imageView;

    private String imageUrl;

    public SwipeImageViewFragment(){

    }

    public SwipeImageViewFragment(String url) {
        this.imageUrl = url;

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

        if (cacheBitmap == null) {
            File imageFile = loader.getDiskCache().get(imageURL);

            if(imageFile.exists()) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;

                options.inSampleSize = 4;
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
            options.inSampleSize = 4;
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
                DialogFragment dialog = new popuoMenu();
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

    /**
     * 画像長押しで表示されるポップアップメニュー
     */
    public class popuoMenu extends DialogFragment {

        private static final String IMAGE_DOWNLOAD_MENU_TEXT = "画像を保存";

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            //ポップアップメニューに表示する内容を表示するView
            View menuView = View.inflate(getActivity(), R.layout.popup_photoview_menu, null);

            //メニュー一覧を表示するリストビュー
            ListView menuListView = (ListView) menuView.findViewById(R.id.menulistview_popup_photoview_menu);

            IconAndTextListViewAdapter iconAndTextListViewAdapter = new IconAndTextListViewAdapter(getActivity());

            Resources res = getResources();
            Bitmap saveIcon = BitmapFactory.decodeResource(res, android.R.drawable.ic_menu_save);

            iconAndTextListViewAdapter.addMenuItem(new IconAndTextData(new BitmapDrawable(saveIcon),
                    IMAGE_DOWNLOAD_MENU_TEXT));
            menuListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        //画像を保存するメニューが押された
                        Uri uri = Uri.parse(imageUrl);
                        DownloadManager.Request request = new DownloadManager.Request(uri);

                        //保存ファイル名
                        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                        request.setTitle(fileName);

                        //DLに使う回線種別
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                                | DownloadManager.Request.NETWORK_WIFI);

                        request.setShowRunningNotification(true);
                        request.setVisibleInDownloadsUi(true);

                        DownloadManager downLoadManager = (DownloadManager) getActivity().getSystemService(Activity.DOWNLOAD_SERVICE);
                        downLoadManager.enqueue(request);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            menuListView.setAdapter(iconAndTextListViewAdapter);


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // タイトル
            builder.setTitle("画像メニュー");
            // メニューのリスト
            builder.setView(menuView);
            // 閉じるボタン
            builder.setPositiveButton("閉じる", null);

            Dialog dialog = builder.create();


            return dialog;
            //return super.onCreateDialog(savedInstanceState);
        }
    }
}
