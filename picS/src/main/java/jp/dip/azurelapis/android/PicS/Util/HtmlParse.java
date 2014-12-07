package jp.dip.azurelapis.android.PicS.Util;

import android.util.Log;
import jp.dip.azurelapis.android.PicS.UI.TextFragment.TextCardData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamiyama on 2014/07/20.
 * HTMLをParseして必要な情報だけをフィルタリングするクラス
 */
public class HtmlParse {

    public static List<URL> getImatgeUrl(String pageUrl){
        List<URL> imageURLs = new ArrayList<URL>();

        Document document = null;//Jsoup.parse(html);
        try {
            Log.d("loadHTML", pageUrl);
            document = Jsoup.connect(pageUrl).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Elements imgElements = document.getElementsByTag("img");

        for (Element element : imgElements) {
            for (Element tempElement : element.getElementsByAttribute("src")) {
                String imageUrl = tempElement.attr("src");
                if (imageUrl != null) {
                    System.out.println(imageUrl);

                    String imageMasterUrl = UrlUtiles.urlSupplement(pageUrl, imageUrl);
                    try {
                        System.out.println(">> try make url : " + imageMasterUrl) ;
                        imageURLs.add(new URL(imageMasterUrl));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return imageURLs;

    }

    public static List<TextCardData> gtTextCardDatas(String pageUrl){

        List<TextCardData> textCardDatas = new ArrayList<TextCardData>();

        Document document = null;//Jsoup.parse(html);
        try {
            Log.d("loadHTML", pageUrl);
            document = Jsoup.connect(pageUrl).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //見出しの取得
        Elements h1Elements = document.getElementsByTag("h1");
        for(Element h1 : h1Elements){
            TextCardData textCardData = new TextCardData(TextCardData.TextSize.LARGE_TEXT_SIZE,h1.text());
            textCardDatas.add(textCardData);

        }

        //見出し2の取得
        Elements h2Elements = document.getElementsByTag("h2");
        for(Element h2 : h2Elements){
            TextCardData textCardData = new TextCardData(TextCardData.TextSize.LARGE_TEXT_SIZE,h2.text());
            textCardDatas.add(textCardData);

        }

        //見出し3の取得
        Elements h3Elements = document.getElementsByTag("h3");
        //System.out.println("<><><><" + h3Elements.size());
        for(Element h3 : h3Elements){

            TextCardData textCardData = new TextCardData(TextCardData.TextSize.NORMAL_TEXT_SIZE,h3.text());
            textCardDatas.add(textCardData);

        }

        //見出し4の取得
        Elements h4Elements = document.getElementsByTag("h4");
        for(Element h4 : h4Elements){
            TextCardData textCardData = new TextCardData(TextCardData.TextSize.NORMAL_TEXT_SIZE,h4.text());
            textCardDatas.add(textCardData);

        }


        //見出し5の取得
        Elements h5Elements = document.getElementsByTag("h5");
        for(Element h5 : h5Elements){
            TextCardData textCardData = new TextCardData(TextCardData.TextSize.SMALL_TEXT_SIZE,h5.text());
            textCardDatas.add(textCardData);

        }

        //見出し6の取得
        Elements h6Elements = document.getElementsByTag("h6");
        for(Element h6 : h6Elements){
            TextCardData textCardData = new TextCardData(TextCardData.TextSize.SMALL_TEXT_SIZE,h6.text());
            textCardDatas.add(textCardData);

        }

        //全文
        String lnString = document.text().replace("<br>","\n");

        TextCardData textCardData = new TextCardData(TextCardData.TextSize.SMALL_TEXT_SIZE, lnString);
        textCardDatas.add(textCardData);


        return textCardDatas;
    }

}
