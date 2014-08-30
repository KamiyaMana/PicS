package jp.dip.azurelapis.android.PicS.UI.BrowserFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import jp.dip.azurelapis.android.PicS.R;

/**
 * Created by kamiyama on 2014/07/09.
 */
public class BrowserFragment extends Fragment {

    private OnLoadFinishWebPage onFinishLoadWebPage;

    private WebView webView;

    private ImageButton backButton;
    private ImageButton forwardButton;

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

        //ナビゲーションボタン関係
        this.backButton = (ImageButton)view.findViewById(R.id.navigation_back_button_browser_fragment);

        this.forwardButton = (ImageButton)view.findViewById(R.id.navigation_forward_button_browser_fragment);

        //Clickリスナーの設定
        //戻るボタン
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });


        //進むボタン
        this.forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goForward();
            }
        });

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

    /**
     * 履歴から進むことが可能かを返す
     * @return
     */
    public boolean canGoForward(){
        return  this.webView.canGoForward();
    }


    /**
     * browserの履歴から進む
     */
    public void goForward(){
        if(this.webView.canGoForward()){
            this.webView.goForward();
        }
    }

}
