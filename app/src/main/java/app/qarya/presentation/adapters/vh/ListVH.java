package app.qarya.presentation.adapters.vh;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.qarya.R;

import app.qarya.model.ModelType;
import app.qarya.model.models.Relation;
import app.qarya.presentation.base.MyActivity;
import app.qarya.presentation.base.MyAdapter;
import tn.core.presentation.base.adapters.BaseViewHolder;
import app.qarya.model.models.Commun;

import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import java.util.ArrayList;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class ListVH extends BaseViewHolder<Commun> {
    public final View mView;
    public final TextView name;
    public Button more;
    public RecyclerView recyclerView;
    public Commun mItem;
    MyAdapter adapter;
    ContentLoadingProgressBar pd;

    public ListVH(View view) {
        super(view);
        mView = view;
        name = view.findViewById(R.id.name);
        pd = view.findViewById(R.id.looking);
        more = view.findViewById(R.id.more);
        recyclerView = view.findViewById(R.id.horizontal);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_list;
    }

    @Override
    public void bind(Commun model) {
        if(model.modelType == ModelType.RELATION){
            mView.setBackgroundColor(mView.getResources().getColor(R.color.transparent));
            mView.setPadding(0, 0,0, 0);
        }
        more.setVisibility(View.GONE);
        mItem = model;
        if (model.getName()!=null && !model.getName().isEmpty()){
            name.setVisibility(View.VISIBLE);
            name.setText(model.getName());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(mItem);
                }
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(mItem);
                }
            }
        });
        if(!mItem.loaded && mItem.lista.size()==0){
            pd.show();
            ((OnInteractListener) listener).onInteract(mItem, Action.LOAD);
        }
        else
            loadME();
    }

    public void setList() {
        adapter.items = mItem.lista;
        recyclerView.setAdapter(adapter);
        Timber.d("horizontal items count: "+mItem.lista.size());
        pd.hide();
    }


    public void loadME(){
        MyActivity.log("get horizontal list");
        if(mItem.modelType == ModelType.STORY){
            MyActivity.log("show horizontal posts list...");
            adapter = new MyAdapter<>(new ArrayList<>(), VHStory.class, item -> listener.onClick(item), false);
            setList();
        }
        else if(mItem.modelType == ModelType.USER){
            MyActivity.log("show horizontal users list...");
            adapter = new MyAdapter<>(new ArrayList<>(), CircleVH.class, item -> listener.onClick(item), false);
            setList();
        }
        else if(mItem.modelType == ModelType.RELATION){
            MyActivity.log("show horizontal Relations list...");
            adapter = new MyAdapter<>(new ArrayList<>(), RelationVerticalVH.class, new OnInteractListener<Relation>() {
                @Override
                public void onInteract(Relation item, @NotNull Action action) {
                    ((OnInteractListener) listener).onInteract(item, action);
                }

                @Override
                public void onClick(Relation item) {
                    listener.onClick(item);
                }
            }, false);
            setList();
        } else
            MyActivity.log("show show laho laho :(");
    }



/* moved to ViewModel, don't create connection here
    public void getData(){
        MyActivity.log("get horizontal list");
        if(mItem.modelType == ModelType.NOTE){
            adapter = new MyAdapter<Post, VHPost>(new ArrayList<>(), VHPost.class, item -> listener.onClick(item), false);
            this.<Post>paginate();
            MyActivity.log("getting horizontal posts list...");
        }
        else if(mItem.modelType == ModelType.USER){
            adapter = new MyAdapter<User, CircleVH>(new ArrayList<>(), CircleVH.class, item -> listener.onClick(item), false);
            this.<User>paginate();
            MyActivity.log("getting horizontal users list...");
        }
        else if(mItem.modelType == ModelType.RELATION){
            adapter = new MyAdapter<Relation, RelationVerticalVH>(new ArrayList<>(), RelationVerticalVH.class, new OnInteractListener<Relation>() {
                @Override
                public void onInteract(Relation item, @NotNull Action action) {
                    ((OnInteractListener) listener).onInteract(item, action);
                }

                @Override
                public void onClick(Relation item) {
                    listener.onClick(item);
                }
            }, false);
            this.<Relation>getAll();
            MyActivity.log("getting horizontal Relations list...");
        } else
            MyActivity.log("laho laho :(");


    }

    public void handleError(Failure failure){
        for (String error:errors) {
            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
        }
    }

    <YModel extends Commun> void paginate(){
        pd.show();
        new UCGeneric().<YModel>generic(mItem.modelType, mItem.modelPath, 1, new Closure<List<YModel>>() {
            @Override
            public void onSuccess(List<YModel> response) {
                MyActivity.log("horizontal new items count: "+response.size());
                mItem.lista.addAll(response);
                adapter.items = response;
                setList();
            }

            @Override
            public void onError(Failure failure) {
                handleError(failure);
            }
        });
    }
    <YModel extends Commun> void getAll(){
        pd.show();
        new UCGeneric().<YModel>genericAll(mItem.modelType, mItem.modelPath, new Closure<List<YModel>>() {
            @Override
            public void onSuccess(List<YModel> response) {
                MyActivity.log("horizontal new items count: "+response.size());
                mItem.lista.addAll(response);
                adapter.items = response;
                setList();
            }

            @Override
            public void onError(Failure failure) {
                handleError(failure);
            }
        });
    }

    */
}