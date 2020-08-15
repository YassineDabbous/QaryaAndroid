package app.qarya.presentation.vms

import androidx.lifecycle.MutableLiveData
import app.qarya.Chat.SocketListener
import app.qarya.Chat.SocketResponse
import app.qarya.MyApplication
import app.qarya.domain.usecase.UCGeneral
import app.qarya.domain.usecase.UCMessages
import app.qarya.domain.usecase.UCReport
import app.qarya.domain.usecase.UCUsers
import app.qarya.model.models.*
import app.qarya.model.models.responses.GeneralResponse
import app.qarya.model.shared.YDUserManager
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.base.MyViewModel
import app.qarya.utils.SingleLiveEvent
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import tn.core.model.responses.PagingResponse
import java.io.File

class VMChat : MyViewModel<PagingResponse<Message>>() {
    var users = SingleLiveEvent<PagingResponse<User>>()
    var random = SingleLiveEvent<List<User>>()
    var conversations = MutableLiveData<PagingResponse<Conversation>>()
    var rooms = MutableLiveData<List<Room>>()
    var messages = SingleLiveEvent<List<Message>>()
    //var conversation = SingleLiveEvent<Conversation>()
    var upload = SingleLiveEvent<SFile>()
    var delete = SingleLiveEvent<GeneralResponse>()
    var report = SingleLiveEvent<Any>()
    var rm = SingleLiveEvent<Room>()
    var onConnect = SingleLiveEvent<Boolean>()
    var onDisconnect = SingleLiveEvent<Boolean>()
    @JvmField
    var onInfo = SingleLiveEvent<SocketResponse>()
    @JvmField
    var onTyping = SingleLiveEvent<SocketResponse>()
    @JvmField
    var onStopTyping = SingleLiveEvent<SocketResponse>()
    @JvmField
    var onNewMessage = SingleLiveEvent<SocketResponse>()
    @JvmField
    var onJoin = SingleLiveEvent<SocketResponse>()
    @JvmField
    var onLeft = SingleLiveEvent<SocketResponse>()
    @JvmField
    var onBanned = SingleLiveEvent<SocketResponse>()

    /// http conx

    fun rooms() {
        UCGeneral().rooms(getGenericClosure(rooms))
    }

    fun conversations(page: Int) {
        UCGeneral().conversations(page, getGenericClosure(conversations))
    }

    fun getOnlineUsers(page: Int) {
        UCUsers().online(page, getGenericClosure(users))
    }
    fun getRandomUsers() {
        UCUsers().random(getGenericClosure(random))
    }

    fun getMessages(page: Int) {
        UCMessages().getMessages(room?.id ?: 0, room?.uid ?: 0, page, closure)
    }

    fun pushMessage(fid: Int, msg: String?) {
        //activity()!!.vm.room!!.id, activity()!!.vm.room?.uid ?: 0,
        UCMessages().pushMessage(
                room?.id ?: 0,
                room?.uid ?: 0,
                msg!!,
                fid,
                getGenericClosure(messages)
        )
    }

    fun upload(type: Int, data: File?) {
        UCGeneral().uploadFile(data!!, type, 0, getGenericClosure(upload))
    }

    fun deleteMessage(id: Int) {
        UCMessages().deleteMessage(id, getGenericClosure(delete))
    }

    fun lockConversation() {
        UCMessages().lockConversation(room!!.id, getGenericClosure(delete))
    }

    fun togglePrivacy() {
        UCMessages().togglePrivacy(getGenericClosure(delete))
    }

    fun status(type: Int) {
        UCMessages().status(type, getGenericClosure(delete))
    }

    fun report(description: String?) {
        UCReport().reportConversation(room!!.id, description!!, getGenericClosure(report))
        /*if (model instanceof Room)
            new UCReport().reportComment(model.id, description, getGenericClosure(reportComment));
        else
            new UCReport().reportPost(model.id, description, getGenericClosure(reportPost));*/
    }

