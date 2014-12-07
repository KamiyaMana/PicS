package jp.dip.azurelapis.android.PicS.Util;

import android.net.Uri;

/**
 * Created by kamiyama on 2014/07/12.
 * URLの相対パスを補正する機能などを提供する
 */
public class UrlUtiles {

    public static String urlSupplement(String pageHostUrl, String brokenUrl) {
        if(pageHostUrl == null || brokenUrl == null){
            return null;
        }

        Uri pageUri = Uri.parse(pageHostUrl);
        String pagePath = pageUri.getHost() + pageUri.getPath().replace("//","/");

        int lastIndex = pagePath.length();
        //最後の/のあとに.が出てくる場合
        if(pageHostUrl.lastIndexOf(".") > pagePath.lastIndexOf("/")){
            lastIndex = pagePath.lastIndexOf("/");
        }
        pagePath = pagePath.substring(0,lastIndex);


        if(brokenUrl.startsWith("http://")
                || brokenUrl.startsWith("https://")) {
            return brokenUrl;
        }

        String ret = "";
        if(brokenUrl.startsWith("/")){
            ret = pagePath + brokenUrl;
        }else{
            ret = pagePath + "/" +brokenUrl;

        }
        ret = "http://" + ret;
        return ret;
    }

    public static String getFileNameFromUrl(String url){
        if(url == null){
            return null;
        }

        String ret = null;
        if(url.lastIndexOf("/") != -1){
            ret = url.substring(url.lastIndexOf("/") + 1, url.length());
        }
        return ret;
    }

}
