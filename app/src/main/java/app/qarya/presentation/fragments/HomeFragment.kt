package app.qarya.presentation.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.qarya.R
import app.qarya.model.ModelHolder
import app.qarya.model.ModelType
import app.qarya.model.models.*
import app.qarya.model.models.responses.LikeResponse
import app.qarya.model.models.responses.MarkResponse
import app.qarya.presentation.adapters.MultiAdapter
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.base.MyRecyclerFragment
import app.qarya.presentation.fragments.creator.CreatorFragment
import app.qarya.presentation.vms.HomeViewModel
import app.qarya.utils.SpeedyLinearLayoutManager
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_home.*
import tn.core.presentation.listeners.Action
import tn.core.presentation.listeners.EndlessListener
import tn.core.presentation.listeners.OnInteractListener


class HomeFragment : MyRecyclerFragment<ModelHolder, HomeViewModel>() {

    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v:View = inflater.inflate(R.layout.fragment_home, container, false)
        initDefaultViews(v)
        return v
    }

    fun setCreatorBtn(){
        fab_menu.visibility = View.VISIBLE

        fab_add_story.setOnClickListener { (activity as MyActivity).setFragment(CreatorFragment.newInstance(ModelType.STORY)) }
        fab_add_post.setOnClickListener { (activity as MyActivity).setFragment(CreatorFragment.newInstance(ModelType.POST)) }
        fab_add_note.setOnClickListener { (activity as MyActivity).setFragment(CreatorFragment.newInstance(ModelType.NOTE)) }
        fab_add_store.setOnClickListener { (activity as MyActivity).setFragment(CreatorFragment.newInstance(ModelType.PRODUCT)) }

        fab_add_story.setImageDrawable( ContextCompat.getDrawable(activity!!, R.drawable.flash_outline) )
        fab_add_note.setImageDrawable( ContextCompat.getDrawable(activity!!, R.drawable.file_edit_outline) )
        fab_add_post.setImageDrawable( ContextCompat.getDrawable(activity!!, R.drawable.square_edit_outline) )
        fab_add_store.setImageDrawable( ContextCompat.getDrawable(activity!!, R.drawable.store) )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCreatorBtn()

        adapter = MultiAdapter(lista, object : OnInteractListener<Commun>{
            override fun onInteract(item: Commun, action: Action) {
                MyActivity.log("action: ", action.toString())
                when(action){
                    Action.LOAD         -> {mViewModel.load(item as Commun)}

                    Action.SHOW         -> {onClick(item)}
                    Action.LIKE         -> {mViewModel.like(item)}
                    Action.COMMENT      -> (activity as MyActivity).comment(item)
                    Action.SHARE        -> (activity as MyActivity).share(item)
                    Action.BOOKMARK     -> {mViewModel.bookmark(item)}

                    Action.FOLLOW       -> {mViewModel.addRelation( (item as Relation).getUid() )}
                    Action.REFUSE       -> {mViewModel.refuseRelation( (item as Relation).getUid() )}

                    else                -> onClick(item)
                }
            }
            override fun onClick(item: Commun?) {
                println("selected as Any! => ${item}")
                when (item) {
                    is Post -> (activity as MyActivity).onItemSelected(item)
                    is User -> (activity as MyActivity).onItemSelected(item)
                    is Relation -> (activity as MyActivity).onItemSelected(item)
                    else    -> println("No type found for this item => ${item}")
                }
            }
        })

        //val manager = LinearLayoutManager(context)
        recyclerView.setVisibility(View.VISIBLE)
        recyclerView.setLayoutManager(SpeedyLinearLayoutManager(context, SpeedyLinearLayoutManager.VERTICAL, false))
        //recyclerView.layoutManager = manager
        recyclerView.adapter = adapter

        endlessListener = EndlessListener(20, 7, object : EndlessListener.Action() {
            override fun getOnScroll() {
                MyActivity.log("get in scroll")
                getData()
            }
        })
        recyclerView.addOnScrollListener(endlessListener)
    }



    override fun onResume() {
        super.onResume()
        MyActivity.logHome("onResume onResume "+lista.size);
        //doit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mViewModel!!.callErrors.observe(this, Observer<List<String>> { response -> onError(response) })
        mViewModel!!.loadStatus.observe(this, Observer<Boolean> { response -> onStatusChanged(response) })
        mViewModel.like.observe(this, Observer { this.onDataReceived(it) })
        mViewModel.bookmark.observe(this, Observer { this.onDataReceived(it) })
        mViewModel.modified.observe(this, Observer { this.onDataReceived(it) })
        mViewModel!!.getLiveData().observe(this, Observer<List<Commun>> { response -> onDataReception(response) })
        //mViewModel.configs?.observe(this, Observer { this.onDataReceived(it) })
        //mViewModel.configs()
        getData()
    }

    /*fun onDataReceived(data: ConfigsResponse) {
        YDUserManager.save(data)
        (activity as MainActivity).setBackground()
        MyActivity.log("configs btn "+data.editor)
        if (data.editor){
            //newTopic?.visibility = View.VISIBLE
        }
    }*/
    fun onDataReceived(data: LikeResponse) {
        MyActivity.log("search for liked item...")
        for (i in lista.indices) {
            val model = lista[i].model as Commun
            if (model.getId() == data.id) {
                MyActivity.log("Liked item found!")
                if (model is Post){
                    model.setLiked(data.liked)
                    model.setLikesCount(data.likesCount)
                }
                MyActivity.log("Refresh adapter at " + i + " position with " + data.likesCount + " likes")
                //adapter.notifyItemChanged(i);
                //adapter.refresh(i, lista[i])
                adapter.notifyDataSetChanged()
            }
        }
    }
    fun onDataReceived(data: MarkResponse) {
        MyActivity.log("search for liked item...")
        for (i in lista.indices) {
            val model = lista[i].model as Commun
            if (model.getId() == data.id) {
                MyActivity.log("Liked item found!")
                if (model is Post){
                    model.marked = data.bookmarked
                }
                MyActivity.log("Refresh adapter at " + i + " position with marked = " + data.bookmarked)
                //adapter.notifyItemChanged(i);
                //adapter.refresh(i, model)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun onDataReceived(data: Commun) {
        MyActivity.log("search for updated item...")
        for (i in lista.indices) {
            val model = lista[i].model as Commun
            if (model.modelType == data.modelType && model.modelPath == data.modelPath) {
                MyActivity.log("updated item found!")
                if(data.lista.size==0)
                    lista.removeAt(i);
                else
                    lista[i].model = data
                MyActivity.log("Refresh adapter at " + i + " position with data children size = " + data.lista.size)
                //adapter.notifyItemChanged(i);
                //adapter.refresh(i, lista[i])
                adapter.notifyDataSetChanged()
                break
            }
        }
    }


    override fun getData() {
        super.getData()
        mViewModel!!.init(page)
    }

    override fun onError(errors: List<String>) {

    }
    override fun onStatusChanged(b: Boolean) {
        MyActivity.logHome("Status changed to (" + b + ")")
        if (b)
            showPD()
        else
            dismissPD()
    }

     fun onDataReception(data:List<Commun>) {
        //super.onDataReceived(data)

        //val l: MutableList<ModelHolder> = ArrayList()
        for (item in data) {
            //lista.add(ModelHolder(item, ModelType.GENERAL))
            when(item) {
                is Fork -> lista.add(ModelHolder(item, ModelType.FORK))
                is User -> lista.add(ModelHolder(item, ModelType.USER))
                is Post -> {
                    lista.add(ModelHolder(item, item.type))
                }
                else     -> lista.add(ModelHolder(item, ModelType.FORK)) //is Fork
            }
        }
        //lista.addAll(l)
        endlessListener.total = lista.size+data.size;
        page++;
        MyActivity.logHome("ellllllldata "+lista.size);
        if(lista.size == 0){
            emptyView?.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //return
        }else{
            recyclerView.setVisibility(View.VISIBLE)
            adapter?.notifyDataSetChanged()
        }
    }



}
