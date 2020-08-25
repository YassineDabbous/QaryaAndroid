package app.qarya.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import app.qarya.R;
import app.qarya.model.ModelType;
import app.qarya.presentation.vms.VMGeneral;
import app.qarya.presentation.activities.MainActivity;
import app.qarya.presentation.adapters.NotificationsAdapter;
import app.qarya.presentation.base.MyRecyclerFragment;
import app.qarya.model.models.Notification;


public class NotificationsFragment extends MyRecyclerFragment<Notification, VMGeneral> {

    public static NotificationsFragment newInstance() {

        Bundle args = new Bundle();

        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.notifications.observe(this, this::onDataReceived);
        mViewModel.notifications();
    }

    @Override
    public void onDataReceived(List<Notification> data) {
        super.onDataReceived(data);
        adapter.notifyDataSetChanged();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initDefaultViews(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationsAdapter(lista, item -> {((MainActivity) getActivity()).onItemSelected(item);});
        recyclerView.setAdapter(adapter);
        view.findViewById(R.id.pageHeader).setVisibility(View.VISIBLE);
        view.findViewById(R.id.pageAction).setOnClickListener(view1 -> {
            setFragment(SubscriptionsFragment.newInstance(0));
        });
        return view;
    }



}
