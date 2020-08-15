package app.qarya.model.models.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateEmailRequest(@field:SerializedName("email")
                         @field:Expose
                         var email: String?,
                         @field:SerializedName("password")
                         @field:Expose
                         var password: String?)


class UpdatePasswordRequest(@field:SerializedName("password_current")
                            @field:Expose
                            var password_current: String,
                            @field:SerializedName("password")
                            @field:Expose
                            var password: String,
                            @field:SerializedName("password_confirmation")
                            @field:Expose
                            var password_confirmation: String)

data class UpdateProfileRequest(
                           @field:SerializedName("category_id")
                           @field:Expose
                           var category_id: Int,
                           @field:SerializedName("city_id")
                            @field:Expose
                            var city_id: Int,
                           @field:SerializedName("name")
                           @field:Expose
                           var name: String,
                           @field:SerializedName("phone")
                           @field:Expose
                           var phone: String,
                            @field:SerializedName("summary")
                            @field:Expose
                            var summary: String,
                            @field:SerializedName("address")
                            @field:Expose
                            var address: String)