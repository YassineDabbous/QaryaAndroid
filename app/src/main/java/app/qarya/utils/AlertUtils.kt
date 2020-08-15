package app.qarya.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView


import android.widget.TextView

import app.qarya.MyApplication
import app.qarya.R
import app.qarya.presentation.base.MyActivity

import tn.core.model.net.net.NetworkUtils

/**
 * Created by X on 2/25/2018.
 */

object AlertUtils {

    open abstract class Alert {
        open var isInfo: Boolean = false
        var title = ""
        var message = ""
        var positive = ""
        var negative = ""
        fun setIsInfo(b: Boolean){isInfo = b}
        abstract fun onAccept(o: Any)
        fun onCancel(o: Any?) {}
    }


    fun alert(context: Context, action: Alert) {
        val alert: AlertDialog.Builder
        //action.title = (action.title.isEmpty())? "" : action.title;
        action.message = if (action.message.isEmpty()) context.getString(R.string.are_you_sure) else action.message
        action.positive = if (action.positive.isEmpty()) context.getString(android.R.string.yes) else action.positive
        action.negative = if (action.negative.isEmpty()) context.getString(android.R.string.no) else action.negative
        alert = AlertDialog.Builder(context)
        alert.setTitle(action.title)
        alert.setMessage(action.message)
        alert.setPositiveButton(action.positive) { dialog, id ->
            action.onAccept("")
            dialog.dismiss()
        }
        if (!(action.isInfo)!!)
            alert.setNegativeButton(action.negative) { dialog, id ->
                action.onCancel(null)
                dialog.dismiss()
            }
        alert.show()
    }

    @JvmOverloads fun report(context: Context, action: Alert, txt:String="", hint:String?=null) {
        val alert: AlertDialog.Builder
        //action.title = (action.title.isEmpty())? "" : action.title;
        action.message = if (action.message.isEmpty()) context.getString(R.string.are_you_sure) else action.message
        action.positive = if (action.positive.isEmpty()) context.getString(android.R.string.yes) else action.positive
        action.negative = if (action.negative.isEmpty()) context.getString(android.R.string.no) else action.negative
        alert = AlertDialog.Builder(context)
        alert.setTitle(action.title)
        alert.setMessage(action.message)
        val text = TextView(context)
        text.text = action.title
        alert.setCustomTitle(text)
        val input = EditText(context)
        input.hint = hint ?: context.getText(R.string.report_cause)
        input.setText(txt)
        alert.setView(input)
        alert.setPositiveButton(action.positive) { dialog, id ->
            action.onAccept(input.text.toString())
            dialog.dismiss()
        }
        alert.setNegativeButton(action.negative) { dialog, id -> dialog.dismiss() }
        alert.show()
    }


    fun popup(activity: Activity, image: String, url: String, strict: Boolean) {
        MyActivity.log("popup: $url")
        val alertadd: AlertDialog.Builder
        var popupImage: String?
        var popupUrl: String?
        popupImage = image
        popupUrl = url
        if (popupImage != null) {
            alertadd = AlertDialog.Builder(activity)
            alertadd.setCancelable(!strict)
            val factory = LayoutInflater.from(MyApplication.get())
            val view = factory.inflate(R.layout.popup, null)
            val banner = view.findViewById<ImageView>(R.id.imageview)
            ImageHelper.load(banner, popupImage)
            banner.setOnClickListener { NetworkUtils.openWebPage(MyApplication.get(), popupUrl) }
            alertadd.setView(view)
            alertadd.show()
            popupImage = null
            popupUrl = null
        }
    }


    fun popupList(activity: Activity, listID:Int, action: Alert ) {
        val b = AlertDialog.Builder(activity)
        b.setItems(listID, DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
            action.onAccept(which)
        })
        b.show()
    }
}