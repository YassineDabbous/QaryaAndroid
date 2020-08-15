package app.qarya.domain.usecase

import tn.core.domain.Failure
import app.qarya.presentation.base.MyActivity
import app.qarya.model.models.Message
import app.qarya.model.models.requests.MessageSetter
import app.qarya.model.models.responses.GeneralResponse
import tn.core.model.responses.BaseResponse
import tn.core.model.responses.PagingResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCMessages : UseCase() {
    fun getMessages(id: Int, uid: Int, page:Int, closure: Closure<PagingResponse<Message>>) {
        MyActivity.log("conversation id $id with user $uid")
        val c = object :MyCallBack<BaseResponse<PagingResponse<Message>>>(){
            override fun onSuccess(response: BaseResponse<PagingResponse<Message>>) {
                super.onSuccess(response)
                //MyActivity.log("Messages count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        if (id==0)api.getMessagesWithUser(uid, page).enqueue(c)
        else api.getMessages(id, page).enqueue(c)
    }

    fun pushMessage(cid:Int, to:Int, msg:String, pid:Int, closure: Closure<List<Message>>){
        MyActivity.log("pushMessage: $msg")
        getApi().pushMessage(MessageSetter(cid, to, msg, pid)).enqueue(object: MyCallBack<BaseResponse<List<Message>>>(){
            override fun onSuccess(response: BaseResponse<List<Message>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        });
    }



    fun deleteMessage(id: Int, closure: Closure<GeneralResponse>) {
        api.deleteMessage(id).enqueue( object :MyCallBack<BaseResponse<GeneralResponse>>(){
            override fun onSuccess(response: BaseResponse<GeneralResponse>) {
                super.onSuccess(response)
                //MyActivity.log("Messages count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }

    fun lockConversation(id: Int, closure: Closure<GeneralResponse>) {
        api.lockConversation(id).enqueue( object :MyCallBack<BaseResponse<GeneralResponse>>(){
            override fun onSuccess(response: BaseResponse<GeneralResponse>) {
                super.onSuccess(response)
                //MyActivity.log("Messages count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }




    fun status(type: Int, closure: Closure<GeneralResponse>) {
        api.status(type).enqueue( object :MyCallBack<BaseResponse<GeneralResponse>>(){
            override fun onSuccess(response: BaseResponse<GeneralResponse>) {
                super.onSuccess(response)
                //MyActivity.log("Messages count " + response.data?.data?.size)
                if (response.data!=null)
                    closure.onSuccess(response.data)
            }

            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
    fun togglePrivacy(closure: Closure<GeneralResponse>) {
        api.privateChatPrivacy().enqueue( object :MyCallBack<BaseResponse<GeneralResponse>>(){
            override fun onSuccess(response: BaseResponse<GeneralResponse>) {
                super.onSuccess(response)
                //MyActivity.log("Messages count " + response.data?.data?.size)
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
