package app.qarya.utils

import com.onesignal.OneSignal
import app.qarya.model.shared.YDUserManager
import app.qarya.model.models.Broadcast
import app.qarya.presentation.base.MyActivity
import tn.core.util.Utilities

class NotificationsSubscriber {

    companion object {
        fun subscribe(broadcasts:List<Broadcast>){
            if (YDUserManager.auth()!=null){
                val key = "user"+ YDUserManager.auth().id;
                MyActivity.log("Follow Broadcast: " + key)
                OneSignal.sendTag("user", "user"+ YDUserManager.auth().id)
            }
            for (b in broadcasts) {
                MyActivity.log("Follow Broadcast: " + b.tagKey)
                OneSignal.sendTag(b.tagKey, b.tagKey)
            }
        }

        fun unsubscribe(){
            OneSignal.getTags { tags ->
                OneSignal.deleteTags(Utilities.keys(tags))
                MyActivity.log("OneSignal.deleteTags($tags)")
            }
        }
    }

    
}