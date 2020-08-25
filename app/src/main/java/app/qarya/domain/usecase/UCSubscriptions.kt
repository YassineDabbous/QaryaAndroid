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

class UCSubscriptions : UseCase() {


    fun followedCategories(page:Int, closure: Closure<PagingResponse<ModelHolder>>) {
        api.followedCategories(page).enqueue(object :MyCallBack<BaseResponse<PagingResponse<Category>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Category>>) {
                super.onSuccess(response)
                //MyActivity.log("Ideas count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(ModelHolder.paginate(response.data, ModelType.CATEGORY))
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        )
    }


    fun followedUsers(page:Int, closure: Closure<PagingResponse<ModelHolder>>) {
        api.followedUsers(page).enqueue(object :MyCallBack<BaseResponse<PagingResponse<User>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<User>>) {
                super.onSuccess(response)
                if (response.data!=null)
                    closure.onSuccess(ModelHolder.paginate(response.data, ModelType.USER))
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

    fun followedStores(page:Int, closure: Closure<PagingResponse<ModelHolder>>) {
        api.followedStores(page).enqueue(object :MyCallBack<BaseResponse<PagingResponse<User>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<User>>) {
                super.onSuccess(response)
                if (response.data!=null)
                    closure.onSuccess(ModelHolder.paginate(response.data, ModelType.USER))
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }


}
