package jp.dip.azurelapis.android.PicS.Datas.BookMark;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * １件のブックマークを構成するクラス
 * Created by kamiyama on 2014/09/07.
 */
public class BookMark {

    private int id;
    private String url;
    private String titile;
    private Date addData;
    private Bitmap icon;

    public BookMark(){

    }

    //セッター
    public void setId(int id){
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public void setAddData(Date addData) {
        this.addData = addData;
    }

    public void setIcon(Bitmap bitmap){
        this.icon = bitmap;
    }

    //ゲッター
    public int getId(){
        return this.id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitile() {
        return titile;
    }

    public Date getAddData() {
        return addData;
    }

    public Bitmap getIcon() {
        return icon;
    }
}


