package app.qarya.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import app.qarya.R;
import app.qarya.model.ModelHolder;
import app.qarya.model.models.Commun;
import app.qarya.presentation.adapters.MultiAdapter;
import app.qarya.presentation.base.MyActivity;
import app.qarya.presentation.base.MyRecyclerFragment;
import app.qarya.presentation.vms.VMSubscription;
import tn.core.model.responses.PagingResponse;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.Const;


public class SubscriptionsFragment extends MyRecyclerFragment<ModelHolder, VMSubscription> {

    RecyclerView recyclerView;
    public MultiAdapter adapter;
    private int[] tabIcons = {
            R.drawable.post_outline,
            R.drawable.account_group_outline,
            R.drawable.store,
    };
    private int[] tabTitles = {
            R.string.categories,
            R.string.users,
            R.string.stores
    };
    List<ModelHolder> stores = new ArrayList<>();
    List<ModelHolder> users = new ArrayList<>();



    public SubscriptionsFragment() {
        // Required empty public constructor
    }
    public static SubscriptionsFragment newInstance(int tab) {
        Bundle args = new Bundle();
        args.putInt(Const.TAG, tab);
        SubscriptionsFragment fragment = new SubscriptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMSubscription.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);

        mViewModel.getCategories().observe(this, this::onDataReceived);
        mViewModel.getStores().observe(this, this::onStoresReceived);
        mViewModel.getUsers().observe(this, this::onUsersReceived);

        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if (current == 0)
            mViewModel.followedCategories(1);
        else if (current == 1)
            mViewModel.followedUsers(1);
        else if (current == 2)
            mViewModel.followedStores(1);
    }

    @Override
    public void onDataReceived(List<ModelHolder> data) {
        super.onDataReceived(data);
        adapter = new MultiAdapter( lista , new OnInteractListener<Commun>() {
            @Override
            public void onInteract(Commun item, @NotNull Action action) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
            @Override
            public void onClick(Commun item) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
        });
        recyclerView.setAdapter(adapter);
        toggleDataVisibility( lista.size()==0 );
    }
    public void onStoresReceived(PagingResponse<ModelHolder> data) {
        stores.addAll(data.getData());
        adapter = new MultiAdapter( stores , new OnInteractListener<Commun>() {
            @Override
            public void onInteract(Commun item, @NotNull Action action) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
            @Override
            public void onClick(Commun item) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
        });
        recyclerView.setAdapter(adapter);
        toggleDataVisibility( stores.size()==0 );
    }
    public void onUsersReceived(PagingResponse<ModelHolder> data) {
        users.addAll(data.getData());
        adapter = new MultiAdapter( users , new OnInteractListener<Commun>() {
            @Override
            public void onInteract(Commun item, @NotNull Action action) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
            @Override
            public void onClick(Commun item) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
        });
        recyclerView.setAdapter(adapter);
        toggleDataVisibility( users.size()==0 );
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        getData();
    }
    @Override
    public void setTabs() {
        super.setTabs();
        super.tabIcons = tabIcons;
        super.tabTitles = tabTitles;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initDefaultViews(view);
        initTabs(view, false); //MODE_FIXED

        tabLayout.getTabAt(getArgs().getInt(Const.TAG, 0)).select();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MultiAdapter(new ArrayList<>(), new OnInteractListener<Commun>() {
            @Override
            public void onInteract(Commun item, @NotNull Action action) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
            @Override
            public void onClick(Commun item) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

}
