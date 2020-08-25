package app.qarya.model.models.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowRequest(
    @field:Expose @field:SerializedName("id") var id: Int,
    @field:Expose @field:SerializedName("type") var type: Int,
    @field:Expose @field:SerializedName("follow") var follow: Boolean
)


data class FollowResponse(
     @field:Expose @field:SerializedName("id") val id: Int,
     @field:Expose @field:SerializedName("follow") val followed: Boolean,
     @field:Expose @field:SerializedName("tagKey") val tagKey:String
)