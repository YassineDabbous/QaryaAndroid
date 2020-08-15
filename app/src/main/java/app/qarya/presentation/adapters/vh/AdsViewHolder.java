package app.qarya.presentation.adapters.vh;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


import app.qarya.MyApplication;
import app.qarya.R;

import tn.core.presentation.base.adapters.BaseViewHolder;

public class AdsViewHolder extends BaseViewHolder<Object> {
    AdView adView;
    LinearLayout adContainer;
    //View itemView;
    public AdsViewHolder(View itemView) {
        super(itemView);
        adContainer = itemView.findViewById(R.id.adsContainer);
        //adView = new AdView(MyApplication.get());//new AdView(itemView.getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_ads_banner;
    }

    @Override
    public void bind(Object model) {
        if (adView==null){
            adView = new AdView(MyApplication.get());//new AdView(itemView.getContext());
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(itemView.getContext().getResources().getString(R.string.admob_banner));
            AdRequest adRequest = new AdRequest.Builder().build(); //.addTestDevice("0E49C2FF62648F7BB5C6990735C95689")
            adView.loadAd(adRequest);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            adContainer.addView(adView, params);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    //interstitial.show();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    if (itemView==null) itemView.setVisibility(View.GONE);
                }
            });
            Toast.makeText(MyApplication.get(), "ads", Toast.LENGTH_SHORT).show();
        }
    }
}