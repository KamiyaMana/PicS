package jp.dip.azurelapis.android.PicS.UI.TextFragment;

/**
 * Created by kamiyama on 2014/07/16.
 * textCardの関するデータを入れるクラス
 */
public class TextCardData {

    private String textSize;
    private String text;

    /**
     * コンストラクタ
     */
    public TextCardData(TextSize textSizeArg, String text){
        this.textSize = textSizeArg.toString();
        this.text = text;
    }

    public String getTextSize() {
        return textSize;
    }

    public String getText() {
        return text;
    }

    /**
     * テキストの大きさの種別を示す列挙体
     */
    public enum TextSize{

        LARGE_TEXT_SIZE,NORMAL_TEXT_SIZE,SMALL_TEXT_SIZE;

    }

}
