package app.qarya.utils

import android.app.Activity
import android.graphics.Color.parseColor
import android.view.View
import com.wooplr.spotlight.SpotlightView
import app.qarya.presentation.base.MyActivity
import tn.core.presentation.listeners.OnClickItemListener


class Showcase {
    companion object {
        var random:Int = 40;
        fun show(activity:Activity, view:View, title:String, content:String, listener:OnClickItemListener<String>):View{
            random++
            MyActivity.log("$random showcase for: $title")
            return SpotlightView.Builder(activity)
                    .introAnimationDuration(400)
                    .enableRevealAnimation(true)
                    .performClick(true)
                    .fadeinTextDuration(400)
                    .headingTvColor(parseColor("#1cff91"))
                    .headingTvSize(32)
                    .headingTvText(title)
                    .subHeadingTvColor(parseColor("#ffffff"))
                    .subHeadingTvSize(18)
                    .subHeadingTvText(content)
                    .maskColor(parseColor("#501a1a1a"))
                    .target(view)
                    .lineAnimDuration(400)
                    .lineAndArcColor(parseColor("#1cff91"))
                    .dismissOnTouch(true)
                    .dismissOnBackPress(true)
                    .enableDismissAfterShown(true)
                    .usageId(random.toString()) //UNIQUE ID
                    .setListener {
                        listener.onClick(it)
                    }
                    .show()
        }
    }
}