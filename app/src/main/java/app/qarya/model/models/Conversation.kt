package app.qarya.model.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by YaSsIne on 24/09/2016.
 */
class Conversation : Room(), Serializable {
    init {
        isPrivate = true
    }

}