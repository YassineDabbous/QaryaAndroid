package app.qarya.presentation.fragments.posts


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_story.*
import kotlinx.android.synthetic.main.fragment_zoom.imageSingle
import app.qarya.R
import app.qarya.model.models.Post
import app.qarya.model.models.responses.GeneralResponse
import app.qarya.model.models.responses.LikeResponse
import app.qarya.model.shared.YDUserManager
import app.qarya.presentation.activities.MainActivity
import app.qarya.presentation.base.MyDialogFragment
import app.qarya.presentation.vms.VMPost
import app.qarya.utils.AlertUtils
import app.qarya.utils.ImageHelper
import app.qarya.utils.MyConst
import java.util.ArrayList


class StoryFragment : MyDialogFragment<VMPost>() {

    companion object {
        fun newInstance(id: Int): StoryFragment {
            val args = Bundle()
            args.putInt(MyConst.ID, id)
            val fragment = StoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(VMPost::class.java)
        mViewModel.callErrors.observe(this, Observer<List<String>> { this.onError(it) })
        mViewModel.loadStatus.observe(this, Observer<Boolean> { this.onStatusChanged(it) })
        mViewModel.like.observe(this, Observer { this.handleLike(it) })
        mViewModel.reportPost.observe(this, Observer<Any> { this.handleReport(it) })
        mViewModel.deletePost.observe(this, Observer<GeneralResponse> { this.handleDelete(it) })
        mViewModel.getLiveData().observe(this, Observer<Post> { this.onDataReceived(it) })
        getData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_story, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path:String = args.getString(MyConst.PATH, "")
    }

    fun bind(){
        ImageHelper.load(pictureImageView, mViewModel.item?.upicture)
        ImageHelper.load(imageSingle, mViewModel.item?.photo)
        usernameTextView.text = mViewModel.item?.uname
        dateTextView.text = mViewModel.item?.timeAgo
        setLiker()
        likeBtn.setOnClickListener {
            mViewModel.like(mViewModel.item?.id!!)
        }
        menuBtn.setOnClickListener {
            showOptions()
        }
    }



    override fun getData() {
        super.getData()
        mViewModel.init(args.getInt(MyConst.ID))
    }

    fun onDataReceived(data: Post) {
        mViewModel.item = data
        bind()
    }



    fun handleLike(response: LikeResponse) {
        mViewModel.item?.setLikesCount(response.likesCount)
        mViewModel.item?.setLiked(response.liked)
        setLiker()
    }

    fun setLiker() {
        likesCountTextView.setText( mViewModel.item?.getLikesCount().toString())
        if ( mViewModel.item?.getLiked()!!) {
            likeBtn.setImageResource(R.drawable.heart)
        } else {
            likeBtn.setImageResource(R.drawable.heart_outline)
        }
    }
    fun handleDelete(response: GeneralResponse) {
        activity!!.onBackPressed()
    }
    fun handleReport(response: Any) {
        Toast.makeText(context, "Done", Toast.LENGTH_LONG).show()
    }


    override fun showOptions() {
        val list = ArrayList<String>()
        list.add(getText(R.string.set_as_background).toString())
        list.add(getText(R.string.report).toString())
        val me = YDUserManager.auth()
        if (me != null && me.id != null && me.id == mViewModel.item?.getUid())
            list.add(getText(R.string.delete).toString())

        val builderSingle = MaterialAlertDialogBuilder(activity!!)

        val arrayAdapter = ArrayAdapter<String>(activity!!, android.R.layout.select_dialog_item, list.toTypedArray<String>())

        builderSingle.setAdapter(arrayAdapter) { dialog, which ->
            when (which) {
                0 -> {
                    YDUserManager.setBackground( mViewModel.item?.photo)
                    (activity as MainActivity).setBackground()
                }
                1 -> reportAlert()
                2 -> deleteAlert()
            }
            dialog.dismiss()
        }
        builderSingle.show()
    }

    fun deleteAlert() {
        val action = object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                mViewModel.delete(mViewModel.item)
            }
        }
        action.message = context!!.getText(R.string.delete).toString()
        AlertUtils.alert(context!!, action)
    }

    fun reportAlert() {
        val action = object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                mViewModel.report(mViewModel.item, o.toString())
            }
        }
        action.message = context!!.getText(R.string.report).toString()
        AlertUtils.report(context!!, action)
    }

}