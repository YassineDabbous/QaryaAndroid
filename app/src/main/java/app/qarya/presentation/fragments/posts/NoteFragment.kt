package app.qarya.presentation.fragments.posts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.qarya.R
import app.qarya.model.models.Comment
import app.qarya.model.models.User
import app.qarya.presentation.adapters.CommentsAdapter
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.fragments.PostsFragment
import app.qarya.presentation.fragments.dialogs.PhotoZoomFragment.Companion.newInstance
import app.qarya.utils.ImageHelper
import app.qarya.utils.MyConst
import kotlinx.android.synthetic.main.fragment_note.*
import tn.core.presentation.listeners.Action
import tn.core.presentation.listeners.OnInteractListener
import tn.core.util.TextDrawable
import tn.core.util.TextUtils
import tn.core.util.WebUtils.openLink
import java.util.*

open class NoteFragment : BasePostFragment() {

    companion object {
        @JvmStatic
        fun newInstance(pid: Int): NoteFragment {
            val args = Bundle()
            args.putInt(MyConst.ID, pid)
            val fragment = NoteFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        initDefaultViews(view)
        errorShow = false
        return view
    }










    override fun refreshCommentsList(){
        if (lista.size > 0) {
            if (total > lista.size) loadMoreView!!.visibility = View.VISIBLE
            Collections.reverse(lista)
            adapter.notifyDataSetChanged()
            recyclerView.layoutManager!!.scrollToPosition(lista.size - 1)
            commentEditText!!.setText("")
        }
    }
    override fun setLiker() {
        likesCountTV!!.text = post!!.likesCount.toString() + ""
        if (post!!.liked) {
            likeBtn!!.setImageResource(R.drawable.heart)
        } else {
            likeBtn!!.setImageResource(R.drawable.heart_outline)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMoreView.setOnClickListener(View.OnClickListener { (activity as MyActivity?)!!.passToComments(post) })
        sendBtn.setOnClickListener(View.OnClickListener {
            Log.wtf("click click click click ", "send send send send send send ")
            if (commentEditText != null) {
                val c = commentEditText!!.text.toString()
                if (!c.isEmpty()) {
                    pushComment(c)
                }
            }
        })
        likeBtn.setOnClickListener(View.OnClickListener {
            Log.wtf("click click click click ", "like like like like like like like ")
            mViewModel!!.like(post!!.getId())
        })
        userPartView.setOnClickListener(View.OnClickListener { (activity as MyActivity?)!!.setFragment(PostsFragment.newInstance(User(post!!.getUid(), post!!.getUname(), post!!.upicture))) })
        recyclerView.layoutManager = LinearLayoutManager(context)
        listener = object : OnInteractListener<Comment> {
            override fun onInteract(item: Comment, action: Action) {
                when (action) {
                    Action.LIKE -> mViewModel!!.likeComment(item.id)
                    Action.OPTIONS -> showOptions(item)
                }
            }

            override fun onClick(item: Comment) {}
        }
        adapter = CommentsAdapter(lista, listener)
        recyclerView.adapter = adapter
    }


    override fun bind() {
        setLiker()
        if (post!!.photo != null && !post!!.photo.isEmpty()) {
            Log.e("EkhdemniEkhdemniEkh", post!!.photo)
            postImageView!!.visibility = View.VISIBLE
            ImageHelper.load(postImageView, post!!.photo)
            postImageView!!.setOnClickListener { v: View? -> (activity as MyActivity?)!!.setFragment(newInstance(post!!.photo)) }
        } else postImageView!!.visibility = View.GONE
        titleTextView!!.text = post!!.title
        if (post!!.description != null) TextUtils.setTextViewHTML(contentTextView, post!!.description) { url: String ->
            Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
            if (url.contains(MyConst.HASHTAGS_URL)) {
                val tag = url.replace(MyConst.HASHTAGS_URL, "")
                (activity as MyActivity?)!!.setFragment(PostsFragment.newInstance("#$tag"))
            } else openLink(context!!, url)
        }
        likesCountTV!!.text = post!!.likesCount.toString()
        nameTextView!!.text = post!!.getUname()
        dateTextView!!.text = post!!.getTimeAgo()
        //String me = YDUserManager.get(getContext(), YDUserManager.ID_KEY);
        if (post!!.canComment == 0) { //((getActivity()instanceof ForumsActivity) && ((ForumsActivity) getActivity()).forum!=null && !((ForumsActivity) getActivity()).forum.canComment )
            senderView!!.visibility = View.GONE
            closedCommentsView!!.visibility = View.VISIBLE
        }
        if (post!!.getUpicture() == "") {
            val drawable = TextDrawable.builder().buildRound(post!!.getUname()[0].toString() + "")
            authorImageView!!.setImageDrawable(drawable)
        } else {
            ImageHelper.loadCircle(authorImageView, post!!.getUpicture()) //, 40,40);
        }
    }
}