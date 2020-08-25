package app.qarya.model;

import java.util.ArrayList;
import java.util.List;

import app.qarya.model.models.Category;
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

    public static PagingResponse<ModelHolder> paginate(PagingResponse pagination, int type){
        PagingResponse<ModelHolder> p = new PagingResponse<>();
        p.setCurrentPage(pagination.getCurrentPage());
        p.setFirstPageUrl(pagination.getFirstPageUrl());
        p.setLastPageUrl(pagination.getLastPageUrl());
        p.setTotal(pagination.getTotal());
        List<ModelHolder> lista = new ArrayList<>();
        switch (type){
            case ModelType.POST : lista = posts(pagination); break;
            case ModelType.USER : lista = users(pagination); break;
            case ModelType.CATEGORY : lista = categories(pagination); break;
        }
        p.setData(lista);
        return p;
    }
    static List<ModelHolder> posts(PagingResponse pagination){
        List<ModelHolder> lista = new ArrayList<>();
        for (Object c : pagination.getData()){
            Post m = (Post) c;
            lista.add(new ModelHolder(m, m.getType()));
        }
        return lista;
    }
    static List<ModelHolder> users(PagingResponse pagination){
        List<ModelHolder> lista = new ArrayList<>();
        for (Object c : pagination.getData()){
            User m = (User) c;
            lista.add(new ModelHolder(m, m.getType()));
        }
        return lista;
    }
    static List<ModelHolder> categories(PagingResponse pagination){
        List<ModelHolder> lista = new ArrayList<>();
        for (Object c : pagination.getData()){
            Category m = (Category) c;
            lista.add(new ModelHolder(m, ModelType.CATEGORY));
        }
        return lista;
    }


}
