package app.qarya.domain.usecase

import tn.core.domain.Failure
import app.qarya.presentation.base.MyActivity
import app.qarya.model.models.Broadcast
import app.qarya.model.models.requests.FacebookLoginRequest
import app.qarya.model.models.requests.LoginRequest
import app.qarya.model.models.requests.RegisterRequest
import app.qarya.model.models.responses.AuthResponse
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCAuth : UseCase() {

    fun broadcasts(closure: Closure<List<Broadcast>>){
        getApi().subscriptions().enqueue(object: MyCallBack<BaseResponse<List<Broadcast>>>(){
            override fun onSuccess(response: BaseResponse<List<Broadcast>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }

    fun recover(email:String, closure: Closure<BaseResponse<Any>>){
        MyActivity.log("recover: $email")
        getApi().recover(email).enqueue(object: MyCallBack<BaseResponse<Any>>(){
            override fun onSuccess(response: BaseResponse<Any>?) {
                super.onSuccess(response)
                closure.onSuccess(response)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }


    fun loginFacebook(id:String, token:String, closure: Closure<BaseResponse<AuthResponse>>){
        MyActivity.log("login: $id")
        getApi().loginFacebook(FacebookLoginRequest(id, token)).enqueue(object: MyCallBack<BaseResponse<AuthResponse>>(){
            override fun onSuccess(response: BaseResponse<AuthResponse>?) {
                super.onSuccess(response)
                closure.onSuccess(response)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }

    fun login(email:String, password:String, closure: Closure<BaseResponse<AuthResponse>>){
        MyActivity.log("login: $email")
        getApi().login(LoginRequest(email, password)).enqueue(object: MyCallBack<BaseResponse<AuthResponse>>(){
            override fun onSuccess(response: BaseResponse<AuthResponse>?) {
                super.onSuccess(response)
                closure.onSuccess(response)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }

    fun register(register:RegisterRequest, closure: Closure<BaseResponse<AuthResponse>>){
        MyActivity.log("login: {$register.email}")
        getApi().register(register).enqueue(object: MyCallBack<BaseResponse<AuthResponse>>(){
            override fun onSuccess(response: BaseResponse<AuthResponse>?) {
                super.onSuccess(response)
                closure.onSuccess(response)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }
}
