package jp.dip.azurelapis.android.PicS.UI.BrowserFragment;

/**
 * Created by kamiyama on 2014/07/12.
 * webViewのロードが終わった時に呼ばれるメソッドを定義するインターフェイス
 */
public interface OnLoadFinishWebPage {
    public abstract void onLoadFinishWebPage(String url, String html);
}
