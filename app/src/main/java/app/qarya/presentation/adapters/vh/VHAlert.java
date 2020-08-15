package app.qarya.presentation.adapters.vh;


import android.view.View;

import app.qarya.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.util.TextUtils;

import app.qarya.model.models.Alert;
import android.widget.TextView;

public class VHAlert extends BaseViewHolder<Alert> {
    public final View mView;
    public final TextView message, type, date;

    @Override
    public int getLayoutId() {
        return R.layout.item_alert;
    }

    public VHAlert(View view) {
        super(view);
        mView = view;
        message = mView.findViewById(R.id.message);
        type = mView.findViewById(R.id.type);
        date = mView.findViewById(R.id.date);
    }

    @Override
    public void bind(Alert item) {
        TextUtils.htmlToView(message, item.getTitle());
        TextUtils.htmlToViewNonClickable(type, item.getType());
        date.setText(item.getCreatedAt()+"");
    }
}