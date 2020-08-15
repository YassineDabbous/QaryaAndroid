package app.qarya.utils

import android.content.Context
import android.content.Intent


import app.qarya.presentation.activities.ChatActivity

import tn.core.util.Const

/**
 * Created by X on 2/25/2018.
 */

object AppHelpers {


    fun private(context: Context, uid:Int, cid: Int = 0 ) {
        val i = Intent(context, ChatActivity::class.java)
        i.putExtra(Const.CATEGORY, 1)
        i.putExtra(Const.UID, uid)
        i.putExtra(Const.ID, cid)
        context?.startActivity(i)
    }
}