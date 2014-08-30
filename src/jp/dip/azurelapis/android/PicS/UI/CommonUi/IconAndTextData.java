package jp.dip.azurelapis.android.PicS.UI.CommonUi;

/**
 * Created by kamiyama on 2014/08/14.
 */

import android.graphics.drawable.Drawable;

/**
 * アイコンとテキストの組み合わせを示すクラス
 */
public class IconAndTextData{

    private Drawable icon;
    private String text;

    public IconAndTextData(Drawable icon, String text){
        this.icon = icon;
        this.text = text;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
}