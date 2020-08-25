package app.qarya.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.qarya.model.ModelHolder
import app.qarya.model.ModelType
import app.qarya.presentation.base.MyActivity
import tn.core.presentation.listeners.OnClickItemListener
import app.qarya.model.models.*
import app.qarya.model.models.User
import app.qarya.presentation.adapters.vh.*
import tn.core.presentation.listeners.OnInteractListener

class MultiAdapter(private var items : MutableList<ModelHolder>, private val listener: OnInteractListener<Commun>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_ADS = ModelType.ADS
    var ADS_ENABLED = true
    var ADS_AFTER = 8


    //CREATE ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        MyActivity.log("viewType is :: " + viewType)

        when(viewType){
            VIEW_TYPE_ADS -> {
                val v = LayoutInflater.from(parent.context).inflate(AdsViewHolder(View(parent.context)).layoutId,parent,false)
                return AdsViewHolder(v)
            }
            ModelType.HEADER -> {
                val v = LayoutInflater.from(parent.context).inflate(HeaderVH(View(parent.context)).layoutId,parent,false)
                return HeaderVH(v)
            }
            ModelType.USER -> {
                val v = LayoutInflater.from(parent.context).inflate(UsersVH(View(parent.context)).layoutId,parent,false)
                return UsersVH(v)
            }
            ModelType.RELATION -> {
                val v = LayoutInflater.from(parent.context).inflate(RelationVH(View(parent.context)).layoutId,parent,false)
                return RelationVH(v)
            }
            ModelType.GENERAL -> {
                val v = LayoutInflater.from(parent.context).inflate(ItemVH(View(parent.context)).layoutId,parent,false)
                return ItemVH(v)
            }
            ModelType.CATEGORY -> {
                val v = LayoutInflater.from(parent.context).inflate(CategoryVH(View(parent.context)).layoutId,parent,false)
                return CategoryVH(v)
            }
            ModelType.POST -> {
                val v = LayoutInflater.from(parent.context).inflate(VHPost(View(parent.context)).layoutId,parent,false)
                return VHPost(v)
            }
            ModelType.NOTE -> {
                val v = LayoutInflater.from(parent.context).inflate(VHNote(View(parent.context)).layoutId,parent,false)
                return VHNote(v)
            }
            ModelType.PRODUCT -> {
                val v = LayoutInflater.from(parent.context).inflate(ProductVH(View(parent.context)).layoutId,parent,false)
                return ProductVH(v)
            }
            ModelType.FORK-> {
                MyActivity.log("it's a Fork ! " + viewType)
                val v = LayoutInflater.from(parent.context).inflate(ListVH(View(parent.context)).layoutId,parent,false)
                return ListVH(v) //Fork
            }
            ModelType.SHARE_PRIVATE -> {
                val v = LayoutInflater.from(parent.context).inflate(SharerVH(View(parent.context)).layoutId,parent,false)
                return SharerVH(v)
            }
            else -> {
                MyActivity.log("else it's a Fork ! " + viewType)
                val v = LayoutInflater.from(parent.context).inflate(ListVH(View(parent.context)).layoutId,parent,false)
                return ListVH(v) //Fork
            }
        }
    }

    /// BIND DATA
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_ADS -> (holder as AdsViewHolder).bind(null)
            else -> {
                var p = getRealIndex(position)
                val item = items[p]
                if (item.TYPE == ModelType.FORK){           (holder as ListVH).bind(item.model as Fork, listener)}
                else if (item.TYPE == ModelType.RELATION)   (holder as RelationVH).bind(item.model as Relation, (listener as OnClickItemListener<Relation>))
                else if (item.TYPE == ModelType.GENERAL)   (holder as ItemVH).bind(item.model as Post, (listener as OnClickItemListener<Post>))
                else if (item.TYPE == ModelType.POST)       (holder as VHPost).bind(item.model as? Post, (listener as OnClickItemListener<Post?>))
                else if (item.TYPE == ModelType.NOTE)       (holder as VHNote).bind(item.model as? Post, (listener as OnClickItemListener<Post?>))
                else if (item.TYPE == ModelType.PRODUCT)       (holder as ProductVH).bind(item.model as? Post, (listener as OnClickItemListener<Post?>))
                else if (item.TYPE == ModelType.USER)       (holder as UsersVH).bind(item.model as User, (listener as OnClickItemListener<User>))
                else if (item.TYPE == ModelType.CATEGORY)       (holder as CategoryVH).bind(item.model as Category, (listener as OnClickItemListener<Category>))
                else if (item.TYPE == ModelType.HEADER)     (holder as HeaderVH).bind(item.model as String, (listener as OnClickItemListener<String>))
                else if (item.TYPE == ModelType.SHARE_PRIVATE)       (holder as SharerVH).bind(item.model as Commun, (listener as OnClickItemListener<Commun>))
            }
        }
    }


    override fun getItemCount(): Int {
        if (!ADS_ENABLED || ADS_AFTER == 0)
            return items.size
        val s = items.size
        return s + s / ADS_AFTER
    }

    fun getRealIndex(position:Int): Int {
        if (ADS_ENABLED && (position >= ADS_AFTER)) {
            return (position - position / ADS_AFTER)
        } else {
            return position
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (ADS_ENABLED && (position >= ADS_AFTER) && (position % ADS_AFTER == 0)) {
            return VIEW_TYPE_ADS
        } else {
            return items.get(getRealIndex(position)).TYPE
        }
    }


    fun changeList(items : MutableList<ModelHolder>){
        this.items = items;
        notifyDataSetChanged()
    }
    fun refresh(yModel: ModelHolder) {
        refresh(yModel, -1)
    }

    fun refresh(item: ModelHolder, i: Int) {
        var i = i
        if (items.indexOf(item) >= 0)
            items.set(items.indexOf(item), item)
        if (i == -1) i = items.indexOf(item)
        MyActivity.log("initial index, $i")
        i = getRealIndex(i)
        MyActivity.log("indeeeeeeeex after 2 years, $i")
        if (i < itemCount)
            notifyDataSetChanged()
        else
            MyActivity.log("indeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeex, " + i + "but size is " + itemCount)//notifyItemChanged(i, item);
    }
}