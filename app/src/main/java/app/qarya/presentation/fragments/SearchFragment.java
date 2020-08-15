package app.qarya.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import app.qarya.R;
import app.qarya.model.ModelHolder;
import app.qarya.model.ModelType;
import app.qarya.model.models.Commun;
import app.qarya.model.models.Post;
import app.qarya.model.models.Relation;
import app.qarya.model.models.User;
import app.qarya.model.models.requests.Filter;
import app.qarya.presentation.adapters.MultiAdapter;
import app.qarya.presentation.base.MyActivity;
import app.qarya.presentation.base.MyRecyclerFragment;
import app.qarya.presentation.vms.VMSearch;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tn.core.model.responses.PagingResponse;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.Const;


public class SearchFragment extends MyRecyclerFragment<Post, VMSearch> {

    RecyclerView recyclerView;
    public MultiAdapter adapter;
    private int[] tabIcons = {
            R.drawable.post_outline,
            R.drawable.store,
            R.drawable.account_group_outline,
    };
    private int[] tabTitles = {
            R.string.posts,
            R.string.products,
            R.string.users
    };
    List<Post> products = new ArrayList<>();
    List<User> users = new ArrayList<>();



    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(Integer uid, int position) {
        return newInstance( new Filter(null, 0, uid,0), position );
    }
    public static SearchFragment newInstance(String s) {
        return newInstance( new Filter(s, 0, 0,0), 0 );
    }
    public static SearchFragment newInstance(Filter filter, int tab) {
        Bundle args = new Bundle();
        args.putInt(Const.TAG, tab);
        args.putSerializable(Const.ITEM, filter);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMSearch.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);

        mViewModel.getLiveData().observe(this, this::onDataReceived);
        mViewModel.getProducts().observe(this, this::onProductsReceived);
        mViewModel.getUsers().observe(this, this::onUsersReceived);

        getData();
    }

    @Override
    public void getData() {
        super.getData();
        Serializable s =  getArgs().getSerializable(Const.ITEM);
        if(s!=null){
            Filter filter = (Filter) s;
            if (current == 0)
                mViewModel.searchPosts(filter, 1);
            else if (current == 1)
                mViewModel.searchProducts(filter, 1);
            else if (current == 2)
                mViewModel.searchUsers(filter, 1);
        } else
            Toast.makeText(getContext(), "No Search Filter", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataReceived(List<Post> data) {
        super.onDataReceived(data);
        List<ModelHolder> l = new ArrayList<>();
        for (Post item : lista) {
            l.add(new ModelHolder(item, ModelType.GENERAL));
        }
        // adapter.changeList(l);
        adapter = new MultiAdapter( l , new OnInteractListener<Commun>() {
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
        toggleDataVisibility( l.size()==0 );
    }
    public void onProductsReceived(PagingResponse<Post> data) {
        products.addAll(data.getData());
        List<ModelHolder> l = new ArrayList<>();
        for (Post item : products) {
            l.add(new ModelHolder(item, ModelType.GENERAL));
        }
        // adapter.changeList(l);
        adapter = new MultiAdapter( l , new OnInteractListener<Commun>() {
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
        toggleDataVisibility( l.size()==0 );
    }
    public void onUsersReceived(PagingResponse<User> data) {
        users.addAll(data.getData());
        MyActivity.log("new users: "+ users.size());
        Toast.makeText(getContext(), "new users: "+ users.size(), Toast.LENGTH_SHORT).show();
        List<ModelHolder> l = new ArrayList<>();
        for (User item : users) {
            l.add(new ModelHolder(new Relation(item), ModelType.RELATION));
        }
       // adapter.changeList(l);
        adapter = new MultiAdapter( l , new OnInteractListener<Commun>() {
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
        toggleDataVisibility( l.size()==0 );
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
