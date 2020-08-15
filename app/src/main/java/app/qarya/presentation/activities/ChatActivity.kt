package app.qarya.presentation.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chat.*
import app.qarya.Chat.Private.ChatFragment
import app.qarya.Chat.RoomFragment
import app.qarya.MyApplication
import app.qarya.R
import app.qarya.model.models.Song
import app.qarya.model.models.User
import app.qarya.presentation.adapters.vh.AdsViewHolder
import app.qarya.presentation.adapters.vh.RoomUserVH
import app.qarya.presentation.adapters.vh.SongVH
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.fragments.RoomsFragment
import app.qarya.presentation.services.MediaPlayerService
import app.qarya.presentation.vms.VMChat
import app.qarya.utils.AlertUtils
import tn.core.model.responses.PagingResponse
import tn.core.presentation.base.adapters.BaseAdapter
import tn.core.presentation.listeners.OnClickItemListener
import tn.core.util.Const

class ChatActivity : MyActivity() {

    //lateinit public var vm: VMChat
    public val vm: VMChat by viewModels()


    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerLeft: RecyclerView? = null
    private var mDrawerRight: RecyclerView? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var frame: FrameLayout? = null
    private var lastTranslate = 0.0f

    fun setTitle(t:String){
        titleView.setText(t)
    }

    fun setInfoMessage(txt:String){
        infoTxt.setText(txt)
        infoTxt.visibility = View.VISIBLE
        val shake = AnimationUtils.loadAnimation(applicationContext, R.anim.shake);
        infoTxt.startAnimation(shake);
    }

    fun privateRoom(cid: Int, uid: Int, name:String=""){
        //AppHelpers.private(this@ChatActivity, currentUser!!.getId())
        setFirstFragment(ChatFragment.newInstance(cid, uid, name))
    }

    fun handlePush(){
        if (intent != null) {
            val type = intent.getIntExtra(Const.CATEGORY, -1)
            //log("type iss ${type}")
            if(type == 1){
                val cid = intent.getIntExtra(Const.ID, 0)
                val uid = intent.getIntExtra(Const.UID, 0)
                privateRoom(cid, uid)
            }else{
                val push = intent.getIntExtra("push", -1)
                if (push >= 0) {
                    setFirstFragment(RoomsFragment.newInstance(1))
                }else{
                    setFirstFragment(RoomsFragment.newInstance(0))
                }
            }
        }
    }


