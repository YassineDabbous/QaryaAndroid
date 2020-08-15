package app.qarya.presentation.adapters.vh;

import android.view.View;
import android.widget.ImageView;

import app.qarya.R;
import app.qarya.model.models.App;
import tn.core.presentation.base.adapters.BaseViewHolder;
import app.qarya.utils.ImageHelper;

import android.widget.TextView;

public class VHApps extends BaseViewHolder<App> {
    public final View mView;
    public final TextView title;
    public ImageView thumbnail;
    public ImageView overflow;

    public VHApps(View view) {
        super(view);
        mView = view;
        title = view.findViewById(R.id.title);
        thumbnail = view.findViewById(R.id.thumbnail);
        overflow = view.findViewById(R.id.overflow);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_square;
    }

    @Override
    public void bind(final App model) {
        overflow.setVisibility(View.GONE);
        mView.findViewById(R.id.description).setVisibility(View.GONE);

        title.setText(model.getName());
        ImageHelper.load(thumbnail,model.getLogo());
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(model);
            }
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + title.getText() + "'";
    }
}