package app.qarya.presentation.adapters.vh

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.qarya.R
import app.qarya.model.models.Post
import app.qarya.utils.ImageHelper
import tn.core.presentation.base.adapters.BaseViewHolder
import tn.core.util.TextUtils

class ItemVH(val mView: View) : BaseViewHolder<Post>(mView) {

    val nameTV: TextView? = mView.findViewById<View>(R.id.nameTV) as? TextView
    val textTV: TextView? = mView.findViewById<View>(R.id.textTV) as? TextView
    val timeTV: TextView? = mView.findViewById<View>(R.id.timeTV) as? TextView

    val likesCountTV: TextView? = mView.findViewById<View>(R.id.likesCountTV) as? TextView
    val commentsCountTV: TextView? = mView.findViewById<View>(R.id.commentsCountTV) as? TextView

    val photoView: ImageView? = mView.findViewById<View>(R.id.photoView) as? ImageView
    val commentIcon: ImageView? = mView.findViewById<View>(R.id.commentIcon) as? ImageView
    val likeIcon: ImageView? = mView.findViewById<View>(R.id.likeIcon) as? ImageView

    override fun getLayoutId(): Int {
        return R.layout.item_general
    }

    override fun bind(item: Post) {
        nameTV!!.text = item.uname ?: ""
        timeTV!!.text = item.timeAgo ?: ""

        val t = "<b>${item.title}</b> ${item.description}"
        TextUtils.htmlToView(textTV, t)

        ImageHelper.loadCircle(photoView, item.upicture)


        if (item.likesCount > 0){
            likeIcon!!.visibility = View.VISIBLE
            likesCountTV!!.text = "${item.likesCount}"
        }
        if (item.commentsCount > 0){
            commentIcon!!.visibility = View.VISIBLE
            commentsCountTV!!.text = "${item.likesCount}"
        }

        mView.setOnClickListener {
            if (null != listener) {
                listener.onClick(item)
            }
        }
    }


}