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

import app.qarya.R;
import app.qarya.model.ModelHolder;
import app.qarya.model.ModelType;
import app.qarya.model.models.Commun;
import app.qarya.model.models.Relation;
import app.qarya.model.shared.YDUserManager;
import app.qarya.presentation.activities.MainActivity;
import app.qarya.presentation.adapters.MultiAdapter;
import app.qarya.presentation.base.MyActivity;
import app.qarya.presentation.base.MyRecyclerFragment;
import app.qarya.presentation.vms.VMRelations;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.Const;


public class RelationsFragment extends MyRecyclerFragment<Relation, VMRelations> {

    RecyclerView recyclerView;
    public MultiAdapter adapter;
    List<ModelHolder> l = new ArrayList<>();
    public int[] tabIcons = {
            R.drawable.account_multiple_plus,
            R.drawable.account_question,
            R.drawable.account_arrow_right,
            R.drawable.account_group_outline,
            R.drawable.account_remove,
    };
    public int[] tabTitles = {
            R.string.requests,
            R.string.suggested,
            R.string.requested,
            R.string.friends,
            R.string.blocked
    };

    @Override
    public void setTabs() {
        super.setTabs();
        super.tabIcons = tabIcons;
        super.tabTitles = tabTitles;
    }

    public RelationsFragment() {
        // Required empty public constructor
    }

    public static RelationsFragment newInstance() {
        RelationsFragment fragment = new RelationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMRelations.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);

        mViewModel.getRelation().observe(this, this::onDataReceived);

        mViewModel.getRelationsFriends().observe(this, this::onDataReceived);
        mViewModel.getRelationsSuggested().observe(this, this::onSuggestedReceived);
        mViewModel.getRelationsRequests().observe(this, this::onDataReceived);
        mViewModel.getRelationsRequested().observe(this, this::onDataReceived);
        mViewModel.getRelationsBlocked().observe(this, this::onDataReceived);

        getData();
    }

    @Override
    public void getData() {
        super.getData();

        l.clear();

        if (current == 0)
            mViewModel.requests();
        else if (current == 1)
            mViewModel.suggested();
        else if (current == 2)
            mViewModel.requested();
        else if (current == 3){
            int id = YDUserManager.auth().getId();
            mViewModel.friends(id);
        }
        else if (current == 4)
            mViewModel.blocked();

    }


    public void onDataReceived(Relation data) {
        adapter.refresh(new ModelHolder(data, ModelType.RELATION));
    }

    @Override
    public void onDataReceived(List<Relation> data) {
        super.onDataReceived(data);
        for (Relation item : data) {
            l.add(new ModelHolder(item, ModelType.RELATION));
        }
        adapter.notifyDataSetChanged();
        toggleDataVisibility( l.size()==0 );
        // suggest friends if he is feeling lonely :p
        /*if (l.size()<5)
            mViewModel.suggested();*/
    }

    public void onSuggestedReceived(List<Relation> data) {
        l.add(new ModelHolder(getText(R.string.suggested), ModelType.HEADER));
        for (Relation item : data) {
            l.add(new ModelHolder(item, ModelType.RELATION));
        }
        adapter.notifyDataSetChanged();
        toggleDataVisibility( l.size()==0 );
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initDefaultViews(view);
        initTabs(view, true);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MultiAdapter(l, new OnInteractListener<Commun>() {
            @Override
            public void onInteract(Commun item, @NotNull Action action) {
                MyActivity.log(item.toString());
                Timber.e(item.toString());
                if (action == Action.FOLLOW)
                    mViewModel.addRelation(item.getUid());
                else if (action == Action.ACCEPT)
                    mViewModel.acceptRelation(item.getUid());
                else if (action == Action.REFUSE)
                    mViewModel.refuseRelation(item.getUid());
                else
                    ((MainActivity) getActivity()).onItemSelected((Relation) item);
            }

            @Override
            public void onClick(Commun item) {
                ((MyActivity) getActivity()).onItemSelected((Relation) item);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }


}
