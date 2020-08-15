package app.qarya.presentation.fragments


import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import app.qarya.R
import app.qarya.model.models.APath
import app.qarya.presentation.adapters.LeftDrawerRecyclerAdapter
import app.qarya.presentation.vms.VMLeftDrawer

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.widget.ImageViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.list_item_drawer_header.*
import app.qarya.model.ModelType
import app.qarya.model.shared.YDUserManager
import app.qarya.model.models.responses.AuthResponse
import app.qarya.presentation.activities.MainActivity
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.services.MediaPlayerService
import app.qarya.utils.MyConst
import app.qarya.utils.ImageHelper
import app.qarya.utils.NotificationsSubscriber
import app.qarya.utils.Showcase

import timber.log.Timber
import tn.core.presentation.base.BaseActivity

import app.qarya.presentation.base.MyRecyclerFragment
import app.qarya.presentation.fragments.profile.ProfileUpdateFragment
import app.qarya.presentation.fragments.profile.UserProfileFragment
import kotlinx.android.synthetic.main.fragment_drawer_left.*
import tn.core.presentation.listeners.OnClickItemListener
import tn.core.util.Completion
import tn.core.util.EMailUtils

/**
 * Fragment handles the drawer menu.
 */
class DrawerFragmentLeft : MyRecyclerFragment<APath, VMLeftDrawer>() {


    private var mDrawerToggle: ActionBarDrawerToggle? = null

