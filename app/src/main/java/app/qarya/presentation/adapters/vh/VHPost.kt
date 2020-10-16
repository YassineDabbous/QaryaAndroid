package app.qarya.presentation.adapters.vh

import android.content.Intent
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import app.qarya.R
import app.qarya.model.ModelType
import app.qarya.model.models.Post
import app.qarya.utils.ImageHelper
import tn.core.presentation.base.adapters.BaseViewHolder
import tn.core.presentation.listeners.Action
import tn.core.presentation.listeners.OnInteractListener
import tn.core.util.TextUtils

class VHPost(val mView: View) : BaseViewHolder<Post?>(mView) {

    override fun getLayoutId(): Int {
        return R.layout.item_post
    }

    override fun bind(item: Post?) {
        if (item?.message != null) {
            TextUtils.htmlToViewNonClickable(message, item?.message)
            message?.visibility = View.VISIBLE
        }
        if (item?.getUname() != null) uname?.text = item?.getUname()
        timestamp?.text = item?.getTimeAgo()


        when (item?.type) {
            ModelType.PRODUCT -> {
                typeTV?.text = typeIV?.context?.getText(R.string.buy_and_sell)
                typeIV?.setImageResource(R.drawable.shopping)
                typeLayout?.background = ContextCompat.getDrawable(typeLayout?.context!!, R.drawable.rounded_red)
                typeLayout?.visibility = View.VISIBLE
            }
            ModelType.LOST -> {
                typeTV?.text = typeIV?.context?.getText(R.string.lost_and_found)
                typeIV?.setImageResource(R.drawable.magnify)
                typeLayout?.background = ContextCompat.getDrawable(typeLayout?.context!!, R.drawable.rounded_blue)
                typeLayout?.visibility = View.VISIBLE
            }
            ModelType.NOTE -> {
                typeTV?.text = typeIV?.context?.getText(R.string.note)
                typeIV?.setImageResource(R.drawable.post_outline)
                typeLayout?.background = ContextCompat.getDrawable(typeLayout?.context!!, R.drawable.rounded_green)
                typeLayout?.visibility = View.VISIBLE
            }
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
    //val title: TextView?
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
    var typeLayout:View?
    var typeIV:ImageView?
    var typeTV:TextView?
    init {
        message = mView.findViewById<View>(R.id.messageView) as? TextView
        uname = mView.findViewById<View>(R.id.nameTextView) as? TextView
        postBody = mView.findViewById(R.id.postBodyView) as? View

        typeLayout = mView.findViewById<View>(R.id.typeLayout)
        typeIV = mView.findViewById(R.id.typeIV)
        typeTV = mView.findViewById(R.id.typeTV) as? TextView

        //title = mView.findViewById<View>(R.id.titleTextView) as? TextView
        timestamp = mView.findViewById<View>(R.id.dateTextView) as? TextView
        statusMsg = mView.findViewById<View>(R.id.contentTextView) as? TextView
        url = mView.findViewById<View>(R.id.urlTextView) as? TextView
        profilePic = mView.findViewById(R.id.authorImageView) as? ImageView
        feedImageView = mView.findViewById(R.id.postImageView) as? ImageView
        comment = mView.findViewById(R.id.commentBtn) as? ImageView
        share = mView.findViewById(R.id.shareBtn) as? ImageView
        mark = mView.findViewById(R.id.saveBtn) as? ImageView
        like = mView.findViewById(R.id.likeBtn) as? ImageView
        likesCount = mView.findViewById<View>(R.id.likesCountTV) as? TextView
        commentsCount = mView.findViewById<View>(R.id.commentsCountTV) as? TextView
    }
}