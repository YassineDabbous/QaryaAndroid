package app.qarya.presentation.adapters;


import app.qarya.model.models.Category;
import app.qarya.presentation.adapters.vh.CategoryVH;
import app.qarya.presentation.base.MyAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import tn.core.presentation.listeners.OnInteractListener;

public class CategoryAdapter extends MyAdapter<Category, CategoryVH> {
    public CategoryAdapter(@NotNull List<Category> items, @NotNull OnInteractListener<Category> listener) {
        super(items, CategoryVH.class, listener);
    }
}
