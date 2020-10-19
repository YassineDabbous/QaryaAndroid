package app.qarya.presentation.fragments.posts


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import app.qarya.R
import app.qarya.model.models.Post
import app.qarya.model.models.SFile
import app.qarya.model.models.responses.GeneralResponse
import app.qarya.model.models.responses.LikeResponse
import app.qarya.model.shared.YDUserManager
import app.qarya.presentation.activities.MainActivity
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.base.MyFragment
import app.qarya.presentation.fragments.dialogs.PhotoZoomFragment
import app.qarya.presentation.vms.VMProduct
import app.qarya.utils.AlertUtils
import app.qarya.utils.AppHelpers
import app.qarya.utils.ImageHelper
import app.qarya.utils.MyConst
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.android.synthetic.main.toolbar.*
import tn.core.util.TextUtils
import tn.core.util.WebUtils
import java.util.*
import kotlin.collections.ArrayList


class ProductFragment : MyFragment<VMProduct>() {

    //////// For Slider ////////:
    //private val viewPager: ViewPager? = null
    //private val dotsLayout: LinearLayout? = null
    var dots: MutableList<TextView> = mutableListOf()

    var images: List<SFile> = ArrayList()
    var currentPage = 0
    ///////////////////////////

    companion object {
        fun newInstance(pid: Int): ProductFragment {
            val args = Bundle()
            args.putInt(MyConst.ID, pid)
            val fragment = ProductFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(VMProduct::class.java)
        mViewModel.callErrors.observe(this, Observer<List<String>> { this.onError(it) })
        mViewModel.loadStatus.observe(this, Observer<Boolean> { this.onStatusChanged(it) })
        mViewModel.like.observe(this, Observer { this.handleLike(it) })
        mViewModel.reportProduct.observe(this, Observer<Any> { this.handleReport(it) })
        mViewModel.deleteProduct.observe(this, Observer<GeneralResponse> { this.handleDelete(it) })
        mViewModel.getLiveData().observe(this, Observer<Post> { this.onDataReceived(it) })
        getData()
    }

    override fun getData() {
        super.getData()
        mViewModel.one(args.getInt(MyConst.ID))
    }

    fun onDataReceived(data: Post) {
        mViewModel.item = data
        bind()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_store, container, false)
        initDefaultViews(v)
        errorShow = false
        return v
    }


