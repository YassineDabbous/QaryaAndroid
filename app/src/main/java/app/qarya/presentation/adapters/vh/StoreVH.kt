package app.qarya.presentation.adapters.vh

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.qarya.R
import app.qarya.model.models.Post
import app.qarya.utils.ImageHelper
import tn.core.presentation.base.adapters.BaseViewHolder

class ProductVH(val mView: View) : BaseViewHolder<Post>(mView) {

    val title: TextView? = mView.findViewById<View>(R.id.title) as? TextView
    //val description: TextView? = mView.findViewById<View>(R.id.description) as? TextView
    val priceTV: TextView? = mView.findViewById<View>(R.id.priceTV) as? TextView
    val categoryName : TextView? = mView.findViewById<View>(R.id.categoryName) as? TextView
    val storeImage = mView.findViewById(R.id.storeImage) as? ImageView
    val userPhoto = mView.findViewById(R.id.userPhoto) as? ImageView

    override fun getLayoutId(): Int {
        return R.layout.item_store
    }

    override fun bind(item: Post) {
        ImageHelper.load(userPhoto, item?.upicture)
        title!!.text = item.title ?: ""
        priceTV?.text = (item.price)
        //description!!.text = item.address ?: ""

        if (item?.photo != null && item?.photo != "") {
            storeImage?.visibility = View.VISIBLE
            ImageHelper.load(storeImage, item?.photo)
        } else storeImage?.visibility = View.GONE

        if(item?.categoryName != null){
            categoryName?.text = item?.categoryName
            categoryName?.visibility = View.VISIBLE
        } else categoryName?.visibility = View.GONE

        itemView.setOnClickListener {
            if (null != listener) {
                listener.onClick(item)
            }
        }
    }


}