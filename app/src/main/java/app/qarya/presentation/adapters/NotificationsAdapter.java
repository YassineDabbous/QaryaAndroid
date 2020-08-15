package app.qarya.presentation.adapters;


import java.util.List;

import app.qarya.presentation.base.MyAdapter;
import app.qarya.presentation.adapters.vh.NotificationsVH;
import tn.core.presentation.listeners.OnClickItemListener;
import app.qarya.model.models.Notification;
/**
 * Created by X on 5/23/2018.
 */

public class NotificationsAdapter extends MyAdapter<Notification, NotificationsVH> {

    public NotificationsAdapter(List<Notification> itemList, OnClickItemListener<Notification> mListener) {
        super(itemList, NotificationsVH.class, mListener);
        this.mListener = mListener;
        this.items = itemList;
    }

}