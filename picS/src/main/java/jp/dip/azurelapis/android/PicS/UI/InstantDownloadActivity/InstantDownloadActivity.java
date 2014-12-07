package jp.dip.azurelapis.android.PicS.UI.InstantDownloadActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import jp.dip.azurelapis.android.PicS.R;
import jp.dip.azurelapis.android.PicS.UI.ImageFragment.ImageCardListViewAdapter;
import jp.dip.azurelapis.android.PicS.UI.ImageFragment.ImageFragment;
import jp.dip.azurelapis.android.PicS.Util.HtmlParse;

import java.net.URL;
import java.util.List;

/**
 * Created by kamiyama on 14/11/30.
 * 他アプリケーションの共有メニューからのIntentで起動する小ウインドウ
 * Dialogとして小さく出てきて、ダウンロードの指令をだしたらすぐ閉じるActivity
 * メインアプリの方への遷移はこの画面のボタンを押すことで可能にする
 */
public class InstantDownloadActivity extends FragmentActivity {


    private ListView imageCardListView;
    private ImageCardListViewAdapter imageCardListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final String url = (String)getIntent().getExtras().getCharSequence(Intent.EXTRA_TEXT);
        List<URL> imageUrls;


        final DialogFragment dialogFragment = new DialogFragment(){



            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {

                ImageFragment imageFragment;

                Dialog dialog = new Dialog(getActivity());

                dialog.setContentView(R.layout.instant_download_fragment);

                View view = dialog.findViewById(R.id.image_fragment_instant_downlod_fragment);
                imageCardListView = (ListView)view.findViewById(R.id.image_fragment_imagecard_listView);
                imageCardListViewAdapter = new ImageCardListViewAdapter(getActivity());
                imageCardListView.setAdapter(imageCardListViewAdapter);

                LoadUrlPageImagese loadUrlPageImagese = new LoadUrlPageImagese();
                loadUrlPageImagese.execute(url);


                return dialog;

            }



            @Override
            public void onDismiss(DialogInterface dialog) {
                super.onDismiss(dialog);

                //ダイアログが閉じられた時にActivityを終了する
                finish();
            }
        };


        dialogFragment.show(getSupportFragmentManager(),"instant_downloder");

    }


    /**
     * 表示する画像をセットする
     */
    public void setImages(List<URL> imageURLs){
        this.imageCardListView.smoothScrollToPosition(0);
        this.imageCardListViewAdapter.setImageList(imageURLs);
        this.imageCardListView.refreshDrawableState();
        this.imageCardListView.deferNotifyDataSetChanged();
    }

    private class LoadUrlPageImagese extends AsyncTask<String,Void,List<URL>>{


        @Override
        protected List<URL> doInBackground(String... strings) {
            String url = strings[0];

            return HtmlParse.getImatgeUrl(url);

        }

        @Override
        protected void onPostExecute(List<URL> urlLists) {
            //super.onPostExecute(strings);

            setImages(urlLists);

        }
    }



}
