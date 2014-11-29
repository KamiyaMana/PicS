package jp.dip.azurelapis.android.PicS.UI.InstantDownloadActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import jp.dip.azurelapis.android.PicS.R;

/**
 * Created by kamiyama on 14/11/30.
 * 他アプリケーションの共有メニューからのIntentで起動する小ウインドウ
 * Dialogとして小さく出てきて、ダウンロードの指令をだしたらすぐ閉じるActivity
 * メインアプリの方への遷移はこの画面のボタンを押すことで可能にする
 */
public class InstantDownloadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.instant_download_activity);

        TextView textView = (TextView)findViewById(R.id.instant_downloader_text);
        textView.setText(getIntent().getExtras().getCharSequence(Intent.EXTRA_TEXT));

    }
}
