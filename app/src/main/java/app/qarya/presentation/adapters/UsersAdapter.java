package app.qarya.presentation.adapters;

import java.util.List;

import app.qarya.presentation.base.MyAdapter;
import app.qarya.presentation.adapters.vh.UsersVH;
import tn.core.presentation.listeners.OnClickItemListener;
import app.qarya.model.models.User;

public class UsersAdapter extends MyAdapter<User, UsersVH> {

    public UsersAdapter(List<User> items, OnClickItemListener<User> listener) {
        super(items, UsersVH.class, listener);
    }



}
