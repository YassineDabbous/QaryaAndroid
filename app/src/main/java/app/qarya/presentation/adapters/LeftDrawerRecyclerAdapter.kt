package app.qarya.presentation.adapters

import app.qarya.model.models.APath
import app.qarya.presentation.adapters.vh.VHLeftDrawerItem
import app.qarya.presentation.base.MyAdapter
import tn.core.presentation.listeners.OnClickItemListener

class LeftDrawerRecyclerAdapter(val items:List<APath>, val listener:OnClickItemListener<APath>):
        MyAdapter<APath, VHLeftDrawerItem>(items, VHLeftDrawerItem::class.java, listener, false) {
}