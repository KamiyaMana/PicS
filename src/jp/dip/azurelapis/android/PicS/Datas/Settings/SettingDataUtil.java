package jp.dip.azurelapis.android.PicS.Datas.Settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kamiyama on 2014/09/07.
 * プレファレンスへ設定を保存、取り出し
 */

public class SettingDataUtil {

    private Context contex;


    //例外メッセージ
    private static final String CONTEXT_ARG_NULL = "コンストラクタに渡された引数CONTEXTがNULLです";

    private String prefName; //このクラスで扱うPreferenceの名前

    /**
     * コンストラクタ
     */
    public SettingDataUtil(Context contex, String prefName){
        if(contex == null || prefName == null){
            throw new NullPointerException(CONTEXT_ARG_NULL);
        }

        this.contex = contex;
        this.prefName = prefName;
    }


    public void saveString(String title, String value){

        SharedPreferences.Editor editor = this.getEditor(this.prefName);
        editor.putString(title, value);

        editor.commit();


    }


    /**
     * 指定したデータを削除する
     */
    public void remove(String title){

        SharedPreferences.Editor editor = this.getEditor(this.prefName);

        editor.remove(title);
        editor.commit();


    }


    /**
     * キーに設置されたPreferenceに値が入ってるかを確認する<br>
     * 値が入っていればtrueを返す
     * @param titile
     * @return
     */
    public boolean isInValue(String titile) {

        if (this.loadString(titile) == null) {
            return false;
        } else{
            return true;
        }

    }


    /**
     * 指定したキーで値を撮る
     * デフォルト値として空文字を返す
     * @param title
     * @return
     */
    public String loadString(String title){
        String ret = null;

        ret = this.getPreference(this.prefName).getString(title,"");

        return ret;
    }


    private SharedPreferences getPreference(String preferenceName){

        SharedPreferences preference = null;

        preference = this.contex.getSharedPreferences(preferenceName ,Context.MODE_PRIVATE);

        return  preference;


    }

    private SharedPreferences.Editor getEditor(String preferenceName){

        SharedPreferences.Editor retEditor = null;

        retEditor = this.getPreference(preferenceName).edit();

        return retEditor;
    }


}


