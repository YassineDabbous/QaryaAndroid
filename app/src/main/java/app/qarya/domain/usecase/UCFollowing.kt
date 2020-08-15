package app.qarya.domain.usecase

import com.onesignal.OneSignal
import app.qarya.model.models.requests.FollowRequest
import app.qarya.model.models.responses.FollowResponse
import tn.core.domain.Failure
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCFollowing : UseCase() {


    fun follow(request: FollowRequest, closure: Closure<FollowResponse>) {

        api.follow(request).enqueue(object : MyCallBack<BaseResponse<FollowResponse>>() {
            override fun onSuccess(response: BaseResponse<FollowResponse>) {
                super.onSuccess(response)
                response.data.id = request.id
                if(response.data.followed)
                    OneSignal.sendTag(request.key, request.value)
                else
                    OneSignal.deleteTag(request.key)
                closure.onSuccess(response.data)
            }


            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

}
