package app.qarya.presentation.base;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import app.qarya.R;
import app.qarya.presentation.adapters.PostsAdapter;


import tn.core.presentation.base.BaseDialogFragment;
import tn.core.presentation.base.BaseFragment;
import tn.core.presentation.base.BaseRecyclerFragment;
import tn.core.presentation.listeners.EndlessListener;
import tn.core.util.Const;

public class MyRecyclerFragment<A,B> extends BaseRecyclerFragment<A,B> {

    public int current = 0; // 0 convs 1 rooms
    public TabLayout tabLayout;
    public int[] tabIcons = {R.drawable.post_outline, R.drawable.store};
    public int[] tabTitles = { R.string.posts, R.string.products };
    public void onTabSelected(TabLayout.Tab tab) {
    }
    public void setTabs() {
    }
    public void initTabs(View view, boolean scrollable) {
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setVisibility(View.VISIBLE);

        if (scrollable)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        else
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        setTabs();
        
        for (int i = 0; i < tabTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]).setIcon(tabIcons[i]));
        }
        current = getArgs().getInt(Const.CATEGORY, 0);
        tabLayout.getTabAt(current).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                current = tabLayout.getSelectedTabPosition();
                MyRecyclerFragment.this.onTabSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}

        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewModel!=null) // if it's used
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
        recyclerView = v.findViewById(R.id.recycler_view);
        if(recyclerView==null){
            Toast.makeText(getContext(), "recyclerView==null", Toast.LENGTH_SHORT).show();
        }
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
    public void initDefaultViewsWithListener(View v){
        initDefaultViews(v);
        endlessListener = new EndlessListener(0, PostsAdapter.ADS_AFTER, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                MyActivity.log("get in scroll");
                getData();
            }
        });
        recyclerView.addOnScrollListener(endlessListener);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ((MyActivity) getActivity()).toggleSwipe(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0); // 0 is for first item position
            }
        });
    }

    public void initMsgView(View v){
        empty_view = v.findViewById(R.id.emptyView);
        View retryBtn = v.findViewById(R.id.retryBtn);
        if(retryBtn!=null) retryBtn.setOnClickListener((View) -> {
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
        });
        */
    }
}
