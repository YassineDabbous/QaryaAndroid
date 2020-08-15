package app.qarya.presentation.adapters;


import app.qarya.model.models.City;
import app.qarya.presentation.adapters.vh.CityVH;
import app.qarya.presentation.base.MyAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import tn.core.presentation.listeners.OnInteractListener;

public class CityAdapter extends MyAdapter<City, CityVH> {


    public CityAdapter(@NotNull List<City> items, @NotNull OnInteractListener<City> listener) {
        super(items, CityVH.class, listener);
    }
}
