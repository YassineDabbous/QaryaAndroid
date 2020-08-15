package app.qarya.domain.usecase

import app.qarya.model.ModelHolder
import app.qarya.model.ModelType
import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import app.qarya.model.models.Post
import app.qarya.model.models.requests.PostSetter
import app.qarya.model.models.responses.GeneralResponse

class UCPosts : UseCase() {



    fun bookmarks(closure: Closure<PagingResponse<Post>>) {
        api.bookmarks().enqueue(object :MyCallBack<BaseResponse<PagingResponse<Post>>>(){
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
        })
    }

    fun one(id:Int, closure: Closure<Post>) {
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

    fun push(type:Int, post:PostSetter, closure: Closure<Post>) {
        val c = object : MyCallBack<BaseResponse<Post>>(){
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
        }
        if(type === ModelType.PRODUCT)
            api.pushProduct(post).enqueue(c)
        else if(type === ModelType.STORY)
            api.pushStory(post).enqueue(c)
        else if(type === ModelType.NOTE)
            api.pushNote(post).enqueue(c)
        else
            api.pushPost(post).enqueue(c)
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

    fun getPosts(location: Int, category: Int, uid: Int, page:Int, closure: Closure<PagingResponse<ModelHolder>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Post>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Post>>) {
                super.onSuccess(response)
                //MyActivity.log("Ideas count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(ModelHolder.pagination(response.data))
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        if (location!=0)
            api.getLocationPosts(location, page).enqueue(c)
        else if (category!=0)
            api.getCategoryPosts(category, page).enqueue(c)
        else
            api.getUserPosts(uid, page).enqueue(c)
    }



    fun getTagPosts(tag:String, page:Int, closure: Closure<PagingResponse<ModelHolder>>) {
        val c = object :MyCallBack<BaseResponse<PagingResponse<Post>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Post>>) {
                super.onSuccess(response)
                //MyActivity.log("Ideas count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(ModelHolder.pagination(response.data))
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        api.getTagPosts(tag, page).enqueue(c)
    }
}
