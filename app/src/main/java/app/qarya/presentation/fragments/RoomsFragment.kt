package app.qarya.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.qarya.R
import app.qarya.model.models.Conversation
import app.qarya.model.models.Room
import app.qarya.presentation.activities.ChatActivity
import app.qarya.presentation.adapters.ConversationsAdapter
import app.qarya.presentation.adapters.RoomsAdapter
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.base.MyRecyclerFragment
import app.qarya.presentation.vms.VMChat
import app.qarya.presentation.vms.VMGeneral
import app.qarya.utils.AlertUtils
import com.google.android.material.tabs.TabLayout
import tn.core.model.responses.PagingResponse
import tn.core.presentation.listeners.OnClickItemListener
import tn.core.util.Const
import java.util.*

class RoomsFragment : MyRecyclerFragment<Room?, VMChat?>() {
    var pageConvs = 0
    init {
        tabIcons = intArrayOf(
                R.drawable.comment_outline,
                R.drawable.telegram
        )
        tabTitles = intArrayOf(
                R.string.rooms,
                R.string.conversations
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mViewModel = ViewModelProvider(this).get(VMChat::class.java)
        activity()?.vm?.callErrors?.observe(this, Observer { errors: List<String?>? -> onError(errors) })
        activity()?.vm?.loadStatus?.observe(this, Observer { b: Boolean? -> onStatusChanged(b) })
        activity()?.vm?.rooms?.observe(this, Observer { data: List<Room> -> this.onDataReceived(data) })
        activity()?.vm?.conversations?.observe(this, Observer { data: PagingResponse<Conversation> -> this.onConvsReceived(data) })
        getData()
    }

    override fun getData() {
        super.getData()
        if (current == 0) {
            page++
            activity()?.vm?.rooms()
        } else {
            pageConvs++
            activity()?.vm?.conversations(1)
        }
    }


    override fun onDataReceived(data: List<Room?>?) {
        super.onDataReceived(data)
        adapter.notifyDataSetChanged()
    }

    var lista2: MutableList<Conversation?> = ArrayList()
    fun onConvsReceived(data: PagingResponse<Conversation>) {
        endlessListener.total = data.total
        pageConvs = data.currentPage + 1
        onConvsReceived(data.data)
    }

    fun onConvsReceived(data: List<Conversation>) {
        lista2.addAll(data!!)
        MyActivity.log("Convs super count:" + lista.size)
        toggleDataVisibility(lista2.size == 0)
        /*if(lista2.size() == 0) {
            if (super.empty_view != null) super.empty_view.setVisibility(View.VISIBLE);
            if (recyclerView != null) recyclerView.setVisibility(View.GONE);
            return;
        }*/adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        (activity as ChatActivity?)!!.setTitle(getText(R.string.rooms).toString())
        initDefaultViewsWithListener(view)
        view.findViewById<View>(R.id.pageHeader).visibility = View.GONE
        initTabs(view, false)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = if (current == 0) RoomsAdapter(super.lista, OnClickItemListener { item: Room? -> (activity as MyActivity?)!!.onItemSelected(item) }) else ConversationsAdapter(lista2, OnClickItemListener { item: Conversation? -> (activity as MyActivity?)!!.onItemSelected(item) })
        recyclerView.adapter = adapter
        return view
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        super.onTabSelected(tab)
        val args = args
        args.putInt(Const.CATEGORY, tabLayout.selectedTabPosition)
        arguments = args
        if (tabLayout.selectedTabPosition == 0) {
            if (lista.size == 0) getData()
            (activity as ChatActivity?)!!.setTitle(getText(R.string.rooms).toString())
            adapter = RoomsAdapter(lista, OnClickItemListener { item: Room? -> (activity as MyActivity?)!!.onItemSelected(item) })
        } else if (tabLayout.selectedTabPosition == 1) {
            if (lista2.size == 0) getData()
            (activity as ChatActivity?)!!.setTitle(getText(R.string.conversations).toString())
            adapter = ConversationsAdapter(lista2, OnClickItemListener { item: Conversation? -> (activity as MyActivity?)!!.onItemSelected(item) })
        }
        recyclerView.adapter = adapter
    }

    override fun setTabs() {
        super.setTabs()
        super.tabIcons = tabIcons
        super.tabTitles = tabTitles
    }

    companion object {
        fun newInstance(public0Private1: Int): RoomsFragment {
            val args = Bundle()
            args.putInt(Const.CATEGORY, public0Private1)
            val fragment = RoomsFragment()
            fragment.arguments = args
            return fragment
        }
    }



    fun activity(): ChatActivity? {
        return activity as? ChatActivity
    }


    internal var actions = R.array.chat_settings
    override fun showOptions() {
        //super.showOptions()
        AlertUtils.popupList(activity!!, actions, object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                val which = o as Int
                when (which) {
                    0 -> {
                        activity()?.showStatusOptions()
                    }
                    1 -> {
                        activity()?.vm?.togglePrivacy()
                    }
                    2 -> {
                        //
                    }
                }
            }
        })
    }
}