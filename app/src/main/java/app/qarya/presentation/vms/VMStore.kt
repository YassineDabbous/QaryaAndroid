package app.qarya.presentation.vms

import androidx.lifecycle.MutableLiveData
import app.qarya.domain.usecase.*
import app.qarya.model.models.Post

import app.qarya.model.models.responses.GeneralResponse
import app.qarya.model.models.responses.LikeResponse
import app.qarya.presentation.base.MyViewModel

import tn.core.model.responses.PagingResponse

class VMProduct : MyViewModel<Post>() {

    var item: Post? = null

    var products = MutableLiveData<PagingResponse<Post>>()
    var like = MutableLiveData<LikeResponse>()
    var reportProduct= MutableLiveData<Any>()
    var deleteProduct = MutableLiveData<GeneralResponse>()



    fun init(id: Int, page: Int) {
        UCProducts().products(id, page, getGenericClosure(products))
    }

    fun one(id: Int) {
        UCProducts().store(id, closure)
    }



    fun like() {
        UCLike().likeProduct(item?.getId()!!, getGenericClosure(like));
    }
    fun report(description:String) {
            UCReport().reportProduct(item?.getId()!!, description, getGenericClosure(reportProduct));
    }

    fun delete() {
        UCProducts().delete(item?.getId()!!, getGenericClosure(deleteProduct));
    }


}