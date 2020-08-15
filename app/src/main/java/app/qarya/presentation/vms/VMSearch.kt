package app.qarya.presentation.vms

import androidx.lifecycle.MutableLiveData

import app.qarya.domain.usecase.UCGeneral
import app.qarya.model.ModelType
import app.qarya.model.models.*
import app.qarya.model.models.requests.Filter
import app.qarya.presentation.base.MyViewModel

import tn.core.model.responses.PagingResponse

class VMSearch : MyViewModel<PagingResponse<Post>>() {

    var products = MutableLiveData<PagingResponse<Post>>()
    var users  = MutableLiveData<PagingResponse<User>>()

    fun searchPosts(search: Filter, page: Int) {
        search.type = ModelType.NOTE
        UCGeneral().search(search, page, closure)
    }
    fun searchProducts(search: Filter, page: Int) {
        search.type = ModelType.PRODUCT
        UCGeneral().search(search, page, getGenericClosure(products))
    }
    fun searchUsers(search: Filter, page: Int) {
        search.type = 0
        UCGeneral().searchUsers(search, page, getGenericClosure(users))
    }
}