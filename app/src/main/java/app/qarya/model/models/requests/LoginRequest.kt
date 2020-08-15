package app.qarya.model.models.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FacebookLoginRequest(
        @field:Expose @field:SerializedName("id") var id: String,
        @field:Expose @field:SerializedName("token") var token: String
)

class LoginRequest(@field:Expose @field:SerializedName("email") var email: String, @field:Expose @field:SerializedName("password") var password: String)


class RegisterRequest(
    @field:SerializedName("name")
    @field:Expose
    var name: String? = null,

    @field:SerializedName("email")
    @field:Expose
    var email: String? = null,

    @field:SerializedName("password")
    @field:Expose
    var password: String? = null
)