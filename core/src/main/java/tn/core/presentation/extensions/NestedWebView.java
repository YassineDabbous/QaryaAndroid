package tn.core.presentation.extensions;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by X on 2/21/2018.
 */

public class NestedWebView extends WebView {

    private OnScrollChangedCallback  mOnScrollChangedCallback;

    public NestedWebView(Context context) {
        super(context);
    }

    public NestedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScrollChange(this, l, t, oldl, oldt);
        }

    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback mOnScrollChangedCallback) {
        this.mOnScrollChangedCallback = mOnScrollChangedCallback;
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public interface OnScrollChangedCallback {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v          The view whose scroll position has changed.
         * @param scrollX    Current horizontal scroll origin.
         * @param scrollY    Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollChange(WebView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
