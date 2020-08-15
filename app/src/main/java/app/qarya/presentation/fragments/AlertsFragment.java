package app.qarya.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.qarya.R;


import app.qarya.model.models.APath;
import app.qarya.presentation.adapters.AlertsAdapter;

import app.qarya.presentation.base.MyRecyclerFragment;

import app.qarya.model.models.Alert;
import app.qarya.presentation.vms.VMGeneral;

import java.util.List;


public class AlertsFragment extends MyRecyclerFragment<Alert, VMGeneral> {


    public AlertsFragment() {
        // Required empty public constructor
    }

    public static AlertsFragment newInstance(APath aPath) {

        Bundle args = new Bundle();

        AlertsFragment fragment = new AlertsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.alerts.observe(this, this::onDataReceived);
    }

    @Override
    public void onDataReceived(List<Alert> data) {
        super.onDataReceived(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initDefaultViews(view);
        adapter = new AlertsAdapter(super.lista);
        recyclerView.setAdapter(adapter);
        if(super.lista.size()==0){
            getData();
        }
        return view;
    }

    @Override
    public void getData() {
        super.getData();
        mViewModel.alerts();
    }
}
