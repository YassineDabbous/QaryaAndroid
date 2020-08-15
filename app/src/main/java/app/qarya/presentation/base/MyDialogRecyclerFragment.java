package app.qarya.presentation.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import app.qarya.R;

import tn.core.presentation.base.BaseDialogRecyclerFragment;

public class MyDialogRecyclerFragment<Model, VM> extends BaseDialogRecyclerFragment<Model, VM> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);
    }


    public void initDefaultViews(View v){
        //pd = ProgressUtils.getProgressDialog(getContext());  //v.findViewById((R.id.loading));
        recyclerView = v.findViewById((R.id.recycler_view));
        //initAdmob(v);
        initMsgView(v);
        View toolbar_right = v.findViewById(R.id.toolbar_right);
        View toolbar_left = v.findViewById(R.id.toolbar_left);
        toolbar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });
        toolbar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
    public void showOptions(){}

    public void initMsgView(View v){
        empty_view = v.findViewById(R.id.emptyView);
        View retryBtn = v.findViewById(R.id.retryBtn);
        if (retryBtn!=null)
            retryBtn.setOnClickListener((View) -> {
                getData();
            });

    }
}
