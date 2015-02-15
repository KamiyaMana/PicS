package jp.dip.azurelapis.android.PicS.UI.CommonUi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

    //各行にセットされるリスナー
    private OnRowLongClickListnere onRowLongClickListnere;

    public IconAndTextListViewAdapter(Context context){
        this.context = context;
    }

    public Bitmap defaultIconImageBitmap;
    public Drawable defaultIconImageDrawable;

    /**
     * 行をクリックされたときのリスナーのセッター
     * @param onRowLongClickListnere
     */
    public void setOnRowLongClickListnere(OnRowLongClickListnere onRowLongClickListnere) {
        this.onRowLongClickListnere = onRowLongClickListnere;
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
        TextView textView = (TextView)view.findViewById(R.id.title_textview_icon_and_text_menu_row);
        TextView urlTextView = (TextView) view.findViewById(R.id.url_textview_icon_and_text_menu_row);

        IconAndTextData iconAndTextData = this.iconAndTextDatas.get(i);

        if(iconAndTextData.getIcon() != null) {
            iconImageView.setImageDrawable(iconAndTextData.getIcon());
        }else if(defaultIconImageBitmap != null){
            iconImageView.setImageBitmap(defaultIconImageBitmap);
        }else if(defaultIconImageDrawable != null){
            iconImageView.setImageDrawable(defaultIconImageDrawable);
        }else{
            iconImageView.setVisibility(View.INVISIBLE);
        }

        textView.setText(iconAndTextData.getText());
        urlTextView.setText(iconAndTextData.getSubText());

        if(this.onRowLongClickListnere != null){
            view.setOnLongClickListener(this.onRowLongClickListnere);
        }

        return view;
    }

    /**
     * すべてのアイテムを削除する
     */
    public void clear(){
        iconAndTextDatas.clear();
    }


    //このリストビューで生成されるListViewの行に対するOnClickListnere
    public static abstract class OnRowLongClickListnere implements View.OnLongClickListener{

        /**
         * コンストラクタ
         */
        public OnRowLongClickListnere(){

        }

        private IconAndTextData iconAndTextData;

        public OnRowLongClickListnere(IconAndTextData iconAndTextData){
            this.iconAndTextData = iconAndTextData;
        }

        /**
         * 一行が押された時に呼ばれるメソッド
         * 引数に押された行のItemデータが渡される
         * @param iconAndTextData
         */
       public abstract void onRowClick(IconAndTextData iconAndTextData);

        @Override
        public boolean onLongClick(View view) {

            this.onRowClick(iconAndTextData);

            return false;
        }
    }


    /**
     * iconの設定値なしの場合に表示されるデフォルトアイコン
     * @param defaultIconImage
     */
    public void setDefaultIconImage(Drawable defaultIconImage) {
        this.defaultIconImageDrawable = defaultIconImage;
        this.defaultIconImageBitmap = null;
    }

    /**
     * iconの設定値なしの場合に表示されるデフォルトアイコン
     * @param defaultIconImage
     */
    public void setDefaultIconImage(Bitmap defaultIconImage) {
        this.defaultIconImageDrawable = null;
        this.defaultIconImageBitmap =  defaultIconImage;
    }


}
