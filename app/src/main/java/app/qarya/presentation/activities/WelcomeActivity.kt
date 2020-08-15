package app.qarya.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import app.qarya.R
//import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    val STARTUP_DELAY = 0 //300
    val ANIM_ITEM_DURATION = 1000
    val ITEM_DELAY = 300
    private val animationStarted = false

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val motionLayout = findViewById<MotionLayout>(R.id.motionLayout)
        motionLayout.transitionToEnd()

        val mHandler = Handler()
        mHandler.postDelayed(Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1200);
    }

    /*
    override fun onWindowFocusChanged(hasFocus: Boolean) {

        if (!hasFocus || animationStarted) {
            return
        }

        animate()

        super.onWindowFocusChanged(hasFocus)
    }

    private fun animate() {
        val logoImageView = findViewById<View>(R.id.img_logo) as ImageView

        ViewCompat.animate(logoImageView)
                .translationY(-250f)
                .setStartDelay(STARTUP_DELAY.toLong())
                .setDuration(ANIM_ITEM_DURATION.toLong()).withEndAction {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.setInterpolator(
                        DecelerateInterpolator(1.2f)).start()

        ViewCompat.animate(slogon)
                .translationY(50f).alpha(1f)
                .setStartDelay((ITEM_DELAY * 500).toLong())
                .setDuration(1000)
                .withEndAction {
                    //startActivity(Intent(this, MainActivity::class.java))
                    //finish()
                }.setInterpolator(DecelerateInterpolator()).start()
    }
    */
}