package app.qarya.presentation.adapters.vh

import android.content.Intent
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.qarya.R
import app.qarya.model.models.Post
import app.qarya.utils.ImageHelper
import tn.core.presentation.base.adapters.BaseViewHolder
import tn.core.presentation.listeners.Action
import tn.core.presentation.listeners.OnInteractListener
import tn.core.util.TextUtils

class VHNote(val mView: View) : BaseViewHolder<Post?>(mView) {

    override fun getLayoutId(): Int {
        return R.layout.item_note
    }

    override fun bind(item: Post?) {
        if (item?.message != null) {
            TextUtils.htmlToViewNonClickable(message, item?.message)
            message?.visibility = View.VISIBLE
        }
        //if (item?.getUname() != null) uname?.text = item?.getUname()
        timestamp?.text = item?.getTimeAgo()
        if (item?.title != null) {
            title?.visibility = View.VISIBLE
            title?.text = item?.title
        }


        //statusMsg.setText(item?.description);
        if (item?.description != null) {
            if (item?.description.endsWith("...")) {
                val start = item?.description.lastIndexOf("...")
                val builder = StringBuilder()
                builder.append(item?.description.substring(0, start))
                builder.append("<font color='#A2A2A2'>   ..." + mView.context.getText(R.string.more).toString().toLowerCase() + "</font>")
                item?.description = builder.toString()
            }
            TextUtils.htmlToView(statusMsg, item?.description)
            //TextUtils.htmlToViewNonClickable(statusMsg, item?.getDescription());
        }
        ImageHelper.loadCircle(profilePic, item?.getUpicture()) //, 150,150);
        if (item?.photo != null && item?.photo != "") {
            feedImageView?.visibility = View.VISIBLE
            ImageHelper.load(feedImageView, item?.photo)
        } else feedImageView?.visibility = View.GONE


        // show Link if exist
        if (item?.description != null) {
            val urls = TextUtils.extractUrls(item?.description)
            if (urls.size > 0) {
                url?.text = Html.fromHtml("<a href=\"" + urls[0] + "\">"
                        + urls[0] + "</a> ")
                // Making url clickable
                url?.movementMethod = LinkMovementMethod.getInstance()
                url?.visibility = View.VISIBLE
            } else {
                url?.visibility = View.GONE
            }
        } else {
            url?.visibility = View.GONE
        }
        if (item?.canComment == 0) {
            comment?.visibility = View.GONE
        }
        if (item!!.likesCount > 0) {
            likesCount?.text = item?.likesCount.toString()
        }
        if (item?.commentsCount != null && item?.commentsCount > 0) {
            commentsCount?.text = item?.commentsCount.toString()
        }
        if (item?.liked != null) if (item?.liked) like?.setImageResource(R.drawable.thumb_up) else like?.setImageResource(R.drawable.thumb_up_outline)
        if (item?.marked != null) if (item?.marked) mark?.setImageResource(R.drawable.bookmark) else mark?.setImageResource(R.drawable.bookmark_outline)
        like?.setOnClickListener { v: View? -> if (listener is OnInteractListener<*>) (listener as OnInteractListener<Post?>).onInteract(item, Action.LIKE) }
        mark?.setOnClickListener { v: View? -> if (listener is OnInteractListener<*>) (listener as OnInteractListener<Post?>).onInteract(item, Action.BOOKMARK) }
        comment?.setOnClickListener { v: View? -> if (listener is OnInteractListener<*>) (listener as OnInteractListener<Post?>).onInteract(item, Action.COMMENT) }
        share?.setOnClickListener { v: View? ->
            if (listener is OnInteractListener<*>) (listener as OnInteractListener<Post?>).onInteract(item, Action.SHARE) else {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "qarya.net")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, item?.title)
                mView.context.startActivity(Intent.createChooser(sharingIntent, mView.context.resources.getString(R.string.share_via)))
            }
        }
        feedImageView?.setOnClickListener {
            if (null != listener) {
                listener.onClick(item)
            }
        }
        postBody?.setOnClickListener {
            if (null != listener) {
                listener.onClick(item)
            }
        }
    }


    val postBody: View?
    val message: TextView?
    val uname: TextView?
    val timestamp: TextView?
    val title: TextView?
    val statusMsg: TextView?
    val url: TextView?
    val likesCount: TextView?
    val commentsCount: TextView?
    var profilePic: ImageView?
    var feedImageView: ImageView?
    var like: ImageView?
    var comment: ImageView?
    var mark: ImageView?
    var share: ImageView?
    init {
        message = mView.findViewById<View>(R.id.message) as? TextView
        uname = mView.findViewById<View>(R.id.name) as? TextView
        postBody = mView.findViewById(R.id.postBody) as? View
        title = mView.findViewById<View>(R.id.txtStatusTitle) as? TextView
        timestamp = mView.findViewById<View>(R.id.timestamp) as? TextView
        statusMsg = mView.findViewById<View>(R.id.txtStatusMsg) as? TextView
        url = mView.findViewById<View>(R.id.txtUrl) as? TextView
        profilePic = mView.findViewById(R.id.profilePic) as? ImageView
        feedImageView = mView.findViewById(R.id.postImage) as? ImageView
        comment = mView.findViewById(R.id.comment) as? ImageView
        share = mView.findViewById(R.id.share) as? ImageView
        mark = mView.findViewById(R.id.save) as? ImageView
        like = mView.findViewById(R.id.like) as? ImageView
        likesCount = mView.findViewById<View>(R.id.likesCount) as? TextView
        commentsCount = mView.findViewById<View>(R.id.commentsCount) as? TextView
    }
}