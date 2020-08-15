package app.qarya.presentation.adapters.vh;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.qarya.R;
import app.qarya.model.models.Room;
import app.qarya.utils.ImageHelper;

import tn.core.presentation.base.adapters.BaseViewHolder;

public class RoomVH extends BaseViewHolder<Room> {
    public final TextView usernameTV,  commentTV, dateTV, like;
    public ImageView user_photo;
    View mView;
    Room model;

    public RoomVH(View view) {
        super(view);
        mView = view;
        usernameTV = view.findViewById(R.id.tv_username);
        commentTV = view.findViewById(R.id.tv_comment);
        dateTV = view.findViewById(R.id.tv_time);
        user_photo =  view.findViewById(R.id.user_photo);
        like = view.findViewById(R.id.like);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_comment;
    }

    @Override
    public void bind(final Room model) {
        this.model = model;
        like.setVisibility(View.GONE);
        commentTV.setVisibility(View.GONE);
        dateTV.setVisibility(View.GONE);
        usernameTV.setText(model.getName());
        ImageHelper.load(user_photo, model.getIcon(), 150,150);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(model);
                }
            }
        });
    }

}