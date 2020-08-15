package app.qarya.domain.usecase;


import app.qarya.MyApplication;
import app.qarya.model.RestAPI;

public class UseCase {

    public RestAPI getApi() {
        return MyApplication.get().getRestAPI();
    }

}