    /// socket conx
    class SendTo(uid: Int, username: String) {
        @JvmField
        var uid = 0
        @JvmField
        var username = ""

        init {
            this.uid = uid
            this.username = username
        }
    }

    @JvmField
    var sendTo: SendTo? = null
    @JvmField
    var room: Room? = null
    //var users: List<User>? = null
    //lateinit var MyApplication.get().socket: Socket
    var isConnected = true
    var mTyping = false
    fun setRoom(room: Room?) {
        this.room = room
    }

    var v = "v1_"
    fun init() {
        MyActivity.log("Socket ---> connecting")
        //MyApplication.get().socket = MyApplication.get().socket
        MyApplication.get().socket.on(Socket.EVENT_CONNECT, onConnectListener)
        MyApplication.get().socket.on(Socket.EVENT_DISCONNECT, onDisconnectListener)
        MyApplication.get().socket.on(Socket.EVENT_CONNECT_ERROR, onConnectErrorListener)
        MyApplication.get().socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectErrorListener)
        MyApplication.get().socket.connect()
    }
    fun listen() {
        MyApplication.get().socket.on(v + "new_msg", onNewMessageListener)
        MyApplication.get().socket.on(v + "info", onInfoListener)
        MyApplication.get().socket.on(v + "join", onUserJoinedListener)
        MyApplication.get().socket.on(v + "leave", onUserLeftListener)
        MyApplication.get().socket.on(v + "typing", onTypingListener)
        MyApplication.get().socket.on(v + "stop_typing", onStopTypingListener)
        MyApplication.get().socket.on(v + "ban", onBannedListener)
    }
    fun unlisten(){
        MyApplication.get().socket!!.off(v + "new_msg", onNewMessageListener)
        MyApplication.get().socket!!.off(v + "info", onInfoListener)
        MyApplication.get().socket!!.off(v + "join", onUserJoinedListener)
        MyApplication.get().socket!!.off(v + "leave", onUserLeftListener)
        MyApplication.get().socket!!.off(v + "typing", onTypingListener)
        MyApplication.get().socket!!.off(v + "stop_typing", onStopTypingListener)
        MyApplication.get().socket!!.off(v + "ban", onBannedListener)
    }

    fun destroy() {
        MyActivity.log("Socket ---> disconnecting")
        MyApplication.get().socket!!.off(Socket.EVENT_CONNECT, onConnectListener)
        MyApplication.get().socket!!.off(Socket.EVENT_DISCONNECT, onDisconnectListener)
        MyApplication.get().socket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectErrorListener)
        MyApplication.get().socket!!.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectErrorListener)
        MyApplication.get().socket!!.disconnect()
    }

    fun updateRoom(description: String?) {
        room!!.description = description!!
        UCGeneral().roomsUpdate(room!!.id, room!!, getGenericClosure(rm))
    }

    fun ban(id: Int) {
        val o = JSONObject()
        try {
            o.put("uid", id)
            o.put("room", room!!.key)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Timber.e("MyApplication.get().socket ban$o")
        MyApplication.get().socket!!.emit(v + "ban", o)
    }

    fun join() {
        val o = JSONObject()
        try {
            o.put("room", room!!.key)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        MyActivity.log("MyApplication.get().socket joinRoom$o")
        MyApplication.get().socket!!.emit(v + "join", o)
    }

    fun joinPrivate() {
        val o = JSONObject()
        try {
            o.put("room", room!!.key)
            o.put("cid", room!!.id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        MyActivity.log("MyApplication.get().socket joinRoom$o")
        MyApplication.get().socket!!.emit(v + "join", o)
    }

    fun leave() {
        MyActivity.log("MyApplication.get().socket Leaviiiiiiiiiiiiiiiiiing")
        if(room?.key == null ){
            MyActivity.log("NO ROOM TO LEAVE !!")
            return;
        }
        val o = JSONObject()
        try {
            o.put("room", room!!.key)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        MyActivity.log("MyApplication.get().socket leaveRoom$o")
        MyApplication.get().socket!!.emit(v + "leave", o)
    }

    fun typing() {
        if (!MyApplication.get().socket!!.connected()) return
        MyActivity.log("typing $mTyping")
        if (!mTyping) {
            mTyping = true
            //MyApplication.get().socket.emit("typing");
            val o = JSONObject()
            try {
                o.put("room", room!!.key)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            MyActivity.log("MyApplication.get().socket typing$o")
            MyApplication.get().socket!!.emit(v + "typing", o)
        }
    }

    fun stopTyping() {
        if (!MyApplication.get().socket!!.connected()) return
        if (!mTyping) return
        mTyping = false
        //MyApplication.get().socket.emit("stop typing");
        val o = JSONObject()
        try {
            o.put("room", room!!.key)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        MyActivity.log("MyApplication.get().socket stop typing$o")
        MyApplication.get().socket!!.emit(v + "stop_typing", o)
    }

    fun sendMessage(message: String?, data: SFile?, isPublic: Boolean) {
        MyActivity.log("MyApplication.get().socket.connected() " + MyApplication.get().socket!!.connected())
        if (!MyApplication.get().socket!!.connected()) return
        mTyping = false
        val o = JSONObject()
        try {
            o.put("room", room!!.key)
            o.put("message", message)
            if (!isPublic) {
                o.put("cid", room!!.id)
                o.put("to", 0)
            }
            if (data != null) {
                o.put("fid", data.id)
                o.put("furl", data.url)
                o.put("ftype", data.type)
            }
            if (sendTo != null) {
                o.put("toUid", sendTo!!.uid)
                o.put("toUsername", sendTo!!.username)
            }
        } catch (e: JSONException) {
            MyActivity.log("new_msg JSONException " + e.message)
            e.printStackTrace()
        }
        MyActivity.log("MyApplication.get().socket new_msg$o")
        if (isPublic) MyApplication.get().socket!!.emit(v + "new_msg", o) else MyApplication.get().socket!!.emit(v + "chat", o)
    }

    private val onConnectListener = Emitter.Listener { args: Array<Any?>? ->
        MyActivity.log("onConnectListener")
        onConnect.postValue(true)
        isConnected = true
    }
    private val onDisconnectListener = Emitter.Listener { args: Array<Any?> ->
        MyActivity.log(args.toString())
        onConnect.postValue(false)
        MyActivity.log("diconnected")
        isConnected = false
    }
    private val onConnectErrorListener = Emitter.Listener { args: Array<Any?>? -> onDisconnect.postValue(true) }
    private val onInfoListener: Emitter.Listener = object : SocketListener() {
        override fun onResponse(data: SocketResponse) {
            MyActivity.log("onInfoListener")
            onInfo.postValue(data)
        }
    }
    private val onNewMessageListener: Emitter.Listener = object : SocketListener() {
        override fun onResponse(data: SocketResponse) {
            MyActivity.log("onNewMessageListener")
            onNewMessage.postValue(data)
        }
    }
    private val onBannedListener: Emitter.Listener = object : SocketListener() {
        override fun onResponse(data: SocketResponse) {
            MyActivity.log("onBannedListener")
            val me = YDUserManager.auth().id
            if (data.uid == me) {
                leave()
                MyApplication.get().socket!!.disconnect()
            }
            onBanned.postValue(data)
        }
    }
    private val onUserJoinedListener: Emitter.Listener = object : SocketListener() {
        override fun onResponse(data: SocketResponse) {
            onJoin.postValue(data)
        }
    }
    private val onUserLeftListener: Emitter.Listener = object : SocketListener() {
        override fun onResponse(data: SocketResponse) {
            onLeft.postValue(data)
        }
    }
    private val onTypingListener: Emitter.Listener = object : SocketListener() {
        override fun onResponse(data: SocketResponse) {
            onTyping.postValue(data)
        }
    }
    private val onStopTypingListener: Emitter.Listener = object : SocketListener() {
        override fun onResponse(data: SocketResponse) {
            onStopTyping.postValue(data)
        }
    }
}