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
    private List<IconAndUrlData> iconAndUrlDatas = new ArrayList<IconAndUrlData>();

    //各行にセットされるリスナー
    private OnRowLongClickListnere onRowLongClickListnere;

    public IconAndTextListViewAdapter(Context context){
        this.context = context;
    }

    /**
     * 行をクリックされたときのリスナーのセッター
     * @param onRowLongClickListnere
     */
    public void setOnRowLongClickListnere(OnRowLongClickListnere onRowLongClickListnere) {
        this.onRowLongClickListnere = onRowLongClickListnere;
    }

    /**
     * メニューに追加
     * @param iconAndUrlData
     */
    public void addMenuItem(IconAndUrlData iconAndUrlData){
        this.iconAndUrlDatas.add(iconAndUrlData);
    }


    @Override
    public int getCount() {

        return this.iconAndUrlDatas.size();
    }

    @Override
    public Object getItem(int i) {

        return this.iconAndUrlDatas.get(i);
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
        TextView textView = (TextView)view.findViewById(R.id.title_textview_icon_and_text_menu_row);
        TextView urlTextView = (TextView) view.findViewById(R.id.url_textview_icon_and_text_menu_row);

        IconAndUrlData iconAndUrlData = this.iconAndUrlDatas.get(i);

        iconImageView.setImageDrawable(iconAndUrlData.getIcon());
        textView.setText(iconAndUrlData.getText());
        urlTextView.setText(iconAndUrlData.getUrl());

        if(this.onRowLongClickListnere != null){
            view.setOnLongClickListener(this.onRowLongClickListnere);
        }

        return view;
    }

    /**
     * すべてのアイテムを削除する
     */
    public void clear(){
        iconAndUrlDatas.clear();
    }


    //このリストビューで生成されるListViewの行に対するOnClickListnere
    public static abstract class OnRowLongClickListnere implements View.OnLongClickListener{

        /**
         * コンストラクタ
         */
        public OnRowLongClickListnere(){

        }

        private IconAndUrlData iconAndUrlData;

        public OnRowLongClickListnere(IconAndUrlData iconAndUrlData){
            this.iconAndUrlData = iconAndUrlData;
        }

        /**
         * 一行が押された時に呼ばれるメソッド
         * 引数に押された行のItemデータが渡される
         * @param iconAndUrlData
         */
       public abstract void onRowClick(IconAndUrlData iconAndUrlData);

        @Override
        public boolean onLongClick(View view) {

            this.onRowClick(iconAndUrlData);

            return false;
        }
    }

}
