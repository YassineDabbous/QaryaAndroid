package app.qarya.presentation.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import app.qarya.R;

import tn.core.presentation.base.BaseDialogFragment;

public class MyDialogFragment<VM> extends BaseDialogFragment<VM> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);
    }


    public void initDefaultViews(View v){
        //pd = ProgressUtils.getProgressDialog(getContext());
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
        retryBtn.setOnClickListener((View) -> {
            getData();
        });
    }
}