    fun hideInfo(){
        infoTxt.visibility = View.GONE
    }
    fun toggleInfo(){
        if (infoTxt.visibility == View.GONE)
            infoTxt.visibility = View.VISIBLE
        else
            infoTxt.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setBackground()
        initVM()
        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        mDrawerLeft = findViewById(R.id.left_drawer) as RecyclerView
        mDrawerRight = findViewById(R.id.right_drawer) as RecyclerView
        frame = findViewById(R.id.content_main) as FrameLayout

        titleView.setOnClickListener {
            toggleInfo()
        }
        infoTxt.setOnClickListener { hideInfo() }

        mDrawerLeft!!.tag = "1";
        mDrawerRight!!.tag = "2";

        mDrawerToggle = object : ActionBarDrawerToggle(this, mDrawerLayout, R.string.content_description_open_navigation_drawer, R.string.content_description_close_navigation_drawer) {
            //@SuppressLint("NewApi")
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                var moveFactor = mDrawerLeft!!.width * slideOffset

                //log("sliding: $slideOffset")
                if(drawerView.tag.toString().equals("2")) moveFactor *= -1
                //log("moveFactor: $moveFactor")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    frame!!.translationX = moveFactor
                } else {
                    val anim = TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f)
                    anim.duration = 0
                    anim.fillAfter = true
                    frame!!.startAnimation(anim)

                    lastTranslate = moveFactor
                }
            }

        }

        mDrawerLayout!!.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout!!.addDrawerListener(mDrawerToggle as ActionBarDrawerToggle)


        handlePush()
        setRadioList()

        privateBtn.setOnClickListener{
            //setFragment(ConversationFragment())
            vm.getRandomUsers()
        }
        members.setOnClickListener{
            mDrawerLayout!!.openDrawer(GravityCompat.START)
        }
        player.setOnClickListener{
            if (MediaPlayerService.isPlaying()) {
                MediaPlayerService.play(this, -1)
                player.setImageResource(R.drawable.exo_controls_play)
            }
            else
                mDrawerLayout!!.openDrawer(GravityCompat.END)
        }
        settings.setOnClickListener{ currentFragment.showOptions(); }
    }


    fun closeLeft(){
        mDrawerLayout!!.closeDrawer(GravityCompat.START, true)
    }
    fun closeRight(){
        mDrawerLayout!!.closeDrawer(GravityCompat.END, true)
    }
    override fun onBackPressed() {
        if(mDrawerLayout!!.isDrawerVisible(GravityCompat.START))
            mDrawerLayout!!.closeDrawer(GravityCompat.START, true)
        else if(mDrawerLayout!!.isDrawerVisible(GravityCompat.END))
            mDrawerLayout!!.closeDrawer(GravityCompat.END, true)
        else
            super.onBackPressed()
    }

    fun setList(users:List<User>){
        log("room clients count "+users.size);
        //runOnUiThread {
        if(vm.room != null) {
            for (user:User in users)
                if (user.id in vm.room!!.admins)
                    user.isAdmin = true
        }
        mDrawerLeft!!.setLayoutManager(LinearLayoutManager(applicationContext))
        mDrawerLeft!!.adapter = BaseAdapter<User, RoomUserVH, AdsViewHolder>(users, RoomUserVH::class.java, AdsViewHolder::class.java, OnClickItemListener {
            currentUser = it
            popupList()
        })
        //}
    }
    var adapter : BaseAdapter<Song, SongVH, AdsViewHolder>? = null
    fun setRadioList(){
        log("room clients count "+MediaPlayerService.playlist.size);
        //runOnUiThread {
        mDrawerRight!!.setLayoutManager(LinearLayoutManager(applicationContext))
        adapter = BaseAdapter<Song, SongVH, AdsViewHolder>(MediaPlayerService.playlist, SongVH::class.java, AdsViewHolder::class.java, OnClickItemListener {
            Toast.makeText(applicationContext, "play ${it.title}", Toast.LENGTH_SHORT).show()
            val i = MediaPlayerService.playlist.indexOf(it)
            MediaPlayerService.playlist.forEach{
                it.isPlaying = false
            }
            it.isPlaying = true
            MediaPlayerService.playlist.set(i, it)
            MediaPlayerService.play(this, i)
            player.setImageResource(R.drawable.exo_controls_pause)
            adapter!!.items = MediaPlayerService.playlist;
            adapter!!.notifyDataSetChanged()
        }, 0)
        mDrawerRight!!.adapter = adapter;
        //}
    }


    var currentUser:User? = null
    internal var actions = R.array.room_members_options
    internal var actionsAdmins = R.array.room_members_options_admin
    internal fun popupList() {
        if(currentFragment is RoomFragment && vm.room != null && vm.room!!.admin){
            actions = actionsAdmins
        }
        AlertUtils.popupList(this, actions, object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                val which = o as Int
                when (which) {
                    0 -> {
                        val i = Intent(this@ChatActivity, MainActivity::class.java)
                        i.putExtra(ACTION, MainActivity.PROFILE)
                        i.putExtra(DATA, User(currentUser))
                        startActivity(i)
                    }
                    1 -> {
                        if(currentFragment is RoomFragment && currentUser!=null){
                            (currentFragment as RoomFragment).tagInRoom(currentUser!!.getId(), currentUser!!.getName());
                            closeLeft()
                        } else {
                            Toast.makeText(applicationContext, "choose room first", Toast.LENGTH_SHORT).show()
                        }
                    }
                    2 -> {
                        privateRoom(0, currentUser!!.getId(), currentUser!!.getName())
                        closeLeft()
                    }
                    3 -> {
                        if(currentFragment is RoomFragment && currentUser!=null){
                            (currentFragment as RoomFragment).ban(currentUser!!.getId());
                            closeLeft()
                        } else {
                            Toast.makeText(applicationContext, "choose room first", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } )
    }

    fun refreshList() {
        // TODO KotlinNullPointerException error
        vm.users.value?.data.takeIf {
            it != null
        }.apply {
            setList(this!!)
        }
    }
    fun onDataReceived(data: PagingResponse<User>) {
        setList(data.data)
    }
    fun onDataReceived(data: List<User>) {
        if (data.size > 0){
            val u = data.get(0)
            privateRoom(0, u.id, u.name)
        } else
            MyApplication.get().toast("No one for random chat")
            //Toast.makeText(this, "No one for random chat", Toast.LENGTH_LONG).show()
    }

    // VM & Socket

    fun initVM() {
        //vm = new ViewModelProvider(this).get(VMChat::class.java)
        vm.onConnect?.observe(this, Observer{ b: Boolean -> this.onConnect(b) })
        vm.users?.observe(this, Observer{ d -> this.onDataReceived(d) })
        vm.random?.observe(this, Observer{ d -> this.onDataReceived(d) })
        vm.init()
        vm.getOnlineUsers(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.destroy()
    }

    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // ----------------------------------- Events -----------------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    fun onConnect(b: Boolean) {
        if (b) Toast.makeText(applicationContext, R.string.connect, Toast.LENGTH_LONG).show() else Toast.makeText(applicationContext, R.string.disconnect, Toast.LENGTH_LONG).show()
    }


    fun showStatusOptions() {
        //super.showOptions()
        AlertUtils.popupList(this!!, R.array.user_status, object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                val which = o as Int
                vm.status(which)
            }
        })
    }

}
