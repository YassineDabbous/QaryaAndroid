package app.qarya.presentation.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import app.qarya.R;
import app.qarya.model.models.APath;
import app.qarya.presentation.adapters.AppsAdapter;
import app.qarya.presentation.base.MyActivity;

import app.qarya.presentation.base.MyRecyclerFragment;
import app.qarya.model.models.App;
import app.qarya.presentation.vms.VMGeneral;



public class AppsFragment extends MyRecyclerFragment<App, VMGeneral> {

    public AppsFragment() {
    }

    public static AppsFragment newInstance(APath aPath) {

        Bundle args = new Bundle();

        AppsFragment fragment = new AppsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMGeneral.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.apps.observe(this, this::onDataReceived);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        mViewModel.apps();
    }

    @Override
    public void onDataReceived(List<App> data) {
        super.onDataReceived(data);
        adapter.notifyDataSetChanged();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initDefaultViews(view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new AppsAdapter(super.lista, item -> {
            MyActivity.log("package name =>=>=> "+item.getPackage());
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + item.getPackage())));
            } catch (ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + item.getPackage())));
            }
        });
        recyclerView.setAdapter(adapter);
        return view;

    }

}