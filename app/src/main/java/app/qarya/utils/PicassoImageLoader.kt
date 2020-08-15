package app.qarya.utils;

import android.widget.ImageView
import com.esafirm.imagepicker.features.imageloader.ImageLoader
import com.esafirm.imagepicker.features.imageloader.ImageType
import com.squareup.picasso.Picasso
import java.io.File
import app.qarya.R;

class PicassoImageLoader : ImageLoader {

        companion object {
                const val WIDTH = 300
                const val HEIGHT = 300
        }

        override fun loadImage(path: String, imageView: ImageView, imageType: ImageType) {
                val placeholderResId = when(imageType) {
                        ImageType.FOLDER -> R.drawable.ef_folder_placeholder
                        else -> R.drawable.ef_image_placeholder
                }

                Picasso.get()
                .load(File(path))
                .resize(WIDTH, HEIGHT)
                .centerCrop()
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .into(imageView)
        }
}