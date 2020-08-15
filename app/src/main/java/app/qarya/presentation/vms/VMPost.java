package app.qarya.presentation.vms;

import app.qarya.domain.usecase.UCGeneral;
import app.qarya.domain.usecase.UCPosts;
import app.qarya.domain.usecase.UCReport;
import app.qarya.model.models.Category;
import app.qarya.model.models.Commun;
import app.qarya.model.models.SFile;
import app.qarya.model.models.requests.PostSetter;
import app.qarya.model.models.responses.GeneralResponse;
import app.qarya.model.models.responses.MarkResponse;
import app.qarya.presentation.base.MyViewModel;
import app.qarya.model.models.Comment;
import app.qarya.model.models.Post;
import app.qarya.model.models.responses.LikeResponse;
import tn.core.model.responses.PagingResponse;
import app.qarya.domain.usecase.UCComments;
import app.qarya.domain.usecase.UCLike;

import java.io.File;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class VMPost extends MyViewModel<Post> {

    public Post item;

    public MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    public MutableLiveData<List<Comment>> comments = new MutableLiveData<>();
    public MutableLiveData<PagingResponse<Comment>> pagingComments = new MutableLiveData<>();
    public MutableLiveData<LikeResponse> like = new MutableLiveData<>();
    public MutableLiveData<MarkResponse> bookmark = new MutableLiveData<>();
    public MutableLiveData<LikeResponse> likeComment = new MutableLiveData<>();
    public MutableLiveData<Object> reportPost = new MutableLiveData<>();
    public MutableLiveData<Object> reportComment = new MutableLiveData<>();
    public MutableLiveData<GeneralResponse> deletePost = new MutableLiveData<>();
    public MutableLiveData<GeneralResponse> deleteComment = new MutableLiveData<>();

    public void init(int id) {
        new UCPosts().one(id, getClosure());
    }

    public void push(int type, PostSetter setter) {
        new UCPosts().push(type, setter, getClosure());
    }


    public void bookmark(int id) {
            new UCLike().bookmark(id, getGenericClosure(bookmark));

    }
    public void like(int id) {
        new UCLike().likePost(id, getGenericClosure(like));
    }
    public void report(Commun model, String description) {
        if (model instanceof Comment)
            new UCReport().reportComment(model.id, description, getGenericClosure(reportComment));
        else
            new UCReport().reportPost(model.id, description, getGenericClosure(reportPost));
    }

    public void delete(Commun model) {
        if (model instanceof Comment)
            new UCComments().delete(model.id, getGenericClosure(deleteComment));
        else
            new UCPosts().delete(model.id, getGenericClosure(deletePost));
    }


    public void getComments(int id) {
        new UCComments().getCommentsPost(id,1, getGenericClosure(pagingComments));
    }


    public void pushComment(int id, String cmt) {
        new UCComments().pushPostComment(id, cmt, getGenericClosure(comments));
    }

    public void categoriesOfProducts() {
        new UCGeneral().categoriesOfProducts(0, getGenericClosure(categories));
    }
    public void categoriesOfNotes() {
        new UCGeneral().categoriesOfNotes(0, getGenericClosure(categories));
    }


    public void likeComment(int id) {
        new UCLike().likeComment(id, getGenericClosure(likeComment));
    }



    public MutableLiveData<SFile> upload = new MutableLiveData<>();
    public void upload(int positionInLocalList, File data) {
        new UCGeneral().uploadFile(data, 0, positionInLocalList, getGenericClosure(upload));
    }
}