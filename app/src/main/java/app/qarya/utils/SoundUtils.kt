package app.qarya.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import app.qarya.R
import app.qarya.presentation.base.MyActivity

class SoundUtils {
    companion object {
        fun notificationUri(app:Context) : Uri {
            val sound = "plucky"
            val soundUrl = "android.resource://" + app.packageName + "/raw/" + sound// + R.raw.sound
            Log.w("soundUrl", soundUrl)
            return Uri.parse(soundUrl)
        }

        fun playChatOpening(ctx:Context){
            val mPlayer = MediaPlayer.create(ctx, R.raw.communication_channel)
            val am = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
            when (am!!.ringerMode) {
                AudioManager.RINGER_MODE_SILENT -> Log.i("MyApp", "Silent mode")
                AudioManager.RINGER_MODE_VIBRATE -> Log.i("MyApp", "Vibrate mode")
                AudioManager.RINGER_MODE_NORMAL -> {
                    MyActivity.log("RINGER MODE NORMAL")
                    mPlayer.start()
                }
            }

        }
    }
}