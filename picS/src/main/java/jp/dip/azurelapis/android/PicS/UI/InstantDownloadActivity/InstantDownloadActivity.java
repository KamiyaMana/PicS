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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private GridView imageGridView;

    private ImageCardListViewAdapter imageCardListViewAdapter;


    //メニュー関連
    private ImageButton imageGridViewChangeButton;
    private ImageButton imageListViewChangeButton;


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

                dialog.setTitle(R.string.instant_download_dialog_title);


                View view = dialog.findViewById(R.id.image_fragment_instant_downlod_fragment);

                imageGridViewChangeButton = (ImageButton)view.findViewById(R.id.image_grid_view_change_image_button_image_fragment);
                imageGridViewChangeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageGridViewShow();
                    }
                });

                imageListViewChangeButton = (ImageButton)view.findViewById(R.id.image_list_change_image_button_image_fragment);
                imageListViewChangeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageListViewShow();
                    }
                });

                imageCardListView = (ListView)view.findViewById(R.id.image_fragment_imagecard_listView);
                imageCardListView.setVisibility(View.VISIBLE);

                imageGridView = (GridView)view.findViewById(R.id.image_fragment_image_grid_view);
                imageGridView.setVisibility(View.GONE);

                imageCardListViewAdapter = new ImageCardListViewAdapter(getActivity());
                imageCardListView.setAdapter(imageCardListViewAdapter);

                TextView urlTextView = (TextView)dialog.findViewById(R.id.instant_downloader_url_text);
                urlTextView.setText(url);

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


    /**
     * イメージのサムネイルをグリッドビューで表示に切り替える
     */
    private void imageGridViewShow(){

        this.imageCardListView.setVisibility(View.GONE);
        this.imageGridView.setVisibility(View.VISIBLE);

        this.imageGridView.setAdapter(this.imageCardListViewAdapter);
        this.imageCardListView.setAdapter(null);

    }


    /**
     * イメージのサムネイルをリストビューで表示に切り替える
     */
    private void imageListViewShow(){

        this.imageGridView.setVisibility(View.GONE);
        this.imageCardListView.setVisibility(View.VISIBLE);


        this.imageCardListView.setAdapter(this.imageCardListViewAdapter);
        this.imageGridView.setAdapter(null);
    }


}
