package app.qarya.presentation.services

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.IBinder
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import app.qarya.R
import app.qarya.model.models.Song
import app.qarya.presentation.base.MyActivity
import tn.core.util.Completion

class MediaPlayerService : Service() {

    companion object {
        var initialized = false
        var ACTION_CURRENT_STATE = false
        val ACTION_PLAY = "play"
        val ACTION_STOP = "stop"
        val ACTION_SEEK = "SEEK"

        fun isPlaying():Boolean = initialized && ACTION_CURRENT_STATE

        fun play(context: Context, i:Int) {
            val intent = Intent(context, MediaPlayerService::class.java )
            if(initialized && ACTION_CURRENT_STATE){
                if (i < 0)
                    intent.setAction( ACTION_STOP);
                else{
                    intent.putExtra("to", i)
                    intent.setAction( ACTION_SEEK);
                }
            }
            else
                intent.setAction( ACTION_PLAY );
            context.startService( intent );
        }

        fun toggleMusicService(context: Context, completion: Completion<Boolean>) {
            val intent = Intent( context, MediaPlayerService::class.java )
            if(initialized && ACTION_CURRENT_STATE)
                intent.setAction( ACTION_STOP);
            else
                intent.setAction( ACTION_PLAY );
            context.startService( intent );
            completion.finish(ACTION_CURRENT_STATE)
        }

        var playlist: ArrayList<Song> = object : ArrayList<Song>() {
            init {
                add(Song("http://rtstream.tanitweb.com/tataouine", "Radio Tataouine", "Radio Tataouine - إذاعة تطاوين", "https://cdn.onlineradiobox.com/img/logo/3/44253.v2.png"))
                add(Song("http://rtstream.tanitweb.com/sfax", "Sfax", "", "https://cdn.onlineradiobox.com/img/logo/0/44180.v2.png"))
                add(Song("http://streaming.knoozfm.net:8000/knoozfm", "Knooz Fm", "", "https://cdn.onlineradiobox.com/img/logo/0/27650.v2.png"))
//            add(Song("http://streaming.radionomy.com/radiotunisiamed", "Radio Tunisia Med", "", ""))
                add(Song("http://stream6.tanitweb.com/shems", "Shems FM", "", "https://cdn.onlineradiobox.com/img/logo/6/27236.v2.png"))
                add(Song("http://streaming2.toutech.net:8000/jawharafm", "Jawhara fm", "", "https://cdn.onlineradiobox.com/img/logo/1/26871.v7.png"))
                add(Song("http://stream8.tanitweb.com/diwanfm", "Diwan fm", "", "https://cdn.onlineradiobox.com/img/logo/3/27723.v1.png"))
                add(Song("http://expressfm.ice.infomaniak.ch/expressfm-64.mp3", "Express FM", "", "https://cdn.onlineradiobox.com/img/logo/0/27280.v4.png"))
                add(Song("http://rtstream.tanitweb.com/jeunes", "Radio Jeunes", "Radio Jeunes Tunisie - إذاعة الشّباب", "https://cdn.onlineradiobox.com/img/logo/0/44260.v2.png"))
                add(Song("http://streaming.radionomy.com/JamendoLounge?lang=en-US%2cen%3bq%3d0.9%2cfr%3bq%3d0.8", "Radio Lamma إذاعة اللمة", "", "https://cdn.onlineradiobox.com/img/logo/2/62802.v3.png"))
                add(Song("http://stream6.tanitweb.com/radiomed", "Radio Med Tunisie", "", "https://cdn.onlineradiobox.com/img/logo/5/27665.v1.png"))
                add(Song("http://stream6.tanitweb.com/sabrafm", "Sabra FM", "", "https://cdn.onlineradiobox.com/img/logo/6/27226.v1.png"))
                add(Song("http://rtstream.tanitweb.com/rtci", "Radio Tunis Internationale", "Radio Tunis Chaîne Internationale - إذاعة تونس الدولية", "https://cdn.onlineradiobox.com/img/logo/1/44261.v2.png"))
                add(Song("http://rtstream.tanitweb.com/nationale", "Radio Nationale Tunisienne", "", "https://cdn.onlineradiobox.com/img/logo/3/73743.v4.png"))
                add(Song("http://rtstream.tanitweb.com/monastir", "Radio Monastir", "Radio Monastir - إذاعة المنستير", "https://cdn.onlineradiobox.com/img/logo/8/44258.v2.png"))
                add(Song("http://rtstream.tanitweb.com/gafsa", "Radio Gafsa FM", "Radio Gafsa FM - الصفحة الرسمية لإذاعة قفصة", "https://cdn.onlineradiobox.com/img/logo/5/44255.v1.png"))
                add(Song("http://rtstream.tanitweb.com/kef", "Radio kef fm", "", "https://cdn.onlineradiobox.com/img/logo/6/44256.v7.png"))
                /*forEach {
                    MyActivity.logPlayer("INSERT INTO `radios`(`id`, `name`, `description`, `icon`, `url`) VALUES (null,'${it.title}','${it.description}','${it.image}','${it.url}');")
                }*/
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        //objService = this;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getAction() == ACTION_PLAY) {
            ACTION_CURRENT_STATE = true
            if (!initialized)
                initializePlayer()
            play()
        } else if (intent?.getAction() == ACTION_STOP) {
            ACTION_CURRENT_STATE = false
            if(::exoPlayer.isInitialized){
                exoPlayer.release()
                exoPlayer.stop()
            }
            stopForeground(true)
            stopSelf()
        } else if(intent?.getAction() == ACTION_SEEK) {
            val i = intent.getIntExtra("to", 0)
            MyActivity.logPlayer("go to $i")
            exoPlayer.seekTo(i, C.TIME_UNSET);
            exoPlayer.setPlayWhenReady(true);
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager


    private fun initializePlayer() {
        initialized = true
        val trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(applicationContext)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(applicationContext, renderersFactory, trackSelector, loadControl)

        playerNotificationManager = PlayerNotificationManager(
                    applicationContext,
                    "Radio",
                    0,
                object : PlayerNotificationManager.MediaDescriptionAdapter {
                    override fun createCurrentContentIntent(player: Player?): PendingIntent? {
                        return null
                    }

                    override fun getCurrentContentText(player: Player?): String? {
                        val window = player?.getCurrentWindowIndex()
                        return playlist.get(window!!).description
                    }

                    override fun getCurrentContentTitle(player: Player?): String {
                        val window = player?.getCurrentWindowIndex()
                        return playlist.get(window!!).title
                    }

                    override fun getCurrentLargeIcon(player: Player?, callback: PlayerNotificationManager.BitmapCallback?): Bitmap? {
                        val window = player?.getCurrentWindowIndex()
                        return playlist.get(window!!).bitmap
                        //return BitmapFactory.decodeResource(getResources(), R.drawable.logo)
                    }

                }
        );
        playerNotificationManager.setUseNavigationActions(true);
        playerNotificationManager.setPlayer(exoPlayer);
        // omit skip previous and next actions
// omit fast forward action by setting the increment to zero
//        playerNotificationManager.setFastForwardIncrementMs(0);
// omit rewind action by setting the increment to zero
//        playerNotificationManager.setRewindIncrementMs(0);
// omit the stop action
        //playerNotificationManager.setStopAction(null);


    }




    fun play() {
        //1
        val userAgent = Util.getUserAgent(applicationContext, applicationContext.getString(R.string.app_name))
        //2

        val concatenatedSource = ConcatenatingMediaSource();
       // var list:MutableList<MediaSource> = ArrayList()
        for(song:Song in playlist){
            val mediaSource = ExtractorMediaSource
                    .Factory(DefaultDataSourceFactory(applicationContext, userAgent))
                    .setExtractorsFactory(DefaultExtractorsFactory())
                    .createMediaSource(Uri.parse(song.url))
            concatenatedSource.addMediaSource(mediaSource)
        }


        //3
        exoPlayer.prepare(concatenatedSource)
        //4
        exoPlayer.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        ACTION_CURRENT_STATE = true
        initialized = false
    }
}
