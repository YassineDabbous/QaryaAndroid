package app.qarya.presentation.adapters.vh;

import android.view.View;
import android.widget.TextView;

import app.qarya.R;

import tn.core.presentation.base.adapters.BaseViewHolder;

public class HeaderVH extends BaseViewHolder<String> {
    public final View mView;
    public final TextView titleView;
    public String mItem;

    public HeaderVH(View view) {
        super(view);
        mView = view;
        titleView = (TextView) view.findViewById(R.id.title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_category;
    }

    @Override
    public void bind(String model) {
        mItem = model;
        titleView.setText(model);
    }
}