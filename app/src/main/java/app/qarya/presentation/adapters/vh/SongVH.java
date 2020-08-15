package app.qarya.presentation.adapters.vh;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.qarya.R;
import app.qarya.model.models.Song;
import app.qarya.utils.ImageHelper;

import tn.core.presentation.base.adapters.BaseViewHolder;

public class SongVH extends BaseViewHolder<Song> {
    public final TextView title;
    public final ImageView play;
    public ImageView logo;
    View mView;
    Song model;

    public SongVH(View view) {
        super(view);
        mView = view;
        title = view.findViewById(R.id.title);
        play = view.findViewById(R.id.play);
        logo =  view.findViewById(R.id.logo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_song;
    }

    @Override
    public void bind(final Song model) {
        this.model = model;
        title.setText(model.title);
        ImageHelper.load(logo,model.image, 100,100);
        if (model.isPlaying)
            play.setVisibility(View.VISIBLE);
        else
            play.setVisibility(View.GONE);
        /*accept.setOnClickListener(view -> ((OnRequestInteractListener<Song>)listener).onAccept(model)); //setUser(2)
        reject.setOnClickListener(view -> ((OnRequestInteractListener<Song>)listener).onRefuse(model)); //setUser(0));**/
        mView.setOnClickListener(view -> {
            if (null != listener)
                listener.onClick(model);
        });
    }


}