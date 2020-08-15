package app.qarya.presentation.vms;

import app.qarya.presentation.base.MyViewModel;
import app.qarya.model.models.Comment;
import tn.core.model.responses.PagingResponse;
import app.qarya.domain.usecase.UCComments;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class VMComments extends MyViewModel<PagingResponse<Comment>> {

    public MutableLiveData<List<Comment>> comments = new MutableLiveData<>();


    public void getComments(int id, int type, int page) {
        new UCComments().getComments(id,type, page, getClosure());
    }


    public void pushComment(int id, int type, String cmt) {
        new UCComments().pushComment(id, type, cmt, getGenericClosure(comments));
    }

}