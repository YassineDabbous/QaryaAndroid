package app.qarya.model;

import java.util.ArrayList;
import java.util.List;

import app.qarya.model.models.Commun;
import app.qarya.model.models.Post;
import app.qarya.model.models.User;
import tn.core.model.responses.PagingResponse;

public final class ModelHolder {
    private ModelHolder(){}
    public ModelHolder(Object model, int type){
        this.model = model;
        this.TYPE = type;
    }
    public int TYPE = ModelType.NOTE;
    public Object model = null;


    public static PagingResponse<ModelHolder> pagination(PagingResponse pagination){
        List<ModelHolder> lista = new ArrayList<>();
        PagingResponse<ModelHolder> p = new PagingResponse<>();
        p.setCurrentPage(pagination.getCurrentPage());
        p.setFirstPageUrl(pagination.getFirstPageUrl());
        p.setLastPageUrl(pagination.getLastPageUrl());
        p.setTotal(pagination.getTotal());

        for (Object c : pagination.getData()){
            Post m = (Post) c;
            lista.add(new ModelHolder(m, m.getType()));
        }
        p.setData(lista);
        return p;
    }

    public static PagingResponse<ModelHolder> paginationUsers(PagingResponse pagination){
        List<ModelHolder> lista = new ArrayList<>();
        PagingResponse<ModelHolder> p = new PagingResponse<>();
        p.setCurrentPage(pagination.getCurrentPage());
        p.setFirstPageUrl(pagination.getFirstPageUrl());
        p.setLastPageUrl(pagination.getLastPageUrl());
        p.setTotal(pagination.getTotal());

        for (Object c : pagination.getData()){
            User m = (User) c;
            lista.add(new ModelHolder(m, m.getType()));
        }
        p.setData(lista);
        return p;
    }
}
