package jp.dip.azurelapis.android.PicS.UI.InstantDownloadActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.TextView;
import jp.dip.azurelapis.android.PicS.R;
import jp.dip.azurelapis.android.PicS.UI.ImageFragment.ImageFragment;

import java.net.URL;
import java.util.List;

/**
 * Created by kamiyama on 14/11/30.
 * 他アプリケーションの共有メニューからのIntentで起動する小ウインドウ
 * Dialogとして小さく出てきて、ダウンロードの指令をだしたらすぐ閉じるActivity
 * メインアプリの方への遷移はこの画面のボタンを押すことで可能にする
 */
public class InstantDownloadActivity extends FragmentActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final String url = (String)getIntent().getExtras().getCharSequence(Intent.EXTRA_TEXT);
        List<URL> imageUrls;


        DialogFragment dialogFragment = new DialogFragment(){

            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {

                ImageFragment imageFragment;

                Dialog dialog = new Dialog(getActivity());


                dialog.setContentView(R.layout.instant_download_fragment);

                imageFragment = (ImageFragment)getSupportFragmentManager().findFragmentById(R.id.instant_download_image_list_fragment);

                TextView textView = (TextView)dialog.findViewById(R.id.instant_downloader_text);

                textView.setText(url);


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






}