    // Drawer top menu fields.
    private var mDrawerLayout: DrawerLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(VMLeftDrawer::class.java)
        mViewModel.callErrors.observe(this, Observer<List<String>> { this.onError(it) })
        mViewModel.loadStatus.observe(this, Observer<Boolean> { this.onStatusChanged(it) })
        mViewModel.getLiveData().observe(this, Observer<List<APath>> { this.onDataReceived(it) })
    }


    fun setHeader(){
        setingsBtn.setOnClickListener {
            if (YDUserManager.check())
                (activity as MyActivity).setFragment(ProfileUpdateFragment.newInstance())
            else
                MainActivity.getInstance().showLogin()
            closeDrawerMenu()
        }
        styler.setOnClickListener {
            closeDrawerMenu()
            (activity as MyActivity).toggleTheme()
        }
        logout.setOnClickListener {
            YDUserManager.logout()
            NotificationsSubscriber.unsubscribe()
            closeDrawerMenu()
            (activity as MyActivity).showLogin()
        }
        player.setOnClickListener {
            MediaPlayerService.toggleMusicService(requireActivity(), object : Completion<Boolean>(){
                override fun finish(done: Boolean?) {
                    super.finish(done)
                    if (done!!)
                        ImageViewCompat.setImageTintList(player, ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    else
                        ImageViewCompat.setImageTintList(player, ColorStateList.valueOf(getResources().getColor(R.color.white)));
                }
            })
            //closeDrawerMenu()
        }
        bookmarks.setOnClickListener {
            (activity as MyActivity).setFragment(PostsFragment.newInstance(APath("marked", ModelType.NOTE)))
            closeDrawerMenu()
        }
        upicture.setOnClickListener {
            if (YDUserManager.check())
                setFragment(UserProfileFragment.newInstance(YDUserManager.auth().id))
            else
                MainActivity.getInstance().showLogin()
            closeDrawerMenu()
        }
        name.setOnClickListener {
            if (YDUserManager.check())
                (activity as MyActivity).setFragment(UserProfileFragment.newInstance(YDUserManager.auth().id))
            else
                MainActivity.getInstance().showLogin()
            closeDrawerMenu()
        }

        refreshHeader()

        /*Showcase.show(requireActivity(), home, "Home", "Here you find all news!", OnClickItemListener<String> {
            MyActivity.log("showcase for: home clicked")
            Showcase.show(requireActivity(), bookmarks, "Bookmarks", "Your marked posts are there!", OnClickItemListener<String> {
                MyActivity.log("showcase for: bookmarks clicked")
                Showcase.show(requireActivity(), player, "Radio", "You can listen to tunisian radio channel, Just try it!", OnClickItemListener<String> {
                    MyActivity.log("showcase for: player clicked")
                    //(activity as MainActivity).showPrayerCase()
                })
            })
        })*/
    }

    fun showcase(){
        open()
        Showcase.show(requireActivity(), player, "الراديو", "جميع إذاعات الوطن في تطبيق واحد! يمكن تشغيلها من خلال هذا الزر ثم الانتقال بينها في القائمة!", OnClickItemListener<String> {
            MyActivity.log("player showcase tapped")
            Showcase.show(requireActivity(), logout, "تسجيل الحساب", "اغلب خصائص التطبيق تحتاج الى تسجيل الدخول, يمكن إنشاء حساب جديد من هنا", OnClickItemListener<String> {
                MyActivity.log("showcase for: logout clicked")
                Showcase.show(requireActivity(), bookmarks, "المفضلة", "كل الاحداث التي ستقوم بحفضها ستجدها من وراء هذا الرز !", OnClickItemListener<String> {
                    MyActivity.log("showcase for: bookmarks clicked")
                    Showcase.show(requireActivity(), styler, "التصميم", "تغيير تصميم التطبيق بضغظة واحدة", OnClickItemListener<String> {
                        MyActivity.log("showcase for: bookmarks clicked")

                    })
                })
            })
        })
    }

    fun refreshHeader(){
        val me:AuthResponse? = YDUserManager.auth()
        if (me!=null&&me.photo!=null) ImageHelper.loadCircle(upicture, me.photo)
        if (me!=null&&me.name!=null) name.text = me.name
        // TODO set category name too
    }

    override fun onResume() {
        super.onResume()
        setHeader()
    }

    override fun onDataReceived(data: List<APath>) {
        if (lista.size==0){
            super.onDataReceived(data)
            BaseActivity.log(" Drawer data count:" + data.size)
            BaseActivity.log(" Drawer list count:" + lista.size)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("%s - onCreateView", this.javaClass.simpleName)
        val layout = inflater.inflate(R.layout.fragment_drawer_left, container, false)
        recyclerView = layout.findViewById(R.id.drawer_recycler)

        prepareDrawerRecycler()

        mViewModel.menu()
        return layout
    }


    private fun prepareDrawerRecycler() {
        adapter = LeftDrawerRecyclerAdapter(lista, OnClickItemListener {
            closeDrawerMenu()
            when(it.type){
                -4 -> EMailUtils().sentEmail(requireContext(), MyConst.Email.EMAIL, "", MyConst.Email.SUBJECT,"")
                -3 -> (activity as MyActivity).setFragment(AppsFragment.newInstance(it))
                -2 -> (activity as MyActivity).setFragment(AlertsFragment.newInstance(it))
                -1 -> (activity as MyActivity).setFragment(CitiesFragment.newInstance())

                0 -> (activity as MyActivity).setFragment(PostsFragment.newInstance(it))

                10, 11 -> (activity as MyActivity).setFragment(HighlightsFragment.newInstance(it.type))
                else -> (activity as MyActivity).setFragment(CategoriesFragment.newInstance(it.type))
            }

        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    /**
     * Base method for layout preparation. Also set a listener that will respond to events that occurred on the menu.
     *
     * @param drawerLayout   drawer layout, which will be managed.
     * @param toolbar        toolbar bundled with a side menu.
     */
    fun setUp(drawerLayout: DrawerLayout) {
        mDrawerLayout = drawerLayout
        mDrawerToggle = object : ActionBarDrawerToggle(activity, drawerLayout, null, R.string.content_description_open_navigation_drawer, R.string.content_description_close_navigation_drawer) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                requireActivity().invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                requireActivity().invalidateOptionsMenu()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                //                toolbar.setAlpha(1 - slideOffset / 2);
            }
        }

        //toolbar.setOnClickListener { toggleDrawerMenu() }

        mDrawerLayout!!.addDrawerListener(mDrawerToggle!!)
        mDrawerLayout!!.post { mDrawerToggle!!.syncState() }
    }

    fun shakeHomeBtn(){
        val shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        player.startAnimation(shake);
    }

    /**
     * When the drawer menu is open, close it. Otherwise open it.
     */
    fun toggleDrawerMenu() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout!!.isDrawerVisible(GravityCompat.START)) {
                mDrawerLayout!!.closeDrawer(GravityCompat.START, true)
            } else {
                mDrawerLayout!!.openDrawer(GravityCompat.START, true)
                shakeHomeBtn()
            }
        }
    }

    fun isOpened(): Boolean {
        if (mDrawerLayout != null) {
            return mDrawerLayout!!.isDrawerVisible(GravityCompat.START)
        }
        return false
    }
    fun open() {
        if (mDrawerLayout != null && !(mDrawerLayout!!.isDrawerVisible(GravityCompat.START))) {
            toggleDrawerMenu()
        }
    }


    /**
     * When the drawer menu is open, close it.
     */
    fun closeDrawerMenu() {
        if (mDrawerLayout != null) {
            mDrawerLayout!!.closeDrawer(GravityCompat.START, true)
        }
    }

    /**
     * Check if drawer is open. If so close it.
     *
     * @return false if drawer was already closed
     */

    /**
     * PaymentMethod invalidates a drawer menu header. It is used primarily on a login state change.
     */
    fun invalidateHeader() {
        if (adapter != null) {
            Timber.d("Invalidate drawer menu header.")
            adapter.notifyItemChanged(0)
        }
    }


    override fun onDestroy() {
        mDrawerLayout!!.removeDrawerListener(mDrawerToggle!!)
        super.onDestroy()
    }

}
