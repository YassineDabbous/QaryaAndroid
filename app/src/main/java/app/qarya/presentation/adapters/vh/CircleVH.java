package app.qarya.presentation.adapters.vh;

import android.view.View;
import android.widget.ImageView;

import app.qarya.R;
import app.qarya.model.models.User;
import app.qarya.presentation.base.MyActivity;
import tn.core.presentation.base.adapters.BaseViewHolder;
import android.widget.TextView;
import app.qarya.utils.ImageHelper;

public class CircleVH extends BaseViewHolder<User> {
    public final View mView, dot;
    public ImageView thumbnail;
    public TextView name, count;


    public CircleVH(View view) {
        super(view);
        mView = view;
        thumbnail = view.findViewById(R.id.photo);
        name = view.findViewById(R.id.name);
        count = view.findViewById(R.id.counter);
        dot = view.findViewById(R.id.dot);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_circle;
    }

    @Override
    public void bind(User model) {
        name.setText(model.getName());
        if(model.getOnline()){
            dot.setVisibility(View.VISIBLE);
        }
        /*
        if(model.postsCount>0){
            String countxt = model.postsCount+"";
            if (model.postsCount>=100) countxt = "+99";
            count.setText(countxt);
            count.setVisibility(View.VISIBLE);
        }*/
        ImageHelper.loadCircle(thumbnail, model.getPhoto());
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity.log("clicked", model.getName());
                if (null != listener) {
                    listener.onClick(model);
                }
            }
        });
    }

}