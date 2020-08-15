package app.qarya.presentation.fragments.posts

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.qarya.R
import app.qarya.model.models.Comment
import app.qarya.model.models.Commun
import app.qarya.model.models.Post
import app.qarya.model.models.User
import app.qarya.model.models.responses.GeneralResponse
import app.qarya.model.models.responses.LikeResponse
import app.qarya.model.models.responses.MarkResponse
import app.qarya.model.shared.YDUserManager
import app.qarya.presentation.activities.MainActivity
import app.qarya.presentation.adapters.CommentsAdapter
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.base.MyRecyclerFragment
import app.qarya.presentation.fragments.PostsFragment
import app.qarya.presentation.fragments.dialogs.PhotoZoomFragment.Companion.newInstance
import app.qarya.presentation.vms.VMPost
import app.qarya.utils.AlertUtils
import app.qarya.utils.AlertUtils.alert
import app.qarya.utils.AlertUtils.report
import app.qarya.utils.ImageHelper
import app.qarya.utils.MyConst
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tn.core.model.responses.PagingResponse
import tn.core.presentation.base.adapters.BaseAdapter
import tn.core.presentation.listeners.Action
import tn.core.presentation.listeners.OnInteractListener
import tn.core.util.TextDrawable
import tn.core.util.TextUtils
import tn.core.util.WebUtils.openLink
import java.util.*

open class BasePostFragment : MyRecyclerFragment<Comment, VMPost?>() {
    var post: Post? = null

    override fun onDataReceived(data: PagingResponse<Comment>) {
        onDataReceived(data.data)
    }

    override fun onDataReceived(data: List<Comment>) {
        super.onDataReceived(data)
        MyActivity.log("Comments count: data" + data.size)
        MyActivity.log("Comments count:" + lista.size)
        refreshCommentsList()
    }

    fun onDataReceived(data: Post?) {
        post = data
        Log.wtf("qarya.net", "post:" + post!!.title)
        mViewModel!!.getComments(post!!.getId())
        bind()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(VMPost::class.java)
        mViewModel!!.callErrors.observe(this, Observer { errors: List<String?>? -> onError(errors) })
        mViewModel!!.loadStatus.observe(this, Observer { b: Boolean? -> onStatusChanged(b) })
        mViewModel!!.getLiveData().observe(this, Observer { data: Post? -> this.onDataReceived(data) })
        mViewModel!!.like.observe(this, Observer { response: LikeResponse -> handleLike(response) })
        mViewModel!!.bookmark.observe(this, Observer { response: MarkResponse -> handleMark(response) })
        mViewModel!!.reportPost.observe(this, Observer { response: Any? -> handleReportComment(response) })
        mViewModel!!.deletePost.observe(this, Observer { response: GeneralResponse? -> handleDeletePost(response) })
        mViewModel!!.likeComment.observe(this, Observer { response: LikeResponse -> handleLikeComment(response) })
        mViewModel!!.reportComment.observe(this, Observer { response: Any? -> handleReportComment(response) })
        mViewModel!!.deleteComment.observe(this, Observer { response: GeneralResponse -> handleDeleteComment(response) })
        mViewModel!!.comments.observe(this, Observer { data: List<Comment> -> this.onDataReceived(data) })
        mViewModel!!.pagingComments.observe(this, Observer { data: PagingResponse<Comment> -> this.onDataReceived(data) })
        getData()
    }

    override fun getData() {
        super.getData()
        if (lista.size == 0) {
            val id = args.getInt(MyConst.ID)
            mViewModel!!.init(id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        initDefaultViews(view)
        errorShow = false
        return view
    }


    override fun showOptions() {
        val list: MutableList<String> = ArrayList()
        list.add(getText(R.string.report).toString())
        val me = YDUserManager.auth()
        if (me != null && me.id != null && me.id == post!!.getUid()) list.add(getText(R.string.delete).toString())
        val builderSingle = MaterialAlertDialogBuilder(activity)
        val arrayAdapter = ArrayAdapter(activity!!, android.R.layout.select_dialog_item, list.toTypedArray())
        builderSingle.setAdapter(arrayAdapter) { dialog: DialogInterface, which: Int ->
            when (which) {
                0 -> reportAlert(post)
                1 -> deleteAlert(post)
            }
            dialog.dismiss()
        }
        builderSingle.show()
    }

    fun showOptions(model: Comment) {
        val list: MutableList<String> = ArrayList()
        list.add(getText(R.string.report).toString())
        val me = YDUserManager.auth()
        if (me != null && me.id != null && me.id == model.getUid()) list.add(getText(R.string.delete).toString())
        val builderSingle = MaterialAlertDialogBuilder(activity)
        val arrayAdapter = ArrayAdapter(activity!!, android.R.layout.select_dialog_item, list.toTypedArray())

        /*builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/builderSingle.setAdapter(arrayAdapter) { dialog, which ->
            when (which) {
                0 -> reportAlert(model)
                1 -> deleteAlert(model)
            }
            dialog.dismiss()
        }
        builderSingle.show()
    }


    fun handleDeletePost(response: GeneralResponse?) {
        activity!!.onBackPressed()
    }

    fun handleDeleteComment(response: GeneralResponse) {
        for (i in lista.indices) {
            if (lista[i]!!.getId() == response.id) {
                lista.removeAt(i)
                break
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun handleReportComment(response: Any?) {}
    fun handleLikeComment(response: LikeResponse) {
        for (i in lista.indices) {
            if (lista[i]!!.getId() == response.id) {
                lista[i]!!.liked = response.liked
                lista[i]!!.likesCount = response.likesCount
                (adapter as BaseAdapter<Comment, *, *>).refresh(lista[i])
                break
            }
        }
    }

    fun handleLike(response: LikeResponse) {
        post!!.likesCount = response.likesCount
        post!!.liked = response.liked
        setLiker()
    }
    open fun handleMark(response: MarkResponse) {
        post!!.marked = response.bookmarked
    }


    fun pushComment(commentTxt: String?) {
        mViewModel!!.pushComment(post!!.getId(), commentTxt)
    }

    var total = 0
    fun deleteAlert(comment: Commun?) {
        val action: AlertUtils.Alert = object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                mViewModel!!.delete(comment)
            }
        }
        action.message = context!!.getText(R.string.delete).toString()
        alert(context!!, action)
    }

    fun reportAlert(comment: Commun?) {
        val action: AlertUtils.Alert = object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                mViewModel!!.report(comment, o.toString())
            }
        }
        action.message = context!!.getText(R.string.report).toString()
        report(context!!, action)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)!!.hideToolbar()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity?)!!.showToolbar()
    }


    open fun refreshCommentsList() {}
    open fun setLiker() {}
    open fun bind() {}


}