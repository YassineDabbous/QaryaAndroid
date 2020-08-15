package app.qarya.presentation.vms;

import app.qarya.presentation.base.MyViewModel;
import app.qarya.model.models.Broadcast;
import app.qarya.model.models.requests.RegisterRequest;
import app.qarya.model.models.responses.AuthResponse;
import tn.core.model.responses.BaseResponse;
import app.qarya.domain.usecase.UCAuth;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class VMAuth extends MyViewModel<BaseResponse<AuthResponse>> {

    UCAuth uc = new UCAuth();
    public MutableLiveData<List<Broadcast>> broadcasts = new MutableLiveData();
    public MutableLiveData<BaseResponse<Object>> recover = new MutableLiveData();

    public void login(String email, String password) {
        uc.login(email, password, getClosure());
    }
    public void loginFacebook(String id, String token) {
        uc.loginFacebook(id, token, getClosure());
    }


    public void register(RegisterRequest registerRequest) {
        uc.register(registerRequest, getClosure());
    }

    public void recover(String email) {
        uc.recover(email, getGenericClosure(recover));
    }

    public void broadcasts() {
        uc.broadcasts(getGenericClosure(broadcasts));
    }


}