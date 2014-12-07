package jp.dip.azurelapis.android.PicS.UI.BrowserFragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import jp.dip.azurelapis.android.PicS.R;

/**
 * Created by kamiyama on 2014/07/09.
 */
public class BrowserFragment extends Fragment {

    private OnLoadFinishWebPage onFinishLoadWebPage;

    //webviewのロード進捗表示
    private ProgressBar progressBar;

    private WebView webView;

    private ImageButton backButton;
    private ImageButton forwardButton;

    private ImageButton urlGoButton;
    private EditText urlEditText;

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

        //進捗プログレスバー
        this.progressBar = (ProgressBar)view.findViewById(R.id.web_view_load_progressbar_horizontal);
        this.progressBar.setVisibility(View.GONE);

        //WebViewの設定
        this.webView = (WebView)view.findViewById(R.id.webView);
        this.initWebView(this.webView);

        //ナビゲーションボタン関係

        //URL表示EditText
        this.urlEditText = (EditText)getActivity().getActionBar().getCustomView().findViewById(R.id.url_text_edit_actionbar);//(EditText)view.findViewById(R.id.url_text_edit_browser_fragment);

        this.urlEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if( i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent != null && keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    if(urlEditText.getText() != null) {
                        webView.loadUrl(urlEditText.getText().toString());

                        //キーボードを隠す
                        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    }
                }

                return false;
            }
        });

        //URL表示用EditText
        this.urlEditText.setText(this.webView.getUrl());

        return view;

    }



    /**
     * WebViewの初期設定を行う
     */
    private void initWebView(WebView webView){

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.google.co.jp/");
        PicSWebViewClient webViewClient = new PicSWebViewClient(this.onFinishLoadWebPage);

        webView.setWebViewClient(webViewClient);

    }

    public void loadPage(String url){
        this.webView.loadUrl(url);


        //ローディングバー表示
        this.visibleLoadingBar();
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

    /**
     *ページのロードが終わった時の処理を記述する
     * @param url
     */
    public void onFinishLoadPage(String url){
        if(url != null) {
            this.urlEditText.setText(url);
            webView.clearCache(false);
        }

    }

    /**
     * 今表示しているページのタイトルを返す
     * @return
     */
    public String getPageTitle(){
        return this.webView.getTitle();
    }


    /**
     * 今表示しているページのURLを返す
     * @return
     */
    public String getUrl(){
        return this.webView.getUrl();
    }

    /**
     * ページのアイコンを返す
     * @return
     */
    public Bitmap getFavcon(){
        return this.webView.getFavicon();
    }


    /**
     * ロードバーの表示
     */
    public void visibleLoadingBar(){
        this.progressBar.setVisibility(View.VISIBLE);
    }

    public void invisibleLoadingBar(){
        this.progressBar.setVisibility(View.GONE);
    }
}
