package app.qarya.Chat.Private

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.qarya.Chat.SocketResponse
import app.qarya.R
import app.qarya.model.models.Conversation
import app.qarya.model.models.Message
import app.qarya.model.models.Room
import app.qarya.model.models.SFile
import app.qarya.presentation.activities.ChatActivity
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.base.MyRecyclerFragment
import app.qarya.presentation.vms.VMChat
import app.qarya.utils.AlertUtils
import app.qarya.utils.AlertUtils.alert
import app.qarya.utils.AlertUtils.report
import app.qarya.utils.PicassoImageLoader
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import kotlinx.android.synthetic.main.fragment_messages.*
import tn.core.model.responses.PagingResponse
import tn.core.presentation.listeners.EndlessListener
import tn.core.util.Const
import java.io.File
import java.util.*
import androidx.fragment.app.viewModels
import app.qarya.utils.PermissionsHelper

class ChatFragment : MyRecyclerFragment<Message?, VMChat>() {
    var linear: LinearLayoutManager? = null
    private val mTypingHandler = Handler()

    public val vm: VMChat by viewModels()
    
    companion object {
        private const val TYPING_TIMER_LENGTH = 600
        private const val PICK_FILE_REQUEST = 1

        @JvmStatic
        fun newInstance(r:Room): ChatFragment {
            val args = Bundle()
            args.putSerializable(Const.ITEM, r )
            val fragment = ChatFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(cid: Int, uid: Int, name:String): ChatFragment {
            val args = Bundle()
            val c = Conversation()
            c.id = cid
            c.uid = uid
            c.setName(name)
            args.putSerializable(Const.ITEM, c )
            val fragment = ChatFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.callErrors.observe(this, Observer { this.onError(it) })
        vm.loadStatus.observe(this, Observer { this.onStatusChanged(it) })
        vm.getLiveData().observe(this, Observer { this.onPagingDataReceived(it) })
        vm.upload.observe(this, Observer { this.onDataReceived(it) })
        vm.messages.observe(this, Observer { this.onDataReceived(it) })

        val room = args.getSerializable(Const.ITEM) as? Conversation
        //room?.description = ""
        vm.setRoom(room)
        activity().setTitle(room!!.getName())
        if (room!!.isLive())
            setSocketVM(room)

        if (lista.size == 0) {
            getData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.leave()
        vm.unlisten()
        // to release player
        recyclerView.adapter = null
    }

    fun activity(): ChatActivity {
        return activity as ChatActivity
    }

    fun setSocketVM(room:Room) {
        if ( !room.isPrivate && room.description != null   &&   room.description !== "" )
            (activity as ChatActivity?)!!.setInfoMessage(room.description)

        vm.listen()

        //vm.callErrors.observe(this, this::onError);
        //vm.loadStatus.observe(this, this::onStatusChanged);
        vm.upload.observe(this, Observer { this.onDataReceived(it) })
        vm.report.observe(this, Observer { this.handleReport(it) })
        
        vm.onNewMessage.observe(this, Observer { this.onNewMessage(it) })
        vm.onInfo.observe(this, Observer { this.onInfo(it) })
        vm.onJoin.observe(this, Observer { this.onJoin(it) })
        vm.onLeft.observe(this, Observer { this.onLeft(it) })
        vm.onTyping.observe(this, Observer { this.onTyping(it) })
        vm.onStopTyping.observe(this, Observer { this.onStopTyping(it) })
        //vm.onBanned.observe(this, this::onBanned);
        vm.joinPrivate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_messages, container, false)
        initDefaultViews(view)
        adapter = ChatAdapter(lista, listener)
        linear = LinearLayoutManager(context)
        //linear.setReverseLayout(true);
        //linear.setStackFromEnd(false);
        recyclerView.layoutManager = linear
        recyclerView.adapter = adapter
        endlessListener = EndlessListener(0, 0, object : EndlessListener.Action() {
            override fun getOnScroll() {
                getData()
            }
        }, EndlessListener.EndlessDirection.TOP)
        recyclerView.addOnScrollListener(endlessListener)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ( ! vm.room!!.open){
            closed_message.visibility = View.VISIBLE
            layout_chatbox.visibility = View.GONE
            return
        }

        chatboxSendButton.isClickable()
        chatboxSendButton.setOnClickListener(View.OnClickListener {
            val c = chatboxEditText.getText().toString()
            Log.wtf("click click click click ", "sending msg: $c")
            if (!c.isEmpty()) {
                push(c, null)
            }
        })

        // typing ... stop typing ...
        chatboxEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (vm.room!!.isLive()) {
                    vm.typing()
                    mTypingHandler.removeCallbacks(onTypingTimeout)
                    mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH.toLong())
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        val importerView = view.findViewById<View>(R.id.importerView)
        importerView.setOnClickListener { v: View? -> popupList() }
    }

    override fun getData() {
        vm.getMessages(page)
    }

    override fun onError(errors: List<String>) {
        super.onError(errors)
        if (chatboxEditText != null) chatboxEditText!!.isEnabled = true
    }



    fun push(txt: String?, data: SFile?) {
        chatboxEditText!!.isEnabled = false
        val pid = if (data != null) data.id else 0
        if (vm.room!!.isLive()) { // is socket connexion
            addMessage(Message(0, txt, "me", data))
            vm.sendMessage(txt, data, false)
            if (chatboxEditText != null) {
                chatboxEditText!!.setText("")
                chatboxEditText!!.isEnabled = true
            }
        } else {
            vm.pushMessage(pid, txt)
        }
    }

    /*
    @Override
    public void onRefresh(Object o) {
        url = watan.conversations+"/"+conversation.id;
        getData();
    }*/
     fun onPagingDataReceived(data: PagingResponse<Message>) {
        if (endlessListener != null && data!!.total != null) {
            endlessListener.total = data!!.total
            if (endlessListener.total > lista.size) endlessListener.isloading = false
            page = data!!.currentPage + 1
            val ls = data.data
            Collections.reverse(lista)
            lista.addAll(ls) //lista.addAll(0,ls);
            Collections.reverse(lista)
            MyActivity.log("messages count:" + lista.size)
            if (lista.size == 0) {
                empty_view.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                return
            }
            //Collections.reverse(lista);
            adapter = ChatAdapter(lista, listener)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            linear!!.scrollToPosition(lista.size - 1)
        } else {
            empty_view.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            return
        }
    }

    override fun onDataReceived(data: List<Message?>?) {
        super.onDataReceived(data)
        if (isInForegroundMode && recyclerView != null) {
            lista.addAll(data!!)
            MyActivity.log("messages count:" + lista.size)
            empty_view.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            adapter = ChatAdapter(lista, listener)
            recyclerView.adapter = adapter
            linear!!.scrollToPosition(lista.size - 1)
        }
        if (chatboxEditText != null) {
            chatboxEditText!!.setText("")
            chatboxEditText!!.isEnabled = true
        }
    }

    fun handleReport(response: Any?) {}
    fun onDataReceived(data: SFile?) {
        Toast.makeText(context, "File SAVED :D", Toast.LENGTH_SHORT).show()
        push("", data)
    }

    var ftypes = R.array.chat_files
    fun popupList() {
        val b = AlertDialog.Builder(activity)
        b.setItems(ftypes) { dialog, which ->
            Toast.makeText(context, "choice: $which", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            when (which) {
                0 -> {
                    uploadVideo = false
                    showFileChooser()
                }
                1 -> {
                    uploadVideo = true
                    showFileChooser()
                }
                else -> {}
            }
        }
        b.show()
    }

    var path: String? = null
    fun setImageSelector(p: String?) {
        path = p
        vm.upload(if (uploadVideo) 1 else 0, File(path))
        /*String path = Utilities.getPath(uri, getContext());
        bmp = BitmapFactory.decodeFile(ImageHelper.resizeAndCompressImageBeforeSend(getContext(), path, "cache_.jpg"));
        if (bmp==null){
            MyActivity.log("☻ Can't make bitmap from => "+uri);
        }
        setPicture(path);*/
    }

    //permission is automatically granted on sdk<23 upon installation
    val isStoragePermissionGranted: Boolean
        get() = if (Build.VERSION.SDK_INT >= 23) {
            if (requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                MyActivity.log("Permission is granted")
                true
            } else {
                MyActivity.log("Permission is revoked")
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsHelper.showExplanation(activity,"Permission Needed", "Rationale", Manifest.permission.WRITE_EXTERNAL_STORAGE, 1)
                } else {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                }
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            MyActivity.log("Permission is granted")
            true
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            MyActivity.log("Permission: " + permissions[0] + "was " + grantResults[0])
            //resume tasks needing this permission
            showFileChooser()
        }
    }

    var uploadVideo = false
    private fun showFileChooser() {
        if (isStoragePermissionGranted) {
            val picker = ImagePicker.create(this)
                    .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                    .folderMode(false) // folder mode (false by default)
                    .toolbarFolderTitle(requireContext().getString(R.string.app_name)) // folder selection title
                    .toolbarImageTitle("Tap to select") // image selection title
                    .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                    .single() // single mode
                    .showCamera(true) // show camera or not (true by default)
                    .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                    .enableLog(false) // disabling log
                    .imageLoader(PicassoImageLoader()) // custom image loader, must be serializeable
            if (uploadVideo) {
                picker.includeVideo(true)
                picker.onlyVideo(true)
            } else {
                picker.includeVideo(false)
                picker.onlyVideo(false)
            }
            picker.start() // start image picker activity with request code
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            //List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            val image = ImagePicker.getFirstImageOrNull(data)
            if (image != null) setImageSelector(image.path) else Toast.makeText(context, "IMAGE NULL", Toast.LENGTH_SHORT).show()
        }
    }

    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // ----------------------------------- Events -----------------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // for admin
    /*public void onBanned(SocketResponse data){
        int me = YDUserManager.auth().getId();
        MyActivity.log("me ->"+me);
        MyActivity.log("blocked id ->"+data.getUid());
        if (data.getUid().equals(me)){
            AlertUtils.Alert aa = new AlertUtils.Alert() {
                @Override
                public void onAccept(Object o) {
                    isABlock = true;
                    getActivity().onBackPressed();
                }
            };
            aa.setIsInfo(true);
            aa.setMessage(getResources().getString(R.string.message_user_banned, data.getUname()));
            AlertUtils.INSTANCE.alert(getContext(), aa);
        }
        addLog(getResources().getString(R.string.message_user_banned, data.getUname()));
    }*/
    // for clients
    fun onInfo(data: SocketResponse?) {
        if (data == null) return
        addLog(data.message)
        if (data.code == Message.TYPE_ALERT || data.code == Message.TYPE_BLOCK) {
            val aa: AlertUtils.Alert = object : AlertUtils.Alert() {
                override fun onAccept(o: Any) {
                    if (data.code == Message.TYPE_BLOCK) {
                        requireActivity().onBackPressed()
                    }
                }
            }
            aa.setIsInfo(true)
            aa.message = data.message
            alert(requireContext(), aa)
        }
    }

    fun onNewMessage(data: SocketResponse?) {
        if (data == null) return
        MyActivity.log("newwwwwwww msg")
        MyActivity.log(data.toString())
        removeTyping(data.uname)
        addMessage(data)

        /*//if(!isInForegroundMode){
        Intent notificationIntent = new Intent(getContext(), ChatActivity.class);
        notificationIntent.putExtra("room", vm.room?.getName());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
        NotificationsUtils.Companion.notify(getContext(), 999, vm.room?.getName(), data.getMessage(), R.drawable.logo, SoundUtils.Companion.notificationUri(getContext()), intent);
        //}*/
    }



    override fun onResume() {
        super.onResume()
        (activity as ChatActivity?)!!.hideInfo()
        (activity as ChatActivity?)!!.refreshList()
        /*var data: SocketResponse? = mViewModel.onJoin.value
        if(data!=null){
            (activity as ChatActivity?)!!.refreshList(data.users)
        }*/
    }


    fun onJoin(data: SocketResponse?) {
        if (data == null) return
        (activity as ChatActivity?)!!.setList(data.users)
        val txt = resources.getString(R.string.message_user_joined, data.uname)
        addInfo(txt, Message.TYPE_JOIN)
        addParticipantsLog(data.users.size)
    }

    fun onLeft(data: SocketResponse) {
        (activity as ChatActivity?)!!.setList(data.users)
        val txt = resources.getString(R.string.message_user_left, data.uname)
        addInfo(txt, Message.TYPE_LEAVE)
        addParticipantsLog(data.users.size)
        removeTyping(data.uname)
    }

    fun onTyping(data: SocketResponse?) {
        addMessage(data)
        //addTyping(data.getUname());
    }

    fun onStopTyping(data: SocketResponse) {
        removeTyping(data.uname)
    }

    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------- Adapter function -----------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    fun addInfo(message: String?, type: Int) {
        lista.add(
                Message(type, message)
        )
        adapter.notifyItemInserted(lista.size - 1)
        scrollToBottom()
    }

    private fun addLog(message: String) {
        addInfo(message, Message.TYPE_LOG)
    }

    private fun addParticipantsLog(usersCount: Int) {
        addLog(resources.getQuantityString(R.plurals.message_participants, usersCount, usersCount))
    }

    private fun addMessage(message: Message?) {
        lista.add(message)
        //lista.add(new Message.Builder(Message.TYPE_MESSAGE).uid(id).username(username).message(message).build());
        adapter.notifyItemInserted(lista.size - 1)
        scrollToBottom()
    }

    private fun removeTyping(username: String) {
        for (i in lista.indices.reversed()) {
            val message: Message = lista.get(i)!!
            if (message.code == Message.TYPE_ACTION && message.uname == username) {
                lista.removeAt(i)
                adapter.notifyItemRemoved(i)
            }
        }
    }

    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // ----------------------------------- Helpers ----------------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    private fun scrollToBottom() {
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    private val onTypingTimeout = Runnable { vm.stopTyping() }



    internal var actions = R.array.conv_options
    override fun showOptions() {
        //super.showOptions()
        AlertUtils.popupList(requireActivity(), actions, object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                val which = o as Int
                when (which) {
                    0 -> {
                        activity()?.vm?.lockConversation()
                    }
                    1 -> {
                        report()
                    }
                }
            }
        })
    }

    fun report() {
        val action: AlertUtils.Alert = object : AlertUtils.Alert() {
            override fun onAccept(o: Any) {
                activity()?.vm?.report(o.toString())
            }
        }
        action.message = requireContext().getText(R.string.report).toString()
        report(requireContext(), action)
    }
}