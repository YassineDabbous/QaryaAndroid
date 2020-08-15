package app.qarya.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import app.qarya.R;


/**
 * Created by X on 8/6/2018.
 */

public class ProgressUtils {
    public static ProgressDialog getProgressDialog(Context context){
        ProgressDialog pd = new ProgressDialog(context);
        //pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small); //android.R.style.Widget_ProgressBar_Small
        pd.setMessage(context.getString(R.string.please_wait));
        pd.setCancelable(true);
        pd.setIndeterminate(true);
        //pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return pd;
    }

    public static ProgressBar getProgressBar(Activity activity){
        ProgressBar pd = new ProgressBar(activity, null, android.R.attr.progressBarStyleSmall);
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        //params.addRule(RelativeLayout.CENTER_IN_PARENT);
        //layout.addView(pd, params);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams( ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomToBottom = ConstraintSet.PARENT_ID;
        layoutParams.endToEnd = ConstraintSet.PARENT_ID;
        layoutParams.startToStart = ConstraintSet.PARENT_ID;
        layoutParams.topToTop = ConstraintSet.PARENT_ID;
        activity.addContentView(pd, layoutParams);
        return pd;
    }

    public static View getProgressView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View pd = inflater.inflate(R.layout.loading, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        activity.addContentView(pd, params);

        pd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return pd;
    }

}