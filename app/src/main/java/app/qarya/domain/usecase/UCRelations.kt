package app.qarya.domain.usecase

import tn.core.domain.Failure
import app.qarya.model.models.*
import tn.core.model.responses.BaseResponse
import tn.core.model.net.custom.MyCallBack
import tn.core.domain.base.Closure

class UCRelations : UseCase() {



    enum class TYPE {
        friends, requests, requested, blocked, suggested
    }
    enum class ACTION {
        ADD, ACCEPT, BLOCK, REFUSE
    }

    fun relations(closure: Closure<List<Relation>>, type:TYPE = TYPE.friends, uid:Int=0){
        val c:MyCallBack<BaseResponse<List<Relation>>> = object: MyCallBack<BaseResponse<List<Relation>>>(){
            override fun onSuccess(response: BaseResponse<List<Relation>>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        when(type){
            TYPE.friends -> { getApi().relationsFor(uid).enqueue(c) }
            TYPE.requests -> { getApi().requests().enqueue(c) }
            TYPE.requested -> { getApi().requested().enqueue(c) }
            TYPE.blocked -> { getApi().blocked().enqueue(c) }
            TYPE.suggested -> { getApi().suggested().enqueue(c) }
        }
    }


    fun change(action:ACTION, uid:Int, closure: Closure<Relation>){
        var c = object : MyCallBack<BaseResponse<Relation>>(){
            override fun onSuccess(response: BaseResponse<Relation>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        }
        when(action){
            ACTION.REFUSE -> { getApi().relationsChange(0, uid).enqueue(c) }
            ACTION.ADD -> { getApi().relationsChange(1, uid).enqueue(c) }
            ACTION.ACCEPT -> { getApi().relationsChange(2, uid).enqueue(c) }
            ACTION.BLOCK -> { getApi().relationsChange(3, uid).enqueue(c) }
        }
    }

/*
    fun relationsAccept(uid:Int, closure: Closure<Relation>){
        getApi().relationsChange(2, uid).enqueue(object : MyCallBack<BaseResponse<Relation>>(){
            override fun onSuccess(response: BaseResponse<Relation>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
    fun relationsRefuse(uid:Int, closure: Closure<Relation>){
        getApi().relationsChange(0, uid).enqueue(object : MyCallBack<BaseResponse<Relation>>(){
            override fun onSuccess(response: BaseResponse<Relation>?) {
                super.onSuccess(response)
                closure.onSuccess(response?.data)
            }
            override fun onError(failure: Failure?) {
                super.onError(failure)
                closure.onError(failure)
            }
        })
    }
*/
}
