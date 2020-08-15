package app.qarya.presentation.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_home.*
import app.qarya.R
import app.qarya.model.ModelType
import app.qarya.model.models.User
import app.qarya.model.models.responses.ConfigsResponse
import app.qarya.model.models.responses.NewsResponse
import app.qarya.model.shared.YDUserManager
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.fragments.*
import app.qarya.presentation.fragments.posts.NoteFragment
import app.qarya.presentation.fragments.profile.UserProfileFragment
import app.qarya.presentation.ui.fragments.HomeFragment
import app.qarya.presentation.vms.VMMain
import app.qarya.utils.*
import tn.core.presentation.listeners.OnClickItemListener


class MainActivity : MyActivity(), SwipeRefreshLayout.OnRefreshListener {

    var drawerFragmentLeft: DrawerFragmentLeft? = null

    companion object {
        val PROFILE:String = "profile"
        var mInstance: MyActivity?=null
        fun getInstance(): MyActivity {
            return mInstance!!
        }
    }

    override fun onRefresh() {
        currentFragment?.swipeRefresh()
        toggleSwipe()
    }

    fun refreshSoul(){
        mViewModel?.configs()
        if(YDUserManager.check()){
            mViewModel?.news()
            mViewModel?.broadcasts()
        }
        drawerFragmentLeft?.refreshHeader()
    }
    fun resetHeader(){
        drawerFragmentLeft?.refreshHeader()
        drawerFragmentLeft?.toggleDrawerMenu()
    }


    //internal var toolbar: Toolbar? = null
    //var mViewModel:VMMain? = null
    private val mViewModel: VMMain by viewModels()
    fun setViewModel(){
        //mViewModel = ViewModelProvider(this).get(VMMain::class.java)
        mViewModel?.configs?.observe(this, Observer { this.onDataReceived(it) })
        mViewModel?.news?.observe(this, Observer { this.onDataReceived(it) })
        mViewModel?.broadcasts?.observe(this, Observer { NotificationsSubscriber.subscribe(it) })
        refreshSoul()
    }
    fun onDataReceived(data: NewsResponse) {
        data.notifications?.let {
            if (data.notifications != 0){
                notifsCountTV.visibility = View.VISIBLE
                notifsCountTV.setText(data.notifications.toString())
            }
        }
        data.conversations?.let {
            if (data.conversations != 0){
                convsCountTV.visibility = View.VISIBLE
                convsCountTV.setText(data.conversations.toString())
            }
        }
        data.requests?.let {
            if (data.requests != 0){
                reqsCountTV.visibility = View.VISIBLE
                reqsCountTV.setText(data.requests.toString())
            }
        }
    }
    fun onDataReceived(data: ConfigsResponse) {
        log("new configs !!!")
        val old:ConfigsResponse? = YDUserManager.configs()
        log("old", old.toString())
        log("new", data.toString())
        var oldTime = old?.time ?: 0
        //if (old?.time!=null) oldTime=old?.time
        YDUserManager.save(data)
        setBackground()
        if (data.active && (data.time > oldTime || data.strict)) run {
            AlertUtils.popup(this, data.image, data.url, data.strict)
        }
    }

    override fun onBackPressed() {
        if(searchView.visibility == View.VISIBLE)
            searchView.visibility = View.GONE;
        else if(drawerFragmentLeft!!.isOpened())
            drawerFragmentLeft!!.closeDrawerMenu()
        else
            super.onBackPressed()
    }


    fun hideToolbar(){
        tools.visibility = View.GONE
    }
    fun showToolbar(){
        tools.visibility = View.VISIBLE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInstance = this

        MobileAds.initialize(this, resources.getString(R.string.admob_initialize))

        setContentView(R.layout.activity_main)
        setBackground()
        /*setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true)
        } else {
            Timber.e(RuntimeException(), "GetSupportActionBar returned null.")
        }*/
        drawerFragmentLeft = supportFragmentManager.findFragmentById(R.id.main_navigation_drawer_fragment) as DrawerFragmentLeft
        drawerFragmentLeft?.setUp(findViewById(R.id.drawer_layout) as DrawerLayout)


        swiperefresh.setOnRefreshListener(this)

        menuBtn.setOnClickListener {
            drawerFragmentLeft?.toggleDrawerMenu()
        }


        productsBtn.setOnClickListener {
            setFragment(HighlightsFragment.newInstance(ModelType.PRODUCT))
        }
        storesBtn.setOnClickListener {
            setFragment(HighlightsFragment.newInstance(ModelType.STORE))
        }
        usersBtn.setOnClickListener {
            setFragment(HighlightsFragment.newInstance(ModelType.USER))
        }

        /*prayerBtn.setOnClickListener {
            if (prayTimes.visibility == View.GONE)
                prayTimes.visibility = View.VISIBLE
            else
                prayTimes.visibility = View.GONE
        }*/
        //prayTimes.setOnClickListener { it.visibility = View.GONE }
        //prayTimes.setText(PrayTime.getTimes())


        initFragment()

        setSearcher()
        setViewModel()

        //startMusicService()

        //showPrayerCase()
        showCase()
    }


