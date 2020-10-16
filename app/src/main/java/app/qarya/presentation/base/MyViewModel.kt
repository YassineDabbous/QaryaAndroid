package app.qarya.presentation.base

import androidx.lifecycle.MutableLiveData
import app.qarya.MyApplication
import app.qarya.R
import app.qarya.model.RestAPI
import app.qarya.presentation.activities.MainActivity
import tn.core.domain.Failure
import tn.core.presentation.base.BaseViewModel


open class MyViewModel<T>: BaseViewModel<RestAPI, T>() {

    var goTo = MutableLiveData<Boolean>()

    override fun getApi(): RestAPI {
        return MyApplication.get().getRestAPI()
    }
    override fun cancel() {
        return MyApplication.get().httpClient.dispatcher().cancelAll()
    }


    override fun handleError(failure: Failure) {
        // # TODO handle error message view,
        //  TODO toast doesn't appear with DialogFragment
        MyApplication.get().toast("RQST ERROR")
        loadStatus.postValue(false)
        if (failure is Failure.ValidationFailure) {
            MyActivity.log("-------------------------- REQUEST ERROR ☻ Map --------------------------", (failure as Failure.ValidationFailure).validation.toString())
            MyApplication.get().toast(MyApplication.get().getText(R.string.Check_the_entered_data).toString())
            val params = failure.validation
            var t = ""
            for (param in params!!.entries) run {
                t = param.key
                val v = param.value
                for (txt in v)
                    t += "\n # "+txt;
                MyApplication.get().toast(t)
            }
        } else if (failure is Failure.NeedAuthFailure) {
            MainActivity.getInstance().showLogin()
            MyApplication.get().toast( MyApplication.get().getText(R.string.need_auth).toString())
        } else if (failure is Failure.NetworkFailure) {
            MyApplication.get().toast( MyApplication.get().getText(R.string.check_network).toString())
        } else if (failure is Failure.GsonParsingFailure) {
            MyApplication.get().toast( MyApplication.get().getText(R.string.internal_error).toString())
        } else if (failure is Failure.PermissionDeniedFailure) {
            MyApplication.get().toast( MyApplication.get().getText(R.string.server_permisson_error).toString())
        } else if (failure is Failure.JustMessages) {
            for(msg in failure.messages!!)
                MyApplication.get().toast( msg )
        } else if (failure is Failure.GeneralFailure){
            callErrors.postValue(failure.errors)
            for(t in failure.errors!!.iterator())
                MyApplication.get().toast( t)
        }
    }


}