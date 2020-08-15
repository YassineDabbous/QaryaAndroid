package app.qarya.domain.usecase

import app.qarya.model.ModelHolder
import app.qarya.model.ModelType
import tn.core.domain.Failure
import app.qarya.model.models.*
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import retrofit2.Call

class UCGeneric : UseCase() {


    fun <T:Commun> genericPaginator(type: Int, path: String, page:Int, closure: Closure<PagingResponse<T>>){
        val c:MyCallBack<BaseResponse<PagingResponse<T>>>  = object: MyCallBack<BaseResponse<PagingResponse<T>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<T>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        getCall<T>(type, path, page).enqueue((c) );
    }
    fun <T:Commun> genericPostsHolderPaginator(type: Int, path: String, page:Int, closure: Closure<PagingResponse<ModelHolder>>){
        val c:MyCallBack<BaseResponse<PagingResponse<T>>>  = object: MyCallBack<BaseResponse<PagingResponse<T>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<T>>?) {
                super.onSuccess(response)
                closure.onSuccess(ModelHolder.pagination(response?.data))
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        getCall<T>(type, path, page).enqueue((c) );
    }

    fun <T:Commun> generic(type: Int, path: String, page:Int, closure: Closure<List<T>>){
        val c:MyCallBack<BaseResponse<PagingResponse<T>>>  = object: MyCallBack<BaseResponse<PagingResponse<T>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<T>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        getCall<T>(type, path, page).enqueue(c);
    }

    fun <T:Commun> genericAll(type: Int, path: String, closure: Closure<List<T>>){
        val c:MyCallBack<BaseResponse<List<T>>>  = object: MyCallBack<BaseResponse<List<T>>>(){
            override fun onSuccess(response: BaseResponse<List<T>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        (getApi().customRelations(path, 1) as Call<BaseResponse<List<T>>>).enqueue(c);
    }

    fun <T:Commun> getCall(type: Int, path: String, page:Int): Call<BaseResponse<PagingResponse<T>>>{
        when (type){
            ModelType.NOTE -> return getApi().customPosts(path, page) as Call<BaseResponse<PagingResponse<T>>>
            ModelType.STORY -> return getApi().customStories(path, page) as Call<BaseResponse<PagingResponse<T>>>
            ModelType.RELATION -> return getApi().customRelations(path, page) as Call<BaseResponse<PagingResponse<T>>>
            ModelType.USER -> return getApi().customUsers(path, page) as Call<BaseResponse<PagingResponse<T>>>
            else -> return getApi().customPosts(path, page) as Call<BaseResponse<PagingResponse<T>>>
        }
    }




}
