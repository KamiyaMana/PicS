package jp.dip.azurelapis.android.PicS.UI.CommonUi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import jp.dip.azurelapis.android.PicS.R;

import java.util.ArrayList;
import java.util.List;

/**
 * アイコンとテキストを表示するListViewAdapter
 * メニューとかに使えるね
 * Created by kamiyama on 2014/08/13.
 */
public class IconAndTextListViewAdapter extends BaseAdapter{

    private Context context;
    private List<IconAndTextData> iconAndTextDatas = new ArrayList<IconAndTextData>();


    public IconAndTextListViewAdapter(Context context){
        this.context = context;
    }

    /**
     * メニューに追加
     * @param iconAndTextData
     */
    public void addMenuItem(IconAndTextData iconAndTextData){
        this.iconAndTextDatas.add(iconAndTextData);
    }


    @Override
    public int getCount() {

        return this.iconAndTextDatas.size();
    }

    @Override
    public Object getItem(int i) {

        return this.iconAndTextDatas.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = View.inflate(this.context, R.layout.icon_and_text_menu_row, null);

        }

        ImageView iconImageView = (ImageView)view.findViewById(R.id.icon_imageview_icon_and_text_menu_row);
        TextView textView = (TextView)view.findViewById(R.id.textview_icon_and_text_menu_row);

        IconAndTextData iconAndTextData = this.iconAndTextDatas.get(i);

        iconImageView.setImageDrawable(iconAndTextData.getIcon());
        textView.setText(iconAndTextData.getText());

        return view;
    }

    /**
     * すべてのアイテムを削除する
     */
    public void clear(){
        iconAndTextDatas.clear();
    }


}
