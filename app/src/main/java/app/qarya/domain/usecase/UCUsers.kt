package app.qarya.domain.usecase

import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure
import app.qarya.model.models.User
import app.qarya.model.models.requests.UpdateEmailRequest
import app.qarya.model.models.requests.UpdatePasswordRequest
import app.qarya.model.models.requests.UpdateProfileRequest
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.MultipartBody
import tn.core.model.responses.PagingResponse
import java.io.File


class UCUsers : UseCase() {


    fun online(page:Int, closure: Closure<PagingResponse<User>>) {
        api.getUsersOnline(page).enqueue(object :MyCallBack<BaseResponse<PagingResponse<User>>>(){
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
    fun random(closure: Closure<List<User>>) {
        api.getUsersRandomOnline().enqueue(object :MyCallBack<BaseResponse<List<User>>>(){
            override fun onSuccess(response: BaseResponse<List<User>>) {
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




    fun uploadPicture(file:File, closure: Closure<User>) {
        // Map is used to multipart the file using okhttp3.RequestBody

        // Parsing any Media type file
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody)
        val filename = RequestBody.create(MediaType.parse("text/plain"), file.getName())
        api.uploadPicture(fileToUpload, filename).enqueue(object :MyCallBack<BaseResponse<User>>(){
            override fun onSuccess(response: BaseResponse<User>) {
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



    fun one(id:Int, closure: Closure<User>) {
        api.getUser(id).enqueue(object :MyCallBack<BaseResponse<User>>(){
            override fun onSuccess(response: BaseResponse<User>) {
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

    fun update(user:UpdateProfileRequest, closure: Closure<User>) {
        api.updateUser(user).enqueue(object :MyCallBack<BaseResponse<User>>(){
            override fun onSuccess(response: BaseResponse<User>) {
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
    fun updateEmail(user:UpdateEmailRequest, closure: Closure<User>) {
        api.updateEmail(user).enqueue(object :MyCallBack<BaseResponse<User>>(){
            override fun onSuccess(response: BaseResponse<User>) {
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
    fun updatePassword(user:UpdatePasswordRequest, closure: Closure<User>) {
        api.updatePassword(user).enqueue(object :MyCallBack<BaseResponse<User>>(){
            override fun onSuccess(response: BaseResponse<User>) {
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


}
