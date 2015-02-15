package jp.dip.azurelapis.android.PicS.UI.ImageFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.net.URL;
import java.util.List;

import jp.dip.azurelapis.android.PicS.ImageFilter.ImageSizeFilter;
import jp.dip.azurelapis.android.PicS.R;

/**
 * Created by kamiyama on 2014/07/09.
 * 画像一覧を表示するFragment
 */
public class ImageFragment extends Fragment {

    private View view;

    //findViewの処理を省くためにメンバーとして参照を保持する

    private GridView imageGridView;

    private ListView imageCardListView;
    private ImageCardListViewAdapter imageCardListViewAdapter;

    //イメージフィルタメニュー関連
    private ImageButton imageFilterButton;

    //メニュー関連
    private ImageButton imageGridViewChangeButton;
    private ImageButton imageListViewChangeButton;


    //イメージファイル

    //イメージフィルタにセットする最小基準Xサイズ
    private int imageSizeFilterX = 600;

    //イメージフィルタにセットする最小基準Yサイズ
    private int imageSizeFilterY = 400;

    //サイズイメージフィルタ
    private ImageSizeFilter imageSizeFilter = new ImageSizeFilter(imageSizeFilterX,imageSizeFilterY,false);



    public ImageFragment(){
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.image_fragment, container, false);
        this.view = view;


        //画像表示グリッド
        this.imageCardListView = (ListView)view.findViewById(R.id.image_fragment_imagecard_listView);
        this.imageGridView = (GridView)view.findViewById(R.id.image_fragment_image_grid_view);

        this.imageCardListViewAdapter = new ImageCardListViewAdapter(getActivity());
        //ImageFilter追加
        this.imageCardListViewAdapter.addImageFilter(imageSizeFilter);


        this.imageCardListView.setAdapter(this.imageCardListViewAdapter);


        //メニュー関連
        this.imageListViewChangeButton = (ImageButton)view.findViewById(R.id.image_list_change_image_button_image_fragment);
        this.imageListViewChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageListViewShow();
            }
        });

        this.imageGridViewChangeButton = (ImageButton)view.findViewById(R.id.image_grid_view_change_image_button_image_fragment);
        this.imageGridViewChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageGridViewShow();
            }
        });


        //イメージフィルター
        this.imageFilterButton = (ImageButton)view.findViewById(R.id.image_filter_change_image_button_image_fragment);
        this.imageFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageFilterSelecterFragmentDialog imageFilterSelecterFragmentDialog = new ImageFilterSelecterFragmentDialog();
                imageFilterSelecterFragmentDialog.show(getActivity().getSupportFragmentManager(),"image_filter_selecter_dialog");
            }
        });



        //Intent intent = new Intent();
        //intent.setClassName("jp.dip.azurelapis.android.PicS.",
        //        " jp.dip.azurelapis.android.PicS.UI.ImaegViewActivity.ImageViewActivity");
        //this.imageCardListViewAdapter.getImageUrls();

        //this.imageCardListView.setOnItemSelectedListener(new ImageCardTouchListnere(getActivity(), ));

        return view;
    }

    /**
     * 表示する画像をセットする
     */
    public void setImages(List<URL> imageURLs){
        this.imageCardListView.smoothScrollToPosition(0);
        this.imageCardListViewAdapter.setImageList(imageURLs);
        this.imageCardListView.refreshDrawableState();
        this.imageCardListView.deferNotifyDataSetChanged();
    }


    /**
     * イメージのサムネイルをグリッドビューで表示に切り替える
     */
    private void imageGridViewShow(){

        this.imageCardListView.setVisibility(View.GONE);
        this.imageGridView.setVisibility(View.VISIBLE);

        this.imageGridView.setAdapter(this.imageCardListViewAdapter);
        this.imageCardListView.setAdapter(null);

    }


    /**
     * イメージのサムネイルをリストビューで表示に切り替える
     */
    private void imageListViewShow(){

        this.imageGridView.setVisibility(View.GONE);
        this.imageCardListView.setVisibility(View.VISIBLE);


        this.imageCardListView.setAdapter(this.imageCardListViewAdapter);
        this.imageGridView.setAdapter(null);
    }




}
