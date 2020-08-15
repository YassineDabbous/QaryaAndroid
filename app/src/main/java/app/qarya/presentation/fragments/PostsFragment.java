package app.qarya.presentation.fragments;

import android.os.Bundle;

import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;


import app.qarya.R;
import app.qarya.model.ModelHolder;
import app.qarya.model.models.APath;
import app.qarya.model.models.Category;
import app.qarya.model.models.City;
import app.qarya.model.models.Commun;
import app.qarya.model.models.PostsOwner;
import app.qarya.model.models.User;
import app.qarya.model.models.responses.LikeResponse;

import androidx.recyclerview.widget.RecyclerView;

import app.qarya.presentation.adapters.MultiAdapter;
import tn.core.model.responses.PagingResponse;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import app.qarya.model.models.responses.MarkResponse;
import app.qarya.presentation.activities.MainActivity;
import app.qarya.presentation.adapters.PostsAdapter;
import app.qarya.presentation.base.MyActivity;
import app.qarya.presentation.base.MyRecyclerFragment;
import tn.core.presentation.listeners.EndlessListener;

import app.qarya.model.models.Post;
import app.qarya.presentation.vms.VMPosts;

import tn.core.util.Const;

import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class PostsFragment extends MyRecyclerFragment<ModelHolder, VMPosts> {


    //FloatingActionButton newTopic;
    public PostsOwner postsOwner;
    public MultiAdapter adapter;
    Button follow;

    public PostsFragment() {}

    public static PostsFragment newInstance(APath item) {
        return newInstance(PostsOwner.newInstance(item));
    }
    public static PostsFragment newInstance(String item) {
        return newInstance(PostsOwner.newInstance(item));
    }
    public static PostsFragment newInstance(User item) {
        return newInstance(PostsOwner.newInstance(item));
    }
    public static PostsFragment newInstance(Category item) {
        return newInstance(PostsOwner.newInstance(item));
    }
    public static PostsFragment newInstance(City item) {
        return newInstance(PostsOwner.newInstance(item));
    }
    public static PostsFragment newInstance(PostsOwner item) {
        Bundle args = new Bundle();
        if (item!=null)
            args.putSerializable(Const.ITEM, item);
        PostsFragment fragment = new PostsFragment();
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMPosts.class);
        mViewModel.callErrors.observe(this, this::onError);
        mViewModel.loadStatus.observe(this, this::onStatusChanged);
        mViewModel.like.observe(this, this::onDataReceived);
        mViewModel.bookmark.observe(this, this::onDataReceived);
        mViewModel.getLiveData().observe(this, this::onDataReceived);
        if(lista.size()==0){
            getData();
        }
    }

    @Override
    public void getData() {
        super.getData();
        if(postsOwner==null)
            postsOwner = (PostsOwner) getArgs().getSerializable(Const.ITEM);
        switch (postsOwner.type){
            //case SEARCH:
            //    mViewModel.search(postsOwner.name, page);
            //    break;
            case PATH:
                mViewModel.path(postsOwner.path, page);
                break;
            case CATEGORY:
                mViewModel.init(postsOwner.id, page);
                break;
            case USER:
                mViewModel.getUserPosts(postsOwner.id, page);
                break;
            case CITY:
                mViewModel.getLocationPosts(postsOwner.id, page);
                break;
            case TAG:
                mViewModel.getTagPosts(postsOwner.name.replace("#",""), page);
                break;
        }
    }

    @Override
    public void onDataReceived(PagingResponse<ModelHolder> data) {
        super.onDataReceived(data);
        if(lista.size() == 0){
            empty_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        adapter.notifyDataSetChanged();
    }
    public void onDataReceived(LikeResponse data) {
        MyActivity.log("search for liked item...");
        for (int i = 0; i <lista.size(); i++) {
            Post model = (Post) lista.get(i).model;
            if (model.getId().equals(data.getId())){
                MyActivity.log("Liked item found!");
                model.setLiked(data.getLiked());
                model.setLikesCount(data.getLikesCount());
                MyActivity.log("Refresh adapter at "+i+" position with "+data.getLikesCount()+" likes");
                //adapter.notifyItemChanged(i);
                adapter.refresh(new ModelHolder(model, model.modelType), i);
            }
        }
    }
    public void onDataReceived(MarkResponse data) {
        MyActivity.log("search for liked item...");
        for (int i = 0; i <lista.size(); i++) {
            Post model = (Post) lista.get(i).model;
            if (model.getId().equals(data.getId())){
                MyActivity.log("Liked item found!");
                model.setMarked(data.getBookmarked());
                MyActivity.log("Refresh adapter at "+i+" position with marked "+data.getBookmarked());
                //adapter.notifyItemChanged(i);
                adapter.refresh(new ModelHolder(model, model.modelType), i);
            }
        }
    }


    void bindPostOwner(View view){
        if(postsOwner.followable){
            view.findViewById(R.id.following).setVisibility(View.VISIBLE);
            follow = view.findViewById(R.id.follow);
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewModel.subscribe();
                }
            });
            if(postsOwner.followed)
                follow.setText(R.string.unfollow);
            else
                follow.setText(R.string.follow);
        }

        if (postsOwner.followersCount != null  && postsOwner.followersCount>0) {
            //TextView flws = view.findViewById(R.id.followersCount);
            ((TextView) view.findViewById(R.id.followersCount)).setText(postsOwner.followersCount+" "+getString(R.string.followers));
        }
        if (postsOwner.name != null && !postsOwner.name.isEmpty()) {
            ((TextView) view.findViewById(R.id.name)).setText(postsOwner.name);
        }
        if (postsOwner.description != null && !postsOwner.description.isEmpty()) {
            ((TextView) view.findViewById(R.id.description)).setText(postsOwner.description);
            ((TextView) view.findViewById(R.id.description)).setVisibility(View.VISIBLE);
        }
        if (postsOwner.image != null && !postsOwner.image.isEmpty()) {
            Picasso.get().load(postsOwner.image).into(((ImageView) view.findViewById(R.id.logo)));
        }

        /*if(postsOwner.canPost!=null && postsOwner.canPost){
            newTopic.setVisibility(View.VISIBLE);
        }*/
        view.findViewById(R.id.creator).setVisibility(View.VISIBLE);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics, container, false);
        initDefaultViews(view);

        /*newTopic = view.findViewById(R.id.newTopic);
        if (YDUserManager.configs()!=null && YDUserManager.configs().getEditor()){
            newTopic.setVisibility(View.VISIBLE);
            newTopic.setOnClickListener(view1 -> ((MyActivity) getActivity()).setFragment(CreatorFragment.newInstance()));
        }*/


        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        endlessListener = new EndlessListener(0, PostsAdapter.ADS_AFTER, new EndlessListener.Action() {
            @Override
            public void getOnScroll() {
                MyActivity.log("get in scroll");
                getData();
            }
        });
        recyclerView.addOnScrollListener(endlessListener);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ((MainActivity) getActivity()).toggleSwipe(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0); // 0 is for first item position
            }
        });



        adapter = new MultiAdapter(lista, new OnInteractListener<Commun>() {
            @Override
            public void onInteract(Commun item, @NotNull Action action) {
                switch (action){
                    case LIKE: mViewModel.like(item.getId()); break;
                    case BOOKMARK: mViewModel.bookmark(item); break;
                    case COMMENT: ((MainActivity) getActivity()).comment(item); break;
                    case SHARE: ((MainActivity) getActivity()).share(item); break;
                }
            }

            @Override
            public void onClick(Commun item) {
                ((MyActivity) getActivity()).onItemSelected(item);
            }
        });
        recyclerView.setAdapter(adapter);

        if(postsOwner==null)
            postsOwner = (PostsOwner) getArgs().getSerializable(Const.ITEM);
        if(postsOwner!=null){
            bindPostOwner(view);
        }else{
            view.findViewById(R.id.creator).setVisibility(View.GONE);
        }


        return view;
    }
}
