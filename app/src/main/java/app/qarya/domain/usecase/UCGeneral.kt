package app.qarya.domain.usecase

import app.qarya.model.ModelHolder
import app.qarya.model.ModelType
import tn.core.domain.Failure
import app.qarya.model.models.*
import app.qarya.model.models.requests.Filter
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import app.qarya.presentation.base.MyActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tn.core.model.responses.PagingResponse
import tn.core.presentation.base.BaseActivity
import java.io.File

class UCGeneral : UseCase() {


    fun uploadFile(file: File, type:Int, positionInLocalList:Int=0, closure: Closure<SFile>) {
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val filename = RequestBody.create(MediaType.parse("text/plain"), file.getName())
        val c = object :MyCallBack<BaseResponse<SFile>>(){
            override fun onSuccess(response: BaseResponse<SFile>) {
                super.onSuccess(response)
                //MyActivity.log("Ideas count " + response.data?.data?.size)
                if (response.data!=null){
                    var f:SFile = response.data
                    f.positionInLocalList = positionInLocalList
                    closure.onSuccess(f)
                }
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        when(type) {
            0 ->{
                val fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody)
                api.uploadImage(fileToUpload, filename).enqueue(c)
            }
            1 -> {
                val fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestBody)
                api.uploadVideo(fileToUpload, filename).enqueue(c)
            }
            else -> {
                val fileToUpload = MultipartBody.Part.createFormData("audio", file.getName(), requestBody)
                api.uploadAudio(fileToUpload, filename).enqueue(c)
            }
        }
    }



    fun search(search: Filter, page:Int, closure: Closure<PagingResponse<Post>>) {
        api.searchPosts(search, page).enqueue(object :MyCallBack<BaseResponse<PagingResponse<Post>>>(){
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
        )
    }

    fun searchUsers(search: Filter, page:Int, closure: Closure<PagingResponse<User>>) {
        api.searchUsers(search, page).enqueue(object :MyCallBack<BaseResponse<PagingResponse<User>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<User>>) {
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

    fun searchUModels(search: Filter, page:Int, closure: Closure<PagingResponse<ModelHolder>>) {
        api.searchUsers(search, page).enqueue(object :MyCallBack<BaseResponse<PagingResponse<User>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<User>>) {
                super.onSuccess(response)
                if (response.data!=null)
                    closure.onSuccess(ModelHolder.paginationUsers(response.data))
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }



    fun menu(closure: Closure<List<APath>>){
        api.menu().enqueue(object: MyCallBack<BaseResponse<List<APath>>>(){
            override fun onSuccess(response: BaseResponse<List<APath>>?) {
                super.onSuccess(response)
                BaseActivity.log("menu list count:" + response?.data?.size)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                BaseActivity.log("menu list count :/ failure")
                closure.onError(failure)
            }
        })
    }




    fun broadcasts(closure: Closure<List<Broadcast>>){
        getApi().broadcasts().enqueue(object: MyCallBack<BaseResponse<List<Broadcast>>>(){
            override fun onSuccess(response: BaseResponse<List<Broadcast>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }



    fun conversations(page:Int, closure: Closure<PagingResponse<Conversation>>){
        getApi().getConversations(page).enqueue(object: MyCallBack<BaseResponse<PagingResponse<Conversation>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Conversation>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
    fun rooms(closure: Closure<List<Room>>){
        getApi().rooms().enqueue(object: MyCallBack<BaseResponse<List<Room>>>(){
            override fun onSuccess(response: BaseResponse<List<Room>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
    fun roomsUpdate(id:Int, room:Room, closure: Closure<Room>){
        getApi().roomsUpdate(id, room).enqueue(object: MyCallBack<BaseResponse<Room>>(){
            override fun onSuccess(response: BaseResponse<Room>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }


    fun apps(closure: Closure<List<App>>){
        getApi().apps().enqueue(object: MyCallBack<BaseResponse<List<App>>>(){
            override fun onSuccess(response: BaseResponse<List<App>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

    fun categoriesOfUsers(parent:Int=0, closure: Closure<List<Category>>){
        categories(parent, ModelType.USER, closure)
    }
    fun categoriesOfStores(parent:Int=0, closure: Closure<List<Category>>){
        categories(parent, ModelType.STORE, closure)
    }
    fun categoriesOfProducts(parent:Int=0, closure: Closure<List<Category>>){
        categories(parent, ModelType.PRODUCT, closure)
    }
    fun categoriesOfNotes(parent:Int=0, closure: Closure<List<Category>>){
        categories(parent, ModelType.NOTE, closure)
    }
    fun categoriesOfPosts(parent:Int=0, closure: Closure<List<Category>>){
        categories(parent, ModelType.POST, closure)
    }
    fun categories(id:Int, type: Int, closure: Closure<List<Category>>){
        val c:MyCallBack<BaseResponse<List<Category>>> = object: MyCallBack<BaseResponse<List<Category>>>(){
            override fun onSuccess(response: BaseResponse<List<Category>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }

        if (id == 0){
            when (type){
                ModelType.POST -> getApi().postsCategories().enqueue(c)
                ModelType.NOTE -> getApi().notesCategories().enqueue(c)
                ModelType.PRODUCT -> getApi().productsCategories().enqueue(c)
                ModelType.USER -> getApi().usersCategories().enqueue(c)
                ModelType.STORE -> getApi().storesCategories().enqueue(c)
            }
        }
        else
            getApi().getCategories(id).enqueue(c)
    }

    fun cities(closure: Closure<List<City>>){
        getApi().locations().enqueue(object: MyCallBack<BaseResponse<List<City>>>(){
            override fun onSuccess(response: BaseResponse<List<City>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }


    fun notifications(closure: Closure<List<Notification>>){
        getApi().notifications().enqueue(object: MyCallBack<BaseResponse<List<Notification>>>(){
            override fun onSuccess(response: BaseResponse<List<Notification>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }



    fun alerts(closure: Closure<List<Alert>>){
        getApi().alerts().enqueue(object: MyCallBack<BaseResponse<List<Alert>>>(){
            override fun onSuccess(response: BaseResponse<List<Alert>>?) {
                super.onSuccess(response)
                MyActivity.log("uc received alerts" + response?.data?.size)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
}
