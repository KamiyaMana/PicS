package jp.dip.azurelapis.android.PicS.UI.ImageFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import jp.dip.azurelapis.android.PicS.R;

import java.net.URL;
import java.util.List;

/**
 * Created by kamiyama on 2014/07/09.
 * 画像一覧を表示するFragment
 */
public class ImageFragment extends Fragment {

    View view;

    //findViewの処理を省くためにメンバーとして参照を保持する
    ListView imageCardListView;
    ImageCardListViewAdapter imageCardListViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.image_fragment, container, false);
        this.view = view;
        this.imageCardListView = (ListView)view.findViewById(R.id.image_fragment_imagecard_listView);
        this.imageCardListViewAdapter = new ImageCardListViewAdapter(getActivity());
        this.imageCardListView.setAdapter(this.imageCardListViewAdapter);

        Intent intent = new Intent();
        intent.setClassName("jp.dip.azurelapis.android.PicS.",
                " jp.dip.azurelapis.android.PicS.UI.ImaegViewActivity.ImageViewActivity");
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



}
