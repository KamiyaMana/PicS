package jp.dip.azurelapis.android.PicS.UI.ImageFragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import jp.dip.azurelapis.android.PicS.UI.ImageViewerActivity.SwipeImageViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamiyama on 2014/08/03.
 * Imageカードがタップされたときの動作を記述する
 */
public class ImageCardTouchListnere implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private Context context;
    private Intent intent;

    public ImageCardTouchListnere(Context context, String activeImageUrlString, List<String> imageUrls){
        this.context = context;


        intent = new Intent();
        intent.setClassName(this.context,
                "jp.dip.azurelapis.android.PicS.UI.ImageViewerActivity.SwipeImageViewActivity");
        intent.putExtra(SwipeImageViewActivity.ACTIVE_IMAGE_URL,activeImageUrlString);
        intent.putExtra(SwipeImageViewActivity.IMAGE_URLS, (java.io.Serializable) imageUrls);
    }






    public ImageCardTouchListnere(Context context, String imageUrlString){
        this.context = context;


        List<String> urls = new ArrayList<String>();
        urls.add(imageUrlString);

        intent = new Intent();
        intent.setClassName(this.context,
                "jp.dip.azurelapis.android.PicS.UI.ImageViewerActivity.SwipeImageViewActivity");
        intent.putExtra(SwipeImageViewActivity.ACTIVE_IMAGE_URL,imageUrlString);
        intent.putExtra(SwipeImageViewActivity.IMAGE_URLS, (java.io.Serializable) urls);
    }



    @Override
    public void onClick(View view) {
        //画像ビューへ飛ぶ


        this.context.startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //画像ビューへ飛ぶ
        this.context.startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
