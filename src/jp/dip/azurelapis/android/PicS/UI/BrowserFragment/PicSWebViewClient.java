package jp.dip.azurelapis.android.PicS.UI.BrowserFragment;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by kamiyama on 2014/07/12.
 */
public class PicSWebViewClient extends WebViewClient {


    private String html;
    private OnLoadFinishWebPage onLoadFinishWebPage;


    public PicSWebViewClient(OnLoadFinishWebPage onLoadFinishWebPage) {

        this.onLoadFinishWebPage = onLoadFinishWebPage;
    }



    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if(onLoadFinishWebPage == null){
            return;
        }

        onLoadFinishWebPage.onLoadFinishWebPage(url, this.getHtml());
    }



    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        view.addJavascriptInterface(this, "web");
        //view.loadUrl("javascript:window.web.setHtml(document.documentElement.outerHTML);");

        //onLoadFinishWebPage.onLoadFinishWebPage(view.getUrl(), this.getHtml());
    }

    /**
     * 表示中のWebページのHTMｌを返す
     *
     * @return
     */
    public String getHtml() {
        return this.html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

}
