package app.qarya.presentation.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import app.qarya.R
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.base.MyFragment
import app.qarya.presentation.vms.VMUser
import app.qarya.model.models.User
import tn.core.util.Const
import app.qarya.utils.ImageHelper
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_users_show.*
import app.qarya.model.models.Relation
import app.qarya.model.shared.YDUserManager
import app.qarya.presentation.fragments.SearchFragment
import app.qarya.presentation.fragments.UploadFragment
import app.qarya.presentation.fragments.dialogs.PhotoZoomFragment
import app.qarya.utils.AppHelpers

/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : MyFragment<VMUser>() {

    var uid: Int = 0
    internal var relations = R.array.relationsNew


    companion object {
        fun newInstance(uid: Int): UserProfileFragment {
            val args = Bundle()
            args.putInt(Const.ID, uid)
            val fragment = UserProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mViewModel = ViewModelProvider(this, SavedStateViewModelFactory(requireActivity().application, this)).get(SavedStateViewModel::class.java)
        mViewModel = ViewModelProvider(this).get(VMUser::class.java)
        mViewModel.callErrors.observe(this, Observer<List<String>> { this.onError(it) })
        mViewModel.loadStatus.observe(this, Observer { this.onStatusChanged(it) })
        mViewModel.relation.observe(this, Observer { this.onDataReceived(it) })
        mViewModel.getLiveData().observe(this, Observer { this.onDataReceived(it) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users_show, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!mViewModel.isInitialized()) {
            identify()
            getData()
        } else
            bind()
    }

    fun identify() {
        val who = args.getInt(Const.ID, 0)
        if (who != 0)
            uid = who //Integer()
        else if (YDUserManager.check())
            uid = YDUserManager.auth().getId()
    }


    override fun getData() {
        super.getData()
        mViewModel.init(uid)
    }

    //@Override
    fun onDataReceived(data: User) {
        //super.onDataReceived(data);
        mViewModel.user = data
        bind()
        resetRelation()
    }
    fun bind(){
        nameTV.text = mViewModel.user.getName()
        ImageHelper.load(photoView, mViewModel.user.photo, 200, 200)
        if (mViewModel.user.online) {
            onlineView.visibility = View.VISIBLE
        } else {
            onlineView.visibility = View.GONE
        }
        emailTV.text = mViewModel.user.email
        phoneTV.text = mViewModel.user.phone
        addressTV.setText(mViewModel.user.address)
        summaryTV.setText(mViewModel.user.summary)
        postsCount.setText(mViewModel.user.postsCount.toString())
        productsCount.setText(mViewModel.user.productsCount.toString())
        friendsCount.setText(mViewModel.user.friendsCount.toString())
        setListeners()
    }



    fun onDataReceived(data: Relation) {
        mViewModel.user.friendship = data.relation
        mViewModel.user.friendshipMaker = data.userOne
        resetRelation()
        //refresh ?
        if(data.relation==3){
            requireActivity().onBackPressed()
        }
    }






    private fun setListeners() {
        if (mViewModel.user.isMe) {
            photoView.setOnClickListener { setFragment(UploadFragment()) }
            followBtn.visibility = View.GONE // followBtn.text = getText(R.string.resume).toString()
            var updateTxt = getText(R.string.update_profile).toString()
            //if (mViewModel.user.getPercent() < 100) updateTxt += " (" + mViewModel.user.getPercent() + "%)"
            messageBtn.text = updateTxt
            messageBtn.setOnClickListener { view -> setFragment(ProfileUpdateFragment.newInstance()) }
            //followBtn.setOnClickListener { view -> checkResumeAvailability() }
        } else {
            photoView.setOnClickListener { (activity as MyActivity).setFragment(PhotoZoomFragment.newInstance(mViewModel.user.photo)) }

            messageBtn.setOnClickListener {
                AppHelpers.private(requireContext(), mViewModel.user.id)
            }
            followBtn.setOnClickListener {
                popupList()
            }
        }
        btns.visibility = View.VISIBLE

        postsView.setOnClickListener { setFragment(SearchFragment.newInstance(mViewModel.user.id, 0)) }
        portfelioView.setOnClickListener { setFragment(SearchFragment.newInstance(mViewModel.user.id, 1)) }
        friendsView.setOnClickListener { setFragment(SearchFragment.newInstance(mViewModel.user.id, 2)) }
        //summaryTV.setOnClickListener(showResumeListener())
        //showDetails.setOnClickListener(showResumeListener())
    }



    internal fun resetRelation() {
        if (mViewModel.user.friendship === 0) {
            relations = R.array.relationsNew
        } else if (mViewModel.user.friendship === 1 && mViewModel.user.friendshipMaker === mViewModel.user.id) {
            relations = R.array.relationsRequest
        } else if (mViewModel.user.friendship === 1) {
            relations = R.array.relationsRequestSent
        } else if (mViewModel.user.friendship === 2) {
            relations = R.array.relationsFriends
        } else if (mViewModel.user.friendship === 3) {
            requireActivity().onBackPressed()
            return
        }
    }
    

    internal fun popupList() {
        val b = AlertDialog.Builder(requireActivity())
        b.setItems(relations) { dialog, which ->
            dialog.dismiss()
            if (getResources().getStringArray(relations).size == 2)//if (relations.size == 2)
                when (which) {
                    0 -> {
                        //if(mViewModel.user.friendship === 2 && mViewModel.user.friendshipMaker === mViewModel.user.uid)
                        if(relations ===  R.array.relationsRequestSent || relations ===  R.array.relationsFriends)
                            mViewModel.friendship(VMUser.Relations.REMOVE)
                        else
                            mViewModel.friendship()
                    }
                    1 -> mViewModel.friendship(VMUser.Relations.BLOCK)
                }
            else
                when (which) {
                    0 -> mViewModel.friendship(VMUser.Relations.ACCEPT)
                    1 -> mViewModel.friendship(VMUser.Relations.REMOVE)
                    2 -> mViewModel.friendship(VMUser.Relations.BLOCK)
                }
        }
        b.show()
    }
}
