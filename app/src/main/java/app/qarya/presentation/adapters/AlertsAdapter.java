package app.qarya.presentation.adapters;


import app.qarya.presentation.base.MyAdapter;
import app.qarya.presentation.adapters.vh.VHAlert;
import app.qarya.model.models.Alert;

import java.util.List;

public class AlertsAdapter extends MyAdapter<Alert, VHAlert> {
    public AlertsAdapter(List<Alert> itemList) {
        super(itemList, VHAlert.class, null);
    }
}

