package jp.dip.azurelapis.android.PicS.UI.TextFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import jp.dip.azurelapis.android.PicS.R;

import java.util.List;

/**
 * Created by kamiyama on 2014/07/09.
 */
public class TextFragment extends Fragment {

    private View view;

    private ListView textCardListView;

    private TextCardListViewAdapter textCardListViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = View.inflate(getActivity(), R.layout.text_fragment,null);
        this.textCardListView = (ListView)this.view.findViewById(R.id.text_fragment_imagecard_listView);

        this.textCardListViewAdapter = new TextCardListViewAdapter(this.getActivity());
        this.textCardListView.setAdapter(this.textCardListViewAdapter);

        return view;
    }

    /**
     * テキストカードをセットする
     * @param textCardDatas
     */
    public void setTextCards(List<TextCardData> textCardDatas){
        this.textCardListViewAdapter.setTextCardList(textCardDatas);


    }






}
