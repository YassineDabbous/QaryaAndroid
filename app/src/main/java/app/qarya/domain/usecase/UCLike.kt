package app.qarya.domain.usecase

import app.qarya.model.ModelType
import tn.core.domain.Failure
import app.qarya.presentation.base.MyActivity
import tn.core.model.responses.BaseResponse
import app.qarya.model.models.responses.LikeResponse
import app.qarya.model.models.responses.MarkResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCLike : UseCase() {

    fun likePost(id: Int, closure: Closure<LikeResponse>) = like(id, ModelType.NOTE, closure)
    fun likeComment(id: Int, closure: Closure<LikeResponse>) = like(id, ModelType.COMMENT, closure)
    fun likeProduct(id: Int, closure: Closure<LikeResponse>) = like(id, ModelType.PRODUCT, closure)

    fun like(id: Int, type: Int, closure: Closure<LikeResponse>) {
        api.like(id, type).enqueue(object : MyCallBack<BaseResponse<LikeResponse>>() {

            override fun onSuccess(response: BaseResponse<LikeResponse>) {
                super.onSuccess(response)
                MyActivity.log("likes count " + response.data.likesCount!!)
                closure.onSuccess(response.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }


    fun bookmark(id: Int, closure: Closure<MarkResponse>) {
        api.bookmark(id).enqueue(object : MyCallBack<BaseResponse<MarkResponse>>() {
            override fun onSuccess(response: BaseResponse<MarkResponse>) {
                super.onSuccess(response)
                closure.onSuccess(response.data)
            }


            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

}
