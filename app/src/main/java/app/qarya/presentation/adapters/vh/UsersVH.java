package app.qarya.presentation.adapters.vh;

import android.view.View;
import android.widget.ImageView;

import app.qarya.R;

import tn.core.presentation.base.adapters.BaseViewHolder;
import app.qarya.model.models.User;
import app.qarya.presentation.base.MyActivity;
import app.qarya.utils.ImageHelper;
import android.widget.TextView;

public class UsersVH extends BaseViewHolder<User> {
    public final View mView, dot;
    public final TextView title, description;
    public ImageView thumbnail;
    public ImageView overflow;
    User model;

    public UsersVH(View view) {
        super(view);
        mView = view;
        title = view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        thumbnail = view.findViewById(R.id.thumbnail);
        overflow = (ImageView) view.findViewById(R.id.overflow);
        dot = view.findViewById(R.id.online);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_square;
    }

    @Override
    public void bind(final User model) {
        MyActivity.log("user model", model.toString());
        description.setVisibility(View.VISIBLE);
        this.model = model;
        if (model.getOnline()) dot.setVisibility(View.VISIBLE);
        title.setText(model.getName());
        //description.setText(model.getSpeciality());
        ImageHelper.load(thumbnail, model.getPhoto(), 200, 200);
        overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopupMenu(overflow);
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(model);
                }
            }
        });
    }


}