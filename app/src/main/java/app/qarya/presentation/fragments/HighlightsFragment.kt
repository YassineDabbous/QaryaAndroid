package app.qarya.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.qarya.R
import app.qarya.model.ModelHolder
import app.qarya.model.ModelType
import app.qarya.model.models.Category
import app.qarya.model.models.Commun
import app.qarya.model.models.Post
import app.qarya.model.models.requests.Filter
import app.qarya.model.models.responses.LikeResponse
import app.qarya.model.models.responses.MarkResponse
import app.qarya.model.shared.YDUserManager
import app.qarya.presentation.activities.MainActivity
import app.qarya.presentation.adapters.MultiAdapter
import app.qarya.presentation.adapters.PostsAdapter
import app.qarya.presentation.adapters.vh.AdsViewHolder
import app.qarya.presentation.adapters.vh.CategoryHorizontalVH
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.base.MyRecyclerFragment
import app.qarya.presentation.fragments.creator.CreatorFragment
import app.qarya.presentation.fragments.profile.UserProfileFragment
import app.qarya.presentation.vms.VMPosts
import kotlinx.android.synthetic.main.fragment_highlights.*
import tn.core.model.responses.PagingResponse
import tn.core.presentation.base.adapters.BaseAdapter
import tn.core.presentation.listeners.Action
import tn.core.presentation.listeners.EndlessListener
import tn.core.presentation.listeners.OnClickItemListener
import tn.core.presentation.listeners.OnInteractListener
import tn.core.util.Const

/**
 * A fragment representing a list of Items.
 *
 *
 * interface.
 */
