package app.qarya.presentation.adapters.vh

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.qarya.R
import app.qarya.model.models.Comment
import app.qarya.utils.ImageHelper
import tn.core.presentation.base.adapters.BaseViewHolder
import tn.core.presentation.listeners.Action
import tn.core.presentation.listeners.OnInteractListener
import tn.core.util.TextDrawable

class VHComment(view:View): BaseViewHolder<Comment>(view){

    var mView: View? = null
    var usernameTV: TextView? = null
    var commentTV: TextView? = null
    var dateTV: TextView? = null
    var likeTV: TextView? = null
    var user_photo: ImageView? = null
    var more: ImageView? = null

    init {
        mView = view
        usernameTV = view.findViewById(R.id.tv_username)
        commentTV = view.findViewById(R.id.tv_comment)
        dateTV = view.findViewById(R.id.tv_time)
        likeTV = view.findViewById(R.id.like)
        user_photo = view.findViewById(R.id.user_photo)
        more = view.findViewById(R.id.more)
    }

    override fun getLayoutId(): Int = R.layout.item_comment

    override fun bind(model: Comment) {
        more?.setVisibility(View.VISIBLE)
        if (model.getId()<0) {
            mView?.findViewById<View>(R.id.solution)?.setVisibility(View.VISIBLE)
        }
        usernameTV?.setText(model.uname)
        commentTV?.setText(model.getComment())
        dateTV?.setText(model.getTimeAgo())
        var h = "♥ "
        var nbr = if(model.likesCount!=null) "${model.likesCount}" else ""
        if (model.getLikesCount() == 0) {
            h = ""
            nbr = ""
        }
        if (model.getLiked()!!) {
            likeTV?.setText(mView?.context?.getResources()?.getString(R.string.liked) + "   " + h + nbr)
            likeTV?.setTextColor(mView?.context?.getResources()?.getColor(R.color.material_red)!!)
        } else {
            likeTV?.setText(mView?.context?.getResources()?.getString(R.string.like) + "   " + h + nbr)
            likeTV?.setTextColor(mView?.context?.getResources()?.getColor(R.color.colorPrimary)!!)
        }
        if (model.getUpicture() == "") {
            val drawable = TextDrawable.builder().buildRound(model.getUname()[0] + "")
            user_photo?.setImageDrawable(drawable)
        } else {
            ImageHelper.loadCircle(user_photo, model.getUpicture());//, 100, 100)
        }
        likeTV?.setOnClickListener({
            (listener as OnInteractListener<Comment>).onInteract(model, Action.LIKE)
        })
        more?.setOnClickListener{
            (listener as OnInteractListener<Comment>).onInteract(model, Action.OPTIONS)
            false
        }
        mView?.setOnLongClickListener{
            (listener as OnInteractListener<Comment>).onInteract(model, Action.OPTIONS)
            false}

    }

}