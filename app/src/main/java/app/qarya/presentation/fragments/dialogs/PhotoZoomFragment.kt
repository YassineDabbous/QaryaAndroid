package app.qarya.presentation.fragments.dialogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_zoom.*
import app.qarya.R
import app.qarya.presentation.base.MyDialogFragment
import app.qarya.utils.ImageHelper
import app.qarya.utils.MyConst


class PhotoZoomFragment : MyDialogFragment<Any>() {

    companion object {
        fun newInstance(path: String): PhotoZoomFragment {
            val args = Bundle()
            args.putString(MyConst.PATH, path)
            val fragment = PhotoZoomFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_zoom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path:String = args.getString(MyConst.PATH, "")
        ImageHelper.loadFit(imageSingle, path)
    }

}