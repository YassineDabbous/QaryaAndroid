package app.qarya.domain.usecase

import app.qarya.model.ModelType
import tn.core.domain.Failure
import app.qarya.presentation.base.MyActivity
import app.qarya.model.models.Comment
import app.qarya.model.models.requests.CommentSetter
import app.qarya.model.models.responses.GeneralResponse
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCComments : UseCase() {

    fun getCommentsPost(id: Int, page:Int, closure: Closure<PagingResponse<Comment>>) = getComments(id, ModelType.NOTE, page, closure)
    fun getCommentsProduct(id: Int, page:Int, closure: Closure<PagingResponse<Comment>>) = getComments(id, ModelType.PRODUCT, page, closure)

    fun getComments(id: Int, type: Int, page:Int, closure: Closure<PagingResponse<Comment>>) {
        api.getComments(type.toString(), id.toString(), page).enqueue(object :MyCallBack<BaseResponse<PagingResponse<Comment>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Comment>>) {
                super.onSuccess(response)
                //MyActivity.log("comments count " + response.data?.data?.size)
                closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

    fun pushPostComment(id:Int, commentTxt:String, closure: Closure<List<Comment>>) = pushComment(id, ModelType.NOTE, commentTxt, closure)
    fun pushProductComment(id:Int, commentTxt:String, closure: Closure<List<Comment>>) = pushComment(id, ModelType.PRODUCT, commentTxt, closure)
    fun pushComment(id:Int, type:Int, commentTxt:String, closure: Closure<List<Comment>>){
        MyActivity.log("pushComment: $commentTxt")
        getApi().commentsPush(CommentSetter(type, id, commentTxt)).enqueue(object: MyCallBack<BaseResponse<List<Comment>>>(){
            override fun onSuccess(response: BaseResponse<List<Comment>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }


    fun delete(id:Int, closure: Closure<GeneralResponse>) {
        api.commentsDelete(id).enqueue(object : MyCallBack<BaseResponse<GeneralResponse>>() {
            override fun onSuccess(response: BaseResponse<GeneralResponse>) {
                super.onSuccess(response)
                if(response.data!=null)
                    closure.onSuccess(response.data)
            }


            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

}
