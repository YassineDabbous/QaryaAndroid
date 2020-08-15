package app.qarya.presentation.base;

import app.qarya.presentation.adapters.vh.AdsViewHolder;
import app.qarya.utils.MyConst;

import java.util.List;

import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.OnClickItemListener;


public class MyAdapter<YModel, YViewHolder extends BaseViewHolder<YModel>> extends tn.core.presentation.base.adapters.BaseAdapter<YModel,YViewHolder,AdsViewHolder> {
    public MyAdapter(List<YModel> itemList, Class<YViewHolder> clazz) {
        super(itemList, clazz, AdsViewHolder.class, null, MyConst.ADS_AFTER);
    }

    public MyAdapter(List<YModel> itemList, Class<YViewHolder> clazz, OnClickItemListener<YModel> mListener) {
        super(itemList, clazz, AdsViewHolder.class, mListener, MyConst.ADS_AFTER);
    }

    public MyAdapter(List<YModel> itemList, Class<YViewHolder> clazz, OnClickItemListener<YModel> mListener, boolean ads) {
        super(itemList, clazz, AdsViewHolder.class, mListener, ads ? MyConst.ADS_AFTER : 0);
    }
}