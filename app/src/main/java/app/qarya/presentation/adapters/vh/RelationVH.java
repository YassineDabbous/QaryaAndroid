package app.qarya.presentation.adapters.vh;


import android.view.View;
import android.widget.Button;

import android.widget.ImageView;

import tn.core.presentation.base.adapters.BaseViewHolder;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import app.qarya.R;
import app.qarya.model.models.Relation;
import app.qarya.model.shared.YDUserManager;
import app.qarya.utils.ImageHelper;

import android.widget.TextView;

public class RelationVH extends BaseViewHolder<Relation> {
    public TextView usernameTV;
    public Button accept, reject;
    public ImageView user_photo;
    View mView, btns;
    Relation model;

    public RelationVH(View view) {
        super(view);
        mView = view;
        init(view);
    }
    public void init(View view) {
        usernameTV = view.findViewById(R.id.tv_username);
        btns = view.findViewById(R.id.btns);
        accept = view.findViewById(R.id.accept);
        reject = view.findViewById(R.id.reject);
        user_photo =  view.findViewById(R.id.user_photo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_relation;
    }

    @Override
    public void bind(final Relation model) {
        this.model = model;
        String time = model.getTimeAgo() != null ? "\n"+model.getTimeAgo()   : "";
        usernameTV.setText(model.getUname() + time);
        ImageHelper.loadCircle(user_photo,model.getUpicture());//, 100,100);


        if (model.getRelation().equals(0)){
            accept.setText(R.string.add);
            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.GONE);
            accept.setOnClickListener(view -> ((OnInteractListener<Relation>)listener).onInteract(model, Action.FOLLOW)); //setRelation(1)
        } else if (model.getRelation().equals(1)) {
            if (model.getUserOne().equals(YDUserManager.auth().getId())){
                reject.setText(R.string.Cancel);
                reject.setVisibility(View.VISIBLE);
                accept.setVisibility(View.GONE);
                reject.setOnClickListener(view -> ((OnInteractListener<Relation>)listener).onInteract(model, Action.REFUSE)); //setRelation(0)
            } else {
                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
                accept.setOnClickListener(view -> ((OnInteractListener<Relation>)listener).onInteract(model, Action.ACCEPT)); //setRelation(2)
                reject.setOnClickListener(view -> ((OnInteractListener<Relation>)listener).onInteract(model, Action.REFUSE)); //setRelation(0));
            }
        } else if (model.getRelation().equals(3) && model.getUserOne().equals(YDUserManager.auth().getId())) {
            reject.setText(R.string.Cancel_block);
            reject.setVisibility(View.VISIBLE);
            accept.setVisibility(View.GONE);
            reject.setOnClickListener(view -> ((OnInteractListener<Relation>)listener).onInteract(model, Action.REFUSE)); //setRelation(0)
        } else
            btns.setVisibility(View.GONE);

        if (!model.getRelation().equals(3))
            mView.setOnClickListener(view -> {
                if (null != listener)
                    listener.onClick(model);
            });
    }


}