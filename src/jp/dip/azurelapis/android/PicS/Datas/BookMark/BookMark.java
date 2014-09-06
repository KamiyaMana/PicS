package jp.dip.azurelapis.android.PicS.Datas.BookMark;

import java.util.Date;

/**
 * １件のブックマークを構成するクラス
 * Created by kamiyama on 2014/09/07.
 */
public class BookMark {

    private String url;
    private String titile;
    private Date addData;

    public BookMark(){

    }

    //セッター
    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public void setAddData(Date addData) {
        this.addData = addData;
    }

    //ゲッター
    public String getUrl() {
        return url;
    }

    public String getTitile() {
        return titile;
    }

    public Date getAddData() {
        return addData;
    }
}
