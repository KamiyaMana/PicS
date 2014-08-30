package jp.dip.azurelapis.android.PicS.UI.BrowserFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import jp.dip.azurelapis.android.PicS.R;

/**
 * Created by kamiyama on 2014/07/09.
 */
public class BrowserFragment extends Fragment {

    private OnLoadFinishWebPage onFinishLoadWebPage;

    private WebView webView;

    public BrowserFragment(){

    }

    public void setOnFinishLoadWebPage(OnLoadFinishWebPage onLoadFinishWebPage){
        this.onFinishLoadWebPage = onLoadFinishWebPage;
    }

    /**
     * Viewの生成
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.browser_fragment, container, false);
        //WebViewの設定
        this.webView = (WebView)view.findViewById(R.id.webView);
        this.initWebView(this.webView);

        return view;

    }

    /**
     * WebViewの初期設定を行う
     */
    private void initWebView(WebView webView){
        webView.loadUrl("https://www.google.co.jp/");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new PicSWebViewClient(this.onFinishLoadWebPage));

    }

    /**
     * 現在のWebViewが戻れるかどうかを返す
     * @return
     */
    public boolean canGoBack(){
        boolean ret = false;
        return this.webView.canGoBack();

    }

    /**
     * Browserを戻る
     */
    public void goBack(){
        if(canGoBack()){
            this.webView.goBack();
        }
    }

}
