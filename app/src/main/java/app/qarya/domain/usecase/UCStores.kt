package app.qarya.domain.usecase

import app.qarya.model.models.Post
import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import app.qarya.model.models.responses.GeneralResponse

class UCProducts : UseCase() {

    fun products(category: Int, page:Int, closure: Closure<PagingResponse<Post>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Post>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Post>>) {
                super.onSuccess(response)
                //MyActivity.log("Ideas count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        //if (uid==0)
            api.getProducts(category, page).enqueue(c)
        //else api.getUserPosts(uid, page).enqueue(c)
    }
    fun store(id:Int, closure: Closure<Post>) {
        api.getPost(id).enqueue(object :MyCallBack<BaseResponse<Post>>(){
            override fun onSuccess(response: BaseResponse<Post>) {
                super.onSuccess(response)
                //MyActivity.log("Ideas count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }


    fun delete(id:Int, closure: Closure<GeneralResponse>) {
        api.deletePost(id).enqueue(object : MyCallBack<BaseResponse<GeneralResponse>>() {
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
