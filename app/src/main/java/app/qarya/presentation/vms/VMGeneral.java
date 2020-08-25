package app.qarya.presentation.vms;

import app.qarya.domain.usecase.UCFollowing;
import app.qarya.domain.usecase.UCGeneral;
import app.qarya.model.ModelType;
import app.qarya.model.models.Alert;
import app.qarya.model.models.App;
import app.qarya.model.models.Broadcast;
import app.qarya.model.models.Category;
import app.qarya.model.models.City;
import app.qarya.model.models.Conversation;
import app.qarya.model.models.Notification;
import app.qarya.model.models.Room;
import app.qarya.model.models.requests.FollowRequest;
import app.qarya.model.models.requests.FollowResponse;
import app.qarya.presentation.base.MyViewModel;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

import tn.core.model.responses.PagingResponse;

public class VMGeneral extends MyViewModel<Boolean> {

    public MutableLiveData<List<App>> apps = new MutableLiveData<>();
    public MutableLiveData<List<Alert>> alerts = new MutableLiveData<>();

    public MutableLiveData<List<Broadcast>> broadcasts = new MutableLiveData<>();
    public MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    public MutableLiveData<List<City>> cities = new MutableLiveData<>();
    public MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();

    public void apps() {
        new UCGeneral().apps(this.getGenericClosure(apps));
    }

    public void alerts() {
        new UCGeneral().alerts(this.getGenericClosure(alerts));
    }

    public void broadcasts() {
        new UCGeneral().broadcasts(this.getGenericClosure(broadcasts));
    }


    public void categories(int pid, int type){
        new UCGeneral().categories(pid, type,  this.getGenericClosure(categories));
    }



    public void cities() {
        new UCGeneral().cities(this.getGenericClosure(cities));
    }
    public void notifications() {
        new UCGeneral().notifications(this.getGenericClosure(notifications));
    }


    public MutableLiveData<FollowResponse> follow = new MutableLiveData<>();
    public void follow(Category model){
        new UCFollowing().follow(new FollowRequest(model.getId(), ModelType.CATEGORY, !model.getFollowed()), this.getGenericClosure(follow));
    }
    public void follow(City model){
        new UCFollowing().follow(new FollowRequest(model.getId(), ModelType.CITY, !model.getFollowed()), this.getGenericClosure(follow));
    }

}