    fun showCase(){
        if (YDUserManager.showcases().main === 0) {
            val sc = YDUserManager.showcases()
            sc.main = 1
            YDUserManager.save(sc)
            showMainCase()
        } else if (YDUserManager.showcases().drawerLeft === 0) {
            val sc = YDUserManager.showcases()
            sc.drawerLeft = 1
            YDUserManager.save(sc)
            drawerFragmentLeft?.showcase()
        }
    }


    fun showMainCase(){
        ImageViewCompat.setImageTintList(homeBtn, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)))
        ImageViewCompat.setImageTintList(reqsBtn, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)))
        ImageViewCompat.setImageTintList(chat, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)))
        //Showcase.show(this, productsBtn, "المحلات", "معلومات عن جميع المحلات من مقاهي و مغازات وصيدليات ...", OnClickItemListener<String> {
        Showcase.show(this!!, homeBtn, "الرئيسية", "هنا تجد اخر الاخبار من جميع التصنيفات!", OnClickItemListener<String> {
            MyActivity.log("showcase for: home clicked")
            Showcase.show(this, reqsBtn, "الصداقات", "جميع معارفك من نافذة واحدة", OnClickItemListener<String> {
                MyActivity.log("prayer showcase tapped")
                Showcase.show(this, chat, "الدردشة", "مكان للنقاشاء حول جديد الأخبار", OnClickItemListener<String> {
                    MyActivity.log("chat showcase tapped")
                    Showcase.show(this, notifs, "التنبيهات", "تصلك هنا كل التعليقات و الإعجابات بما تنشره", OnClickItemListener<String> {
                        MyActivity.log("notifs showcase tapped")
                        /*Showcase.show(this, prayerBtn, "مواقيت الصلاة", "مواقيت الصلاوات الخمس، المواقيت غير مضبوطة حاليا نرجوا ان تأخذ هذا في عين الإعتبار", OnClickItemListener<String> {
                            MyActivity.log("prayer showcase tapped")
                        })*/

                        //drawerFragmentLeft?.showcase()
                    })
                })
            })
        })
        //Showcase.show(this, prayerBtn, "Prayer time", "times are not very exact, please put this in your consideration")
    }

    fun shakeHomeBtn(){
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        menuBtn.startAnimation(shake);
    }
    fun initFragment(){
        handleIntent(intent)
        if (currentFragment==null) {

            if (YDUserManager.showcases().done())
                Handler().postDelayed({
                    shakeHomeBtn()
                    //drawerFragmentLeft?.toggleDrawerMenu()
                }, 1200)
            if(!Themes.isDarkTheme(this))
                setFragment(HomeFragment())
        }
    }



    fun setSearcher(){
        //val searchView = getView(R.id.searchView) as SearchView
        //val searchClose = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn);
        //searchClose.setImageResource(R.drawable.close);
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null || query.trim { it <= ' ' }.isEmpty()) {
                    //resetSearch()
                    return false
                }
                setFragment(SearchFragment.newInstance(query))
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //Do some magic
                return false
            }
        })

        searcher.setOnClickListener {
            searchView.visibility = View.VISIBLE
            searchView.isIconified = false
        }
        searchView.setOnCloseListener {
            searchView.visibility = View.GONE
            false
        }
    }


    fun toggleSwipe(b:Boolean) {
        swiperefresh?.isEnabled = b
    }
    fun toggleSwipe() {
        swiperefresh?.isRefreshing = !swiperefresh?.isRefreshing!!
    }


    fun onHomeBtnClicked(view: View) {
        if(currentFragment is HomeFragment)
            currentFragment.swipeRefresh()
        else
            setFragment(HomeFragment.newInstance())
    }
    fun onNotifClicked(v:View) {
        notifsCountTV.visibility = View.GONE
        if(currentFragment is NotificationsFragment)
            currentFragment.swipeRefresh()
        else
            setFragment(NotificationsFragment.newInstance())
    }
    fun onReqsClicked(view: View) {
        reqsCountTV.visibility = View.GONE
        if(currentFragment is RelationsFragment)
            currentFragment.swipeRefresh()
        else
            setFragment(RelationsFragment.newInstance())
        //setFragment(CategoriesFragment.newInstance(CategoriesFragment.GoToProducts))
    }
    fun onChatClicked(v:View) {
        if(YDUserManager.check()){
            convsCountTV.visibility = View.GONE
            startActivity(Intent(this, ChatActivity::class.java))
        } else
            showLogin()
    }
    fun onSearchClicked(v:View) {
        searchView.visibility = View.VISIBLE;
    }



    // -------------------------------------------------------------------------------------------
    // -------------------------------------- Handle Intent --------------------------------------
    // -------------------------------------------------------------------------------------------


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    fun handleIntent(receiverdIntent:Intent){
        //val receiverdIntent = intent
        val receivedAction = receiverdIntent.action
        val receivedType = receiverdIntent.type
        if (receivedAction == Intent.ACTION_MAIN) {
            handleMainIntent(receiverdIntent)
        } else if (receivedAction == Intent.ACTION_SEND) {
            onSharedIntent(receiverdIntent, receivedType!!)
        } else if (receivedAction == Intent.ACTION_VIEW) {
            val data: Uri? = intent?.data
            //val spPath= data?.getPath()?.split("/");
            //val lastPathSegment = spPath?.get(spPath?.size-1);
            val split = data?.lastPathSegment?.split("-")
            val idStr = split!!.get(0)
            val idInt = Integer.parseInt(idStr!!)
            //Toast.makeText(this, idStr, Toast.LENGTH_LONG)
            //log("open post by url, ID is: ", idStr)
            setFragment(NoteFragment.newInstance(idInt))
        }
    }

    fun handleMainIntent(intent:Intent){ // for navigation or push notifs
        //val roomId = intent.getStringExtra(Const.ID)
        //setFragment(RoomMessagesFragment.newInstance(roomId))
        val action = intent.getStringExtra(ACTION)
        if (action != null && action== PROFILE) {
                val item = intent.getSerializableExtra(DATA) as User
                setFragment(UserProfileFragment.newInstance(item.getId()))
        }
        else {
            val push = intent.getIntExtra("push", -1)
            if (push >= 0) {
                when (push) {
                    //3 -> setFirstFragment(RelationsFragment.newInstance())
                    99 -> setFirstFragment(AlertsFragment())
                    else -> setFirstFragment(NotificationsFragment())
                }
            }
        }
    }


    internal fun onSharedIntent(receiverdIntent:Intent, receivedType:String ) {
            // check mime type
            if (receivedType!!.startsWith("text/")) {
                val receivedText = receiverdIntent.getStringExtra(Intent.EXTRA_TEXT)
                if (receivedText != null) {
                    //do your stuff
                    openSharer(receivedText)
                }
            } else if (receivedType.startsWith("image/")) {

                val receiveUri = receiverdIntent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as Uri?
                if (receiveUri != null) {
                    //fileUri = receiveUri;// save to your own Uri object
                    log(receiveUri.toString())
                }
            }
    }





    ////////////////////////////////////////////////



    fun setBottomSheet(){
        val sheetBehavior : BottomSheetBehavior<View>
        val bottom_sheet: LinearLayout
        bottom_sheet = findViewById(R.id.bottom_sheet)
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
// click event for show-dismiss bottom sheet
// click event for show-dismiss bottom sheet
        searcher.setOnClickListener(View.OnClickListener {
            if (sheetBehavior.getState() !== BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                //btn_bottom_sheet.setText("Close sheet")
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                //btn_bottom_sheet.setText("Expand sheet")
            }
        })
// callback for do something
// callback for do something
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //btn_bottom_sheet.setText("Close Sheet")
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        //btn_bottom_sheet.setText("Expand Sheet")
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //
            }
        })
    }






}