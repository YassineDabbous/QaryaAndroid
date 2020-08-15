package app.qarya.presentation.adapters;


import java.util.List;
import app.qarya.presentation.adapters.vh.VHApps;
import app.qarya.presentation.base.MyAdapter;
import tn.core.presentation.listeners.OnClickItemListener;
import app.qarya.model.models.App;


public class AppsAdapter extends MyAdapter<App, VHApps> {

    public AppsAdapter(List<App> itemList, OnClickItemListener<App> mListener) {
        super(itemList, VHApps.class, mListener, false);
    }
}
