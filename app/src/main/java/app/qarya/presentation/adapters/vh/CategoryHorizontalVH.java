package app.qarya.presentation.adapters.vh;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.qarya.R;
import app.qarya.model.models.Category;
import app.qarya.utils.ImageHelper;
import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

public class CategoryHorizontalVH extends BaseViewHolder<Category> {
    public final View mView;
    public final TextView titleView;
    public Category mItem;

    public CategoryHorizontalVH(View view) {
        super(view);
        mView = view;
        titleView = (TextView) view.findViewById(R.id.title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_category_horizontal;
    }

    @Override
    public void bind(Category model) {
        mItem = model;
        titleView.setText(model.getName());
        mView.setOnClickListener(view -> {
            listener.onClick(model);
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mItem.getName() + "'";
    }
}