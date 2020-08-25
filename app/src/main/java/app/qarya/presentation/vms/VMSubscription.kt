package app.qarya.presentation.vms

import androidx.lifecycle.MutableLiveData

import app.qarya.domain.usecase.UCGeneral
import app.qarya.domain.usecase.UCSubscriptions
import app.qarya.model.ModelHolder
import app.qarya.model.ModelType
import app.qarya.model.models.*
import app.qarya.model.models.requests.Filter
import app.qarya.presentation.base.MyViewModel

import tn.core.model.responses.PagingResponse

class VMSubscription : MyViewModel<PagingResponse<Post>>() {

    var users  = MutableLiveData<PagingResponse<ModelHolder>>()
    var stores  = MutableLiveData<PagingResponse<ModelHolder>>()
    var categories  = MutableLiveData<PagingResponse<ModelHolder>>()


    fun followedUsers(page: Int) {
        UCSubscriptions().followedUsers(page, getGenericClosure(users))
    }
    fun followedStores(page: Int) {
        UCSubscriptions().followedStores(page, getGenericClosure(stores))
    }
    fun followedCategories(page: Int) {
        UCSubscriptions().followedCategories(page, getGenericClosure(categories))
    }
}