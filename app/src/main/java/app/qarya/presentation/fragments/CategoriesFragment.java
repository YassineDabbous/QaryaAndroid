package app.qarya.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.qarya.R;
import app.qarya.model.ModelType;
import app.qarya.model.models.Category;
import app.qarya.model.models.responses.FollowResponse;
import app.qarya.presentation.adapters.CategoryAdapter;
import app.qarya.presentation.base.MyActivity;
import app.qarya.presentation.base.MyRecyclerFragment;
import app.qarya.presentation.vms.VMGeneral;
import app.qarya.utils.MyConst;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;


public class CategoriesFragment extends MyRecyclerFragment<Category, VMGeneral> {

    public CategoriesFragment() {
    }

    public static CategoriesFragment newInstance(int go) {
        Bundle args = new Bundle();
        args.putInt(MyConst.TYPE, go);
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.categories.observe(this, this::onDataReceived);
        mViewModel.follow.observe(this, this::onDataReceived);
       getData();
    }

    @Override
    public void getData() {
        super.getData();
        int GoTo = getArgs().getInt(MyConst.TYPE, 0);
        if(GoTo == ModelType.POST)
            mViewModel.categoriesOfPosts(0);
        else
            mViewModel.categoriesOfProducts(0);
    }

    @Override
    public void onDataReceived(List<Category> data) {
        super.onDataReceived(data);
        lista = data;
        adapter.notifyDataSetChanged();
    }
    public void onDataReceived(FollowResponse data) {
        MyActivity.log("search for followed item...");
        for (int i = 0; i <lista.size(); i++) {
            if (lista.get(i).getId().equals(data.getId())){
                MyActivity.log("Followed item found!");
                lista.get(i).setFollowed(data.getFollowed());
                MyActivity.log("Refresh adapter at "+i+" position with marked "+data.getFollowed());
                //adapter.notifyItemChanged(i);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initDefaultViews(view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryAdapter(super.lista, new OnInteractListener<Category>() {
            @Override
            public void onInteract(Category item, @NotNull Action action) {
                if (action == Action.FOLLOW)
                    mViewModel.follow(item);
            }

            @Override
            public void onClick(Category item) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
        });
        recyclerView.setAdapter(adapter);

        return view;

    }

}