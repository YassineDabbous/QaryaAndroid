package app.qarya.presentation.adapters;


import app.qarya.model.models.Comment;
import app.qarya.presentation.adapters.vh.VHComment;
import app.qarya.presentation.base.MyAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import tn.core.presentation.listeners.OnInteractListener;

public class CommentsAdapter extends MyAdapter<Comment, VHComment> {
    public CommentsAdapter(@NotNull List<Comment> items, @NotNull OnInteractListener<Comment> listener) {
        super(items, VHComment.class, listener);
    }
}