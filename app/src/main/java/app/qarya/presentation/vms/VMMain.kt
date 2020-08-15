package app.qarya.presentation.vms

import androidx.lifecycle.MutableLiveData
import app.qarya.domain.usecase.UCGeneral
import app.qarya.domain.usecase.UCHome
import app.qarya.model.shared.YDUserManager
import app.qarya.model.models.Broadcast
import app.qarya.presentation.base.MyViewModel
import app.qarya.presentation.base.MyActivity
import app.qarya.model.models.Commun
import app.qarya.model.models.responses.ConfigsResponse
import app.qarya.model.models.responses.NewsResponse

class VMMain : MyViewModel<List<Commun>>() {


    var configs = MutableLiveData<ConfigsResponse>()
    var news = MutableLiveData<NewsResponse>()


    fun configs() {
        MyActivity.logHome("---------------------- REQUEST DATA FOR configs -----------------------------")
        UCHome().configs(getGenericClosure(configs))
    }
    fun news() {
        MyActivity.logHome("---------------------- REQUEST DATA FOR news -----------------------------")
        UCHome().news(getGenericClosure(news))
    }

    var broadcasts = MutableLiveData<List<Broadcast>>()
    fun broadcasts() {
        if(YDUserManager.check())
            UCGeneral().broadcasts(this.getGenericClosure(broadcasts))
    }

}
