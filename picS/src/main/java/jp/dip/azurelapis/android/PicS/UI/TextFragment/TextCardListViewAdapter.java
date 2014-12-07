package jp.dip.azurelapis.android.PicS.UI.TextFragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import jp.dip.azurelapis.android.PicS.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamiyama on 2014/07/16.
 */
public class TextCardListViewAdapter extends BaseAdapter{

    //ListViewに表示するカード内のテキスト情報
    private List<TextCardData> cardDatas;

    private Context context;

    /**
     * コンストラクタ
     */
    public TextCardListViewAdapter(Context context){
        this.context = context;
        this.cardDatas = new ArrayList<TextCardData>();
    }

    /**
     * カードデータをセットするメソッド
     * @param textCardList
     */
    public void setTextCardList(List<TextCardData> textCardList){
        this.cardDatas = textCardList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.cardDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return this.cardDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = View.inflate(this.context, R.layout.text_card_row, null);
        }

        TextCardData textCardData = this.cardDatas.get(i);

        TextView textView = (TextView)view.findViewById(R.id.text_card_row_textview);
        textView.setText(textCardData.getText());


        return view;
    }
}
