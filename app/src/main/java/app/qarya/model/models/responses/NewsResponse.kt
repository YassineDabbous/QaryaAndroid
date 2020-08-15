package app.qarya.model.models.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NewsResponse {
    @SerializedName("notifications")
    @Expose
    var notifications: Int? = null
    @SerializedName("conversations")
    @Expose
    var conversations: Int? = null
    @SerializedName("requests")
    @Expose
    var requests: Int? = null
}