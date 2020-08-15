package app.qarya.presentation.adapters.vh;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.qarya.R;
import app.qarya.model.models.Commun;

import tn.core.presentation.base.adapters.BaseViewHolder;

public class SharerVH extends BaseViewHolder<Commun> {
    public final View mView;
    public final TextView titleView, counter;
    public Button follow;
    public ImageView icon;
    public Commun mItem;

    public SharerVH(View view) {
        super(view);
        mView = view;
        titleView = (TextView) view.findViewById(R.id.title);
        counter = (TextView) view.findViewById(R.id.counter);
        follow =  view.findViewById(R.id.follow);
        icon =  view.findViewById(R.id.icon);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_category;
    }

    @Override
    public void bind(Commun model) {
        mItem = model;

        titleView.setText(model.getName()!=null ? model.getName() : model.getUname());
        follow.setVisibility(View.VISIBLE);
        follow.setText(itemView.getContext().getText(R.string.action_send));
        follow.setOnClickListener(view -> {
            listener.onClick(mItem);
        });
        mView.setOnClickListener(view -> {
            listener.onClick(mItem);
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + titleView.getText() + "'";
    }
}