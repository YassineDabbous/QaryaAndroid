package app.qarya.presentation.adapters;

import java.util.List;

import app.qarya.presentation.adapters.vh.VHPost;
import app.qarya.presentation.base.MyAdapter;
import app.qarya.model.models.Post;
import app.qarya.utils.MyConst;

import tn.core.presentation.listeners.OnInteractListener;

public class PostsAdapter extends MyAdapter<Post, VHPost> {
    public static final int ADS_AFTER = MyConst.ADS_AFTER;

    public PostsAdapter(List<Post> itemList, OnInteractListener<Post> mListener) {
        super(itemList, VHPost.class, mListener);
        this.mListener = mListener;
        this.items = itemList;
    }
}
