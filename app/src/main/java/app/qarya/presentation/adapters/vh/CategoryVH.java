package app.qarya.presentation.adapters.vh;


import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import app.qarya.R;
import app.qarya.model.models.Category;
import app.qarya.utils.ImageHelper;

import android.widget.TextView;

public class CategoryVH extends BaseViewHolder<Category> {
    public final View mView;
    public final TextView titleView, counter;
    public Button follow;
    public ImageView icon;
    public Category mItem;

    public CategoryVH(View view) {
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
    public void bind(Category model) {
        mItem = model;
        titleView.setText(model.getName());
        if (model.getIcon()!=null && model.getIcon()!=""){
            icon.setVisibility(View.VISIBLE);
            ImageHelper.load(icon, model.getIcon());
        }
        if (model.getCount()!=null && model.getCount()>0){
            counter.setVisibility(View.VISIBLE);
            counter.setText(model.getCount().toString());
        }
        if (model.getFollowable()!=null && model.getFollowable()!=0){
            follow.setVisibility(View.VISIBLE);
            if (model.getFollowed()) follow.setText(itemView.getContext().getText(R.string.unfollow));
            else follow.setText(itemView.getContext().getText(R.string.follow));
            follow.setOnClickListener(view -> {
                ((OnInteractListener<Category>) listener).onInteract(mItem, Action.FOLLOW);
            });
        }
        mView.setOnClickListener(view -> {
            listener.onClick(model);
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + titleView.getText() + "'";
    }
}