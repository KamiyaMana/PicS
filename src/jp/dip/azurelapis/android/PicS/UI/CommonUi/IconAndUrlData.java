package jp.dip.azurelapis.android.PicS.UI.CommonUi;

/**
 * Created by kamiyama on 2014/08/14.
 */

import android.graphics.drawable.Drawable;

/**
 * アイコンとテキストの組み合わせを示すクラス
 */
public class IconAndUrlData {

    private Drawable icon;
    private String text;
    private String url;

    public IconAndUrlData(Drawable icon, String text, String url){
        this.icon = icon;
        this.text = text;
        this.url = url;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }
}