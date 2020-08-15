package app.qarya.presentation.adapters;


import app.qarya.model.models.Room;
import app.qarya.presentation.adapters.vh.RoomVH;
import app.qarya.presentation.base.MyAdapter;

import java.util.List;

import tn.core.presentation.listeners.OnClickItemListener;

public class RoomsAdapter extends MyAdapter<Room, RoomVH> {
    public RoomsAdapter(List<Room> itemList, OnClickItemListener<Room> mListener) {
        super(itemList, RoomVH.class, mListener);
        this.mListener = mListener;
        this.items = itemList;
    }
}