    fun bind() {
        usernameTV.setText(mViewModel.item?.uname?: "")
        titleTV.setText(mViewModel.item?.title?: "")
        priceTV.setText("${getText(R.string.price)}: ${mViewModel.item?.price}")


        /*
        categoryTV.setText("${getText(R.string.category)}: ${mViewModel.item?.categoryName}")
        addressTV.setText("${getText(R.string.address)}: ${mViewModel.item?.address}")
        shippingTV.setText("${getText(R.string.shipping)}: ${mViewModel.item?.shipping}")
        descriptionTV.setText("${getText(R.string.description)}: "+(mViewModel.item?.description ?: ""))
        */


        TextUtils.htmlToView(categoryTV, "<b>${getText(R.string.category)}: </b> ${mViewModel.item?.categoryName}")
        TextUtils.htmlToView(addressTV, "<b>${getText(R.string.pricing)}: </b> ${mViewModel.item?.priceType}")
        TextUtils.htmlToView(shippingTV, "<b>${getText(R.string.shipping)}: </b> ${mViewModel.item?.shipping}")
        TextUtils.htmlToView(descriptionTV, "<b>${getText(R.string.description)}: </b> ${mViewModel.item?.description}")
        //TextUtils.htmlToView(descriptionTV, mViewModel.item?.description ?: "")

        setLiker()
        setSlider()

        ImageHelper.load(userPhoto, mViewModel.item?.upicture);

        /*mViewModel.item?.photo?.let {
            ImageHelper.load(photo, mViewModel.item?.photo);
            photo.setOnClickListener { (activity as MyActivity).setFragment(PhotoZoomFragment.newInstance(mViewModel.item!!.photo)) }
        }*/
        if (mViewModel.item!!.likesCount > 0)
            likesCount.setText(mViewModel.item?.likesCount.toString());
        if (mViewModel.item!!.viewsCount > 0)
            viewsCount.setText(mViewModel.item?.viewsCount.toString());
        if (mViewModel.item!!.commentsCount > 0)
            commentsCount.setText(mViewModel.item?.commentsCount.toString());

        mViewModel.item?.mapLink?.let {
            map.setOnClickListener{
                //WebUtils.openLink(context, mViewModel.item?.mapLink)
                WebUtils.viewOnMap(requireContext(), mViewModel.item?.latitude!!, mViewModel.item?.longitude!!)
            }
        }
        mViewModel.item?.phone?.let {
            phone.setOnClickListener{
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${mViewModel.item?.phone}"))
                startActivity(intent)
            }
        }

        chatBtn.setOnClickListener{
            AppHelpers.private(requireContext(), mViewModel.item!!.getUid())
        }
        likeBtn.setOnClickListener{
            mViewModel.like()
        }


        toolbar_right_2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.share_outline))
        toolbar_right_2.visibility = View.VISIBLE
        toolbar_right_2.setOnClickListener{
            (activity as MyActivity).share(mViewModel.item)
        }
    }






    fun handleLike(response: LikeResponse) {
        mViewModel.item?.setLikesCount(response.likesCount)
        mViewModel.item?.setLiked(response.liked)
        setLiker()
    }

    fun setLiker() {
        likesCount.setText( mViewModel.item?.getLikesCount().toString())
        if ( mViewModel.item?.getLiked()!!) {
            likeBtn.setImageResource(R.drawable.heart)
        } else {
            likeBtn.setImageResource(R.drawable.heart_outline)
        }
    }
    fun handleDelete(response: GeneralResponse) {
        requireActivity().onBackPressed()
    }
    fun handleReport(response: Any) {}




    override fun showOptions() {
        super.showOptions()
        val list = ArrayList<String>()
        list.add(getText(R.string.report).toString())
        val me = YDUserManager.auth()
        if (me != null && me.id != null && me.id == mViewModel.item?.getUid())
            list.add(getText(R.string.delete).toString())

        val builderSingle = MaterialAlertDialogBuilder(requireActivity())

        val arrayAdapter = ArrayAdapter<String>(requireActivity(), android.R.layout.select_dialog_item, list.toTypedArray<String>())

        builderSingle.setAdapter(arrayAdapter) { dialog, which ->
            when (which) {
                0 -> reportAlert()
                1 -> deleteAlert()
            }
            dialog.dismiss()
        }
        builderSingle.show()
    }

    fun deleteAlert() {
        val action = object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                mViewModel.delete()
            }
        }
        action.message = requireContext().getText(R.string.delete).toString()
        AlertUtils.alert(requireContext(), action)
    }

    fun reportAlert() {
        val action = object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                mViewModel.report(o.toString())
            }
        }
        action.message = requireContext().getText(R.string.report).toString()
        AlertUtils.report(requireContext(), action)
    }






    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)!!.hideToolbar()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity?)!!.showToolbar()
    }



    fun setSlider(){
        val stockArr = arrayOf<String>()
        images = mViewModel.item!!.files;
        //images = new String[]{"http://cdn.ekhdemni.net/uploads/photos/11-1536082347.png","http://cdn.ekhdemni.net/uploads/photos/13-1536267929.png"};

        // adding bottom dots
        //images = new String[]{"http://cdn.ekhdemni.net/uploads/photos/11-1536082347.png","http://cdn.ekhdemni.net/uploads/photos/13-1536267929.png"};

        // adding bottom dots
        addBottomDots(0)

        viewPager!!.adapter = myViewPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        autoSliding()
    }

    fun autoSliding() {
        val handler = Handler()
        val timer = Timer()
        val Update = Runnable {
            if (viewPager != null && isInForegroundMode) {
                if (currentPage ==  mViewModel.item!!.files.size - 1) {
                    currentPage = 0
                }
                MyActivity.log("☺ ViewPager setCurrentItem: " + (currentPage + 1))
                viewPager.setCurrentItem(currentPage++, true)
            } else {
                MyActivity.log("☺ STOP TIMER !! ")
                timer.cancel()
            }
        } // This will create a new Thread
        timer.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)
    }

    private fun addBottomDots(currentPage: Int) {
        var currentPage = currentPage
        if (isInForegroundMode) {R.dimen.album_title
            val colorsActive = resources.getIntArray(R.array.array_dot_active)
            val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
            if (currentPage >= colorsInactive.size) currentPage = 0
            dotsLayout!!.removeAllViews()
            for (i in dots.indices) {
                val v:TextView = TextView(context)
                v.text = Html.fromHtml("&#8226;")
                v.textSize = 35f
                v.setTextColor(colorsInactive[currentPage])
                dots.set(i, v)
                dotsLayout.addView(dots[i])
            }
            if (dots.size > 0) dots[currentPage].setTextColor(colorsActive[currentPage])
        }
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    //  viewpager change listener
    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            currentPage = position
            addBottomDots(position)
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * View pager adapter
     */
    private var myViewPagerAdapter: PagerAdapter = object : PagerAdapter() {
        // //private LayoutInflater layoutInflater;
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = ImageView(getContext())

            ImageHelper.load(view, images.get(position).url)
            view.setOnClickListener { (activity as MyActivity).setFragment(PhotoZoomFragment.newInstance(images.get(position).url)) }

            view.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return images.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}