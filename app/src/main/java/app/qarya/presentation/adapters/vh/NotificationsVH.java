package app.qarya.presentation.adapters.vh;

import android.view.View;

import android.widget.ImageView;

import app.qarya.R;
import tn.core.presentation.base.adapters.BaseViewHolder;
import app.qarya.model.models.Notification;
import app.qarya.utils.ImageHelper;
import android.widget.TextView;

public class NotificationsVH extends BaseViewHolder<Notification> {
    public final TextView usernameTV,  commentTV, dateTV, like;
    public ImageView user_photo;
    View mView;

    public NotificationsVH(View view) {
        super(view);
        mView = view;
        usernameTV = (TextView) view.findViewById(R.id.tv_username);
        commentTV = (TextView) view.findViewById(R.id.tv_comment);
        dateTV = (TextView) view.findViewById(R.id.tv_time);
        user_photo =  view.findViewById(R.id.user_photo);
        like = (TextView) view.findViewById(R.id.like);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_comment;
    }

    public void bind(final Notification message) {
        usernameTV.setVisibility(View.GONE);
        like.setVisibility(View.GONE);

        commentTV.setText(message.getMessage());
        dateTV.setText(message.getTimeAgo());
        usernameTV.setText(message.getUname());
        ImageHelper.load(user_photo,message.getUpicture(), 100,100);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(message);
                }
            }
        });
    }
}