class HighlightsFragment : MyRecyclerFragment<ModelHolder, VMPosts>() {
    //FloatingActionButton newTopic;
    var adapterVertical: MultiAdapter? = null
    var follow: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(VMPosts::class.java)
        mViewModel!!.callErrors.observe(
            this,
            Observer { errors: List<String?>? -> onError(errors) })
        mViewModel!!.loadStatus.observe(this, Observer { b: Boolean? -> onStatusChanged(b) })
        mViewModel!!.like.observe(
            this,
            Observer { data: LikeResponse -> this.onDataReceived(data) })
        mViewModel!!.bookmark.observe(this, Observer { data: MarkResponse ->
            this.onDataReceived(
                data
            )
        })
        mViewModel!!.getLiveData().observe(this, Observer { data: PagingResponse<ModelHolder> ->
            this.onDataReceived(
                data
            )
        })
        mViewModel!!.categories.observe(this, Observer { this.onCategoriesReceived(it) })
        if (lista.size == 0) {
            getData()
        }
    }

    override fun getData() {
        super.getData()
        var categoryID = args.getInt(Const.CATEGORY, 0)
        var type = args.getInt(Const.TYPE, ModelType.PRODUCT)
        if(categoryID!=0)
            when(type){
                ModelType.USER, ModelType.STORE -> {
                    mViewModel?.accounts(Filter(null, type, null, categoryID), page)
                }
                else -> {
                    mViewModel?.init(categoryID, page)
                }
            }
        else
            // TODO
            // get highlights
        ;
    }

    fun onCategoriesReceived(list: List<Category>){
        var adapterHorizontal = BaseAdapter(
            list,
            CategoryHorizontalVH::class.java,
            AdsViewHolder::class.java,
            OnClickItemListener {
                args.putSerializable(Const.CATEGORY, it.id)
                page = 1
                lista.removeAll(lista)
                getData()
            },
            999
        )
        horizontalRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        horizontalRV.adapter = adapterHorizontal
    }


    override fun onDataReceived(data: PagingResponse<ModelHolder>) {
        super.onDataReceived(data)
        if (lista.size == 0) {
            empty_view.visibility = View.VISIBLE
            recycler_view.visibility = View.GONE
            return
        } else {
            empty_view.visibility = View.GONE
            recycler_view.visibility = View.VISIBLE
        }
        adapterVertical?.notifyDataSetChanged()
    }

    fun onDataReceived(data: LikeResponse) {
        MyActivity.log("search for liked item...")
        for (i in lista.indices) {
            val model = lista[i]!!.model as Post
            if (model.getId() == data.id) {
                MyActivity.log("Liked item found!")
                model.liked = data.liked
                model.likesCount = data.likesCount
                MyActivity.log("Refresh adapter at " + i + " position with " + data.likesCount + " likes")
                //adapterVertical?.notifyItemChanged(i);
                adapterVertical?.refresh(ModelHolder(model, model.modelType), i)
            }
        }
    }

    fun onDataReceived(data: MarkResponse) {
        MyActivity.log("search for liked item...")
        for (i in lista.indices) {
            val model = lista[i]!!.model as Post
            if (model.getId() == data.id) {
                MyActivity.log("Liked item found!")
                model.marked = data.bookmarked
                MyActivity.log("Refresh adapter at " + i + " position with marked " + data.bookmarked)
                //adapterVertical?.notifyItemChanged(i);
                adapterVertical?.refresh(ModelHolder(model, model.modelType), i)
            }
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_highlights, container, false)
        initDefaultViews(view)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recycler_view.layoutManager = mLayoutManager
        endlessListener = EndlessListener(
            0,
            PostsAdapter.ADS_AFTER,
            object : EndlessListener.Action() {
                override fun getOnScroll() {
                    MyActivity.log("get in scroll")
                    getData()
                }
            })
        recycler_view.addOnScrollListener(endlessListener)
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recycler_view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recycler_view, dx, dy)
                (activity as MainActivity?)!!.toggleSwipe(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) // 0 is for first item position
            }
        })
        adapterVertical = MultiAdapter(lista, object : OnInteractListener<Commun> {
            override fun onInteract(item: Commun, action: Action) {
                when (action) {
                    Action.LIKE -> mViewModel!!.like(item.getId())
                    Action.BOOKMARK -> mViewModel!!.bookmark(item)
                    Action.COMMENT -> (activity as MainActivity?)!!.comment(item)
                    Action.SHARE -> (activity as MainActivity?)!!.share(item)
                }
            }

            override fun onClick(item: Commun) {
                (activity as MyActivity?)!!.onItemSelected(item)
            }
        })
        recycler_view.adapter = adapterVertical

        setType()
    }

    fun setType(){
        var type = args.getInt(Const.TYPE, ModelType.PRODUCT)
        when(type){
            ModelType.PRODUCT -> {
                pageTitle.text = "Buy & sell"
                createBtn.setOnClickListener { setFragment(CreatorFragment.newInstance(ModelType.PRODUCT)) }
                moreBtn.setOnClickListener { setFragment(PostsFragment.newInstance("more")) }
                mViewModel!!.categoriesOfProducts()
            }
            ModelType.STORE -> {
                pageTitle.text = "Stores & Markets"
                createBtn.setOnClickListener { setFragment(CreatorFragment.newInstance(ModelType.STORE)) }
                moreBtn.setOnClickListener { setFragment(PostsFragment.newInstance("more")) }
                mViewModel!!.categoriesOfStores()
            }
            ModelType.USER -> {
                pageTitle.text = "Employees"
                createBtn.setOnClickListener {
                    setFragment(
                        UserProfileFragment.newInstance(
                            YDUserManager.auth().id
                        )
                    )
                }
                moreBtn.setOnClickListener { setFragment(PostsFragment.newInstance("more")) }
                mViewModel!!.categoriesOfUsers()
                recyclerView.layoutManager = GridLayoutManager(context, 2)
            }
        }
    }

    companion object {
        fun newInstance(type: Int): HighlightsFragment {
            val args = Bundle()
            args.putSerializable(Const.TYPE, type)
            val fragment = HighlightsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}