package app.qarya.presentation.adapters.vh;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.qarya.R;
import app.qarya.model.models.User;
import app.qarya.utils.ImageHelper;

import tn.core.presentation.base.adapters.BaseViewHolder;

public class RoomUserVH extends BaseViewHolder<User> {
    public final TextView usernameTV;
    public ImageView user_photo, userTypeIV;
    User model;

    public RoomUserVH(View view) {
        super(view);
        usernameTV = view.findViewById(R.id.title);
        user_photo =  view.findViewById(R.id.logo);
        userTypeIV =  view.findViewById(R.id.userTypeIV);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_room_user;
    }

    @Override
    public void bind(final User model) {
        this.model = model;
        usernameTV.setText(model.getName());
        if(model.isAdmin)
            userTypeIV.setVisibility(View.VISIBLE);
        ImageHelper.loadCircle(user_photo,model.getPhoto());//, 100,100);
        itemView.setOnClickListener(view -> {
            if (null != listener)
                listener.onClick(model);
        });
    }


}