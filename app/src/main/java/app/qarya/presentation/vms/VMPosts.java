package app.qarya.presentation.vms;


import app.qarya.domain.usecase.UCGeneral;
import app.qarya.domain.usecase.UCGeneric;
import app.qarya.domain.usecase.UCLike;
import app.qarya.domain.usecase.UCPosts;
import app.qarya.model.ModelHolder;
import app.qarya.model.ModelType;
import app.qarya.model.models.Category;
import app.qarya.model.models.Model;
import app.qarya.model.models.User;
import app.qarya.model.models.requests.Filter;
import app.qarya.model.models.responses.LikeResponse;
import app.qarya.model.models.responses.MarkResponse;
import app.qarya.presentation.base.MyViewModel;
import app.qarya.model.models.Post;
import tn.core.model.responses.PagingResponse;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class VMPosts extends MyViewModel<PagingResponse<ModelHolder>> {

    public MutableLiveData<LikeResponse> like = new MutableLiveData<>();
    public MutableLiveData<MarkResponse> bookmark = new MutableLiveData<>();
    public MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    //public MutableLiveData<PagingResponse<User>> users = new MutableLiveData<>();

    public void accounts(Filter search, int page) {
        new UCGeneral().searchUModels(search, page, getClosure());
    }
    public void categoriesOfProducts() {
        new UCGeneral().categoriesOfProducts(0, getGenericClosure(categories));
    }
    public void categoriesOfStores() {
        new UCGeneral().categoriesOfStores(0, getGenericClosure(categories));
    }
    public void categoriesOfUsers() {
        new UCGeneral().categoriesOfUsers(0, getGenericClosure(categories));
    }

    public void like(int id) {
        new UCLike().likePost(id, getGenericClosure(like));
    }

    public void init(int cid, int page) {
        new UCPosts().getPosts(0, cid, 0, page, getClosure());
    }


    public void path(String path, int page) {
        new UCGeneric().<Post>genericPostsHolderPaginator(ModelType.NOTE, path, page, getClosure());
    }

    public void getUserPosts(int id, int page) {
        new UCPosts().getPosts(0, 0, id, page, getClosure());
    }
    public void getLocationPosts(int id, int page) {
        new UCPosts().getPosts(id, 0, 0, page, getClosure());
    }

    public void getTagPosts(String tag, int page) {
        new UCPosts().getTagPosts(tag, page, getClosure());
    }

    public void subscribe(){

    }

    public void bookmark(Model model) {
        if (model instanceof Post)
            new UCLike().bookmark(model.id, getGenericClosure(bookmark));

    }

}