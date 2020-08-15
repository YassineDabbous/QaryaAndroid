package app.qarya.presentation.adapters.vh;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.qarya.R;
import app.qarya.model.models.Relation;
import app.qarya.presentation.base.MyActivity;
import app.qarya.utils.ImageHelper;

import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

public class RelationVerticalVH extends BaseViewHolder<Relation> {
    public final View mView;
    Button btn;
    public final TextView title;
    public ImageView thumbnail;
    Relation model;

    public RelationVerticalVH(View view) {
        super(view);
        mView = view;
        title = view.findViewById(R.id.title);
        thumbnail = view.findViewById(R.id.thumbnail);
        btn = view.findViewById(R.id.btn);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_square;
    }

    @Override
    public void bind(final Relation model) {
        this.model = model;
        title.setText(model.getUname());
        ImageHelper.load(thumbnail, model.getUpicture(), 200, 200);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    btn.setEnabled(false);
                    btn.setAlpha(0.5f);
                    if (model.getRelation()==0)
                        ((OnInteractListener<Relation>) listener).onInteract(model, Action.FOLLOW);
                    else
                        ((OnInteractListener<Relation>) listener).onInteract(model, Action.REFUSE);
                }
            }
        });
        mView.setOnClickListener(v -> {
            if (null != listener) {
                MyActivity.log(model.getUname());
                listener.onClick(model);
            }
        });
    }


}