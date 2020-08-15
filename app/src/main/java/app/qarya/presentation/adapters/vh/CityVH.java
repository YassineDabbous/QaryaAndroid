package app.qarya.presentation.adapters.vh;

import android.view.View;

import android.widget.Button;

import app.qarya.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import app.qarya.model.models.City;

import android.widget.TextView;

public class CityVH extends BaseViewHolder<City> {
    public final View mView;
    public final TextView titleView, counter;
    public Button follow;
    public City mItem;

    public CityVH(View view) {
        super(view);
        mView = view;
        titleView = (TextView) view.findViewById(R.id.title);
        counter = (TextView) view.findViewById(R.id.counter);
        follow =  view.findViewById(R.id.follow);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_category;
    }

    @Override
    public void bind(City model) {
        mItem = model;
        titleView.setText(model.getName());
        if (model.getFollowed() != null && model.getFollowed()) follow.setText(itemView.getContext().getText(R.string.unfollow));
        else follow.setText(itemView.getContext().getText(R.string.follow));
        follow.setOnClickListener(view -> {
            ((OnInteractListener<City>) listener).onInteract(mItem, Action.FOLLOW);
        });
        mView.setOnClickListener(view -> {
            ((OnInteractListener<City>) listener).onClick(model);
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + titleView.getText() + "'";
    }
}