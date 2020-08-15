package app.qarya.presentation.adapters.vh

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.qarya.R
import app.qarya.model.models.Conversation
import app.qarya.utils.ImageHelper
import tn.core.presentation.base.adapters.BaseViewHolder

class ConversationVH(val mView: View) : BaseViewHolder<Conversation?>(mView) {

    val usernameTV: TextView? = mView.findViewById(R.id.tv_username) as? TextView
    val commentTV: TextView? = mView.findViewById(R.id.tv_comment) as? TextView
    val dateTV: TextView? = mView.findViewById(R.id.tv_time) as? TextView
    val like: TextView? = mView.findViewById(R.id.like) as? TextView
    val user_photo: ImageView? = mView.findViewById(R.id.user_photo) as? ImageView
    val onlineView: View? = mView.findViewById(R.id.onlineView) as? View

    override fun getLayoutId(): Int {
        return R.layout.item_comment
    }

    override fun bind(model: Conversation?) {
        like?.visibility = View.GONE
        commentTV?.text = model?.description
        dateTV?.text = model?.timeAgo
        usernameTV?.text = model?.getName()
        if(model!!.online!!) onlineView?.visibility = View.VISIBLE
        ImageHelper.loadCircle(user_photo, model?.icon) //, 150,150);
        mView.setOnClickListener {
            if (null != listener) {
                listener.onClick(model)
            }
        }
    }
}