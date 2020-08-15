package app.qarya.presentation.adapters;

import java.util.List;

import tn.core.presentation.base.adapters.BaseAdapter;
import tn.core.presentation.listeners.OnClickItemListener;
import app.qarya.model.models.Relation;
import app.qarya.presentation.adapters.vh.RelationVH;

/**
 * Created by X on 5/23/2018.
 */

public class RelationsAdapter extends BaseAdapter<Relation, RelationVH, RelationVH> {

    public RelationsAdapter(List<Relation> items, OnClickItemListener<Relation> mListener) {
        super(items, RelationVH.class, RelationVH.class, mListener);
    }


}