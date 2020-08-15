package app.qarya.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.core.app.ActivityCompat;

import app.qarya.presentation.base.MyActivity;

public class PermissionsHelper {


    public static void showExplanation(Activity activity, String title, String message, final String permission, final int permissionRequestCode) {
        MyActivity.log("Buiding AlertDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionRequestCode);
                    }
                });
        MyActivity.log("show AlertDialog");
        builder.create().show();
    }

/*


    private fun showExplanation(title: String, message: String, permission: String, permissionRequestCode: Int) {
        MyActivity.log("Buiding AlertDialog")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, id -> ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1) }
        MyActivity.log("show AlertDialog")
        var a:AlertDialog = builder.create();
        a.show()
    }


    private fun showExplanation(title: String, message: String, permission: String, permissionRequestCode: Int) {
        MyActivity.log("Buiding AlertDialog")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, id -> ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1) }
        MyActivity.log("show AlertDialog")
        builder.create().show()
    }
*/
}
