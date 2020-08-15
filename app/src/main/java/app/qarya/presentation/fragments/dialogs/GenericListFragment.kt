package app.qarya.presentation.fragments.dialogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import app.qarya.R
import app.qarya.model.ModelHolder
import app.qarya.model.ModelType
import app.qarya.model.models.Commun
import app.qarya.model.models.Relation
import app.qarya.model.shared.YDUserManager
import app.qarya.presentation.adapters.MultiAdapter
import app.qarya.presentation.base.MyDialogRecyclerFragment
import app.qarya.presentation.vms.VMLister
import app.qarya.utils.MyConst
import tn.core.presentation.listeners.Action
import tn.core.presentation.listeners.OnInteractListener
import tn.core.util.Const


class GenericListFragment(var callback:OnInteractListener<Any>) : MyDialogRecyclerFragment<ModelHolder, VMLister>() {

    companion object {
        val SHARE = 0;

        fun newInstance(action: Int, listener:OnInteractListener<Any>): GenericListFragment {
            val args = Bundle()
            args.putInt(MyConst.CATEGORY, action)
            val fragment = GenericListFragment(listener)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(VMLister::class.java)
        mViewModel.callErrors.observe(this, Observer<List<String>> { this.onError(it) })
        mViewModel.loadStatus.observe(this, Observer<Boolean> { this.onStatusChanged(it) })
        mViewModel.relationsFriends.observe(this, Observer<List<Relation>> { this.onItemsReceived(it) })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MultiAdapter(lista, object : OnInteractListener<Commun> {
            override fun onInteract(item: Commun, action: Action) {
                onClick(item)
            }

            override fun onClick(item: Commun) {
                callback.onClick(item.getUid())
            }
        })
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter

        val action = args.getInt(MyConst.CATEGORY, 0)
        when (action) {
            0 -> {
                val id = args.getInt(Const.ID, YDUserManager.auth()!!.id!!)
                mViewModel.friends(id)
            }
        }
    }


    override fun onDataReceived(data: MutableList<ModelHolder>?) {
        super.onDataReceived(data)
        adapter.notifyDataSetChanged()
    }
    fun onItemsReceived(data: List<Commun>) {
        lista.add(ModelHolder(getText(R.string.share_via), ModelType.HEADER))
        for (item in data) {
            lista.add(ModelHolder(item, ModelType.SHARE_PRIVATE))
        }
        adapter.notifyDataSetChanged()
    }

}