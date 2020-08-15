package app.qarya.presentation.base;

import android.view.View;

import com.google.android.gms.ads.AdView;

import app.qarya.R;

import tn.core.presentation.base.BaseDialogFragment;
import tn.core.presentation.base.BaseFragment;

public class MyFragment<T> extends BaseFragment<T> {

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MyViewModel) mViewModel).cancel();
    }

    public void setFragment(BaseFragment fragment){
        ((MyActivity) getActivity()).setFragment(fragment);
    }
    public void setFragment(BaseDialogFragment fragment){
        ((MyActivity) getActivity()).setFragment(fragment);
    }

    public void initDefaultViews(View v){
        /*pd = ProgressUtils.getProgressView(getActivity());  //v.findViewById((R.id.loading));
        if (pd!=null){
            pd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPD();
                }
            });
        }*/
        initToolbar(v);
        initAdmob(v);
        initMsgView(v);
    }
    public void showOptions(){}
    public void initToolbar(View v){
        View toolbar_right = v.findViewById(R.id.toolbar_right);
        if (toolbar_right!=null){
            View toolbar_left = v.findViewById(R.id.toolbar_left);
            toolbar_right.setOnClickListener(v1 -> showOptions());
            toolbar_left.setOnClickListener(v12 -> getActivity().onBackPressed());
        }
    }

    public void initMsgView(View v){
        empty_view = v.findViewById(R.id.emptyView);
        View retryBtn = v.findViewById(R.id.retryBtn);
        retryBtn.setOnClickListener((View) -> {
            getData();
        });
    }

    AdView adView;
    public void initAdmob(View v){
        /*adView = v.findViewById(R.id.admob);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }


            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adView.setVisibility(View.GONE);
            }
        });*/
    }
}
