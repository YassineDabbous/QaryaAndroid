package app.qarya.presentation.adapters.vh

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.qarya.R
import app.qarya.model.models.APath
import app.qarya.utils.ImageHelper
import tn.core.presentation.base.adapters.BaseViewHolder

class VHLeftDrawerItem(val view:View): BaseViewHolder<APath>(view) {
    var title:TextView? = null
    var count:TextView? = null
    var icon:ImageView? = null

    override fun getLayoutId(): Int {
        return R.layout.list_item_drawer_category_left
    }

    init {
        title = view.findViewById(R.id.drawer_list_item_text)
        count = view.findViewById(R.id.drawer_list_item_count)
        icon = view.findViewById(R.id.drawer_list_item_icon)
    }

    override fun bind(model: APath?) {
        if (model?.count!=null && model?.count>0){
            count?.text = model?.count.toString()
            count?.visibility = View.VISIBLE
        }
        if (model?.icon!=null && !model?.icon.isEmpty()){
            ImageHelper.load(icon, model.icon)
            icon?.visibility = View.VISIBLE
        }
        title?.text = model?.name
        view.setOnClickListener { listener.onClick(model) }
    }
}