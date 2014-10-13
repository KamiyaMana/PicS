package jp.dip.azurelapis.android.PicS.UI.BrowserFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import jp.dip.azurelapis.android.PicS.R;

/**
 * Created by kamiyama on 2014/07/09.
 */
public class BrowserFragment extends Fragment {

    private OnLoadFinishWebPage onFinishLoadWebPage;

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
        //WebViewの設定
        this.webView = (WebView)view.findViewById(R.id.webView);
        this.initWebView(this.webView);

        //ナビゲーションボタン関係
        //this.backButton = (ImageButton)view.findViewById(R.id.navigation_back_button_browser_fragment);

        //this.forwardButton = (ImageButton)view.findViewById(R.id.navigation_forward_button_browser_fragment);

        //URL表示EditText
        this.urlEditText = (EditText)getActivity().getActionBar().getCustomView().findViewById(R.id.url_text_edit_actionbar);//(EditText)view.findViewById(R.id.url_text_edit_browser_fragment);

        this.urlEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


                Log.v("hhhhh" , "aaaaaa");
                if( i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent != null && keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    if(urlEditText.getText() != null) {
                        webView.loadUrl(urlEditText.getText().toString());

                        //キーボードを隠す
                        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(textView.getWindowToken(), 0);
                        Log.v("hhhhh" , "aaaaaa");
                    }
                }

                return false;
            }
        });


        //URL横の進むボタン
        //this.urlGoButton = (ImageButton)view.findViewById(R.id.url_go_button_browser_fragment);


        //Clickリスナーの設定
        //戻るボタン
/*        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
*/

        //進むボタン
        /*this.forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goForward();
            }
        });*/


        //URL表示用EditText
        this.urlEditText.setText(this.webView.getUrl());

        //URL横の進むボタン
        /*this.urlGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(urlEditText.getText() != null) {
                    webView.loadUrl(urlEditText.getText().toString());
                }
            }
        });*/

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

}
