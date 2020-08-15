package app.qarya.model.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

open class Room : Serializable {


    constructor(id: Int, key: String, name: String) {
        this.id = id
        this.key = key
        this.name = name
    }

    constructor()

    var isPrivate = false

    // -------------------------- FOR CONVERSATION ---------------------------
    @SerializedName("is_online")
    @Expose
    var online: Boolean? = null

    @SerializedName("uid")
    @Expose
    var uid: Int? = null

    @SerializedName("time_ago")
    @Expose
    var timeAgo: String? = null

    fun isLive(): Boolean {
        return id > 0 //   && key == "conv"+cid;
    }
    // -----------------------------------------------------------------------



    @SerializedName("id")
    @Expose
    var id = 0

    @Expose
    @SerializedName("room_key")
    var key: String? = null

    @SerializedName("is_open")
    @Expose
    var open: Boolean = true

    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("description")
    @Expose
    var description = ""

    @SerializedName("icon")
    @Expose
    var icon = ""

    @SerializedName("admins")
    @Expose
    var admins: List<Int> = ArrayList()

    @SerializedName("isAdmin")
    @Expose
    var admin = false


    fun getName(): String {
        return (if (name != null) name!! else "Chat")
    }

    fun setName(name: String?) {
        this.name = name
    }

}