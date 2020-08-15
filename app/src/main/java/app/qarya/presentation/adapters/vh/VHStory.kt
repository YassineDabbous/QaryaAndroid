package app.qarya.presentation.adapters.vh

import android.view.View
import android.widget.ImageView
import app.qarya.R
import app.qarya.model.models.Post
import app.qarya.utils.ImageHelper
import tn.core.presentation.base.adapters.BaseViewHolder

class VHStory(val mView: View) : BaseViewHolder<Post?>(mView) {
    val pictureView: ImageView? = mView.findViewById(R.id.pictureView) as? ImageView
    val imageView: ImageView? = mView.findViewById(R.id.imageView) as? ImageView


    override fun getLayoutId(): Int {
        return R.layout.item_story
    }

    override fun bind(item: Post?) {
        ImageHelper.loadCircle(pictureView, item?.upicture) //, 150,150);
        ImageHelper.load(imageView, item?.photo) //, 150,150);
        imageView?.setOnClickListener {
            if (null != listener) {
                listener.onClick(item)
            }
        }
    }


}